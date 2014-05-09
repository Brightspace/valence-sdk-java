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


/**
 * Entry point for all security operations with the server.
 * 
 */

public abstract class D2LSecurityFactory {
	/**
	 * Called to get an application context for performing signing operations. 
	 * 
	 * @param appID - ID string provided from Desire2Learn
	 * @param appKey - Key string provided from Desire2Learn 
	 * @return null if the appID or appKey parameters are null or malformed (can not check for validity). 
	 * Otherwise returns a D2LAuthenticationSecurity object.
	 */
	public static D2LAuthenticationSecurity createSecurityContext(String appID, String appKey)
	{
		return new D2LAuthenticationSecurityImpl(appID, appKey);
	}
	

}
