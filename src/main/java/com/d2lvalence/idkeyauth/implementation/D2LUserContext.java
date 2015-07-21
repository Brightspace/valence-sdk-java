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
import com.d2lvalence.idkeyauth.ID2LUserContext;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * An instance of ID2LUserContext
 *
 * @see ID2LUserContext
 */
public class D2LUserContext implements ID2LUserContext {

    private final String instanceUrl;
    private final String appId;
    private final String appKey;
    private final String userId;
    private final String userKey;

    private long _serverSkewMillis;
    private final ITimestampProvider _timestampProvider;

    private static final String APP_ID_PARAMETER = "x_a";
    private static final String USER_ID_PARAMETER = "x_b";
    private static final String SIGNATURE_BY_APP_KEY_PARAMETER = "x_c";
    private static final String SIGNATURE_BY_USER_KEY_PARAMETER = "x_d";
    private static final String TIMESTAMP_PARAMETER = "x_t";

    /**
     * Creates a User Context with the provided parameters
     *
     * @param url the url of the D2Linstance
     * @param appId the Application Id
     * @param appKey The Application Key
     * @param userId the User Id
     * @param userKey the User Key
     */
    public D2LUserContext(String url, String appId, String appKey, String userId, String userKey) {
        this.instanceUrl = url;
        this.appId = appId;
        this.appKey = appKey;
        this.userId = userId;
        this.userKey = userKey;
        _timestampProvider = new DefaultTimestampProvider();
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String getUserKey() {
        return userKey;
    }

    @Override
    public long getServerSkewMillis() {
        return _serverSkewMillis;
    }

    @Override
    public void setServerSkewMillis(long _serverSkewMillis) {
        this._serverSkewMillis = _serverSkewMillis;
    }

    @Override
    public URI createAuthenticatedUri(String path, String httpMethod) {
        int split = path.indexOf("?");
        String query = "";
        if (split >= 0) {
            query = path.substring(split + 1);
            path = path.substring(0, split);
        }
        String queryString = getQueryString(path, query, httpMethod);
        try {
            URI uri = new URI(instanceUrl + path + queryString);
            return uri;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean calculateServerSkewFromResponse(String responseBody) {
        TimestampParser timestampParser = new TimestampParser();
        long serverTimestampSeconds = timestampParser.tryParseTimestamp(responseBody);
        if (serverTimestampSeconds > 0) {
            long clientTimestampMilliseconds = _timestampProvider.getCurrentTimestampInMilliseconds();
            _serverSkewMillis = serverTimestampSeconds * 1000 - clientTimestampMilliseconds;
            return true;
        }
        return false;
    }

    /**
     * Constructs the query string with the appropriate parameters for
     * authentication with the D2L authentication system.
     *
     * @param signature The signature based on the path, http method and time
     * @param timestamp The timestamp for the query string
     * @return A query string with the relevant authentication parameters
     */
    private String buildAuthenticatedUriQueryString(String signature, long timestamp) {
        String queryString = "?" + APP_ID_PARAMETER + "=" + appId;
        if (getUserId() != null) {
            queryString += "&" + USER_ID_PARAMETER + "=" + getUserId();
        }
        queryString += "&" + SIGNATURE_BY_APP_KEY_PARAMETER;
        queryString += "=" + D2LSigner.getBase64HashString(appKey, signature);
        if (getUserId() != null) {
            queryString += "&" + SIGNATURE_BY_USER_KEY_PARAMETER;
            queryString += "=" + D2LSigner.getBase64HashString(getUserKey(), signature);
        }
        queryString += "&" + TIMESTAMP_PARAMETER + "=" + timestamp;
        return queryString;
    }

    /**
     * Provides the unix timestamp adjusted for the approximate delay between
     * the D2L server and client
     *
     * @return The unix timestamp adjusted for the approximate delay between the
     * D2L server and client
     */
    private long getAdjustedTimestampInSeconds() {
        long timestampMilliseconds = _timestampProvider.getCurrentTimestampInMilliseconds();
        long adjustedTimestampSeconds = (timestampMilliseconds + _serverSkewMillis) / 1000;
        return adjustedTimestampSeconds;
    }

    /**
     * Constructs the query string with the appropriate parameters for
     * authentication with the D2L authentication system.
     *
     * @param path The absolute server path of the api (ie. /d2l/api/versions/)
     * @param httpMethod The http method to access the url with (GET,POST,etc.)
     * @return The query string with the appropriate parameters for
     * authentication
     */
    private String getQueryString(String path, String query, String httpMethod) {
        long adjustedTimestampSeconds = getAdjustedTimestampInSeconds();
        String signature = formatSignature(path, httpMethod, adjustedTimestampSeconds);
        String queryString = buildAuthenticatedUriQueryString(signature, adjustedTimestampSeconds);
        if (query != null) {
            queryString += "&" + query;
        }
        return queryString;
    }

    /**
     * Creates a signature formatted to the D2L specifications for connecting to
     * a given path
     *
     * @param path The absolute server path of the api (ie. /d2l/api/versions/)
     * @param httpMethod The http method to access the url with (GET,POST,etc.)
     * @param timestampSeconds
     * @return
     */
    private static String formatSignature(String path, String httpMethod, long timestampSeconds) {
        return httpMethod.toUpperCase() + "&" + URI.create(path).getPath().toLowerCase() + "&" + timestampSeconds;
    }

    @Override
    public int interpretResult(int resultCode, String responseBody) {
        if (resultCode == 200) {
            return D2LUserContext.RESULT_OKAY;
        } else if (resultCode == 403) {
            if (calculateServerSkewFromResponse(responseBody)) {
                return D2LUserContext.RESULT_INVALID_TIMESTAMP;
            } else if (responseBody.toLowerCase().equals("invalid token") || responseBody.toLowerCase().equals("token expired")) {
                return D2LUserContext.RESULT_INVALID_SIG;
            } else {
                return D2LUserContext.RESULT_NO_PERMISSION;
            }
        }
        return D2LUserContext.RESULT_UNKNOWN;
    }

    public String getHostName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getPort() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Deprecated
    public D2LUserContextParameters getParameters() {
        return new D2LUserContextParameters(appId, appKey, userId, userKey, userId);
    }

}
