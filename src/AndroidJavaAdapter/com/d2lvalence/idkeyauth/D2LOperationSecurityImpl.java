// Copyright 2011 Desire2Learn Incorporated
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.d2lvalence.idkeyauth;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

@SuppressWarnings("serial")
class D2LOperationSecurityImpl implements D2LOperationSecurity {

	private ID2LUserContext wrappedContext;
	
    @SuppressWarnings("unused")
    private D2LOperationSecurityImpl() {
    }// hide default constructor
    
    D2LOperationSecurityImpl(ID2LUserContext context) {
    	wrappedContext = context;
    }

    @Override
    public Uri createUnauthenticatedUri(String path) {

        String scheme = getEncryptOperations() ? "https" : "http";
        Uri.Builder b = Uri.parse(scheme + "://" + getHostName()).buildUpon();
        b.path(path);

        Uri securedURI = b.build();

        return securedURI;
    }

    @Override
    public Uri createAuthenticatedUri(String path, String httpMethod) {
    	return Uri.parse(wrappedContext.createAuthenticatedUri(path, httpMethod).toString());
    }

	@Override
	public int handleResult(int resultCode, InputStream is,
			String[] logMessageArray) {
		if (resultCode == 200) {
			// No need to read the stream if we get 200
			return RESULT_OKAY;
		}
		StringBuffer response = new StringBuffer();
		InputStreamReader reader = new InputStreamReader(is);
		char[] buffer = new char[16384];
		int read = 0;
		try {
			while ((read = reader.read(buffer, 0, buffer.length)) >= 0) {
				response.append(buffer, 0, read);
			}
		} catch (IOException e) {
			Log.e(this.getClass().getName(),
					"Error reading result stream for non-200: " + e.toString());
			return RESULT_UNKNOWN;
		}

        String responseBody = response.toString();

        // [GF] Need to make this extra check, otherwise we get "ID2LUserContext.RESULT_NO_PERMISSION"
        // when the user's token is really expired/invalid (need to re-authenticate with credentials).
        String responseLowercase = responseBody.toLowerCase(Locale.getDefault());
        boolean isInvalidToken = responseLowercase.equals("invalid token");
        boolean isExpiredToken = responseLowercase.equals("token expired");
        if (isInvalidToken || isExpiredToken)
        {
            return RESULT_INVALID_SIG;
        }

        int result = wrappedContext.interpretResult(resultCode, responseBody);
		switch (result) {
            case ID2LUserContext.RESULT_OKAY:
                return RESULT_OKAY;
            case ID2LUserContext.RESULT_INVALID_SIG:
                return RESULT_INVALID_SIG;
            case ID2LUserContext.RESULT_INVALID_TIMESTAMP:
                return RESULT_INVALID_TIMESTAMP;
            case ID2LUserContext.RESULT_NO_PERMISSION:
                return RESULT_NO_PERMISSION;
            default:
                return RESULT_UNKNOWN;
		}
	}

    @Override
    public String getUserID() {
        return wrappedContext.getUserId();
    }

    @Override
    public String getUserKey() {
        return wrappedContext.getUserKey();
    }

    @Override
    public long getServerSkewMillis() {
        return wrappedContext.getServerSkewMillis();
    }

    @Override
    public void setServerSkewMillis(long skew) {
        wrappedContext.setServerSkewMillis(skew);
    }

    @Override
    public String getHostName() {
        return wrappedContext.getHostName();
    }

    public boolean getEncryptOperations() {
    	return wrappedContext.getPort() == 443;
    }
}