<%-- 
D2LValence package, auth module.

Copyright (c) 2012 Desire2Learn Inc.

Licensed under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy of
the license at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations under
the License.
--%>

<%@page import="java.net.URI"%>
<%@page import="com.d2lvalence.idkeyauth.*" %>

<%
    session.setAttribute("userID", "");
    session.setAttribute("userKey", "");
    if(session.getAttribute("appKey") == null) session.setAttribute("appID", "G9nUpvbZQyiPrk3um2YAkQ");
    if(session.getAttribute("appId") == null) session.setAttribute("appKey", "ybZu7fm_JKJTFwKEHfoZ7Q");
    if(session.getAttribute("host") == null) session.setAttribute("host", "lms.valence.desire2learn.com");
    if(session.getAttribute("port") == null) session.setAttribute("port", "443");
    if(session.getAttribute("scheme") == null) session.setAttribute("scheme", "https");

    int port;
    try {
      port = Integer.parseInt((String)session.getAttribute("port"));
    } catch(Exception e) {
      port = 443;
      session.setAttribute("port", "443");
    }
    
    //if returned from authentication page store the credentials
    
    //This is how ID2LUserContext objects should be created from the URL the authentication returns to
    AuthenticationSecurityFactory factory = new AuthenticationSecurityFactory();
    ID2LAppContext appContext = factory.createSecurityContext(session.getAttribute("appID").toString(), session.getAttribute("appKey").toString());
    URI uri=new URI(request.getRequestURI()+"?"+request.getQueryString());
    ID2LUserContext userContext=appContext.createUserContext(uri, (String)session.getAttribute("host"), port, session.getAttribute("scheme") == "https");
    if(userContext!=null) {
        session.setAttribute("userContext", userContext);
  
        session.setAttribute("userID", userContext.getUserId());
        session.setAttribute("userKey", userContext.getUserKey());
    }   
  
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Desire2Learn Auth SDK Sample</title>
    <style type = "text/css">
        table.plain
        {
          border-color: transparent;
          border-collapse: collapse;
        }

        table td.plain
        {
          padding: 5px;
          border-color: transparent;
        }

        table th.plain
        {
          padding: 6px 5px;
          text-align: left;
          border-color: transparent;
        }

        tr:hover
        {
            background-color: transparent !important;
        }

        .error
        {
            color: #FF0000;
        }
    </style>
    <script src="sample.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type = "text/javascript"></script>
</head>
<body>
    <span id="errorField" hidden=true></span>
    <form method="get" action="authenticateUser.jsp" id="configForm">
    <input type="button" onclick="window.location.replace('defaults.jsp')" name="authBtn" value="Load Defaults" id="resetButton" />
    <hr />
    <table>
        <tr>
            <td>
                <b>Host: </b>
            </td>
            <td>
                <input name="hostField" type="text" value="<%= session.getAttribute("host") %>" id="hostField" />
            </td>
            <td>
                <b>Port:</b>
            </td>
            <td>
                <input name="portField" type="text" value="<%= session.getAttribute("port") %>" id="portField" />
            </td>
            <td>
                <input id="schemeField" type="checkbox" name="schemeField" <%= (session.getAttribute("scheme").toString().equals("https") ? "checked=\"true\"" : "") %> />
                HTTPS
            </td>
        </tr>
        <tr>
            <td>
                <b>App ID:</b>
            </td>
            <td>
                <input name="appIDField" type="text" value="<%= session.getAttribute("appID") %>" id="appIDField" />
            </td>
            <td>
                <b>App Key:</b>
            </td>
            <td>
                <input name="appKeyField" type="text" value="<%= session.getAttribute("appKey") %>" id="appKeyField" />
            </td>
        </tr>
    </table>
    <div id="userDiv">
        <br />
        <span>This information is returned by the authentication server and is valid only for this application:</span>
        <table>
            <tr>
                <td>
                    <b>User ID:</b>
                </td>
                <td>
                    <input type="text" id="userIDField" value="<%= session.getAttribute("userID") == null ? "" : session.getAttribute("userID") %>" />
                </td>
            </tr>
            <tr>
                <td>
                    <b>User Key:</b>
                </td>
                <td>
                    <input type="text" id="userKeyField" value="<%= session.getAttribute("userKey") %>" />
                </td>
            </tr>
        </table>
        <input type="button" onclick="deauthenticate()" name="authBtn" value="Deauthenticate" id="deauthBtn">
    </div>
    <span id="authNotice">Note: to authenticate against the test server, you can user username
                          "sampleapiuser" and password "Tisabiiif".
    </span><br />
    <input type="submit" name="authBtn" value="Authenticate" id="authenticateButton" /><br>
    <input type="button" id="manualBtn" value="Manually set credentials" onclick="setCredentials()" />
    <input type="button" name="authBtn" value="Save" id="manualAuthBtn" hidden=true onclick="saveCredentials()"/>
    </form>

    <hr />
    <table>
        <tr>
            <td>
                <b>Examples:</b>
            </td>
            <td>
                <button type="button" onclick="exampleGetVersions()">
                    Get Versions</button>
            </td>
        </tr>
        <tr>
            <td>
            </td>
            <td>
                <button type="button" onclick="exampleWhoAmI()">
                    WhoAmI</button>
            </td>
        </tr>
        <tr>
            <td>
            </td>
            <td>
                <button type="button" onclick="exampleCreateUser()">
                    Create User</button>
            </td>
        </tr>
    </table>
    <br />
    <b>Method:</b>&nbsp;
    <input value="GET" name="method" type="radio" id="GETField" checked="checked" onclick="hideData()" />GET
    &nbsp;
    <input value="POST" name="method" type="radio" id="POSTField" onclick="showData()" />POST
    &nbsp;
    <input value="PUT" name="method" type="radio" id="PUTField" onclick="showData()" />PUT
    &nbsp;
    <input value="DELETE" name="method" type="radio" id="DELETEField" onclick="hideData()" />DELETE<br />
    <b>Action:</b>&nbsp;<input name="actionField" type="text" id="actionField" style="width:600px;" /><br />
    <b id="dataFieldLabel">Data:</b><br />
    <textarea name="dataField" rows="2" cols="20" id="dataField" style="height:400px;width:600px;">
