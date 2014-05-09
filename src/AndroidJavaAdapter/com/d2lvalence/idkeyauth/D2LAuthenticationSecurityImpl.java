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

import java.net.URI;

import android.net.Uri;

class D2LAuthenticationSecurityImpl implements D2LAuthenticationSecurity {

	private ID2LAppContext wrappedAppContext;

	@SuppressWarnings("unused")
	private D2LAuthenticationSecurityImpl() {
		// prevent incorrect construction
	}

	D2LAuthenticationSecurityImpl(String appID, String appKey) {
		wrappedAppContext = new AuthenticationSecurityFactory()
				.createSecurityContext(appID, appKey);
	}

	@Override
	public Uri createWebURLForAuthentication(String host, Uri resultURI) {
		return createWebURLForAuthentication(host, resultURI, false);
	}

	@Override
	public Uri createWebURLForAuthentication(String host, Uri resultURI,
			boolean encryptOperations) {

		URI resultURIJava = URI.create(resultURI.toString());
		URI uriToReturn = wrappedAppContext.createWebUrlForAuthentication(host,
				Util.getPort(encryptOperations), encryptOperations,
				resultURIJava);
		return Uri.parse(uriToReturn.toString());
	}

	@Override
	public D2LOperationSecurity createOperationContextFromResult(Uri resultURI,
			String hostName, boolean encryptOperations) {

		ID2LUserContext userContext = wrappedAppContext.createUserContext(URI.create(resultURI.toString()), hostName, Util.getPort(encryptOperations), encryptOperations);
		return new D2LOperationSecurityImpl(userContext);
	}

	@Override
	public D2LOperationSecurity createAnonymousUserContext(String hostName,
			boolean encryptOperations) {
		ID2LUserContext context = wrappedAppContext.createAnonymousUserContext(hostName, Util.getPort(encryptOperations), encryptOperations);
		return new D2LOperationSecurityImpl(context);
	}

	@Override
	public D2LOperationSecurity createOperationContextFromUserValues(
			String userID, String userKey, String hostName,
			boolean encryptOperations) {
		ID2LUserContext context = wrappedAppContext.createUserContext(userID, userKey, hostName, Util.getPort(encryptOperations), encryptOperations);
		return new D2LOperationSecurityImpl(context);
	}

}
