package com.exasol.adapter.document.files.stringfilter.wildcardexpression.renderer.regex;

import java.util.regex.Pattern;

import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;

/**
 * This class renders a {@link WildcardExpression} into a regular expression that applies the nonrecursive wildcards
 only for file / directory names, not for whole paths. It does so by excluding the directory separator for the matches.
 */
public class DirectoryAwareRegexRenderer extends AbstractRegexRenderer {

    /**
     * Create a new instance of {@link DirectoryAwareRegexRenderer}.
     * 
     * @param directorySeparator the separator between directories (e.g. {@code /}).
     */
    public DirectoryAwareRegexRenderer(final String directorySeparator) {
        super("[^" + Pattern.quote(directorySeparator) + "]");
    }
}
