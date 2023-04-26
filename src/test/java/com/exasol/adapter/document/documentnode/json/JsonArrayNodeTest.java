package com.exasol.adapter.document.documentnode.json;

import static com.exasol.adapter.document.testutil.DocumentNodeMatchers.decimalNode;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentnode.DocumentArray;
import com.exasol.adapter.document.documentnode.DocumentNode;

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
        assertThat(result.getValue(0), decimalNode(1));
    }

    @Test
    void testGetValueList() {
        final DocumentArray result = (DocumentArray) JsonNodeFactory.getInstance().getJsonNode(TEST_ARRAY);
        assertThat(result.getValuesList(), contains(decimalNode(1), decimalNode(2)));
    }
}
