package com.exasol.adapter.document.documentnode.util;

import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;
import java.util.function.Function;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import com.exasol.adapter.document.documentnode.*;

public class DocumentNodeMatchers {
    private DocumentNodeMatchers() {
        // Not instantiable
    }

    public static Matcher<DocumentNode> stringNode(final String expected) {
        return castingMatch(DocumentStringValue.class, DocumentStringValue::getValue, equalTo(expected));
    }

    public static Matcher<DocumentNode> booleanNode(final boolean expected) {
        return castingMatch(DocumentBooleanValue.class, DocumentBooleanValue::getValue, equalTo(expected));
    }

    public static Matcher<DocumentNode> decimalNode(final double expected) {
        return decimalNode(BigDecimal.valueOf(expected));
    }

    public static Matcher<DocumentNode> decimalNode(final int expected) {
        return decimalNode(BigDecimal.valueOf(expected));
    }

    public static Matcher<DocumentNode> decimalNode(final BigDecimal expected) {
        return castingMatch(DocumentDecimalValue.class, DocumentDecimalValue::getValue, equalTo(expected));
    }

    public static Matcher<DocumentNode> doubleNode(final double expected) {
        return castingMatch(DocumentFloatingPointValue.class, DocumentFloatingPointValue::getValue, equalTo(expected));
    }

    public static Matcher<DocumentNode> timestampNode(final String expected) {
        return timestampNode(java.sql.Timestamp.valueOf(expected));
    }

    public static Matcher<DocumentNode> timestampNode(final java.sql.Timestamp expected) {
        return castingMatch(DocumentTimestampValue.class, DocumentTimestampValue::getValue, equalTo(expected));
    }

    public static Matcher<DocumentNode> dateNode(final String expected) {
        return dateNode(java.sql.Date.valueOf(expected));
    }

    public static Matcher<DocumentNode> dateNode(final java.sql.Date expected) {
        return castingMatch(DocumentDateValue.class, DocumentDateValue::getValue, equalTo(expected));
    }

    private static <N extends DocumentNode, T> Matcher<DocumentNode> castingMatch(final Class<N> expectedNodeType,
            final Function<N, T> getter, final Matcher<T> subMatcher) {
        final Function<DocumentNode, N> cast = expectedNodeType::cast;
        return allOf( //
                instanceOf(expectedNodeType), //
                new DocumentNodeMatcher<>(cast.andThen(getter), subMatcher));
    }

    private static class DocumentNodeMatcher<T, U> extends FeatureMatcher<T, U> {
        private final Function<T, U> getter;

        private DocumentNodeMatcher(final Function<T, U> getter, final Matcher<? super U> subMatcher) {
            super(subMatcher, "value", "value");
            this.getter = getter;
        }

        @Override
        protected U featureValueOf(final T actual) {
            return this.getter.apply(actual);
        }
    }
}
