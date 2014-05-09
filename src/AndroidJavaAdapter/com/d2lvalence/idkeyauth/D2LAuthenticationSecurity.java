//Copyright 2011 Desire2Learn Incorporated
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.

package com.d2lvalence.idkeyauth;

import android.net.Uri;

/**
 * Performs operations related to app level security.
 */

public interface D2LAuthenticationSecurity {

	public static class Util {
		public static int getPort(boolean encryptOperations) {
			return (encryptOperations ? 443 : 80); 
		}
	}
	
	/**
	 * Using the application id and key passed from D2LSecurityFactory at
	 * instantiation time and the callback uri this object will create a uri
	 * properly signed to request authentication and authorization In a browser
	 * window. Native apps must be able to intercept the uri provided.
	 * 
	 * @param resultURI
	 *            - a call back uri that will be invoked by the browser, it will
	 *            have additional parameters appended to the query string when
	 *            the user has authenticated and authorized this application.
	 * 
	 * @return uri object to open in a browser.
	 */

	Uri createWebURLForAuthentication(String host, Uri resultURI);

	Uri createWebURLForAuthentication(String host, Uri resultURI, boolean encryptOperations);

	/**
	 * This method should be called when the resultURL from a call to
	 * createWebURLForAuthentication is received. It will extract the user
	 * specific id and key and create a D2LOperationSecurity object that can be
	 * used to generate subsequent calls.
	 * 
	 * @param resultURL
	 *            - the uri as received from the browser, this will be the same
	 *            as the uri passed to createWebURLForAuthentication but with a
	 *            user specific field values
	 * @param hostName
	 *            - the host that was originally logged into (host pass to to
	 *            createWebURLForAuthentication)
	 * @param encryptOperations
	 *            - true if ssl should be used
	 * 
	 * @return a D2LOperationSecurity object to be used to sign API requests in
	 *         the users context.
	 * 
	 */

	D2LOperationSecurity createOperationContextFromResult(Uri resultURI, String hostName, boolean encryptOperations);

	/**
	 * This method should be called from non interactive components or on
	 * interactive components that already have determined a userID and userKey
	 * that they want to use. (These values can be extracted from
	 * D2LOperationSecurity objects and retained between instantiations of
	 * apps).
	 * 
	 * @param userID
	 * @param userKey
	 * @param hostName
	 * @return
	 */
	D2LOperationSecurity createOperationContextFromUserValues(String userID, String userKey, String hostName, boolean encryptOperations);

	/**
	 * Create an anonymous user context for making calls that require only appKey and appId
	 * @param hostName 
	 * @param encryptOperations
	 * @return
	 */
	D2LOperationSecurity createAnonymousUserContext(String hostName, boolean encryptOperations);

}
