package com.exasol.adapter.document.documentfetcher.files.segmentation;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class FileSegmentTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(FileSegment.class).verify();
    }
}
