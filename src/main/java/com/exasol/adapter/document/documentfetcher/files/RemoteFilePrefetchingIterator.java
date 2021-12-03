package com.exasol.adapter.document.documentfetcher.files;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.exasol.adapter.document.iterators.CloseableIterator;
import com.exasol.errorreporting.ExaError;

/**
 * This {@link Iterator} for {@link RemoteFile}s prefetches the content of small files asynchronously.
 */
public class RemoteFilePrefetchingIterator implements CloseableIterator<RemoteFile> {
    private static final int MAX_PREFETCH = 100;
    private static final int MAX_PREFETCH_FILE_SIZE = 1_000_000;
    private final CloseableIterator<RemoteFile> source;
    private final Queue<LoadingRemoteFile> retryQueue;
    private final List<LoadingRemoteFile> buffer;
    private float dynamicPrefetchSize = MAX_PREFETCH;
    private RemoteFile next;
    private boolean hasNext;

    /**
     * Create a new instance of {@link RemoteFilePrefetchingIterator}.
     *
     * @param source iterator to wrap
     */
    public RemoteFilePrefetchingIterator(final CloseableIterator<RemoteFile> source) {
        this.source = source;
        this.retryQueue = new LinkedList<>();
        this.buffer = new LinkedList<>();
        this.hasNext = true;
        loadNext();
    }

    private void loadNext() {
        final Optional<RemoteFile> nonPrefetchableFile = fillBuffer();
        if (nonPrefetchableFile.isPresent()) {
            this.next = nonPrefetchableFile.get();
            return;
        }
        if (this.buffer.isEmpty()) {
            this.hasNext = false;
            this.next = null;
            return;
        }
        Optional<RemoteFile> nextCandidate = findReady();
        while (nextCandidate.isEmpty()) {
            final Optional<RemoteFile> newNonPrefetchableFile = fillBuffer();
            if (newNonPrefetchableFile.isPresent()) {
                this.next = newNonPrefetchableFile.get();
                return;
            }
            wait10Ms();
            nextCandidate = findReady();
        }
        this.next = nextCandidate.get();
    }

    private void wait10Ms() {
        try {
            Thread.sleep(10);
        } catch (final InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(ExaError.messageBuilder("F-VSDF-21")
                    .message("Interrupted while waiting for buffered ready or non-prefetchable file.").toString(),
                    exception);
        }
    }

    private Optional<RemoteFile> findReady() {
        final Iterator<LoadingRemoteFile> iterator = this.buffer.iterator();
        while (iterator.hasNext()) {
            LoadingRemoteFile each = iterator.next();
            if (each.isDone()) {
                try {
                    final RemoteFile loadedFile = each.getLoadedFile();
                    iterator.remove();
                    this.dynamicPrefetchSize += 0.1;
                    if (this.dynamicPrefetchSize > MAX_PREFETCH) {
                        this.dynamicPrefetchSize = MAX_PREFETCH;
                    }
                    return Optional.of(loadedFile);
                } catch (final TooManyRequestsException exception) {
                    this.retryQueue.add(each);
                    iterator.remove();
                    this.dynamicPrefetchSize = this.buffer.size() - 1f;
                    if (this.dynamicPrefetchSize <= 0) {
                        this.dynamicPrefetchSize = 1;
                    }
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Fill the buffer with preloaded files.
     *
     * @return {@link RemoteFile} from {@link #source} if it's not suitable for preloading
     */
    private Optional<RemoteFile> fillBuffer() {
        while ((this.source.hasNext() || !this.retryQueue.isEmpty()) && this.buffer.size() < this.dynamicPrefetchSize) {
            if (this.retryQueue.isEmpty()) {
                final RemoteFile nextFile = this.source.next();
                if (nextFile.getSize() > MAX_PREFETCH_FILE_SIZE) {
                    return Optional.of(nextFile);
                } else {
                    this.buffer.add(new LoadingRemoteFile(nextFile));
                }
            } else {
                final LoadingRemoteFile retryFile = this.retryQueue.poll();
                retryFile.retry();
                this.buffer.add(retryFile);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean hasNext() {
        return this.hasNext;
    }

    @Override
    public RemoteFile next() {
        if (!this.hasNext) {
            throw new NoSuchElementException();
        }
        final RemoteFile nextCached = this.next;
        loadNext();
        return nextCached;
    }

    @Override
    public void close() {
        this.source.close();
    }

    private static class LoadingRemoteFile {
        private final RemoteFile remoteFile;
        private Future<byte[]> pendingContent;

        public LoadingRemoteFile(final RemoteFile remoteFile) {
            this.remoteFile = remoteFile;
            this.pendingContent = remoteFile.getContent().loadAsync();
        }

        public void retry() {
            this.pendingContent.cancel(true);
            this.pendingContent = this.remoteFile.getContent().loadAsync();
        }

        public RemoteFile getLoadedFile() {
            return this.remoteFile.withContent(new InMemoryRemoteFileContent(getContent()));
        }

        private byte[] getContent() {
            try {
                return this.pendingContent.get();
            } catch (final InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException(ExaError.messageBuilder("E-VSDF-19")
                        .message("Interrupted while waiting for file to load.").toString(), exception);
            } catch (final ExecutionException exception) {
                throw new IllegalStateException(ExaError.messageBuilder("E-VSDF-20")
                        .message("Error while waiting for file to load.").toString(), exception);
            }
        }

        public boolean isDone() {
            return this.pendingContent.isDone();
        }
    }
}
