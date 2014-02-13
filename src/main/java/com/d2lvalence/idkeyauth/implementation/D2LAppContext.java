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
package com.d2lvalence.idkeyauth.implementation;

import com.d2lvalence.idkeyauth.D2LUserContextParameters;
import com.d2lvalence.idkeyauth.ID2LAppContext;
import com.d2lvalence.idkeyauth.ID2LUserContext;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * An instance of ID2LAppContext
 *
 * @see ID2LAppContext
 */
public class D2LAppContext implements ID2LAppContext {

    private final String _appId;
    private final String _appKey;

    private final String APP_ID_PARAMETER = "x_a";
    private final String APP_KEY_PARAMETER = "x_b";
    private final String CALLBACK_URL_PARAMETER = "x_target";
    private final String TYPE_PARAMETER = "type";
    private final String TYPE_PARAMETER_VALUE = "mobile";
    private final String USER_ID_CALLBACK_PARAMETER = "x_a";
    private final String USER_KEY_CALLBACK_PARAMETER = "x_b";

    /**
     * Constructs a D2LAppContext with the provided application values
     *
     * @param appId The application ID provided by the key tool
     * @param appKey The application key provided by the key tool
     */
    public D2LAppContext(String appId, String appKey) {
        _appId = appId;
        _appKey = appKey;
    }

    @Override
    public URI createWebUrlForAuthentication(String url, URI resultUri) {
        try {
            URI uri = new URI(url + Constants.AUTHENTICATION_SERVICE_URI_PATH + "?" + buildAuthenticationUriQueryString(resultUri));
            //uri. = BuildAuthenticationUriQueryString( resultUri );
            return uri;
        } catch (Exception e) {
            return null;
        }
    }

    private HashMap<String, String> getParameters(String queryString) {
        HashMap<String, String> result = new HashMap<String, String>();
        String[] strings = queryString.split(("&"));
        for (String s : strings) {
            String[] split = s.split("=");
            if (split.length == 2) {
                result.put(split[0], split[1]);
            }
        }
        return result;
    }

    @Override
    public ID2LUserContext createUserContext(URI uri, String url) {

        HashMap<String, String> r = getParameters(uri.getQuery());
        //use a mock servlet request so we can retrieve parameters from the uri convenientyl
        String userId = r.get(USER_ID_CALLBACK_PARAMETER);
        String userKey = r.get(USER_KEY_CALLBACK_PARAMETER);
        if (userId == null || userKey == null) {
            return null;
        }
        D2LUserContextParameters parameters = compileOperationSecurityParameters(userId, userKey, url);
        return new D2LUserContext(parameters);//new D2LOperationSecurity( parameters );
    }

    @Override
    public ID2LUserContext createUserContext(String userId, String userKey, String url) {
        D2LUserContextParameters parameters = compileOperationSecurityParameters(userId, userKey, url);
        return new D2LUserContext(parameters);
    }

    @Override
    public ID2LUserContext createAnonymousUserContext(String url) {
        D2LUserContextParameters parameters = compileOperationSecurityParameters(null, null, url);
        return new D2LUserContext(parameters);
    }

    @Override
    public ID2LUserContext createUserContext(D2LUserContextParameters parameters) {
        return new D2LUserContext(parameters);
    }

    /**
     * Constructs a URI to call for authentication given the target URI provided
     *
     * @param callbackUri The target which the D2L server should return to after
     * authenticating
     * @return The URI for the user to authenticate against
     */
    private String buildAuthenticationUriQueryString(URI callbackUri) {
        String callbackUriString = callbackUri.toString();
        String uriHash = D2LSigner.getBase64HashString(_appKey, callbackUriString);
        String result = APP_ID_PARAMETER + "=" + _appId;
        result += "&" + APP_KEY_PARAMETER + "=" + uriHash;
        try {
            result += "&" + CALLBACK_URL_PARAMETER + "=" + URLEncoder.encode(callbackUriString, "UTF-8");
        } catch (Exception e) {
            result += "&" + CALLBACK_URL_PARAMETER + "=" + URLEncoder.encode(callbackUriString);
        }
        //result += "&" + TYPE_PARAMETER + "=" + TYPE_PARAMETER_VALUE;
        return result;
    }

    /**
     * Stores the provided parameters in OperationSecurityParameters so that
     * they can be saved and loaded via serialization
     *
     * @param userId The D2L user ID to be stored
     * @param userKey The D2L user key to be stored
     * @param hostName The host name of the D2L server
     * @param port The port to connect to the D2L server on
     * @param encryptOperations Whether the connection should be encrypted
     * @see OperationSecurityParameters
     * @return
     */
    private D2LUserContextParameters compileOperationSecurityParameters(String userId, String userKey, String url) {
        D2LUserContextParameters parameters = new D2LUserContextParameters(_appId, _appKey, userId, userKey, url);
        return parameters;
    }

}
