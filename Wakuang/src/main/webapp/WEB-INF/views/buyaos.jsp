<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page session="false" %>
<html>  
<head>  
<script type="text/javascript" src="/resource/js/jquery-3.2.1.js"/>

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
  	<script src="/js/jquery.min.js"></script>
    <script src="/js/sockjs-0.3.min.js"></script>  
  
    <script type="text/javascript">  
        var ws = null;  
        var url = null;  
        var transports = [];  
        
        var onload = false;
        
        var thisRate = "168.3";
        
        var tk = "no";
  
        function setConnected(connected) {  
            document.getElementById('connect').disabled = connected;  
            document.getElementById('disconnect').disabled = !connected;  
            document.getElementById('echo').disabled = !connected;  
            document.getElementById('echo1').disabled = !connected;  
            document.getElementById('echo2').disabled = !connected;  
        }  
  
        function connect() {  
        	if('${param.tk}' != '') {
        		tk = '${param.tk}';
        	}
/*             if (!url) {  
                alert('Select whether to use W3C WebSocket or SockJS');  
                return;  
            }   */
              
              
            //ws = new WebSocket('ws://192.168.10.107:8080/mspjapi/webSocketServer');/* (url.indexOf('sockjs') != -1) ?   
                //new SockJS(url, undefined, {protocols_whitelist: transports}) :  */  
                ws = new SockJS("/webSocketServer/sockjs");  
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
        	if(msg == 3) {
        		onload = true;
        		document.getElementById('echo1').disabled = true;
        	}else if(msg == 2) {
        		onload = false;
        		document.getElementById('echo1').disabled = false;
        		Reset();
        	}
        	
            if (ws != null) {
            	var rateCNY = $("#rate_CNY").val();
            	var rateJPY = $("#rate_JPY").val();
            	var rateUSD = $("#rate_USD").val();
            	var totalPrice = $("#totalPrice").val();
            	
            	var buyPlatform = $("#platform1").val();
            	var sellPlatform = $("#platform2").val();
            	var obj = {
            		"totalPrice" : totalPrice*10000,
            		"rate" : rateCNY,
            		"rateCNY" : rateCNY,
            		"rateJPY" : rateJPY,
            		"rateUSD" : rateUSD,
            		"type" : msg,
            		"tufaQingkuang" : tk,
            		"buyPlatform" : buyPlatform,
            		"sellPlatform" : sellPlatform
            	}
            	if(rateCNY != "") {
            		thisRate = rateCNY;
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
        Reset();
        start();
        	var jsonInfo = JSON.parse(data);
        	$("#sysTime").html(jsonInfo.SYSTEM_TIME);
        	$("#plaform1th").html(jsonInfo.PLATFORM1);
        	$("#plaform1krwth").html(jsonInfo.PLATFORM1 + "_KRW");
        	$("#plaform2th").html(jsonInfo.PLATFORM2);
        	
        	
        	var html = "";
        	
        	var jsonComaireDatas = JSON.parse(jsonInfo.COMPAIRE_DATA);
        	for(var i in jsonComaireDatas){
        	    html = html + "平台" + jsonComaireDatas[i].COMPAIRE_INFO.PLATFORM1 + "  " + jsonComaireDatas[i].COMPAIRE_INFO.PLATFORM2 +"<br>";
        	    html = html + "汇率：" + jsonComaireDatas[i].COMPAIRE_INFO.EXCHANGERATE ;
        	    html = html + "<table id='coinInfo' border='1'>             ";
	    	    html = html + "	<thead>                                     ";
	    	    html = html + "		<tr>                                    ";
	    	    html = html + "		<th width='50'>方向</th>                ";
	    	    html = html + "		<th width='60'>币种</th>                  ";
	    	    html = html + "		<th id='plaform1th'>"+jsonComaireDatas[i].COMPAIRE_INFO.PLATFORM1+"价格</th>      ";
	    	    html = html + "		<th id='plaform1krwth'>"+jsonComaireDatas[i].COMPAIRE_INFO.PLATFORM1+"韩元价格</th>";
	    	    html = html + "		<th id='plaform2th'>"+jsonComaireDatas[i].COMPAIRE_INFO.PLATFORM2+"价格</th>      ";
	    	    html = html + "		<th>差价</th>                           ";
	    	    html = html + "		<th width='50'>收益率</th>              ";
	    	    html = html + "		<th width='60'>收益额</th>               ";
	    	    html = html + "		</tr>                                   ";
	    	    html = html + "	</thead>                                    ";
	    	    html = html + "	<tbody>                                     ";

        	    for(var j in jsonComaireDatas[i]){
	        	    if("COMPAIRE_INFO" != j){
	        	        var result = jsonComaireDatas[i][j]
		        		var fromTo = "";
		        		if(result.compare >= 0){
		        			fromTo = "<font size='3' color='red'>-></font>"
		        		}else{
		        			fromTo = "<font size='3' color='blue'><-</font>"
		        		}
		        		
		        		var trColor = "";
		        		if(result.shouyi_rate <= 0){
		        			trColor = "bgcolor='gray'";
		        		}else if(result.shouyi_rate > 0.03){
		        			trColor = "bgcolor='green'";
		        		}
		        	    html = html + "<tr " + trColor + ">";
		        	    html = html + "<td>" + fromTo + "</td>";
		        	    html = html + "<td>" + j + "</td>";
		        	    html = html + "<td>";
		        	    if("XRP"==j){
		        	        html = html + result.map.PRICE;
		        	    }else{
			        	    html = html + formatMoney(result.map.PRICE, true);
		        	    }
		        	    html = html + "</td>";
		        	    html = html + "<td>" + formatMoney(parseFloat((result.map.PRICE) * parseFloat(jsonComaireDatas[i].COMPAIRE_INFO.EXCHANGERATE))+"", 0) + "</td>";
		        	    html = html + "<td>" + formatMoney(result.map2.PRICE, 0) + "</td>";
		        	    html = html + "<td>" + formatMoney(result.compare, 0) + "</td>";
		        	    html = html + "<td>" + ((result.shouyi_rate*100).toFixed(4)) + "%</td>";
		        	    html = html + "<td>" + formatMoney(result.shouyi_e.toFixed(0)/10000, true) + "</td>";
		        	    
		      		  	html = html + "</tr>";
	        	        
	        	    }
        	        
        	    }
	    	    html = html + "	</tbody>                                    ";
	    	    html = html + "</table></br>                                     ";
        	}
        	$("#coinInfos").html(html);
        }
        
        function rmoney(s) {  
        	s = s + "";
            return parseFloat(s.replace(/[^\d\.-]/g, ""));  
        } 
        
        function formatMoney(s, type) {  
        	s = s+"";
            if (/[^0-9\.\-]/.test(s))  
                return "0";  
            if (s == null || s == "")  
                return "0";  
            s = s.toString().replace(/^(\d*)$/, "$1.");  
            s = (s + "00").replace(/(\d*\.\d\d)\d*/, "$1");  
            s = s.replace(".", ",");  
            var re = /(\d)(\d{3},)/;  
            while (re.test(s))  
                s = s.replace(re, "$1,$2");  
            s = s.replace(/,(\d\d)$/, ".$1");  
            if (type == 0) {// 不带小数位(默认是有小数位)  
                var a = s.split(".");  
                //if (a[1] == "00") {  
                    s = a[0];  
                //}  
            }  
            return s;  
        } 
        
        function jiSuanQi() {
            
            var buyPlatform = $("#buyPlatform").val();
            var rate = "";
            if("COINCHECK" == buyPlatform){
                rate = $("#rate_JPY").val();
            }else if("haha" == buyPlatform){
                rate = $("#rate_USD").val();
            }else{
                rate = $("#rate_CNY").val();
            }
            
        	$.ajax({
        	     type: 'GET',
        	     url: '/home/yieldRate' ,
        	     data: {
        	    	 "buy_price":$("#buyPrice").val(),
        	    	 "buyPlatform":buyPlatform,
        	    	 "coinType":$("#coinType").val(),
        	    	 "buyNum":$("#buyNum").val(),
        	    	 "sellPlatform":$("#sellPlatform").val(),
        	    	 "sell_price":$("#sellPrice").val(),
        	    	 "rate":rate
        	     } ,
        	     success: function(data) {
        	    	 console.info(data);
        	    	 if(data.resultCode = "000") {
        	    		 var rs = data.result;
        	    		 $("#yieldRate").val((rs.shouyi_rate*100)+" %");
        	    		 
        	    		 $("#yieldRatePrice").val(formatMoney(rs.shouyi_e,true));
        	    		 $("#yieldRatePriceYuan").val(formatMoney(rs.shouyiEyuan,true));
        	    		 
        	    		 $("#tikuanKrp").val(formatMoney(rs.tikuanShouxuFei,true));
        	    		 $("#tikuanCny").val(formatMoney(rs.tikuanShouxuFeiYuan,true));
        	    	 }
        	     } ,
        	     dataType: 'json'
        	});
        }
        /**초시계****************************************************************************/
 		var hour,minute,second;//时 分 秒
        hour=minute=second=0;//初始化
        var millisecond=0;//毫秒
        var int;
        function Reset()//重置
        {
            window.clearInterval(int);
            millisecond=hour=minute=second=0;
            document.getElementById('timetext').value='00时00分00秒000毫秒';
        }
 
        function start()//开始
        {
            int=setInterval(timer,50);
        }
 
        function timer()//计时
        {
            millisecond=millisecond+50;
            if(millisecond>=1000)
            {
                millisecond=0;
                second=second+1;
            }
            if(second>=60)
            {
                second=0;
                minute=minute+1;
            }
 
            if(minute>=60)
            {
                minute=0;
                hour=hour+1;
            }
            document.getElementById('timetext').value=hour+'时'+minute+'分'+second+'秒'+millisecond+'毫秒';
 
        }
 
        function stop()//暂停
        {
            window.clearInterval(int);
        }
        /**********************************************************************************/
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
        	人民币汇率: <input type="text" value="168.3" id="rate_CNY" /> <br>
        	日元汇率: <input type="text" value="10.31437174" id="rate_JPY" /> <br>
        	美元汇率: <input type="text" value="1136.00" id="rate_USD" /> <br>
        	
        	投入价格： <input type="text" value="1000" id="totalPrice" />
        	<br>
			<select id="platform1">
				<option value='BIDUOBAO'>币多宝</option>
				<option value='JUBI'>聚币</option>
				<option value='COINCHECK'>COINCHECK</option>
				<option value='BETR'>BETR</option>
			</select>
        	
			<select id="platform2">
				<option value='BITHUM'>BITHUM</option>
			</select>
        </div>
        <div>  
            <textarea id="message" style="width: 350px">Here is a message!</textarea>  
        </div>  
        <div>  
            <button style="display: none;" id="echo" onclick="echo();" disabled="disabled">Echo message</button>
            <button id="echo1" onclick="echo('3');" disabled="disabled">START</button>
            <button id="echo2" onclick="echo('2');" disabled="disabled">END</button>
        </div>  
        
        <div>갱신시간 : <div id="sysTime"></div></div>
        
        <input type="text" id="timetext" value="00时00分00秒" readonly><br>
    		<!-- <button type="button" onclick="start()">开始</button> <button type="button" onclick="stop()">暂停</button> <button type="button" onclick="Reset()">重置</button> -->
    </div>  

      
    <br>
    <div style="float: left; " id="coinInfos">
	    <table id="coinInfo" border="1">
	    	<thead>
	    		<tr>
	    		<th width="50">方向</th>
	    		<th width="60">币种</th>
	    		<th id="plaform1th">平台1价格</th>
	    		<th id="plaform1krwth">平台1韩元价格</th>
	    		<th id="plaform2th">平台2价格</th>
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

<div style="width: 100%; float: left; margin-top: 30px; " >
	<table style="">
		<thead>
			<tr>
				<th colspan="2">收益计算器</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>请选择买入的平台:</td>
				<td>
					<select id="buyPlatform">
						<option value='BIDUOBAO'>币多宝</option>
						<option value='BITHUM'>BITHUM</option>
						<option value='JUBI'>JUBI</option>
						<option value='BETR'>BETR</option>
						<option value='COINCHECK'>COINCHECK</option>
					</select>
				</td>
				
			</tr>
			
			<tr>
				<td>请选择币种:</td>
				<td>
					<select id="coinType">
						<option value='BTC'>比特币</option>
						<option value='ETH'>以太坊</option>
						<option value='XRP'>瑞波币</option>
						<option value='LTC'>莱特币</option>
						<option value='ETC'>以太经典</option>
					</select>
				</td>
				
			</tr>
			
			<tr>
				<td>买入价:</td>
				<td>
					<input value="" type="text" id="buyPrice" />
				</td>
				
			</tr>
			
			
			<tr>
				<td>买入个数:</td>
				<td>
					<input value="" type="text" id="buyNum" />
				</td>
				
			</tr>
			
			<tr>
				<td>请选择卖出的平台:</td>
				<td>
					<select id="sellPlatform">
						<option value='BIDUOBAO'>币多宝</option>
						<option value='BITHUM'>BITHUM</option>
						<option value='JUBI'>JUBI</option>
					</select>
				</td>
				
			</tr>
			
			<tr>
				<td>卖出价:</td>
				<td>
					<input value="" type="text" id="sellPrice" />
				</td>
				
			</tr>
			
			<tr>
				<td colspan="2">
					<input onclick="jiSuanQi();" type="button" value=" 计  算  "  />
				</td>

				
			</tr>
			
			<tr>
				<td>收益率:</td>
				<td>
					<input value="" type="text" id="yieldRate" readonly="readonly" />
				</td>
				
			</tr>
			
			<tr>
				<td>最终收益(krp):</td>
				<td>
					<input value="" type="text" id="yieldRatePrice" readonly="readonly" />
				</td>
				
			</tr>
			
			<tr>
				<td>最终收益(cny):</td>
				<td>
					<input value="" type="text" id="yieldRatePriceYuan" readonly="readonly" />
				</td>
				
			</tr>
			
			<tr>
				<td>提款手续费(krp):</td>
				<td>
					<input value="" type="text" id="tikuanKrp" readonly="readonly" />
				</td>
				
			</tr>
			
			<tr>
				<td>提款手续费(cny):</td>
				<td>
					<input value="" type="text" id="tikuanCny" readonly="readonly" />
				</td>
				
			</tr>
		</tbody>
	</table>
</div>

  
</body>  
</html> 
