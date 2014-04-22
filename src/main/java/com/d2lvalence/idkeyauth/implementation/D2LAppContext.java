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
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * An instance of ID2LAppContext
 *
 * @see ID2LAppContext
 */
public class D2LAppContext implements ID2LAppContext {
    
    private static final String APP_ID_PARAMETER = "x_a";
    private static final String APP_KEY_PARAMETER = "x_b";
    private static final String CALLBACK_URL_PARAMETER = "x_target";
    private static final String TYPE_PARAMETER = "type";
    private static final String TYPE_PARAMETER_VALUE = "mobile";
    private static final String USER_ID_CALLBACK_PARAMETER = "x_a";
    private static final String USER_KEY_CALLBACK_PARAMETER = "x_b";

    private final String _appId;
    private final String _appKey;
    private String _url;

    /**
     * Constructs a D2LAppContext with the provided application values
     *
     * @param appId The application ID provided by the key tool
     * @param appKey The application key provided by the key tool
     * @param url The url of the D2L instance
     */
    public D2LAppContext(String appId, String appKey, String url) {
        _appId = appId;
        _appKey = appKey;
        if(url != null && url.endsWith("/")){
            _url = url.substring(0, url.lastIndexOf("/"));
        }else {
            _url = url;
        }
    }

    public URI createWebUrlForAuthentication(URI redirectUrl) {
        try {
            URI uri = new URI(_url + D2LConstants.AUTHENTICATION_SERVICE_URI_PATH + "?" + buildAuthenticationUriQueryString(redirectUrl));
            return uri;
        } catch (URISyntaxException e) {
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

    public ID2LUserContext createUserContext(URI uri) {
        if(uri.getQuery() != null){
            HashMap<String, String> r = getParameters(uri.getQuery());
            //use a mock servlet request so we can retrieve parameters from the uri conveniently
            String userId = r.get(USER_ID_CALLBACK_PARAMETER);
            String userKey = r.get(USER_KEY_CALLBACK_PARAMETER);
            if (userId == null || userKey == null) {
                return null;
            }
            return new D2LUserContext(_url, _appId, _appKey, userId, userKey);
        } else {
            return null;
        }
    }

    public ID2LUserContext createUserContext(String userId, String userKey) {
        return new D2LUserContext(_url, _appId, _appKey, userId, userKey);
    }

    public ID2LUserContext createAnonymousUserContext() {
        return new D2LUserContext(_url, _appId, _appKey, null, null);
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
        } catch (UnsupportedEncodingException e) {
            result += "&" + CALLBACK_URL_PARAMETER + "=" + URLEncoder.encode(callbackUriString);
        }
        return result;
    }

    public String getAppId() {
        return _appId;
    }

    public String getAppKey() {
        return _appKey;
    }

    public String getUrl() {
        return _url;
    }

    @Deprecated
    public ID2LUserContext createUserContext(D2LUserContextParameters parameters) {
        return this.createUserContext(parameters.getUserId(), parameters.getUserKey());
    }

    @Deprecated
    public URI createWebUrlForAuthentication(String host, int port, URI resultUri) {
        return createWebUrlForAuthentication(host, port, true, resultUri);
    }

    @Deprecated
    public URI createWebUrlForAuthentication(String host, int port, boolean encryptOperations, URI resultUri) {
        String protocol = encryptOperations ? D2LConstants.URI_SECURE_SCHEME : D2LConstants.URI_UNSECURE_SCHEME;
        this._url = protocol + "://" + host + ":" + port;
        return this.createWebUrlForAuthentication(resultUri);
    }

    @Deprecated
    public ID2LUserContext createUserContext(URI uri, String hostName, int port, boolean encryptOperations) {
        String protocol = encryptOperations ? D2LConstants.URI_SECURE_SCHEME : D2LConstants.URI_UNSECURE_SCHEME;
        this._url = protocol + "://" + hostName + ":" + port;
        return this.createUserContext(uri);
    }

    @Deprecated
    public ID2LUserContext createUserContext(String userId, String userKey, String hostName, int port, boolean encryptOperations) {
        String protocol = encryptOperations ? D2LConstants.URI_SECURE_SCHEME : D2LConstants.URI_UNSECURE_SCHEME;
        this._url = protocol + "://" + hostName + ":" + port;
        return this.createUserContext(userId, userKey);
    }

    @Deprecated
    public ID2LUserContext createAnonymousUserContext(String hostName, int port, boolean encryptOperations) {
        String protocol = encryptOperations ? D2LConstants.URI_SECURE_SCHEME : D2LConstants.URI_UNSECURE_SCHEME;
        this._url = protocol + "://" + hostName + ":" + port;
        return this.createAnonymousUserContext();
    }

}
