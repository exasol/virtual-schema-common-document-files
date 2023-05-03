package com.exasol.adapter.document.testutil.csvgenerator;

import java.sql.Timestamp;
import java.time.*;
import java.util.*;

/**
 * This factory creates new {@link RandomValueGenerator}s for various formats, e.g. date or timestamps.
 */
class RandomValueGeneratorFactory {
    private final Random random;

    RandomValueGeneratorFactory(final Random random) {
        this.random = random;
    }

    RandomValueGenerator createRandomString(final int length) {
        return new RandomString(length, this.random);
    }

    RandomValueGenerator createRandomBoolean() {
        return new RandomBoolean(this.random);
    }

    RandomValueGenerator createRandomInteger() {
        return new RandomInteger(this.random);
    }

    RandomValueGenerator createRandomDouble() {
        return new RandomDouble(this.random);
    }

    RandomValueGenerator createRandomDate() {
        return new RandomDate(this.random);
    }

    RandomValueGenerator createRandomTimestamp() {
        return new RandomTimestamp(this.random);
    }

    private static class RandomBoolean implements RandomValueGenerator {
        private final Random random;

        RandomBoolean(final Random random) {
            this.random = random;
        }

        @Override
        public String nextValue() {
            return String.valueOf(this.random.nextBoolean());
        }
    }

    private static class RandomInteger implements RandomValueGenerator {
        private final Random random;

        RandomInteger(final Random random) {
            this.random = random;
        }

        @Override
        public String nextValue() {
            return String.valueOf(this.random.nextInt());
        }
    }

    private static class RandomDouble implements RandomValueGenerator {
        private final Random random;

        RandomDouble(final Random random) {
            this.random = random;
        }

        @Override
        public String nextValue() {
            return String.valueOf(this.random.nextDouble());
        }
    }

    private static class RandomDate implements RandomValueGenerator {
        private final Random random;

        RandomDate(final Random random) {
            this.random = random;
        }

        @Override
        public String nextValue() {
            final Instant randomInstant = Instant.ofEpochSecond(this.random.nextInt());
            final LocalDate randomDate = LocalDate.ofInstant(randomInstant, ZoneId.of("UTC"));
            return randomDate.toString();
        }
    }

    private static class RandomTimestamp implements RandomValueGenerator {
        private final Random random;

        RandomTimestamp(final Random random) {
            this.random = random;
        }

        @Override
        public String nextValue() {
            final Instant randomInstant = Instant.ofEpochSecond(this.random.nextInt());
            return Timestamp.from(randomInstant).toString();
        }
    }

    /**
     * Generates random alphanumeric strings.
     *
     * Adapted from https://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string
     */
    private static class RandomString implements RandomValueGenerator {
        static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        static final String lower = upper.toLowerCase(Locale.ROOT);
        static final String digits = "0123456789";
        static final String alphanum = upper + lower + digits;
        private final Random random;
        private final char[] symbols;
        private final char[] buf;

        private RandomString(final int length, final Random random, final String symbols) {
            if (length < 1) {
                throw new IllegalArgumentException("Invalid length " + length);
            }
            if (symbols.length() < 2) {
                throw new IllegalArgumentException("Invalid symbols " + symbols);
            }
            this.random = Objects.requireNonNull(random);
            this.symbols = symbols.toCharArray();
            this.buf = new char[length];
        }

        RandomString(final int length, final Random random) {
            this(length, random, alphanum);
        }

        @Override
        public String nextValue() {
            for (int idx = 0; idx < this.buf.length; ++idx) {
                this.buf[idx] = this.symbols[this.random.nextInt(this.symbols.length)];
            }
            return new String(this.buf);
        }
    }
}
