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

import java.net.URI;

/**
 * Stores the state of the application (app ID and app key) and provides methods
 * for authentication and for creating instances of ID2LUserContext with the
 * appropriate information.
 */
public interface ID2LAppContext {

    /**
     * Provides the URL on the D2L server for users to authenticate against,
     * with the option to not use HTTPS
     *
     * @param host The host name of the server to authenticate to
     * @param port The port to authenticate with
     * @param encryptOperations True for https, false for http
     * @param resultUri The URI which the server should redirect to after the
     * user has authenticated
     * @return A URI to redirect the user to
     */
    URI createWebUrlForAuthentication(String url, URI resultUri);

    /**
     * Creates an instance of ID2LUserContext with user parameters specified in
     * the URI
     *
     * @param uri The URI containing the user id and user key returned by the
     * server
     * @param hostName The host name of the D2L server
     * @param port The port to connect to the D2L server on
     * @param encryptOperations Whether the connection should be encrypted
     * @return An instance of ID2LUserContext with user parameters specified in
     * the URI
     * @see ID2LUserContext
     */
    ID2LUserContext createUserContext(URI uri, String url);

    /**
     * Creates an instance of ID2LUserContext with the parameters provided
     *
     * @param userId The D2L user ID to be used
     * @param userKey The D2L user key to be used
     * @param hostName The host name of the D2L server
     * @param port The port to connect to the D2L server on
     * @param encryptOperations Whether the connection should be encrypted
     * @return An instance of ID2LUserContext with user parameters specified in
     * the URI
     * @see ID2LUserContext
     */
    ID2LUserContext createUserContext(String userId, String userKey, String url);

    /**
     * Creates an instance of ID2LUserContext without user credentials
     *
     * @param hostName The host name of the D2L server
     * @param port The port to connect to the D2L server on
     * @param encryptOperations Whether the connection should be encrypted
     * @return An instance of ID2LUserContext without user credentials
     * @see ID2LUserContext
     */
    public ID2LUserContext createAnonymousUserContext(String url);

    /**
     * Creates an instance of ID2LUserContext from the given parameters
     *
     * @param parameters The parameters to create the ID2LUserContext with
     * @return The new ID2LUserContext
     */
    public ID2LUserContext createUserContext(D2LUserContextParameters parameters);

}
