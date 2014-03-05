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
@Deprecated
public class D2LUserContextParameters implements Serializable {

    String _userId;
    String _userKey;
    String _appId;
    String _appKey;
    String _url;

    /**
     * Creates D2LUserContextParameters with the parameters provided
     *
     * @param appId The application ID provided by the key tool
     * @param appKey The application key provided by the key tool
     * @param userId The D2L user ID to be used
     * @param userKey The D2L user key to be used
     * @param url The url of the D2L instance
     */
    @Deprecated
    public D2LUserContextParameters(String appId, String appKey, String userId, String userKey, String url) {
        this._userId = userId;
        this._userKey = userKey;
        this._appId = appId;
        this._appKey = appKey;
        this._url = url;
    }

    /**
     * Returns the Application ID of the application
     *
     * @return the Application ID of the application
     */
    @Deprecated
    public String getAppId() {
        return _appId;
    }

    /**
     * Returns the Application Key of the application
     *
     * @return the Application Key of the application
     */
    @Deprecated
    public String getAppKey() {
        return _appKey;
    }

    /**
     * Returns the Url that will be used to access the D2L instance
     *
     * @return the Url of the D2L instance
     */
    @Deprecated
    public String getUrl() {
        return _url;
    }

    /**
     * Returns the user ID (aka token ID) of the current user
     *
     * @return the user ID (aka token ID) of the current user
     */
    @Deprecated
    public String getUserId() {
        return _userId;
    }

    /**
     * Returns the user key (aka token key) of the current user
     *
     * @return the user key (aka token key) of the current user
     */
    @Deprecated
    public String getUserKey() {
        return _userKey;
    }

}
