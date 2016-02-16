/*
 * This file is part of SpongeAPI, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
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
package org.spongepowered.api.text.template;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextElement;
import org.spongepowered.api.text.channel.MessageReceiver;

import java.util.Map;
import java.util.function.Function;

/**
 * Represents a re-usable template that produces a formatted
 * {@link Text.Builder}. Elements will be appended to the result builder in the
 * order that they are specified in {@link #of(Object...)}.
 *
 * TODO: Serialization
 */
public class TextTemplate {
    final Object[] elements;

    TextTemplate(Object... elements) {
        this.elements = elements;
    }

    /**
     * Evaluates this TextTemplate and returns the result in a
     * {@link Text.Builder}.
     *
     * @param params Parameters to evaluate
     * @return Text builder containing result
     * @throws RequiredTextTemplateArgumentException if required parameters are missing
     */
    public Text.Builder eval(Map<String, Object> params) {
        Text.Builder result = Text.builder();
        for (Object element : this.elements) {
            appendElement(result, element, params);
        }
        return result;
    }

    private void appendElement(Text.Builder builder, Object element, Map<String, Object> params) {
        if (element instanceof TextElement) {
            ((TextElement) element).appendTo(builder);
        } else if (element instanceof Arg) {
            Arg<?> arg = (Arg<?>) element;
            Object param = params.get(arg.name);
            if (param == null) {
                if (!arg.optional) {
                    throw new RequiredTextTemplateArgumentException(arg.name);
                }
            } else {
                arg.eval(param).appendTo(builder);
            }
        } else {
            builder.append(Text.of(element.toString()));
        }
    }

    /**
     * Sends the result of this TextTemplate with the specified parameters to
     * the specified {@link MessageReceiver}.
     *
     * @param receiver Receiver to send message to
     * @param params Parameters to apply to template
     */
    public void send(MessageReceiver receiver, Map<String, Object> params) {
        receiver.sendMessage(eval(params).build());
    }

    /**
     * Constructs a new TextTemplate for the given elements. The order of the
     * elements is the order in which they will be appended to the result
     * builder via {@link #eval(Map)}.
     *
     * The provided elements may be of any type.
     *
     * In the case that an element is a {@link TextElement},
     * {@link TextElement#appendTo(Text.Builder)} will be used to append the
     * element to the builder.
     *
     * In the case that an element is an {@link Arg} the argument will be
     * replaced with a {@link TextElement} object via the {@link Function}
     * provided by {@link #arg(String, Class, Function, boolean)}. If no
     * function was provided the argument expects a TextElement type. The
     * parameter value in the provided map in {@link #eval(Map)} must match the
     * type of the input for the arguments Function or a
     * {@link MismatchedTextTemplateArgumentException} will be thrown.
     *
     * In the case that an element is any other type, the parameter value's
     * {@link Object#toString()} method will be used to create a {@link Text}
     * object.
     *
     * @param elements Elements to append to builder
     * @return Newly constructed TextTemplate
     */
    public static TextTemplate of(Object... elements) {
        return new TextTemplate(elements);
    }

    /**
     * Constructs a new {@link Arg} to be supplied to {@link #of(Object...)}.
     * The argument evaluates it's output by applying a {@link Function} that
     * takes any type as it's input and returns a {@link TextElement} as
     * it's output.
     *
     * @param name Name of parameter
     * @param type Type of parameter
     * @param f Function that takes "type" as the input and returns Text as the output
     * @param optional Whether this argument is allowed to be omitted
     * @param <T> Input type of argument
     * @return Newly constructed Arg
     */
    public static <T> Arg<T> arg(String name, Class<T> type, Function<T, TextElement> f, boolean optional) {
        return new Arg<>(name, type, f, optional);
    }

    /**
     * Constructs a new {@link Arg} to be supplied to {@link #of(Object...)}.
     * The argument evaluates it's output by applying a {@link Function} that
     * takes any type as it's input and returns a {@link TextElement} as it's output.
     *
     * @param name Name of parameter
     * @param type Type of parameter
     * @param f Function that takes "type" as the input and returns Text as the output
     * @param <T> Input type of argument
     * @return Newly constructed Arg
     */
    public static <T> Arg<T> arg(String name, Class<T> type, Function<T, TextElement> f) {
        return arg(name, type, f, false);
    }

    /**
     * Constructs a new {@link Arg} to be supplied to {@link #of(Object...)}.
     * This argument expects a {@link TextElement} parameter.
     *
     * @param name Name of parameter
     * @param optional Whether this argument is allowed to be omitted.
     * @return Newly constructed Arg
     */
    public static Arg<TextElement> arg(String name, boolean optional) {
        return new Arg<>(name, TextElement.class, t -> t, optional);
    }

    /**
     * Constructs a new {@link Arg} to be supplied to {@link #of(Object...)}.
     * This argument expects a {@link TextElement} parameter.
     *
     * @param name Name of parameter
     * @return Newly constructed Arg
     */
    public static Arg<TextElement> arg(String name) {
        return arg(name, false);
    }

    /**
     * Represents a variable element within a TextTemplate. Each argument has
     * an input type and all arguments return a {@link TextElement} as it's
     * output.
     *
     * @param <T> Input type
     */
    static class Arg<T> {
        final String name;
        final Class<T> type;
        final Function<T, TextElement> f;
        final boolean optional;

        /**
         * Evaluates the argument for the specified parameter.
         *
         * @param param Parameter to evaluate
         * @return TextElement output
         */
        @SuppressWarnings("unchecked")
        TextElement eval(Object param) {
            Class<?> paramType = param.getClass();
            if (!this.type.isAssignableFrom(paramType)) {
                throw new MismatchedTextTemplateArgumentException(this.type, paramType, this.name);
            }
            return this.f.apply((T) param);
        }

        Arg(String name, Class<T> type, Function<T, TextElement> f, boolean optional) {
            this.name = name;
            this.type = type;
            this.f = f;
            this.optional = optional;
        }
    }

}
