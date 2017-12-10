/*
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.cielyang.android.login.common.utils;

/**
 * Static convenience methods that help a method or constructor check whether it was invoked
 * correctly (that is, whether its <i>preconditions</i> were met).
 *
 * <h3>Full Source Code</h3>
 *
 * <p>See the GitHub file <a
 * href="https://github.com/google/guava/blob/master/guava/src/com/google/common/base
 * /Preconditions.java">
 * Preconditions</a>
 *
 * <h3>More information</h3>
 *
 * <p>See the Guava User Guide on <a
 * href="https://github.com/google/guava/wiki/PreconditionsExplained">using Preconditions</a>.
 *
 * @author Kevin Bourrillion
 * @since 2.0
 */
public class PreconditionUtils {

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }
}
