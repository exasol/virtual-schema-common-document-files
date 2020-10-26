package com.exasol.adapter.document.files.stringfilter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;

class StringFilterFactoryTest {

    @Test
    void testAndWithSingleOperand() {
        final WildcardExpression operand = WildcardExpression.fromGlob("*");
        final StringFilter stringFilter = new StringFilterFactory().and(operand);
        assertThat(stringFilter, equalTo(operand));
    }

    @Test
    void testAndWithTwoOperands() {
        final WildcardExpression operand = WildcardExpression.fromGlob("*");
        final StringFilterAnd and = (StringFilterAnd) new StringFilterFactory().and(operand, operand);
        assertThat(and.getOperands().size(), equalTo(2));
    }

    @Test
    void testOrWithSingleOperand() {
        final WildcardExpression operand = WildcardExpression.fromGlob("*");
        final StringFilter stringFilter = new StringFilterFactory().or(operand);
        assertThat(stringFilter, equalTo(operand));
    }

    @Test
    void testOrWithTwoOperands() {
        final WildcardExpression operand = WildcardExpression.fromGlob("*");
        final StringFilterOr or = (StringFilterOr) new StringFilterFactory().or(operand, operand);
        assertThat(or.getOperands().size(), equalTo(2));
    }
}