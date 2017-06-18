<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page session="false" %>
<html>  
<head>  
<!-- <script type="text/javascript" src="/resource/js/jquery-3.2.1.js"/> -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">  
<title>WebSocket/SockJS Echo Sample (Adapted from Tomcat's echo sample)</title>  
    <style type="text/css">  
        #connect-container {  
            float: left;  
            width: 400px  
        }  
  
        #connect-container div {  
            padding: 5px;  
        }  
  
        #console-container {  
            float: left;  
            margin-left: 15px;  
            width: 400px;  
        }  
  
        #console {  
            border: 1px solid #CCCCCC;  
            border-right-color: #999999;  
            border-bottom-color: #999999;  
            height: 170px;  
            overflow-y: scroll;  
            padding: 5px;  
            width: 100%;  
        }  
  
        #console p {  
            padding: 0;  
            margin: 0;  
        }  
    </style>  
  
    <script src="http://cdn.sockjs.org/sockjs-0.3.min.js"></script>  
  
    <script type="text/javascript">  
        var ws = null;  
        var url = null;  
        var transports = [];  
        
        var thisRate = "165";
  
        function setConnected(connected) {  
            document.getElementById('connect').disabled = connected;  
            document.getElementById('disconnect').disabled = !connected;  
            document.getElementById('echo').disabled = !connected;  
            document.getElementById('echo1').disabled = !connected;  
            document.getElementById('echo2').disabled = !connected;  
        }  
  
        function connect() {  
/*             if (!url) {  
                alert('Select whether to use W3C WebSocket or SockJS');  
                return;  
            }   */
              
              
            //ws = new WebSocket('ws://192.168.10.107:8080/mspjapi/webSocketServer');/* (url.indexOf('sockjs') != -1) ?   
                //new SockJS(url, undefined, {protocols_whitelist: transports}) :  */  
                ws = new SockJS("http://wawakwang.iptime.org:8080/webSocketServer/sockjs");  
                //console.log("http://192.168.10.107:8080/mspjapi/webSocketServer/sockjs");  
                  
            ws.onopen = function () {  
                setConnected(true);  
                $("#message").val('Info: connection opened.');
            };  
              
            ws.onmessage = function (event) {  
                //log('Received: ' + event.data);
                setCoinGrid(event.data);
            };  
              
            ws.onclose = function (event) {  
                setConnected(false);  
                $("#message").val('Info: connection closed.');  
                //log(event);  
            };  
        }  
  
        function disconnect() {  
            if (ws != null) {  
                ws.close();  
                ws = null;  
            }  
            setConnected(false);  
        }  
  
        function echo(msg) {  
            if (ws != null) {
            	var rate = $("#rate").val();
            	var totalPrice = $("#totalPrice").val();
            	var obj = {
            		"totalPrice" : totalPrice,
            		"rate" : rate,
            		"type" : msg
            	}
            	if(rate != "") {
            		thisRate = rate;
            	}
                ws.send(JSON.stringify(obj));  
            } else {  
                alert('connection not established, please connect.');  
            }  
        }  
  
        function updateUrl(urlPath) {  
            if (urlPath.indexOf('sockjs') != -1) {  
                url = urlPath;  
                document.getElementById('sockJsTransportSelect').style.visibility = 'visible';  
            }  
            else {  
              if (window.location.protocol == 'http:') {  
                  url = 'ws://' + window.location.host + urlPath;  
              } else {  
                  url = 'wss://' + window.location.host + urlPath;  
              }  
              document.getElementById('sockJsTransportSelect').style.visibility = 'hidden';  
            }  
        }  
  
        function updateTransport(transport) {  
          transports = (transport == 'all') ?  [] : [transport];  
        }  
          
        function log(message) {  
            var console = document.getElementById('console');  
            var p = document.createElement('p');  
            p.style.wordWrap = 'break-word';  
            p.appendChild(document.createTextNode(message));  
            console.appendChild(p);  
            while (console.childNodes.length > 25) {  
                console.removeChild(console.firstChild);  
            }  
            console.scrollTop = console.scrollHeight;  
        }  
        
        function setCoinGrid(data){
        	var jsonInfo = JSON.parse(data);
        	var html = "";
        	
        
        	for(var i in jsonInfo)
        	{
        		var fromTo = "";
        		if(jsonInfo[i].compare >= 0){
        			fromTo = "<font size='3' color='red'>-></font>"
        		}else{
        			fromTo = "<font size='3' color='blue'><-</font>"
        		}
        		
        		var trColor = "";
        		if(jsonInfo[i].shouyi_rate <= 0){
        			trColor = "bgcolor='gray'";
        		}else if(jsonInfo[i].shouyi_rate > 0.03){
        			trColor = "bgcolor='green'";
        		}
        	    html = html + "<tr " + trColor + ">";
        	    html = html + "<td>" + fromTo + "</td>";
        	    html = html + "<td>" + i + "</td>";
        	    html = html + "<td>" + jsonInfo[i].map.PRICE + "</td>";
        	    html = html + "<td>" + parseFloat(jsonInfo[i].map.PRICE) * parseFloat(thisRate) + "</td>";
        	    html = html + "<td>" + jsonInfo[i].map2.PRICE + "</td>";
        	    html = html + "<td>" + jsonInfo[i].compare + "</td>";
        	    html = html + "<td>" + jsonInfo[i].shouyi_rate.toFixed(4)*100 + "%</td>";
        	    html = html + "<td>" + jsonInfo[i].shouyi_e.toFixed(0)/10000 + "</td>";
        	    
      		  	html = html + "</tr>";
        	}
        	$("#coinInfo tbody").html(html);
        }
    </script>  
