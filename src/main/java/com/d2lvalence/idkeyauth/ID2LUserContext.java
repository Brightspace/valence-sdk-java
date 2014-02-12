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

import java.net.URI;

/**
 * Provides methods to communicate with a server using user credentials and provides some help with handling results and dealing with invalid timestamps
 */
public interface ID2LUserContext {
    
    /**
     * Returns the host name of the D2L server
     * @return The D2L server host name
     */
    public String getHostName();
    
    /**
     * Returns the port used to communicate with the D2L server
     * @return the port used to communicate with the D2L server
     */
    public int getPort();
    
    /**
     * Returns the userID (aka tokenID) of the current user
     * @return The userID (aka tokenID) of the current user
     */
    public String getUserId();
    
    /**
     * Returns the userKey (aka tokenKey) of the current user
     * @return The userKey (aka tokenKey) of the current user
     */
    public String getUserKey();
    
    /**
     * Sets the expected time between when the client sends a request and the server receives it
     * @param millis the expected time between when the client sends a request and the server receives it
     */
    public void setServerSkewMillis(long millis);
    
    /**
     * Returns the expected time between when the client sends a request and the server receives it
     * @return The expected time between when the client sends a request and the server receives it
     */
    public long getServerSkewMillis();

    /**
     * Creates a URI to access the D2L server with the given path and http method
     * @param path The path on the server to call
     * @param httpMethod The http method to call it with
     * @return The resultant URI
     */
    public URI createAuthenticatedUri( String path, String httpMethod );
    
    /**
     * Calculates the time between when the client sends a request and the server receives it
     * @param responseBody The response used to calculate the difference
     */
    public boolean calculateServerSkewFromResponse( String responseBody );
    
    /**
     * 
     * Returned when no result can be identified or as an unitialized value. Val=0x00;
     */
    public final int RESULT_UNKNOWN = 0x00;


    /**
     * 
     * Returned when result 200 okay. Val=0x01
     */
    public final int RESULT_OKAY = 0x01;

    /**
     * Returned when the signature or id was invalid, typically this should trigger a reauthentication. Val=0x02
     */
    public final int RESULT_INVALID_SIG = 0x02;
   
    /**
     * Returned if the timestamp was outside of the validity window, this indicates clocks are skewed. The handleResult
     * message automatically corrects the clock so on receiving this message callers typically should retry the same operation. Val=0x03
     */
    public final int RESULT_INVALID_TIMESTAMP = 0x03;
    
    /**
     * Returned if the requested operation is not allowed, typically user should be prompted that they need to request
     * different permissions from the administrator. Val = 0x04
     */
    public final int RESULT_NO_PERMISSION = 0x04;
    
    /**
     * This utility method can be used to process results from the server, it interprets http result code and
     * optional body messages on error (e.g. distinguishing timestamp skew from bad signature). In addition, 
     * in the special case of timestamp skew this method will update the internal skew so that the next request 
     * should avoid this error. 
     * @param resultCode - http result code, can include the successful result of 2XX in which case the stream is not read 
     * @param responseBody The message body returned by the server 
     * @return one of the RESULT_* results from this interface. Most commonly applications will minimally want to check 
     * for RESULT_OKAY in order to proceed with interpretting results.
     */
    public int interpretResult(int resultCode, String responseBody);
    
    /**
     * Returns the parameters used to authenticate with the user in this user context. This allows them to be cached and reloaded.
     * 
     * @return The UserContextParameters which contain information for this ID2LUserContext
     */
    public D2LUserContextParameters getParameters();
}
