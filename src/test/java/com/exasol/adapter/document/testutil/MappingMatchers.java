package com.exasol.adapter.document.testutil;

import static org.hamcrest.Matchers.*;

import java.util.function.Function;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import com.exasol.adapter.document.edml.*;

/**
 * This class contains Hamcrest {@link org.hamcrest.Matcher}s for implementations of
 * {@link com.exasol.adapter.document.edml.MappingDefinition}.
 */
public class MappingMatchers {
    private MappingMatchers() {
        // Not instantiable
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public static Matcher<MappingDefinition> varcharMapping(final Matcher<ToVarcharMapping>... subMatchers) {
        return castingMatcher(ToVarcharMapping.class, allOf(subMatchers));
    }

    public static Matcher<ToVarcharMapping> varcharColumnsSize(final int expected) {
        return feature(ToVarcharMapping::getVarcharColumnSize, "varchar column size", equalTo(expected));
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public static Matcher<MappingDefinition> boolMapping(final Matcher<ToBoolMapping>... subMatchers) {
        return castingMatcher(ToBoolMapping.class, allOf(subMatchers));
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public static Matcher<MappingDefinition> doubleMapping(final Matcher<ToDoubleMapping>... subMatchers) {
        return castingMatcher(ToDoubleMapping.class, allOf(subMatchers));
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public static Matcher<MappingDefinition> decimalMapping(final Matcher<ToDecimalMapping>... subMatchers) {
        return castingMatcher(ToDecimalMapping.class, allOf(subMatchers));
    }

    public static Matcher<ToDecimalMapping> precision(final int expected) {
        return feature(ToDecimalMapping::getDecimalPrecision, "decimal precision", equalTo(expected));
    }

    public static Matcher<ToDecimalMapping> scale(final int expected) {
        return feature(ToDecimalMapping::getDecimalScale, "decimal scale", equalTo(expected));
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public static Matcher<MappingDefinition> dateMapping(final Matcher<ToDateMapping>... subMatchers) {
        return castingMatcher(ToDateMapping.class, allOf(subMatchers));
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public static Matcher<MappingDefinition> timestampMapping(final Matcher<ToTimestampMapping>... subMatchers) {
        return castingMatcher(ToTimestampMapping.class, allOf(subMatchers));
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public static Matcher<MappingDefinition> columnMapping(final Matcher<AbstractToColumnMapping>... subMatchers) {
        return castingMatcher(AbstractToColumnMapping.class, allOf(subMatchers));
    }

    public static Matcher<AbstractToColumnMapping> destinationName(final String expected) {
        return feature(AbstractToColumnMapping::getDestinationName, "destination name", equalTo(expected));
    }

    private static <M extends MappingDefinition, T> Matcher<M> feature(final Function<M, T> getter,
            final String feature, final Matcher<T> subMatcher) {
        return new SimpleFeatureMatcher<>(getter, subMatcher, feature);
    }

    private static <M extends MappingDefinition, T> Matcher<MappingDefinition> castingMatcher(
            final Class<M> expectedType, final Matcher<M> subMatcher) {
        final Function<MappingDefinition, M> cast = expectedType::cast;
        return allOf( //
                instanceOf(expectedType), //
                new SimpleFeatureMatcher<>(cast, subMatcher, "type " + expectedType.getName()));
    }

    private static class SimpleFeatureMatcher<T, U> extends FeatureMatcher<T, U> {
        private final Function<T, U> getter;

        private SimpleFeatureMatcher(final Function<T, U> getter, final Matcher<? super U> subMatcher,
                final String feature) {
            super(subMatcher, feature, feature);
            this.getter = getter;
        }

        @Override
        protected U featureValueOf(final T actual) {
            return this.getter.apply(actual);
        }
    }
}
