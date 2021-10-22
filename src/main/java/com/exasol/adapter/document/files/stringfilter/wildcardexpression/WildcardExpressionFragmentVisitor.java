package com.exasol.adapter.document.files.stringfilter.wildcardexpression;

import com.exasol.adapter.document.files.stringfilter.wildcardexpression.fragments.*;

/**
 * Visitor for {@link WildcardExpressionFragment}.
 */
public interface WildcardExpressionFragmentVisitor {
    /**
     * Visit {@link DirectoryLimitedMultiCharWildcard}.
     * 
     * @param wildcard {@link DirectoryLimitedMultiCharWildcard} to visit
     */
    void visit(final DirectoryLimitedMultiCharWildcard wildcard);

    /**
     * Visit {@link DirectoryLimitedSingleCharWildcard}.
     *
     * @param wildcard {@link DirectoryLimitedSingleCharWildcard} to visit
     */
    void visit(final DirectoryLimitedSingleCharWildcard wildcard);

    /**
     * Visit {@link CrossDirectoryMultiCharWildcard}.
     *
     * @param wildcard {@link CrossDirectoryMultiCharWildcard} to visit
     */
    void visit(final CrossDirectoryMultiCharWildcard wildcard);

    /**
     * Visit {@link CrossDirectorySingleCharWildcard}.
     *
     * @param wildcard {@link CrossDirectorySingleCharWildcard} to visit
     */
    void visit(final CrossDirectorySingleCharWildcard wildcard);

    /**
     * Visit {@link RegularCharacter}.
     *
     * @param character {@link RegularCharacter} to visit
     */
    void visit(final RegularCharacter character);
}