</textarea><br />
    <b id="responseFieldLabel">Response:</b><br />
    <textarea name="responseField" rows="2" cols="20" id="responseField" style="height:400px;width:600px;">
</textarea><br />
    <input type="button" name="submitButton" value="Submit" id="submitButton" onclick="doAPIRequest()"/>

</body>
<script type="text/javascript">
    function showData() {
        document.getElementById("dataFieldLabel").hidden = false;
        document.getElementById("dataField").hidden=false;
    }

    function hideData() {
        document.getElementById("dataFieldLabel").hidden = true;
        document.getElementById("dataField").hidden=true;
    }

    function exampleGetVersions() {
        hideData();
        document.getElementById("GETField").checked = true;
        document.getElementById("actionField").value = "/d2l/api/versions/";
    }

    function exampleWhoAmI() {
        hideData();
        document.getElementById("GETField").checked = true;
        document.getElementById("actionField").value = "/d2l/api/lp/1.0/users/whoami";
    }

    function exampleCreateUser() {
        showData();
        document.getElementById("POSTField").checked = true;
        document.getElementById("actionField").value = "/d2l/api/lp/1.0/users/";
        document.getElementById("dataField").value = "{\n  \"OrgDefinedId\": \"<string>\",\n  \"FirstName\": \"<string>\",\n  \"MiddleName\": \"<string>\",\n  \"LastName\": \"<string>\",\n  \"ExternalEmail\": \"<string>|null\",\n  \"UserName\": \"<string>\",\n  \"RoleId\": \"<number>\",\n  \"IsActive\": \"<boolean>\",\n  \"SendCreationEmail\": \"<boolean>\"\n}";
    }


    function setCredentials() {
        document.getElementById("manualAuthBtn").hidden = false;
        document.getElementById("deauthBtn").hidden = true;
        document.getElementById("userDiv").hidden = false;
        document.getElementById("userIDField").hidden = false;
        document.getElementById("userKeyField").hidden = false;
        document.getElementById("manualBtn").hidden = true;
        document.getElementById("authenticateButton").hidden = true;
        document.getElementById("authNotice").hidden = true;
    }

    function saveCredentials() {
        document.getElementById("userIDField").disabled = true;
        document.getElementById("userKeyField").disabled = true;
        document.getElementById("manualAuthBtn").hidden = true;
        document.getElementById("deauthBtn").hidden = false;
    }

    hideData();

    if(document.getElementById("userIDField").value != "") {
        document.getElementById("userIDField").disabled = true;
        document.getElementById("userKeyField").disabled = true;
        document.getElementById("manualBtn").hidden = true;
        document.getElementById("authenticateButton").hidden = true;
        document.getElementById("authNotice").hidden = true;
        document.getElementById("hostField").disabled = true;
        document.getElementById("portField").disabled = true;
        document.getElementById("appKeyField").disabled = true;
        document.getElementById("appIDField").disabled = true;
    } else {
        document.getElementById("userDiv").hidden = true;
        document.getElementById("hostField").disabled = false;
        document.getElementById("portField").disabled = false;
        document.getElementById("appKeyField").disabled = false;
        document.getElementById("appIDField").disabled = false;
    }

</script>
</html>
