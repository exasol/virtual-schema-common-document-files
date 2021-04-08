package com.exasol.adapter.document.documentnode.avro;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.holder.BigDecimalHolderNode;
import com.exasol.errorreporting.ExaError;

/**
 * Factory for {@link DocumentNode}s from java object structures.
 */
public class JavaObjectDocumentNodeFactory {
    /**
     * Get a {@link DocumentNode} wrapping a given java object.
     * 
     * @param object object to wrap
     * @return built {@link DocumentNode} tree
     * @throws UnsupportedOperationException if a unsupported java type was passed
     */
    public static DocumentNode getNodeFor(final Object object) {
        if (object instanceof List) {
            return new JavaListNode((List<Object>) object);
        } else if (object instanceof Map) {
            return new JavaObjectNode((Map<String, Object>) object);
        } else if (object instanceof Integer) {
            return new BigDecimalHolderNode(BigDecimal.valueOf((Integer) object));
        } else if (object instanceof BigDecimal) {
            return new BigDecimalHolderNode((BigDecimal) object);
        } else {
            throw new UnsupportedOperationException(ExaError.messageBuilder("F-VSDF-13")
                    .message("Unsupported object type {{type}}.", object.getClass().getName()).ticketMitigation()
                    .toString());
        }
    }
}
