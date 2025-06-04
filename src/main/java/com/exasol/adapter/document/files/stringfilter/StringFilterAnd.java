package com.exasol.adapter.document.files.stringfilter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * And predicate for {@link StringFilter}s.
 */
public class StringFilterAnd implements StringFilter {
    private static final Logger LOGGER = Logger.getLogger(StringFilterAnd.class.getName());
    private static final long serialVersionUID = 3856323687685172983L;
    /** @serial */
    private final ArrayList<StringFilter> operands;

    /**
     * Create a new instance of {@link StringFilterAnd}.
     * 
     * @param operands operands to combine with an AND.
     */
    StringFilterAnd(final List<StringFilter> operands) {
        this.operands = new ArrayList<>(operands);
    }

    @Override
    public String getStaticPrefix() {
        return this.operands.stream().map(StringFilter::getStaticPrefix).max(Comparator.comparingInt(String::length))
                .orElse("");
    }

    @Override
    public boolean hasWildcard() {
        return this.operands.stream().allMatch(StringFilter::hasWildcard);
    }

    /**
     * Determines whether this {@code StringFilter} contains a contradiction among its operands.
     * <p>
     * A contradiction is present if:
     * <ul>
     *   <li>Any operand individually contains a contradiction, or</li>
     *   <li>The static prefixes of operands are inconsistent — i.e., not all start with the shortest prefix.</li>
     * </ul>
     * Such contradictions indicate mutually exclusive conditions that will result in no matches.
     * <p>
     * If a contradiction is detected, a debug-level log message is recorded to assist in troubleshooting.
     *
     * @return {@code true} if a contradiction exists; {@code false} otherwise.
     */
    @Override
    public boolean hasContradiction() {
        if (this.operands.stream().anyMatch(StringFilter::hasContradiction)) {
            return true;
        } else {
            final List<String> prefixes = getPrefixes();
            final String shortestPrefix = getShortestPrefix(prefixes);
            boolean hasContradiction = hasContradiction(prefixes, shortestPrefix);
            if (hasContradiction) {
                final String longestPrefix = getStaticPrefix();
                LOGGER.fine(() -> getContradictionLogMessage(prefixes, shortestPrefix, longestPrefix));
            }
            return hasContradiction;
        }
    }

    @Override
    public void accept(final StringFilterVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Get the operands of this AND.
     * 
     * @return operands
     */
    public List<StringFilter> getOperands() {
        return this.operands;
    }

    @Override
    public String toString() {
        return this.operands.stream().map(operand -> "(" + operand.toString() + ")")
                .collect(Collectors.joining(" AND "));
    }

    /**
     * Constructs a detailed log message describing a contradiction in string filter prefixes.
     * <p>
     * A contradiction occurs when not all prefixes in the given list start with the shortest common prefix,
     * which indicates that the filter conditions are mutually exclusive and will never yield results.
     *
     * @param prefixes        the list of all static prefixes from the filter operands
     * @param shortestPrefix  the shortest prefix found among the operands, expected to be a shared prefix
     * @param longestPrefix   the longest prefix found among the operands
     * @return a formatted log message highlighting the contradiction, the current shortest prefix,
     *         the current longest prefix, and the conflicting prefixes
     */
    String getContradictionLogMessage(List<String> prefixes, String shortestPrefix, String longestPrefix) {
        List<String> conflictingPrefixes = getConflictingPrefixes(prefixes, shortestPrefix);
        return String.format(
                "Contradiction detected in StringFilter: expected all prefixes to start with the shortest prefix '%s'. "
                        + "The longest prefix: '%s'. Conflicting prefixes: %s",
                shortestPrefix,
                longestPrefix,
                conflictingPrefixes
        );
    }

    /**
     * Returns a list of all static prefixes from the filter operands.
     *
     * @return a list of static prefixes
     */
    List<String> getPrefixes() {
        return this.operands.stream()
                .map(StringFilter::getStaticPrefix)
                .collect(Collectors.toList());
    }

    /**
     * Identifies prefixes in the given list that do not start with the specified shortest prefix.
     *
     * @param prefixes       the list of static prefixes to analyze
     * @param shortestPrefix the expected shared prefix
     * @return a list of conflicting prefixes
     */
    List<String> getConflictingPrefixes(List<String> prefixes, String shortestPrefix) {
        return prefixes.stream()
                .filter(prefix -> !prefix.startsWith(shortestPrefix))
                .collect(Collectors.toList());
    }

    /**
     * Determines if a contradiction exists based on the provided list of prefixes and
     * the expected shortest prefix.
     *
     * @param prefixes       the list of static prefixes
     * @param shortestPrefix the expected shared prefix
     * @return {@code true} if a contradiction exists; {@code false} otherwise
     */
    boolean hasContradiction(List<String> prefixes, String shortestPrefix) {
        return !getConflictingPrefixes(prefixes, shortestPrefix).isEmpty();
    }

    /**
     * Returns the shortest prefix among all filter operands.
     *
     * @return the shortest static prefix, or an empty string if none found
     */
    String getShortestPrefix() {
        return getShortestPrefix(getPrefixes());
    }

    /**
     * Returns the shortest string from the provided list of prefixes.
     *
     * @param prefixes the list of prefixes to inspect
     * @return the shortest prefix, or an empty string if the list is empty
     */
    String getShortestPrefix(List<String> prefixes) {
        return prefixes.stream()
                .min(Comparator.comparingInt(String::length))
                .orElse("");
    }
}
