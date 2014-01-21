/*
 * Copyright (C) 2006 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.annotations;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.meta.When;

import com.google.common.base.Objects;

/**
 * Annotates a program element that exists, or is more widely visible than
 * otherwise necessary, only for use in test code.
 *
 * @author Johannes Henkel
 */
@GwtCompatible
public @interface VisibleForTesting {
    Visibility original() default Visibility.PRIVATE;

    // TODO This should return When.NEVER when visibility of annotated Class (or Field or Method) is narrower than original()
    // TODO TypeQualifierValidator is only for constant value. How to validate annotated Class (or Field or Method)?
    When when() default When.UNKNOWN;

    static enum Visibility {
        PROTECTED {
            @Override
            @CheckReturnValue
            public boolean allowsAccess(Class<?> from, Class<?> to) {
                checkNotNull(from);
                checkNotNull(to);

                Class<?> superClass = from.getSuperclass();
                while (superClass != null) {
                    if (Objects.equal(superClass, to)) {
                        return true;
                    }
                    superClass = superClass.getSuperclass();
                };

                return PACKAGE.allowsAccess(from, to);
            }
        },
        PACKAGE {
            @Override
            @CheckReturnValue
            public boolean allowsAccess(Class<?> from, Class<?> to) {
                checkNotNull(from);
                checkNotNull(to);

                return Objects.equal(from.getPackage(), to.getPackage());
            }
        },
        PRIVATE {
            @Override
            @CheckReturnValue
            public boolean allowsAccess(Class<?> from, Class<?> to) {
                checkNotNull(from);
                checkNotNull(to);

                return Objects.equal(declaringClassOf(from), declaringClassOf(to));
            }

            private Object declaringClassOf(Class<?> clazz) {
                while (clazz.getDeclaringClass() != null) {
                    clazz = clazz.getDeclaringClass();
                }
                return clazz;
            }
        };

        @CheckReturnValue
        public abstract boolean allowsAccess(@Nonnull Class<?> from, @Nonnull Class<?> to);

        @CheckReturnValue
        public boolean allowsAccess(@Nonnull Class<?> from, @Nonnull Method to) {
            return allowsAccess(from, to.getDeclaringClass());
        }

        @CheckReturnValue
        public boolean allowsAccess(@Nonnull Class<?> from, @Nonnull Field to) {
            return allowsAccess(from, to.getDeclaringClass());
        }
    }
}
