package com.exasol.adapter.document.files.stringfilter.matcher;

import com.exasol.adapter.document.files.stringfilter.StringFilter;

/**
 * Matcher for {@link StringFilter}.
 */
public interface Matcher {

    /**
     * Checks if a given string is matched by this filter.
     * 
     * @param stringToMatch string that is matched
     * @return {@code true} if string was matched.
     */
    public boolean matches(String stringToMatch);
}
