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

<%@page import="java.net.*" %>
<%@page import="java.io.*" %>
<%@page import="java.util.*" %>
<%@page import="java.lang.reflect.Type" %>

<%@page import="com.d2lvalence.idkeyauth.*" %>

<%!
    public String getResult(HttpSession session, HttpServletRequest request, JspWriter out, ID2LUserContext userContext, String reqURI, String method, String data, int attempts) {   
        URI uri;
        URLConnection connection;
        try {
            uri = userContext.createAuthenticatedUri(reqURI, method);
            connection=uri.toURL().openConnection();
        } catch(NullPointerException  e) {
            return "Error: Must Authenticate";
        } catch(MalformedURLException e) {
            return "Error: " + e.getMessage();
        } catch(IOException e) {
            return "Error: " + e.getMessage();
        }

        //use a StringBuilder because it's more efficient than adding to a String, due to the immutability of Strings
        StringBuilder sb=new StringBuilder();

        

        try {
            //cast the connection to a HttpURLConnection so we can examin the status code
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod(method);
            if(method.equals("POST") || method.equals("PUT")) {
                connection.setRequestProperty("Content-Length", data.length() + "");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                OutputStream os = connection.getOutputStream();
                os.write(data.getBytes());
                os.flush();
            }
            BufferedReader in;
            //if the status code is success then the body is read from the input stream
            if(httpConnection.getResponseCode()==200) { 
                in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            //otherwise the body is read from the output stream
            } else {
                return "Error: " + httpConnection.getResponseCode() + "";
            //    in = new BufferedReader(new InputStreamReader(httpConnection.getErrorStream()));
            }

            String inputLine;
            while ((inputLine = in.readLine()) != null)  {
                sb.append(inputLine);
            }
            in.close();

            //Determine the result of the rest call and automatically adjusts the user context in case the timestamp was invalid
            int result=userContext.interpretResult(httpConnection.getResponseCode(), sb.toString());
            if(result==userContext.RESULT_OKAY) {
                return sb.toString();
            //if the timestamp is invalid and we haven't exceeded the retry limit then the call is made again with the adjusted timestamp
            } else if (result==userContext.RESULT_INVALID_TIMESTAMP&&attempts>0) {
                return getResult(session, request, out, userContext, reqURI, method, data, attempts-1);
            }
        } catch(IllegalStateException e) {
            return "Error: Exception while parsing";  
        } catch(FileNotFoundException e) {
            //404
            return "Error: URI Incorrect";
        } catch (IOException e) {
            
        }
        return "Error: An Unknown Error has occurred";
    }
%>

<%
    
    if(request.getParameter("host") == null) {
        out.println("Please specify host");
    } else if(request.getParameter("port") == null) {
        out.println("Please specify port");
    } else if(request.getParameter("scheme") == null) {
        out.println("Please specify scheme");
    } else if(request.getParameter("appKey") == null) {
        out.println("Please specify appKey");
    } else if(request.getParameter("appID") == null) {
        out.println("Please specify appID");
    } else if(request.getParameter("req") == null) {
        out.println("Please specify req");
    } else if(request.getParameter("method") == null) {
        out.println("Please specify the HTTP method");
    } else if(request.getParameter("userKey") == null) {
        out.println("Please specify the (possibly blank) userKey");
    } else if(request.getParameter("userID") == null) {
        out.println("Please specify the (possibly blank) userID");
    } else {
        session.setAttribute("host", request.getParameter("host"));
        session.setAttribute("port", request.getParameter("port"));
        session.setAttribute("scheme", request.getParameter("scheme"));
        session.setAttribute("appID", request.getParameter("appID"));
        session.setAttribute("appKey", request.getParameter("appKey"));
        session.setAttribute("userID", request.getParameter("userID"));
        session.setAttribute("userKey", request.getParameter("userKey"));

        String data = "";
        if(request.getParameter("data") != null) data = request.getParameter("data").toString();

        AuthenticationSecurityFactory factory = new AuthenticationSecurityFactory();
        ID2LAppContext appContext = factory.createSecurityContext(session.getAttribute("appID").toString(), session.getAttribute("appKey").toString());

        int port;
        try {
            port = Integer.parseInt(session.getAttribute("port").toString());
        } catch(Exception e) {
            //fallback to https port
            port = 443;
        }

        String userID=session.getAttribute("userID").toString();
        String userKey=session.getAttribute("userKey").toString();
        ID2LUserContext userContext;
        if(userID.equals("") || userKey.equals("")) {
            userContext = appContext.createAnonymousUserContext(session.getAttribute("host").toString(),
                    port,
                    session.getAttribute("scheme").toString().equals("https"));  
            out.println(getResult(session, request, out, userContext, request.getParameter("req"), request.getParameter("method"), data, 2));
        } else {
            userContext= appContext.createUserContext(userID,
                    userKey,
                    session.getAttribute("host").toString(),
                    port,
                    session.getAttribute("scheme").toString().equals("https"));
            out.println(getResult(session, request, out, userContext, request.getParameter("req"), request.getParameter("method"), data, 2));
        }
    }
%>
