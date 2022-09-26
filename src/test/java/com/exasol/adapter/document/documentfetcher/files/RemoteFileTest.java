package com.exasol.adapter.document.documentfetcher.files;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class RemoteFileTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.simple().forClass(RemoteFile.class).verify();
    }
}
