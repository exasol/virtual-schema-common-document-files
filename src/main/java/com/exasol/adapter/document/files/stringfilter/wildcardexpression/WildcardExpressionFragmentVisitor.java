package com.exasol.adapter.document.files.stringfilter.wildcardexpression;

import com.exasol.adapter.document.files.stringfilter.wildcardexpression.fragments.*;

/**
 * Visitor for {@link WildcardExpressionFragment}.
 */
public interface WildcardExpressionFragmentVisitor {
    void visit(final DirectoryLimitedMultiCharWildcard wildcard);

    void visit(final DirectoryLimitedSingleCharWildcard wildcard);

    void visit(final CrossDirectoryMultiCharWildcard wildcard);

    void visit(final CrossDirectorySingleCharWildcard wildcard);

    void visit(final RegularCharacter character);
}
