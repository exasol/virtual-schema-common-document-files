package com.exasol.adapter.document.documentfetcher.files.segmentation;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class FileSegmentDescriptionTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(FileSegmentDescription.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(FileSegmentDescription.class).verify();
    }
}
