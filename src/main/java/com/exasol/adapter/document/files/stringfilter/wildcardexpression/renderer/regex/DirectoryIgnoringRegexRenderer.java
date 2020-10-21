package com.exasol.adapter.document.files.stringfilter.wildcardexpression.renderer.regex;

import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;

/**
 * This class renders a {@link WildcardExpression} into a regular expression that always matches cross directory
 * borders.
 */
public class DirectoryIgnoringRegexRenderer extends AbstractRegexRenderer {

    /**
     * Create a new instance of {@link DirectoryIgnoringRegexRenderer}.
     */
    public DirectoryIgnoringRegexRenderer() {
        super(".");
    }
}
