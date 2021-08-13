package com.exasol.adapter.document.files;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.exasol.errorreporting.ExaError;

/**
 * This class parses the source string syntax.
 */
class SourceString {
    private static final Pattern IMPORT_AS_PATTERN = Pattern.compile("\\(import-as:([^\\)]+)\\)");
    private final String fileTypeOverride;
    private final String filePattern;

    /**
     * Create a new instance of {@link SourceString}.
     * 
     * @param sourceString source string
     */
    SourceString(final String sourceString) {
        final Matcher importAsMatcher = IMPORT_AS_PATTERN.matcher(sourceString);
        if (importAsMatcher.find()) {
            this.fileTypeOverride = importAsMatcher.group(1);
            this.filePattern = importAsMatcher.replaceAll("");
        } else {
            this.fileTypeOverride = null;
            this.filePattern = sourceString;
        }
    }

    /**
     * Get the file type.
     * 
     * @return file type
     */
    public String getFileType() {
        if (this.fileTypeOverride != null) {
            return this.fileTypeOverride;
        } else {
            return getFileExtension();
        }
    }

    private String getFileExtension() {
        final int lastDotIndex = this.filePattern.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new IllegalStateException(ExaError.messageBuilder("E-VSDF-15")
                    .message("Invalid file name {{file name}}. This file name does not have a extension.",
                            this.filePattern)
                    .mitigation("Rename the file so that it has a valid extension.")
                    .mitigation(
                            "If you can't rename the file, add '(import-as:<TYPE>)' to the source string in your mapping definition.")
                    .toString());
        }
        return this.filePattern.substring(lastDotIndex + 1);
    }

    /**
     * Get the file pattern.
     * 
     * @return file pattern
     */
    public String getFilePattern() {
        return this.filePattern;
    }
}
