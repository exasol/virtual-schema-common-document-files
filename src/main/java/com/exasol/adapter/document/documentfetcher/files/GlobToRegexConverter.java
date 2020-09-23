package com.exasol.adapter.document.documentfetcher.files;

import java.util.regex.Pattern;

/**
 * This class builds regex {@link Pattern} for glob stings.
 * <p>
 * This converter only supports * and ? wildcards.
 * </p>
 */
public class GlobToRegexConverter {

    private GlobToRegexConverter() {
        // empty on purpose
    }

    public static Pattern convert(final String glob) {
        final StringBuilder regex = new StringBuilder("^");
        StringBuilder quoted = new StringBuilder();

        for (int i = 0; i < glob.length(); ++i) {
            final char c = glob.charAt(i);
            switch (c) {
            case '*':
                regex.append(Pattern.quote(quoted.toString()));
                quoted = new StringBuilder();
                regex.append(".*");
                break;
            case '?':
                regex.append(Pattern.quote(quoted.toString()));
                quoted = new StringBuilder();
                regex.append('.');
                break;
            default:
                quoted.append(c);
            }
        }
        regex.append(Pattern.quote(quoted.toString()));
        return Pattern.compile(regex.toString());
    }
}
