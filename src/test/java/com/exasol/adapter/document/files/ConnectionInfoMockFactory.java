package com.exasol.adapter.document.files;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.exasol.ExaConnectionInformation;

public class ConnectionInfoMockFactory {
    public static ExaConnectionInformation mockConnectionInfoWithAddress(final String address) {
        final ExaConnectionInformation connectionInformation = mock(ExaConnectionInformation.class);
        when(connectionInformation.getAddress()).thenAnswer(I -> address);
        return connectionInformation;
    }
}
