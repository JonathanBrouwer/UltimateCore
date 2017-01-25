/*
 * This file is part of UltimateCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) Bammerbom
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package bammerbom.ultimatecore.sponge.utils;

import org.spongepowered.api.util.Tuple;

public class Tuples {

    private Tuples() {
    }

    public static <A, B> Tuple<A, B> of(A a, B b) {
        return new Tuple<>(a, b);
    }

    public static <A, B, C> Tri<A, B, C> of(A a, B b, C c) {
        return new Tri<>(a, b, c);
    }

    public static <A, B, C, D> Quad<A, B, C, D> of(A a, B b, C c, D d) {
        return new Quad<>(a, b, c, d);
    }

    public static class Tri<A, B, C> extends Tuple {

        private final A a;
        private final B b;
        private final C bc;

        private Tri(A a, B b, C bc) {
            super(a, b);
            this.a = a;
            this.b = b;
            this.bc = bc;
        }

        @Override
        public A getFirst() {
            return a;
        }

        @Override
        public B getSecond() {
            return b;
        }

        public C getThird() {
            return bc;
        }
    }

    public static class Quad<A, B, C, D> extends Tri<A, B, C> {

        private final D d;

        private Quad(A a, B b, C bc, D d) {
            super(a, b, bc);
            this.d = d;
        }

        public D getFourth() {
            return d;
        }
    }
}