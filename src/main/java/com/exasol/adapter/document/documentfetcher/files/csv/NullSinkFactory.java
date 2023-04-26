package com.exasol.adapter.document.documentfetcher.files.csv;

import io.deephaven.csv.sinks.*;
import io.deephaven.csv.util.MutableObject;

/**
 * This is a "null" {@link SinkFactory} for the Deephaven-CSV library. It's purpose is to save memory while detecting
 * data types without collecting column data.
 */
public class NullSinkFactory implements SinkFactory {

    @Override
    public Sink<byte[]> forByte(final int colNum, final MutableObject<Source<byte[]>> source) {
        return new NullSink<>();
    }

    @Override
    public Byte reservedByte() {
        return null;
    }

    @Override
    public Sink<short[]> forShort(final int colNum, final MutableObject<Source<short[]>> source) {
        return new NullSink<>();
    }

    @Override
    public Short reservedShort() {
        return null;
    }

    @Override
    public Sink<int[]> forInt(final int colNum, final MutableObject<Source<int[]>> source) {
        return new NullSink<>();
    }

    @Override
    public Integer reservedInt() {
        return null;
    }

    @Override
    public Sink<long[]> forLong(final int colNum, final MutableObject<Source<long[]>> source) {
        return new NullSink<>();
    }

    @Override
    public Long reservedLong() {
        return null;
    }

    @Override
    public Sink<float[]> forFloat(final int colNum) {
        return new NullSink<>();
    }

    @Override
    public Float reservedFloat() {
        return null;
    }

    @Override
    public Sink<double[]> forDouble(final int colNum) {
        return new NullSink<>();
    }

    @Override
    public Double reservedDouble() {
        return null;
    }

    @Override
    public Sink<byte[]> forBooleanAsByte(final int colNum) {
        return new NullSink<>();
    }

    @Override
    public Sink<char[]> forChar(final int colNum) {
        return new NullSink<>();
    }

    @Override
    public Character reservedChar() {
        return null;
    }

    @Override
    public Sink<String[]> forString(final int colNum) {
        return new NullSink<>();
    }

    @Override
    public String reservedString() {
        return null;
    }

    @Override
    public Sink<long[]> forDateTimeAsLong(final int colNum) {
        return new NullSink<>();
    }

    @Override
    public Long reservedDateTimeAsLong() {
        return null;
    }

    @Override
    public Sink<long[]> forTimestampAsLong(final int colNum) {
        return new NullSink<>();
    }

    @Override
    public Long reservedTimestampAsLong() {
        return null;
    }

    private static class NullSink<T> implements Sink<T> {
        @Override
        public void write(final T src, final boolean[] isNull, final long destBegin, final long destEnd,
                final boolean appending) {
            // empty on purpose
        }

        @Override
        public Object getUnderlying() {
            return null;
        }
    }
}
