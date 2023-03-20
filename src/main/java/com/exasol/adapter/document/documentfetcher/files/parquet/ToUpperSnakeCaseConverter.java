package com.exasol.adapter.document.documentfetcher.files.parquet;

/**
 * This class converts strings to UPPER_SNAKE_CASE.
 */
class ToUpperSnakeCaseConverter {
    private ToUpperSnakeCaseConverter() {
        // empty on purpose
    }

    /**
     * Convert a string to UPPER_SNAKE_CASE.
     *
     * @param inputString input string in camelCase, lower_snake_case or UPPER_SNAKE_CASE.
     * @return string formatted in UPPER_SNAKE_CASE
     */
    static String toUpperSnakeCase(final String inputString) {
        boolean isPreviousUpperOrUnderscore = false;
        boolean isFirst = true;
        final StringBuilder result = new StringBuilder();
        for (int index = 0; index < inputString.length(); index++) {
            final char currentChar = inputString.charAt(index);
            if (Character.isUpperCase(currentChar)) {
                if (!isPreviousUpperOrUnderscore && !isFirst) {
                    result.append("_");
                }
                isPreviousUpperOrUnderscore = true;
            } else {
                isPreviousUpperOrUnderscore = currentChar == '_';
            }
            result.append(Character.toUpperCase(currentChar));
            isFirst = false;
        }
        return result.toString();
    }
}