</head>  
<body>  
<noscript><h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websockets   
    rely on Javascript being enabled. Please enable  
    Javascript and reload this page!</h2></noscript>  
<div>  
    <div id="connect-container">  
        <!-- <input id="radio1" type="radio" name="group1" onclick="updateUrl('/mspjapi');">  
            <label for="radio1">W3C WebSocket</label>  
        <br>  
        <input id="radio2" type="radio" name="group1" onclick="updateUrl('/spring-websocket-test/sockjs/echo');">  
            <label for="radio2">SockJS</label>   -->
        <div id="sockJsTransportSelect" style="visibility:hidden;">  
            <span>SockJS transport:</span>  
            <select onchange="updateTransport(this.value)">  
              <option value="all">all</option>  
              <option value="websocket">websocket</option>  
              <option value="xhr-polling">xhr-polling</option>  
              <option value="jsonp-polling">jsonp-polling</option>  
              <option value="xhr-streaming">xhr-streaming</option>  
              <option value="iframe-eventsource">iframe-eventsource</option>  
              <option value="iframe-htmlfile">iframe-htmlfile</option>  
            </select>  
        </div>  
        <div>  
            <button id="connect" onclick="connect();">Connect</button>  
            <button id="disconnect" disabled="disabled" onclick="disconnect();">Disconnect</button>  
        </div>  
        <div>
        	汇率: <input type="text" value="" id="rate" /> <br>
        	投入价格： <input type="text" id="totalPrice" />
        </div>
        <div>  
            <textarea id="message" style="width: 350px">Here is a message!</textarea>  
        </div>  
        <div>  
            <button id="echo" onclick="echo();" disabled="disabled">Echo message</button>
            <button id="echo1" onclick="echo('1');" disabled="disabled">START</button>
            <button id="echo2" onclick="echo('2');" disabled="disabled">END</button>
        </div>  
    </div>  

      
    <br>
    <div>
	    <table id="coinInfo" border="1">
	    	<thead>
	    		<tr>
	    		<th width="50">方向</th>
	    		<th width="60">币种</th>
	    		<th>平台1价格</th>
	    		<th>平台1韩元价格</th>
	    		<th>平台2价格</th>
	    		<th>差价</th>
	    		<th width="50">收益率</th>
	    		<th width="60">收益额</th>
	    		</tr>
	    	</thead>
	    	<tbody>
	    	
	    	</tbody>
	    </table>
    </div>
    
<!--         <br>
            <br>
                <br>
                    <br>
                        <br>
    <div id="console-container">  
        <div id="console"></div>  
    </div> -->
</div>  

  
</body>  
</html> 
