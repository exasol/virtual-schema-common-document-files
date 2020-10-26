package com.exasol.adapter.document.files.stringfilter.wildcardexpression.renderer.regex;

import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;

/**
 * Interface for classes that render a {@link WildcardExpression} to a regular expression.
 */
public interface RegexRenderer {
    /**
     * Render the {@link WildcardExpression} into a regular expression.
     * 
     * @param expression {@link WildcardExpression} to render
     * @return rendered regular expressions
     */
    public String render(WildcardExpression expression);
}
