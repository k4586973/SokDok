<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Chatting on Net</title>
<link rel="stylesheet" href="css/chatMain.css" /> <!-- .css받아오기 -->
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script> <!-- jQuery 링크로 받아오기 -->
<script type="text/javascript" src="js/chatMain.js"></script> <!-- function() 받아오기 -->
<script type="text/javascript" src="js/newBang.js"></script>
</head>
<body>
<div align="center">
	<div class=Maincontainer style="background-color: teal;">
		<div class=Chat-Title>채팅2번방</div> 
		<div class=MemInfo>
			<div id=MemCnt></div>
			<div id=MemName>
			</div>
		</div>
		<div class=RoomInfo><button onclick="newBang()">다른방 가기</button></div>
		
		<div class=Chat-container></div>
		
		<div id=NickName align="center"></div>
		<div class=W-Chat>
			<button onclick="NickChange()">닉네임 변경</button>
			<select id=wmember>
				<option>귓속말</option>
			</select>
		</div>
		<div class=inputBox><input id="inputToChat" type="text" placeholder="채팅을 입력해주세요"/><button id=sendInputMsg>보내기</button></div>
		<button id=logout onclick="logout()">로그아웃</button>
	</div>
</div>

 
</body>

<script type="text/javascript">
	

	//var textarea = document.getElementById("messageWindow");
	var webSocket = new WebSocket('ws://localhost:8080/WebChat123/kaja2');
	
	var inputMessage = document.getElementById('inputMessage');
	
	var members = new Array;//멤버 배열
	
	var mynick;//현재 접속 닉네임
	
	webSocket.onerror = function(e){//에러 발생처리
		onError(e);
	};
	webSocket.onopen = function(e){//채팅방 입장처리
		onOpen(e);
	};
	webSocket.onmessage = function(e){//채팅방 메세지 주고받는 처리
		onMessage(e);
	};
</script>