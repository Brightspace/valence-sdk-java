/******************************************************************************
 * Javascript functions for the sample HTML file                              *
 ******************************************************************************/

/**
* D2LValence package, auth module.
*
* Copyright (c) 2012 Desire2Learn Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License"); you may not
* use this file except in compliance with the License. You may obtain a copy of
* the license at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations under
* the License.
 */

String.prototype.trim = String.prototype.trim || function(){
    return this.replace(/^\s+|\s+$/g, '');
};

function deauthenticate() {
    window.location.replace('index.jsp');
}

function doAPIRequest() {
    document.getElementById("errorField").hidden = true;
	var host = $('#hostField').val();
	var port = $('#portField').val();
	var scheme = $('#schemeField').is(':checked') ? 'https' : 'http';
    var appKey = $('#appKeyField').val();
    var appID = $('#appIDField').val();
    var userKey = $('#userKeyField').val();
    var userID = $('#userIDField').val();
    var data = $('#dataField').val();

	var req = $('#actionField').val();
	var method = $('#GETField').is(':checked') ? 'GET' :
	             $('#POSTField').is(':checked') ? 'POST' :
							 $('#PUTField').is(':checked') ? 'PUT' :
	             'DELETE';


    
    var error = function(jqXHR, textStatus, errorThrown) {
        document.getElementById('errorField').innerHTML = '<span class="error">Error: </span>' + errorThrown.toString();
        document.getElementById('errorField').hidden = false;
	}

	$.ajax({
		url: 'doAPIRequest.jsp',
		data: {
			host: host,
			port: port,
			scheme: scheme,
			req: req,
			method: method,
            appKey: appKey,
            appID: appID,
            userKey: userKey,
            userID: userID,
            data: data
		},
		success: function(data) {
            data = data.trim();
            var output = "No response (success)";
            if(data.match(/^Error:\ /)) {
                error(null, null, data.replace(/^Error:\ /, ''));
                return;
            }
            try {
                if(data !== "") output = JSON.stringify(JSON.parse(data), null, '\t');
            } catch(e) {
                error(null, null, "Failed to parse data as JSON: " + escape(data));
            }
            document.getElementById('responseField').value = output;            
            document.getElementById('responseField').hidden = false;
		},
        error: error
	});
}
