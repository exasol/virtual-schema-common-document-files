package com.exasol.adapter.document.files.stringfilter.wildcardexpression;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.exasol.adapter.document.files.stringfilter.StringFilter;
import com.exasol.adapter.document.files.stringfilter.StringFilterVisitor;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.fragments.CrossDirectoryMultiCharWildcard;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.fragments.RegularCharacter;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.parser.GlobParser;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.parser.LikeParser;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.parser.NonWildcardParser;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.renderer.regex.DirectoryIgnoringRegexRenderer;

/**
 * This class represents expressions with simple wildcards as can be expressed using the GLOB or SQL-LIKE syntax.
 */
public class WildcardExpression implements StringFilter {
    private static final long serialVersionUID = -9137643648656274318L;
    /** @serial */
    private final ArrayList<WildcardExpressionFragment> fragments;

    /**
     * Create a new instance of {@link WildcardExpression}.
     * 
     * @param fragments fragments the expression consists of.
     */
    public WildcardExpression(final List<WildcardExpressionFragment> fragments) {
        this.fragments = new ArrayList<>(fragments);
    }

    /**
     * Build a {@link WildcardExpression} from a LIKE expression.
     * 
     * @param likeExpression LIKE expression to parse
     * @param likeEscapeChar LIKE escape character
     * @return built {@link WildcardExpression}
     */
    public static WildcardExpression fromLike(final String likeExpression, final char likeEscapeChar) {
        return new LikeParser(likeEscapeChar).parse(likeExpression);
    }

    /**
     * Build a {@link WildcardExpression} from a GLOB expression.
     * 
     * @param globExpression GLOB expression to parse
     * @return built {@link WildcardExpression}
     */
    public static WildcardExpression fromGlob(final String globExpression) {
        return new GlobParser().parse(globExpression);
    }

    /**
     * Build a {@link WildcardExpression} from a non-wildcard-format string.
     * 
     * @param regularString string to parse
     * @return built {@link WildcardExpression} with no wildcards
     */
    public static WildcardExpression forNonWildcardString(final String regularString) {
        return new NonWildcardParser().parse(regularString);
    }

    /**
     * Build a {@link WildcardExpression} from a non-wildcard-format prefix.
     *
     * @param prefix prefix to bild wildcard expression for
     * @return built {@link WildcardExpression}
     */
    public static WildcardExpression forNonWildcardPrefix(final String prefix) {
        final List<WildcardExpressionFragment> fragments = Stream
                .concat(new NonWildcardParser().parse(prefix).getFragments().stream(),
                        Stream.of(new CrossDirectoryMultiCharWildcard()))
                .collect(Collectors.toList());
        return new WildcardExpression(fragments);
    }

    /**
     * Get the fragments of this expression.
     * 
     * @return list of {@link WildcardExpressionFragment}s.
     */
    public List<WildcardExpressionFragment> getFragments() {
        return this.fragments;
    }

    /**
     * Render the expression into a regular expression (regex).
     * <p>
     * Use this method, if the target file system does not support directories.
     * </p>
     *
     * @return rendered regular expression
     */
    public String asDirectoryIgnoringRegex() {
        return new DirectoryIgnoringRegexRenderer().render(this);
    }

    @Override
    public String getStaticPrefix() {
        final StringBuilder wildcardFreePrefixBuilder = new StringBuilder();
        for (final WildcardExpressionFragment fragment : this.fragments) {
            if (isWildcard(fragment)) {
                break;
            } else {
                final RegularCharacter regularCharacter = (RegularCharacter) fragment;
                wildcardFreePrefixBuilder.append(regularCharacter.getCharacter());
            }
        }
        return wildcardFreePrefixBuilder.toString();
    }

    protected boolean isWildcard(final WildcardExpressionFragment fragment) {
        return !(fragment instanceof RegularCharacter);
    }

    @Override
    public boolean hasWildcard() {
        return this.fragments.stream().anyMatch(this::isWildcard);
    }

    @Override
    public boolean hasContradiction() {
        return false;
    }

    @Override
    public void accept(final StringFilterVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return this.fragments.stream().map(Object::toString).collect(Collectors.joining());
    }
}
