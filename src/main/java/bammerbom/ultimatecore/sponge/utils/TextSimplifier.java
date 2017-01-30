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

import org.spongepowered.api.text.*;
import org.spongepowered.api.text.format.TextFormat;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextSimplifier {
    public static Text simplify(Text text) {
        if (!hasContent(text)) {
            return Text.EMPTY;
        }
        if (text.getChildren().isEmpty()) {
            return text;
        }
        // Simplify children
        final Merger merger = new Merger();
        final List<Text> children = streamChildren(text).filter(TextSimplifier::hasContent).map(merger).filter(child -> !child.isEmpty()).collect(Collectors.toList());
        final Text last = merger.getLast();
        if (!last.isEmpty()) {
            children.add(last);
        }
        // We already have the children, so we can drop them from the original
        text = withoutChildren(text);
        if (children.isEmpty()) {
            return text;
        } else {
            final Text first = children.get(0);
            final Optional<Text> merged = mergeWithChild(text, first);
            final Text.Builder builder;
            if (merged.isPresent()) {
                children.remove(0);
                builder = merged.get().toBuilder();
            } else {
                builder = text.toBuilder();
            }
            builder.append(children);
            return builder.build();
        }
    }

    private static boolean hasContent(final Text text) {
        if (text.isEmpty()) {
            return false;
        }
        if (!(text instanceof LiteralText)) {
            return true;
        }
        if (!((LiteralText) text).getContent().isEmpty()) {
            return true;
        }
        for (final Text child : text.getChildren()) {
            if (hasContent(child)) {
                return true;
            }
        }
        return false;
    }

    public static Text simplyfyChildren(final Text parent, final Text child) {
        if (!hasContent(child)) {
            return Text.EMPTY;
        }
        // Obtain unformated builder
        Text.Builder builder;
        if (child instanceof LiteralText) {
            final LiteralText text = (LiteralText) child;
            builder = Text.builder(text.getContent());
        } else if (child instanceof TranslatableText) {
            final TranslatableText text = (TranslatableText) child;
            builder = Text.builder(text.getTranslation(), text.getArguments());
        } else if (child instanceof SelectorText) {
            final SelectorText text = (SelectorText) child;
            builder = Text.builder(text.getSelector());
        } else if (child instanceof ScoreText) {
            final ScoreText text = (ScoreText) child;
            final ScoreText.Builder textBuilder = Text.builder(text.getScore());
            text.getOverride().ifPresent(textBuilder::override);
            builder = textBuilder;
        } else {
            throw new IllegalArgumentException("Unknown text type: " + child.getClass().getName());
        }
        builder.append(child.getChildren());
        // Only apply format if it differs from panret
        if (!parent.getFormat().equals(child.getFormat())) {
            builder.format(child.getFormat());
        }
        if (!parent.getHoverAction().equals(child.getHoverAction())) {
            child.getHoverAction().ifPresent(builder::onHover);
        }
        if (!parent.getClickAction().equals(child.getClickAction())) {
            child.getClickAction().ifPresent(builder::onClick);
        }
        if (!parent.getShiftClickAction().equals(child.getShiftClickAction())) {
            child.getShiftClickAction().ifPresent(builder::onShiftClick);
        }
        return builder.build();
    }

    private static Stream<Text> explode(final Text text) {
        if (hasStyle(text)) {
            return Stream.of(simplify(text));
        } else {
            return Stream.concat(Stream.of(withoutChildren(text)), streamChildren(text));
        }
    }

    private static Stream<Text> streamChildren(final Text parent) {
        return parent.getChildren().stream().filter(TextSimplifier::hasContent).map(child -> simplyfyChildren(parent, child)).flatMap(TextSimplifier::explode);
    }

    private static boolean hasStyle(final Text text) {
        return text.getFormat() != TextFormat.NONE || text.getHoverAction().isPresent() || text.getClickAction().isPresent() || text.getShiftClickAction().isPresent();
    }

    private static Text withoutChildren(final Text text) {
        return text.toBuilder().removeAll().build();
    }

    public static Optional<Text> merge(final Text first, final Text second) {
        if (first.isEmpty()) {
            return Optional.of(second);
        } else if (second.isEmpty()) {
            return Optional.of(first);
        }
        if (first.getChildren().isEmpty() && first.getFormat().equals(second.getFormat()) && first.getHoverAction().equals(second.getHoverAction()) && first.getClickAction().equals(second.getClickAction()) && first.getShiftClickAction().equals(second.getShiftClickAction())) {
            if (first instanceof LiteralText && second instanceof LiteralText) {
                final LiteralText firstText = (LiteralText) first;
                final LiteralText secondText = (LiteralText) second;
                return Optional.of(forceMerge(firstText, secondText));
            }
        }
        return Optional.empty();
    }

    private static Optional<Text> mergeWithChild(final Text parent, final Text child) {
        if (parent.isEmpty()) {
            return Optional.of(child);
        }
        if (!(parent instanceof LiteralText)) {
            return Optional.empty();
        }
        if (!parent.getChildren().isEmpty()) {
            return Optional.empty();
        }
        if (hasStyle(child)) {
            return Optional.empty();
        }
        final LiteralText parentLiteral = (LiteralText) parent;
        if (child instanceof LiteralText) {
            return Optional.of(forceMerge(parentLiteral, (LiteralText) child));
        }
        if (!parentLiteral.getContent().isEmpty()) {
            return Optional.empty();
        }
        final Text.Builder builder = child.toBuilder();
        parent.getFormat().applyTo(builder);
        parent.getHoverAction().ifPresent(builder::onHover);
        parent.getClickAction().ifPresent(builder::onClick);
        parent.getShiftClickAction().ifPresent(builder::onShiftClick);
        return Optional.of(builder.build());
    }

    private static LiteralText forceMerge(final LiteralText first, final LiteralText second) {
        final LiteralText.Builder builder = Text.builder(first, first.getContent() + second.getContent());
        builder.append(second.getChildren());
        return builder.build();
    }

    private static class Merger implements UnaryOperator<Text> {

        private Text last = Text.EMPTY;

        @Override
        public Text apply(final Text text) {
            final Optional<Text> merged = merge(this.last, text);
            if (merged.isPresent()) {
                this.last = merged.get();
                return Text.EMPTY;
            } else {
                final Text temp = this.last;
                this.last = text;
                return temp;
            }
        }

        public Text getLast() {
            return this.last;
        }

    }

}
