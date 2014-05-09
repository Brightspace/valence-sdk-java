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

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

//import android.util.Base64;


/**
 * This class performs fundamental operations used in the Desire2Learn idkey auth system.
 * 
 * Typically this class is used only by other classes in this framework
 *  
 * @author cdunk
 *
 */
public final class D2LSigner {
	/**
	 * Performs Signature and encodes result based on they key string and the signatureBaseString. 
	 * 
	 * 
	 * @param key - either an app or user key in its natural string format. 
	 * @param signatureBaseString - the unicode format of the signature base string, string encoding required for signing is done
	 * inside this method
	 * @return a base64url encoded result of the signing operation suitable for adding to a server request. Null if 
	 * one of the parameters is malformed.
	 */
	
	static public String base64URLSig(String key, String signatureBaseString) {
		
		String data = signatureBaseString;
				
		try {
			
			Mac hmacSha256 = Mac.getInstance("hmacSHA256");
			
			byte[] keyBytes = key.getBytes("UTF-8");
			
			Key k = new SecretKeySpec(keyBytes, "hmacSHA256");
			
			hmacSha256.init(k);
			
			byte[] dataBytes = data.getBytes("UTF-8");
			
			byte[] sig = hmacSha256.doFinal(dataBytes);
			
			String sigString = base64Url( sig );
			
			return sigString;
		} catch (NoSuchAlgorithmException e) {
			return null;
		} catch (InvalidKeyException e) {
			return null;
		} catch (UnsupportedEncodingException e) {
			return null;
		
		}
		
	}
	
	static private String base64Url( byte[] bytes ) {	
		
		byte [] base64bytes = Base64.encode(bytes, Base64.URL_SAFE);
	
				
		String b64;
		try {
			b64 = new String(base64bytes, "UTF-8"); //utf8 is same as ascii for 7bit characters
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} 
		b64 = b64.replaceAll("=+", ""); //trims all sequences of equals signs
		b64 = b64.trim();//trims whitespace (specifically spurious newline)
		
		return b64;
	}
	
	
	

}
