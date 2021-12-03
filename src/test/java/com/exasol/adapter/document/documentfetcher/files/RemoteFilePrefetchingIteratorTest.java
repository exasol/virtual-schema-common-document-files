package com.exasol.adapter.document.documentfetcher.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.AtLeast;
import org.mockito.internal.verification.Times;

import com.exasol.adapter.document.iterators.CloseableIterator;
import com.exasol.adapter.document.iterators.CloseableIteratorWrapper;

class RemoteFilePrefetchingIteratorTest {

    @Test
    void testPrefetching() {
        final StringRemoteFileContent content = spy(new StringRemoteFileContent("test"));
        final CloseableIterator<RemoteFile> inputIterator = spy(new CloseableIteratorWrapper<>(
                IntStream.range(0, 200).mapToObj(i -> new RemoteFile("", 1, content)).iterator()));
        final CloseableIterator<RemoteFile> iterator = new RemoteFilePrefetchingIterator(inputIterator);
        verify(content, new Times(100)).loadAsync();
        verify(inputIterator, new Times(100)).next();
        iterator.next();
        verify(inputIterator, new Times(101)).next();
    }

    @Test
    void testRetryOnError() {
        final UnstableRemoteFileContent content = spy(new UnstableRemoteFileContent());
        final CloseableIterator<RemoteFile> inputIterator = spy(new CloseableIteratorWrapper<>(
                IntStream.range(0, 200).mapToObj(i -> new RemoteFile("", 1, content)).iterator()));
        final CloseableIterator<RemoteFile> iterator = new RemoteFilePrefetchingIterator(inputIterator);
        final List<RemoteFile> result = new ArrayList<>();
        iterator.forEachRemaining(result::add);
        assertThat(result.size(), Matchers.equalTo(200));
        verify(inputIterator, new Times(200)).next();
        verify(content, new AtLeast(205)).loadAsync();
    }

    @Test
    void testNoPrefetchingForBigFiles() {
        final StringRemoteFileContent content = new StringRemoteFileContent("test");
        final CloseableIterator<RemoteFile> spy = spy(new CloseableIteratorWrapper<>(
                IntStream.range(0, 200).mapToObj(i -> new RemoteFile("", 10_000_000, content)).iterator()));
        final CloseableIterator<RemoteFile> iterator = new RemoteFilePrefetchingIterator(spy);
        verify(spy, new Times(1)).next();
    }

    @Test
    void testCorrectness() {
        final StringRemoteFileContent content = new StringRemoteFileContent("test");
        final CloseableIterator<RemoteFile> spy = spy(new CloseableIteratorWrapper<>(
                IntStream.range(0, 200).mapToObj(i -> new RemoteFile("", 1, content)).iterator()));
        final CloseableIterator<RemoteFile> iterator = new RemoteFilePrefetchingIterator(spy);
        final List<RemoteFile> result = new ArrayList<>();
        iterator.forEachRemaining(result::add);
        assertThat(result.size(), Matchers.equalTo(200));
    }

    @Test
    void testClose() {
        final CloseableIterator<RemoteFile> source = spy(new CloseableIteratorWrapper<>(Collections.emptyIterator()));
        final CloseableIterator<RemoteFile> iterator = new RemoteFilePrefetchingIterator(source);
        iterator.close();
        verify(source).close();
    }

    private static class UnstableRemoteFileContent implements RemoteFileContent {

        @Override
        public InputStream getInputStream() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Future<byte[]> loadAsync() {
            return new UnstableFuture();
        }

        private static class UnstableFuture implements Future<byte[]> {
            @Override
            public boolean cancel(final boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return true;
            }

            @Override
            public byte[] get() throws InterruptedException, ExecutionException {
                if (Math.random() > 0.4) {
                    throw new TooManyRequestsException();
                }
                return "test".getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public byte[] get(final long timeout, final TimeUnit unit)
                    throws InterruptedException, ExecutionException, TimeoutException {
                throw new UnsupportedOperationException();
            }
        }
    }
}