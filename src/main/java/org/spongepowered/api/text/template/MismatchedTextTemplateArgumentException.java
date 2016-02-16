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

/**
 * Exception thrown when a parameter for a {@link TextTemplate} does not match
 * the expected type of the corresponding argument.
 */
public class MismatchedTextTemplateArgumentException extends IllegalArgumentException {

    /**
     * Creates a new exception with the "expected" argument type and the
     * "actual" parameter type that was provided for the specified argument
     * name.
     *
     * @param expected Expected type of the argument
     * @param actual Actual type provided by parameter
     * @param argName The name of the argument that was mismatched
     */
    public MismatchedTextTemplateArgumentException(Class<?> expected, Class<?> actual, String argName) {
        super("Mismatched arguments in TextTemplate. Expected " + expected.getName()
                + " got " + actual.getName() + " for argument \"" + argName + "\".");
    }

}
