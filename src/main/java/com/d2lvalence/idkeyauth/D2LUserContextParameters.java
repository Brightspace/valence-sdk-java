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

import java.io.Serializable;

/**
 * Stores parameters for a user context in a format that can be easily
 * serialized for caching purposes
 */
public class D2LUserContextParameters implements Serializable {

    String _userId;
    String _userKey;

    /**
     * Creates D2LUserContextParameters with the parameters provided
     *
     * @param userId The D2L user ID to be used
     * @param userKey The D2L user key to be used
     */
    public D2LUserContextParameters(String userId, String userKey) {
        this._userId = userId;
        this._userKey = userKey;
    }

    /**
     * Returns the user ID (aka token ID) of the current user
     *
     * @return the user ID (aka token ID) of the current user
     */
    public String getUserId() {
        return _userId;
    }

    /**
     * Returns the user key (aka token key) of the current user
     *
     * @return the user key (aka token key) of the current user
     */
    public String getUserKey() {
        return _userKey;
    }

}
