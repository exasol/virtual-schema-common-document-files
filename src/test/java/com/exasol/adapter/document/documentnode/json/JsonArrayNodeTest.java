package com.exasol.adapter.document.documentnode.json;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentnode.DocumentArray;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.holder.BigDecimalHolderNode;

import jakarta.json.Json;
import jakarta.json.JsonArray;

class JsonArrayNodeTest {

    public static final JsonArray TEST_ARRAY = Json.createArrayBuilder().add(1).add(2).build();

    @Test
    void testCreation() {
        final DocumentNode jsonNode = JsonNodeFactory.getInstance().getJsonNode(Json.createArrayBuilder().build());
        assertThat(jsonNode, instanceOf(JsonArrayNode.class));
    }

    @Test
    void testSize() {
        final DocumentArray result = (DocumentArray) JsonNodeFactory.getInstance().getJsonNode(TEST_ARRAY);
        assertThat(result.size(), equalTo(2));
    }

    @Test
    void testGetValue() {
        final DocumentArray result = (DocumentArray) JsonNodeFactory.getInstance().getJsonNode(TEST_ARRAY);
        final BigDecimalHolderNode value = (BigDecimalHolderNode) result.getValue(0);
        assertThat(value.getValue(), equalTo(BigDecimal.valueOf(1)));
    }

    @Test
    void testGetValueList() {
        final DocumentArray result = (DocumentArray) JsonNodeFactory.getInstance().getJsonNode(TEST_ARRAY);
        final List<? extends DocumentNode> valuesList = result.getValuesList();
        final BigDecimalHolderNode firstValue = (BigDecimalHolderNode) valuesList.get(0);
        assertAll(//
                () -> assertThat(firstValue.getValue(), equalTo(BigDecimal.valueOf(1))),
                () -> assertThat(valuesList.size(), equalTo(2))//
        );
    }
}
