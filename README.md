valence-sdk-java
================

D2L Valence SDK for Java

Usage:

Create an Application context using your application key & secret given to you by D2L:
```java
ID2LAppContext appContext = AuthenticationSecurityFactory
    .createSecurityContext("appId", "appKey", "https://school.desire2learn.com");
```
Redirect the user to the uri constructed by this, passing in the callback url:
```java
appContext.createWebUrlForAuthentication(new URI("https://myhost.com/"));
```
Create a user context with the user's key & secret that was requisitioned from the redirect:
```java
ID2LUserContext userContext = appContext.createUserContext(
    new D2LUserContextParameters("userId", "userKey"));
```
Create the Authenticated URI from the user's context:
```java
URI uri = userContext.createAuthenticatedUri("/d2l/api/lp/1.0/users/", "GET");
```
