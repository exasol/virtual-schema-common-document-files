package com.exasol.adapter.document.documentnode.util;

import static org.hamcrest.Matchers.*;

import java.util.function.Function;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.holder.BooleanHolderNode;
import com.exasol.adapter.document.documentnode.holder.StringHolderNode;

public class DocumentNodeMatchers {
    private DocumentNodeMatchers() {
        // Not instantiable
    }

    public static Matcher<DocumentNode> stringHolder(final String expected) {
        return castingMatch(StringHolderNode.class, StringHolderNode::getValue, equalTo(expected));
    }

    public static Matcher<DocumentNode> booleanHolder(final boolean expected) {
        return castingMatch(BooleanHolderNode.class, BooleanHolderNode::getValue, equalTo(expected));
    }

    private static <N extends DocumentNode, T> Matcher<DocumentNode> castingMatch(final Class<N> expectedNodeType,
            final Function<N, T> getter, final Matcher<T> subMatcher) {
        final Function<DocumentNode, T> castingGetter = value -> {
            return getter.apply(expectedNodeType.cast(value));
        };
        return allOf(instanceOf(expectedNodeType), new DocumentNodeMatcher<>(castingGetter, subMatcher));
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
