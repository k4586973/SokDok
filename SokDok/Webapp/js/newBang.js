function newBang() {
	$(".RoomInfo").html("<button onclick='closeBang()'>방닫기</button>");
	
	if($(".Chat-Title").text().split('1').length == 2){
		$(".RoomInfo").append("<button onclick='toSecondRoom()'>채팅2번방가기</button><button onclick='toTherdRoom()'>채팅3번방가기</button>")
	}else if($(".Chat-Title").text().split('2').length == 2){
		$(".RoomInfo").append("<button onclick='toFirstRoom()'>채팅1번방가기</button><button onclick='toTherdRoom()'>채팅3번방가기</button>")
	}else if($(".Chat-Title").text().split('3').length == 2){
		$(".RoomInfo").append("<button onclick='toFirstRoom()'>채팅1번방가기</button><button onclick='toSecondRoom()'>채팅2번방가기</button>")
	}
}

function closeBang() {
	$(".RoomInfo").html("");
	
	$(".RoomInfo").html("<button onclick='newBang()'>다른 방가기</button>");
}

function toFirstRoom(){
	location.href="http://localhost:8080/WebChat123/chatMain.jsp";
}

function toSecondRoom(){
	location.href="http://localhost:8080/WebChat123/chatMain2.jsp";
}

function toTherdRoom(){
	location.href="http://localhost:8080/WebChat123/chatMain3.jsp";
}

function logout() {
	window.location.href="http://localhost:8080/WebChat123/index.html";
}
