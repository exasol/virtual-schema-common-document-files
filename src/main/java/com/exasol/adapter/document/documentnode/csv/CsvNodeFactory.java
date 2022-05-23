package com.exasol.adapter.document.documentnode.csv;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.holder.BigDecimalHolderNode;
import com.exasol.adapter.document.documentnode.holder.BooleanHolderNode;
import com.exasol.adapter.document.documentnode.holder.NullHolderNode;
import com.exasol.adapter.document.documentnode.holder.StringHolderNode;
import com.exasol.adapter.document.documentnode.json.JsonArrayNode;
import com.exasol.adapter.document.documentnode.json.JsonObjectNode;
import com.exasol.errorreporting.ExaError;
import jakarta.json.JsonNumber;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;

/**
 * Factory for JSON {@link DocumentNode}s.
 */
public class CsvNodeFactory {
    private static final CsvNodeFactory INSTANCE = new CsvNodeFactory();

    private CsvNodeFactory() {
        // empty on purpose
    }

    /**
     * Get a singleton instance of {@link CsvNodeFactory}.
     * 
     * @return singleton instance of {@link CsvNodeFactory}
     */
    public static CsvNodeFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Build a CSV {@link DocumentNode} for a given CSV structure.
     * 
     * @param csvValue CSV value
     * @return built CSV {@link DocumentNode}
     */
    //todo: simplify this, probably gonna need to use object
//    public DocumentNode getCsvNode(final CsvValue csvValue) {
//        switch (csvValue.getValueType()) {
//
//        case STRING:
//            return new StringHolderNode(csvString.getString());
//
//        default:
//            throw new UnsupportedOperationException(ExaError.messageBuilder("F-VSDF-8")
//                    .message("Unsupported csv type: {{TYPE}}. Please open an issue.")
//                    .parameter("TYPE", csvValue.getValueType()).toString());
//        }
//    }
}
