/**
 * D2LValence package, auth module.
 *
 * Copyright (c) 2012 Desire2Learn Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the license at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.d2lvalence.idkeyauth;

import com.d2lvalence.idkeyauth.implementation.D2LAppContext;

/**
 * A class for providing an instance of ID2LAppContext
 */
public class AuthenticationSecurityFactory {

    /**
     * Provides an instance of ID2LAppContext for managing connections to D2L
     * servers with the application information
     *
     * @param appId The application ID provided by the key tool
     * @param appKey The application key provided by the key tool
     * @return An ID2LAppContext instance for managing connections to D2L
     * servers with the given application information
     * @see ID2LAppContext
     */
    public ID2LAppContext createSecurityContext(String appId, String appKey) {
        return new D2LAppContext(appId, appKey);
    }
}
