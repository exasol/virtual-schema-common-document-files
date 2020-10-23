package com.exasol.adapter.document.files.stringfilter;

import java.io.Serializable;

import com.exasol.adapter.document.documentfetcher.files.FileLoader;
import com.exasol.adapter.document.files.stringfilter.matcher.Matcher;
import com.exasol.adapter.document.files.stringfilter.matcher.MatcherFactory;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.renderer.regex.DirectoryAwareRegexRenderer;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.renderer.regex.DirectoryIgnoringRegexRenderer;

/**
 * This class represents a filter for file names. Using this class you can filter which files the {@link FileLoader}s
 * will load.
 */
public interface StringFilter extends Serializable {

    /**
     * Get the longest possible static (wildcard-free) prefix of the filter.
     * 
     * @return wildcard-free prefix
     */
    public String getStaticPrefix();

    /**
     * Get if this expression contains one or more wildcards.
     *
     * @return {@code true}, if this expression contains one or more wildcards
     */
    public boolean hasWildcard();

    /**
     * Get if this filter contains a contradiction and by that, never matches.
     * 
     * @return {@code true} if filter contains a contradiction
     */
    public boolean hasContradiction();

    /**
     * Accept {@link StringFilterVisitor}.
     * 
     * @param visitor visitor to accept.
     */
    public void accept(StringFilterVisitor visitor);

    /**
     * Get a directory ignoring {@link Matcher} for this filter.
     * 
     * @return directory ignoring {@link Matcher} for this filter
     */
    public default Matcher getDirectoryIgnoringMatcher() {
        return new MatcherFactory(new DirectoryIgnoringRegexRenderer()).getMatcher(this);
    }

    /**
     * Get a directory aware {@link Matcher} for this filter.
     *
     * @param directorySeparator the separator between directories (e.g. {@code /}).
     * @return directory aware {@link Matcher} for this filter
     */
    public default Matcher getDirectoryAwareMatcher(final String directorySeparator) {
        return new MatcherFactory(new DirectoryAwareRegexRenderer(directorySeparator)).getMatcher(this);
    }
}
