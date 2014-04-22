/**
 * D2LValence package, auth module.
 *
 * Copyright (c) 2012 Desire2Learn Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the license at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.d2lvalence.idkeyauth.implementation;

/**
 * Provides an instance of ITimestampProvider which returns the current time in milliseconds from the epoch.
 * 
 * @see ITimestampProvider
 */
class DefaultTimestampProvider implements ITimestampProvider {

    /**
     * Provides the current time in milliseconds
     * 
     * @return The time in milliseconds
     */
    public long getCurrentTimestampInMilliseconds() {
        return System.currentTimeMillis();
    }

}

