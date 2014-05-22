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

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.d2lvalence.idkeyauth.*" %>
<%@page import="java.net.*" %>
<%@page import="java.io.*" %>
<%@page import="java.util.*" %>
<%@page import="com.google.gson.reflect.TypeToken" %>
<%@page import="com.google.gson.Gson" %>
<%@page import="java.lang.reflect.Type" %>

<%
    if(request.getParameter("appKeyField")!=null) {
        //update session variables with values from the form
        session.setAttribute("appID", request.getParameter("appIDField"));
        session.setAttribute("appKey", request.getParameter("appKeyField"));
    }
    
    AuthenticationSecurityFactory factory = new AuthenticationSecurityFactory();
    ID2LAppContext appContext = factory.createSecurityContext(session.getAttribute("appID").toString(), session.getAttribute("appKey").toString());
   
    int port;
    try {
        //try to read the port specified
        port=Integer.parseInt(request.getParameter("portField"));
    } catch(Exception e) {
        //fallback to https port
        port=443;
    }
    session.setAttribute("host", request.getParameter("hostField"));
    session.setAttribute("port", port + "");
    session.setAttribute("scheme", request.getParameter("schemeField") != null ? "https" : "http");

    //get the current path and replace this page with the target page
    URI resultUri=new URI(request.getRequestURL().toString().replace("authenticateUser.jsp", "index.jsp"));
    
    URI uri=appContext.createWebUrlForAuthentication(session.getAttribute("host").toString(),
                                                     port,
                                                     session.getAttribute("scheme").toString().equals("https"),
                                                     resultUri);
    response.sendRedirect(uri.toString());
%>
