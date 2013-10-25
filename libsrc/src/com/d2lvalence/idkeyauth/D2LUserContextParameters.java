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
package com.d2lvalence.idkeyauth;

import java.io.Serializable;

/**
 * Stores parameters for a user context in a format that can be easily serialized for caching purposes
 */
public class D2LUserContextParameters implements Serializable {

    /**
     * Creates D2LUserContextParameters with the parameters provided
     * @param appId The application ID provided by the key tool 
     * @param appKey  The application key provided by the key tool
     * @param userId The D2L user ID to be used
     * @param userKey The D2L user key to be used
     * @param hostName The host name of the D2L server
     * @param port The port to connect to the D2L server on
     * @param encryptOperations Whether the connection should be encrypted
     */
    public D2LUserContextParameters(  String appId, String appKey, String userId, String userKey, String hostName, int port, boolean encryptOperations) {
        this._port = port;
        this._userId = userId;
        this._userKey = userKey;
        this._appId = appId;
        this._appKey = appKey;
        this._hostName = hostName;
        this._encryptOperations = encryptOperations;
    }

    
    /**
     * Returns the Application ID of the application
     * 
     * @return the Application ID of the application
     */
    public String getAppId() {
        return _appId;
    }

    /**
     * Returns the Application Key of the application 
     * 
     * @return the Application Key of the application
     */
    public String getAppKey() {
        return _appKey;
    }

    /**
     * Returns whether communication with the D2L server should be encrypted
     * 
     * @return whether communication with the D2L server should be encrypted
     */
    public boolean isEncryptOperations() {
        return _encryptOperations;
    }


    /**
     * Returns the host name of the D2L server
     * 
     * @return the host name of the D2L server
     */
    public String getHostName() {
        return _hostName;
    }

    /**
     * Returns the port to connect to on the D2L server
     * 
     * @return the port to connect to on the D2L server
     */
    public int getPort() {
        return _port;
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
    
    int _port;
    String _userId;
    String _userKey;
    String _appId;
    String _appKey;
    String _hostName;// { get; set; }
    boolean _encryptOperations;// { get; set; }
}
