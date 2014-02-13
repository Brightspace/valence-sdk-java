/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.d2lvalence.idkeyauth.unittests;

import com.d2lvalence.idkeyauth.AuthenticationSecurityFactory;
import com.d2lvalence.idkeyauth.ID2LAppContext;
import com.d2lvalence.idkeyauth.ID2LUserContext;
import com.d2lvalence.idkeyauth.implementation.D2LSigner;
import com.d2lvalence.idkeyauth.implementation.D2LUserContext;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import org.junit.*;

/**
 *
 * @author alamarre
 */
public class MainTest {

    private final String TEST_APP_ID = "foo";
    private final String TEST_APP_KEY = "asdfghjkasdfghjk";
    
    private final String TEST_HOST_NAME = "authenticationhost.com";
    private final int TEST_PORT = 44444;
    private final String TEST_HTTP_URL = "http://" + TEST_HOST_NAME +":"+ TEST_PORT;
    private final String TEST_HTTPS_URL = "https://" + TEST_HOST_NAME +":"+ TEST_PORT;
    
    private final String TEST_API_URL = "http://univ.edu/d2l/api/lp/1.0/organization/info";
    private final String TEST_API_PATH = "/d2l/api/lp/1.0/organization/info";

    private final String EXPECTED_TOKEN_SERVICE_PATH = "/d2l/auth/api/token";

    private final long TEST_TIMESTAMP_MILLISECONDS = 1234567890L;
    private final long TEST_TIMESTAMP_SECONDS = 1234567L;

    private final String TEST_USER_ID = "42";
    private final String TEST_USER_KEY = "qwertyuiopqwertyuiop";

