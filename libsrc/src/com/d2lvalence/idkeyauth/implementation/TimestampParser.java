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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses server responses to determine if the delay between the client and server is to large
 * and to help determine the skew if so.
 */
class TimestampParser {
    
    public TimestampParser() {
        
    }
    
    /**
     * Returns the timestamp returned by the server if the time between the request and when the server received it is too large
     * 
     * @param timestampMessage The message returned by the D2L Server
     * @return The timestamp returned by the server or 0 if no timestamp returned
     */
    public long tryParseTimestamp( String timestampMessage ) {
        long result;
        Pattern p = Pattern.compile("Timestamp out of range\\r*\\n*\\s*(([0-9]+))");
        
        Matcher m = p.matcher(timestampMessage);
        boolean match = m.matches();
        
        if( match && m.groupCount() >= 2 ) {
                result = Long.parseLong( m.group(1) );
                return result;
        }
        result = 0;
        return result;
    }
}