    public MainTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
    }

    @Test
    public void authenticationSecurityFactory_CreatesAuthenticationSecurityObject() {
        ID2LAppContext appContext = createAuthenticationSecurityObjectUnderTest();
        assert (appContext instanceof ID2LAppContext);
        //Assert.IsInstanceOf<ID2LAuthenticationSecurity>( appContext );
    }

    @Test
    public void appContext_TestTimestamp() {
        ID2LUserContext context = createOperationSecurityObjectUnderTest();
        String path = "test";

        URI uri = context.createAuthenticatedUri(path, "GET");

        try {
            Assert.assertEquals(getURIQueryParameter(uri, "x_t"), "" + TEST_TIMESTAMP_SECONDS);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void AuthContext_CreateWebUrlForAuth_ReturnsURI_WhereHost_MatchesInput() {
        ID2LAppContext appContext = createAuthenticationSecurityObjectUnderTest();

        try {
            URI uri = appContext.createWebUrlForAuthentication(TEST_HTTP_URL, new URI(TEST_API_URL));

            Assert.assertEquals(TEST_HOST_NAME, uri.getHost());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void AuthContext_CreateWebUrlForAuth_ReturnsURI_WherePortNumber_MatchesInput() {
        final int expectedPortNumber = 44480;
        ID2LAppContext appContext = createAuthenticationSecurityObjectUnderTest();
        try {
            URI uri = appContext.createWebUrlForAuthentication("http://anythinghere.com:44480", new URI(TEST_API_URL));
            Assert.assertEquals(expectedPortNumber, uri.getPort());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void AuthContext_CreateWebUrlForAuth_ReturnsURI_WithPath_ToTokenService() {
        ID2LAppContext appContext = createAuthenticationSecurityObjectUnderTest();

        try {
            URI uri = appContext.createWebUrlForAuthentication(TEST_HTTP_URL, new URI(TEST_API_URL));

            Assert.assertEquals(EXPECTED_TOKEN_SERVICE_PATH, uri.getPath());
        } catch (Exception e) {
            Assert.fail();
        }

    }

    @Test
    public void AuthContext_CreateWebUrlForAuth_ReturnsURI_AndQueryParam_x_a_Matches_AppId() {
        ID2LAppContext appContext = createAuthenticationSecurityObjectUnderTest();

        try {
            URI uri = appContext.createWebUrlForAuthentication(TEST_HTTPS_URL, new URI(TEST_API_URL));

            String parameter = getURIQueryParameter(uri, "x_a");
            Assert.assertEquals(TEST_APP_ID, parameter);
        } catch (Exception e) {
            Assert.fail();
        }

    }

    @Test
    public void AuthContext_CreateWebUrlForAuth_ReturnsURI_AndQueryParam_x_b_MatchesURISignedWithAppKey() {
        ID2LAppContext appContext = createAuthenticationSecurityObjectUnderTest();
        String expectedSignedURI = D2LSigner.getBase64HashString(TEST_APP_KEY, TEST_API_URL);

        try {
            URI uri = appContext.createWebUrlForAuthentication(TEST_HTTP_URL, new URI(TEST_API_URL));

            String parameter = getURIQueryParameter(uri, "x_b");
            Assert.assertEquals(expectedSignedURI, parameter);
        } catch (Exception e) {
            Assert.fail();
        }

    }

    @Test
    public void AuthContext_CreateWebUrlForAuth_ReturnsURI_AndQueryParam_x_target_MatchesResultUrl() {
        ID2LAppContext appContext = createAuthenticationSecurityObjectUnderTest();

        try {
            String expectedTarget = URLEncoder.encode(TEST_API_URL, "UTF-8");
            URI uri = appContext.createWebUrlForAuthentication(TEST_HTTP_URL, new URI(TEST_API_URL));

            String parameter = getURIQueryParameter(uri, "x_target");
            Assert.assertEquals(expectedTarget, parameter);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void AuthContext_CreateWebUrlForAuth_ReturnsUri_AndQueryParam_x_target_MatchesEncodedResultUrl() {
        ID2LAppContext appContext = createAuthenticationSecurityObjectUnderTest();
        final String unencodedUrl = "http://univ.edu/d2l/api/resource?foo=bar";
        try {
            String encodedUrl = URLEncoder.encode(unencodedUrl, "UTF-8");

            URI uri = appContext.createWebUrlForAuthentication(TEST_HTTP_URL, new URI(unencodedUrl));

            String parameter = getURIQueryParameter(uri, "x_target");
            Assert.assertEquals(encodedUrl, parameter);
        } catch (Exception e) {
            Assert.fail();
        }

    }

    @Test
    public void AuthContext_CreateOpContextFromResult_ReturnTypeIs_ID2LOperationSecurity() {
        ID2LAppContext appContext = createAuthenticationSecurityObjectUnderTest();
        URI resultURI = createResultURIWithUserIdAndKey(TEST_USER_ID, TEST_USER_KEY);

        ID2LUserContext result = appContext.createUserContext(resultURI, TEST_HTTPS_URL);

        assert result instanceof ID2LUserContext;
    }
    
    @Test
    public void AuthContext_CreateOpContextFromResult_UserId_IsSameAs_ResultUri_x_a_QueryParam() {
        ID2LAppContext appContext = createAuthenticationSecurityObjectUnderTest();
        final String expectedUserId = "344";
        URI resultURI = createResultURIWithUserIdAndKey(expectedUserId, TEST_USER_KEY);

        ID2LUserContext userContext = appContext.createUserContext(resultURI, TEST_HTTPS_URL);

        Assert.assertEquals(expectedUserId, userContext.getUserId());
    }

    @Test
    public void AuthContext_CreateOpContextFromResult_If_x_a_IsAbsentFromResultUri_ReturnsNull() {
        ID2LAppContext appContext = createAuthenticationSecurityObjectUnderTest();
        try {
            ID2LUserContext userContext = appContext.createUserContext(new URI(TEST_API_URL), TEST_HTTPS_URL);
            Assert.fail();
        } catch (Exception e) {

        }

    }

    @Test
    public void AuthContext_CreateOpContextFromResult_UserKeyIsSameAs_ResultUri_x_b_QueryParam() {
        ID2LAppContext appContext = createAuthenticationSecurityObjectUnderTest();
        final String expectedUserKey = "sampleUserKey";
        URI resultURI = createResultURIWithUserIdAndKey(TEST_USER_ID, expectedUserKey);

        ID2LUserContext userContext = appContext.createUserContext(resultURI, TEST_HTTPS_URL);

        Assert.assertEquals(expectedUserKey, userContext.getUserKey());
    }

    @Test
    public void AuthContext_CreateOpContextFromResult_If_x_b_IsAbsentFromResultUri_ReturnsNull() {
        ID2LAppContext appContext = createAuthenticationSecurityObjectUnderTest();
        URI resultURI = createResultURIWithUserIdAndKey(TEST_USER_ID, "");

        ID2LUserContext userContext = appContext.createUserContext(resultURI, TEST_HTTPS_URL);

        Assert.assertNull(userContext);
    }

    @Test
    public void OpContext_CreateAuthUri_IfEncryptOpsFlagIsSet_UsesHttpsScheme() {
        ID2LAppContext appContext = createAuthenticationSecurityObjectUnderTest();
        URI resultURI = createResultURIWithUserIdAndKey(TEST_USER_ID, TEST_USER_KEY);
        ID2LUserContext userContext = appContext.createUserContext(resultURI, TEST_HTTPS_URL);

        URI authURI = userContext.createAuthenticatedUri(TEST_API_PATH, "GET");

        Assert.assertEquals("https", authURI.getScheme());
    }

    @Test
    public void OpContext_CreateAuthUri_IfEncryptOpsFlagIsNotSet_UsesHttpScheme() {
        ID2LAppContext appContext = createAuthenticationSecurityObjectUnderTest();
        URI resultURI = createResultURIWithUserIdAndKey(TEST_USER_ID, TEST_USER_KEY);
        ID2LUserContext userContext = appContext.createUserContext(resultURI, TEST_HTTP_URL);

        URI authURI = userContext.createAuthenticatedUri(TEST_API_PATH, "DELETE");

        Assert.assertEquals("http", authURI.getScheme());
    }

    @Test
    public void OpContext_CreateAuthUri_AuthUriHost_IsTheSameAs_OpContextHost() {
        final String expectedHostName = "host.com";
        ID2LAppContext appContext = createAuthenticationSecurityObjectUnderTest();
        URI resultURI = createResultURIWithUserIdAndKey(TEST_USER_ID, TEST_USER_KEY);
        ID2LUserContext userContext = appContext.createUserContext(resultURI, "http://host.com:1234");

        URI authURI = userContext.createAuthenticatedUri(TEST_API_PATH, "DELETE");

        Assert.assertEquals(expectedHostName, authURI.getHost());
    }

    @Test
    public void OpContext_CreateAuthUri_AuthUriPort_IsTheSameAs_OpContextPort() {
        final int expectedPort = 44414;
        ID2LAppContext appContext = createAuthenticationSecurityObjectUnderTest();
        URI resultURI = createResultURIWithUserIdAndKey(TEST_USER_ID, TEST_USER_KEY);
        ID2LUserContext userContext = appContext.createUserContext(resultURI, "http://anythinghere.com:44414");

        URI authURI = userContext.createAuthenticatedUri(TEST_API_PATH, "DELETE");

        Assert.assertEquals(expectedPort, authURI.getPort());
    }

    @Test
    public void OpContext_CreateAuthUri_QueryParam_x_a_IsSameAs_AppId() {
        ID2LUserContext userContext = createOperationSecurityObjectUnderTest();

        URI authURI = userContext.createAuthenticatedUri(TEST_API_PATH, "PUT");

        try {
            String parameter = getURIQueryParameter(authURI, "x_a");
            Assert.assertEquals(TEST_APP_ID, parameter);
        } catch (Exception e) {
            Assert.fail();
        }

    }

    @Test
    public void OpContext_CreateAuthUri_QueryParam_x_c_Matches_SignatureSignedWithAppKey() {
        final String httpMethod = "POST";
        String expectedParameter = calculateParameterExpectation(
                TEST_APP_KEY, httpMethod, TEST_API_PATH, TEST_TIMESTAMP_SECONDS);
        ID2LUserContext userContext = createOperationSecurityObjectUnderTest();

        URI authURI = userContext.createAuthenticatedUri(TEST_API_PATH, httpMethod);

        try {
            String parameter = getURIQueryParameter(authURI, "x_c");
            Assert.assertEquals(expectedParameter, parameter);
        } catch (Exception e) {
            Assert.fail();
        }

    }

    @Test
    public void OpContext_CreateAuthUri_QueryParam_x_d_Matches_SignatureSignedWithUserKey() {
        final String httpMethod = "GET";
        String expectedParameter = calculateParameterExpectation(
                TEST_USER_KEY, httpMethod, TEST_API_PATH, TEST_TIMESTAMP_SECONDS);
        ID2LUserContext userContext = createOperationSecurityObjectUnderTest();

        URI authURI = userContext.createAuthenticatedUri(TEST_API_PATH, httpMethod);

        try {
            String parameter = getURIQueryParameter(authURI, "x_d");
            Assert.assertEquals(expectedParameter, parameter);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void OpContext_CreateAuthUri_QueryParam_x_b_Matches_UserId() {
        final String expectedUserId = "24";
        ID2LAppContext appContext = createAuthenticationSecurityObjectUnderTest();
        URI resultURI = createResultURIWithUserIdAndKey(expectedUserId, TEST_USER_KEY);
        ID2LUserContext userContext = appContext.createUserContext(resultURI, TEST_HTTPS_URL);

        URI authURI = userContext.createAuthenticatedUri(TEST_API_PATH, "GET");

        try {
            String parameter = getURIQueryParameter(authURI, "x_b");
            Assert.assertEquals(expectedUserId, parameter);
        } catch (Exception e) {
            Assert.fail();
        }

    }

    @Test
    public void OpContext_CreateAuthUri_IfThereIsClockSkew_SignatureUsesAdjustedTimestamp() {
        final String httpMethod = "POST";
        final long serverClockSkewMilliseconds = 343000;
        String expectedParameter = calculateParameterExpectation(
                TEST_APP_KEY, httpMethod, TEST_API_PATH, TEST_TIMESTAMP_SECONDS);

        ID2LUserContext userContext = createOperationSecurityObjectUnderTest();
        setTimestampProviderReturnValue(TEST_TIMESTAMP_MILLISECONDS - serverClockSkewMilliseconds, (D2LUserContext) userContext);
        userContext.setServerSkewMillis(serverClockSkewMilliseconds);

        URI authURI = userContext.createAuthenticatedUri(TEST_API_PATH, httpMethod);

        try {
            String parameter = getURIQueryParameter(authURI, "x_c");
            Assert.assertEquals(expectedParameter, parameter);
        } catch (Exception e) {
            Assert.fail();
        }

    }

    @Test
    public void AuthContext_CreateOpContextFromUserValues_OpContext_CreateAuthUriMethod_CreatesSameUri() {
        ID2LUserContext userContext = createOperationSecurityObjectUnderTest();
        URI expectedAuthURI = userContext.createAuthenticatedUri(TEST_API_PATH, "GET");
        ID2LAppContext appContext = createAuthenticationSecurityObjectUnderTest();

        ID2LUserContext userContextFromUserValues = appContext.createUserContext(TEST_USER_ID, TEST_USER_KEY, TEST_HTTPS_URL);
        setTimestampProviderReturnValue(TEST_TIMESTAMP_MILLISECONDS, (D2LUserContext) userContextFromUserValues);
        URI actualAuthURI = userContextFromUserValues.createAuthenticatedUri(TEST_API_PATH, "GET");

        Assert.assertEquals(expectedAuthURI.toString(), actualAuthURI.toString());
    }

    @Test
    public void OpContext_ServerSkewMillis_get_Returns_SameAsSetValue() {
        final long expectedSkew = 14438L;
        ID2LUserContext userContext = createOperationSecurityObjectUnderTest();
        userContext.setServerSkewMillis(expectedSkew);

        long actualSkew = userContext.getServerSkewMillis();
        Assert.assertEquals(expectedSkew, actualSkew);
    }

    @Test
    public void OpContext_CreateAuthUri_VerbCase_DoesNotChangeResult() {
        ID2LUserContext userContext = createOperationSecurityObjectUnderTest();
        URI expectedURI = userContext.createAuthenticatedUri(TEST_API_PATH, "GET");

        URI actualURI = userContext.createAuthenticatedUri(TEST_API_PATH, "Get");

        Assert.assertEquals(expectedURI.toString(), actualURI.toString());
    }

    @Test
    public void OpContext_CreateAuthUri_PathCase_DoesNotChangeResult() {
        ID2LUserContext userContext = createOperationSecurityObjectUnderTest();
        URI expectedURI = userContext.createAuthenticatedUri("/d2l/api/someresource", "GET");
        try {
            String expectedParameter = getURIQueryParameter(expectedURI, "x_c");

            URI actualURI = userContext.createAuthenticatedUri("/d2l/api/SomeResource", "GET");

            String actualParameter = getURIQueryParameter(actualURI, "x_c");
            Assert.assertEquals(expectedParameter, actualParameter);
        } catch (Exception e) {
            Assert.fail();
        }

    }

    @Test
    public void OpContext_CalculateServerSkewFromResponse_ChangesServerSkewProperty_ByTimestampDifference() {
        final long clientTimeSeconds = 1319000000L;

        ID2LUserContext userContext = createOperationSecurityObjectUnderTest();
        setTimestampProviderReturnValue(clientTimeSeconds * 1000, (D2LUserContext) userContext);
        final long serverAheadBy = 907L;
        String responseBody = String.format("Timestamp out of range\r\n%s", clientTimeSeconds + serverAheadBy);
        userContext.calculateServerSkewFromResponse(responseBody);

        Assert.assertEquals(serverAheadBy * 1000, userContext.getServerSkewMillis());
    }

    @Test
    public void OpContext_CalculateServerSkewFromResponse_WhenResponseTimestampIsInvalid_ServerSkewPropertyDoesNotChange() {
        ID2LUserContext userContext = createOperationSecurityObjectUnderTest();
        final long expectedSkew = 874000L;
        userContext.setServerSkewMillis(expectedSkew);
        userContext.calculateServerSkewFromResponse("Timestamp out of range\r\n");
        Assert.assertEquals(expectedSkew, userContext.getServerSkewMillis());
    }

    private ID2LAppContext createAuthenticationSecurityObjectUnderTest() {
        AuthenticationSecurityFactory factory = new AuthenticationSecurityFactory();
        return factory.createSecurityContext(TEST_APP_ID, TEST_APP_KEY);
    }

    private void setTimestampProviderReturnValue(long milliseconds, D2LUserContext context) {
        try {
            Field privateStringField = D2LUserContext.class.getDeclaredField("_timestampProvider");

            privateStringField.setAccessible(true);
            privateStringField.set(context, new TimestampProviderStub(milliseconds));
            String fieldValue = (String) privateStringField.get(context);
        } catch (Exception e) {

        }
    }

    private URI createResultURIWithUserIdAndKey(String userId, String userKey) {
        String uriString = TEST_API_URL + "?";
        if (userId != null && !userId.isEmpty()) {
            uriString += "x_a=" + userId;
        }
        if (userKey != null && !userKey.isEmpty()) {
            uriString += "&x_b=" + userKey;
        }
        try {
            return new URI(uriString);
        } catch (Exception e) {

        }

        return null;
    }

    private String getURIQueryParameter(URI uri, String name) throws Exception {
        String queryString = uri.toString().substring(uri.toString().indexOf("?") + 1);
        String[] nameValuePairStrings = queryString.split("&");
        for (String pairString : nameValuePairStrings) {
            String[] nameValuePair = pairString.split("=");
            if (nameValuePair.length >= 2 && nameValuePair[ 0].equals(name)) {
                return nameValuePair[ 1];
            }
        }
        throw new Exception("didn't find query parameter " + name);
    }

    private ID2LUserContext createOperationSecurityObjectUnderTest() {
        ID2LAppContext appContext = createAuthenticationSecurityObjectUnderTest();
        URI resultURI = createResultURIWithUserIdAndKey(TEST_USER_ID, TEST_USER_KEY);

        ID2LUserContext userContext = appContext.createUserContext(resultURI, TEST_HTTPS_URL);
        setTimestampProviderReturnValue(TEST_TIMESTAMP_MILLISECONDS, (D2LUserContext) userContext);
        return userContext;
    }

    private String calculateParameterExpectation(
            String key, String httpMethod, String apiPath, long timestamp) {

        String unsignedResult = String.format("%s&%s&%s", httpMethod, apiPath, timestamp);
        System.out.println(unsignedResult);
        String signedResult = D2LSigner.getBase64HashString(key, unsignedResult);
        return signedResult;
    }

}
