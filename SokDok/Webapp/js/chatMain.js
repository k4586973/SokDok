	function onMessage(e){	//메세지가 온다면
		var chatMsg = event.data;
		var date = new Date();
		var dateInfo = date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
		
		
		//[현재접속자, 현재접속자, ...]로 보내는 메세지를 members 배열에 넣기
 		if(chatMsg.split("[").length == 2 && chatMsg.split(":").length == 2){
			var list = chatMsg.split("[")
				list = list[1].split("]")
				list = list[0].split(", ")
				
			for (var i = 0; i < list.length; i++) {
					members.push(list[i]);
			}
			return;//더이상 진행을 안하고 대기 상태로 돌아간다.
 		}
 		
 		
		
		// server : ~~~ 내용이 들어오면 if문 처리
		 if(chatMsg.substring(0,6) == 'server'){
			var $chat = $("<div class=ServerChat>" + chatMsg + "</div>");
						//server div 태그에 chatMsg를 담는다.
			
			//server : userNickXXXX로 들어오면 if문 처리
			if(chatMsg.split("userNick").length == 2){ //나눠서 [0,1] 배열이 된다면 포함됬다는 인식
				
				var nick = chatMsg.split("userNick")[1]; // userNick을 기준으로 나눠서 XXXX자리는 닉네임이다
				mynick=nick;
				
				setMyNick(mynick); // 내 닉네임이 보이는 위치를 수정
				return;//더이상 진행을 한하고 대기 상태로간다.
			}
			
			$('.Chat-container').append($chat);//지금 들어온 server 메세지를 채팅박스에 넣는다.
			
			
			//server에 내용에 입장이 있으면 실행한다. [0,1] 배열 크기로 나눠진다. == 문장에 입장이 존재한다.
			if(chatMsg.split("입장").length == 2){
			
				//members 내용에 닉네임을 뽑아내는 메소드로 나눠서 [0,1]나눠지지 않으면 ==> 그런 닉네임은 없다는 
				if($('#members').text().split(getNickName(chatMsg)).length != 2){
					members.push(getNickName(chatMsg));
					//members 배열에 닉네임을 넣는다.
				}
			}
			
			//server에 내용에 퇴장이 있으면 [0,1]로 나누워진다.
			if(chatMsg.split("퇴장").length == 2){
				var index1 = members.indexOf(getNickName(chatMsg));//member 배열에 퇴장하는 닉네임의 index번호를 가져온다.
				members.splice(index1,1);
				//member배열에 (index번호 위치에서 한칸을 지운다);
			}
			
			//server에 변경내용이 있으면 실행한다.
			if(chatMsg.split("변경").length == 2){
				var index1 = members.indexOf(getNickName(chatMsg));
							//mebers배열에 변경하는 닉네임의 index번호를 받는다.
							
				var Cname = getChangeNickName(chatMsg)
							//chatMsg에서 바꿀 닉네임을 받는다.
							
					members[index1]=Cname;//member[변경할 닉네임 번호] 값을 = 바꿀 닉네임 
					
				//alert("변경할 :"+Cname+"현재 :"+mynick+"들어온 :"+getNickName(chatMsg));
				
				
				//현재 나의 닉네임이 바꿀 닉네임과 같다면
				if(mynick==getNickName(chatMsg)){
					mynick=Cname; //현재 닉네임을 바꿀 닉네임으로 바꾸고
					setMyNick(Cname);//닉네임이 표시되는 글도 바꾼다.
					
				}
			}
			
		}else{
			//server : 로 시작하는 문장이 아니라면
			//상대방 채팅 박스 정렬에 chat박스에 담아서 보낸다.												//메세지를 보낸 시간 박스
			var $chat = $("<div class=UserChat><div id=Others><div><img alt='person.svg' src='person.svg' width='40px',height='40px'></div><div id=Chat>" + chatMsg + "</div><div class='chatTime'>"+ dateInfo +"</div></div></div>");
			$('.Chat-container').append($chat);
			
		}

		
		//
			
		//메인 채팅창에 스크롤이 생기면 아래로 내린다.
		$('.Chat-container').scrollTop($('.Chat-container')[0].scrollHeight);
		
		//alert(members)
		membersUpdate();//멤버 박스에 인원을 업데이트 한다.
		
	}
	
	//채팅창 열때 기능 추가
	function onOpen(e){
		
	}
	
	//채팅창 에러 기능 추가
	function onError(e){
		alert(e.data);
	}
	
	//채팅창 보내기 기능 추가
	function send(){
	
		//채팅 메세지 값을 가져온다.

		var chatMsg = inputToChat.value;
		
		
		//비었다면 대기상태로 돌아간다.
		if(chatMsg == ''){
			return;
		}
		
		var date = new Date(); //날짜 객체 생성
		
		var dateInfo = date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds(); //시 : 분 : 초 문자열
		
		members.push("all")
		
		var wmember = members[$("#wmember option").index($("#wmember option:selected"))]
		
		if(wmember == "all"){// 그냥 보내기
		
		//채팅을 내가 보내는 chat 정렬 chat 박스에 담는다.
		var $chat = $("<div class=UserChat><div id=My><div><img alt='person.svg' src='person.svg' width='40px',height='40px'></div><div id=Chat>" + chatMsg + "</div><div class='chatTime'>"+ dateInfo +"</div></div></div>");
		
		//현재 채팅표시 창에 문장을 추가한다.
		$('.Chat-container').append($chat);
		
		//서버로 채팅을 보낸다.
		webSocket.send(chatMsg);
		
		//채팅입력창 내용을 지운다.
		inputToChat.value = "";
		
		//스크롤을 아래로 내린다.
		$('.Chat-container').scrollTop($('.Chat-container')[0].scrollHeight);
		
						
		}else{
		var wMsg="/w:"+wmember+":"+chatMsg
		webSocket.send(wMsg);
		
		wMsg="\'"+wmember+"\'"+chatMsg
				
		//채팅을 내가 보내는 chat 정렬 chat 박스에 담는다.	
		var $chat = $("<div class=UserChat><div id=My><div><img alt='person.svg' src='person.svg' width='40px',height='40px'></div><div id=Chat>" +"(귓속말)"+chatMsg + "</div><div class='chatTime'>"+ dateInfo +"</div></div></div>");

		
		//현재 채팅표시 창에 문장을 추가한다.
		$('.Chat-container').append($chat);
			
		//채팅입력창 내용을 지운다.
		inputToChat.value = "";
		
		//스크롤을 아래로 내린다.
		$('.Chat-container').scrollTop($('.Chat-container')[0].scrollHeight);			
		
		
		
		}
		members.splice(members.indexOf("all"),1)
	}
	
	
	function getNickName(chatMsg) {
		//server : user6302님이 입장하셨습니다. 현재 사용자 2명
		var NickNames = chatMsg.split("님");//님 기준으로 나눈다
					
					//server : user6302
					//  [0] [1] [2]
			NickNames = NickNames[0].split(" ");//spacebar기준으로 나눈다.
		
		var NickName = NickNames[2];// 닉네임을 담는다.
		return NickName;//닉네임을 내본낸다.
	};
	
	function getChangeNickName(chatMsg) {
					//server : user4239님이 1234로 변경되었습니다.
		var NickNames = chatMsg.split("로");//로를 기준으로 나눈다.
					
					//server : user4239님이 1234
					// [0]	 [1] 	[2]		[3]
			NickNames = NickNames[0].split(" ");//뛰어 쓰기 기준으로 나눈다.
		var NickName = NickNames[3];//바꿀 닉네임
		return NickName;//닉네임을 내보낸다.
	};

	function membersUpdate() {
		var all = "귓속말";
		$('#MemName').text(""); //members위치를 비운다.
		$('#wmember').text(null);
		
		
		sortMem=members.sort(); //members 배열을 소트해서 담는다.
		
		//alert(sortMem);
		
		for(var i=0; i<members.length; i++){ //members 배열 수만 큼
												//멤버[i]를 박스에 담아서 추가한다.
			$('#MemName').append("<div id=Members>"+sortMem[i]+"</div>");
			$('#wmember').append("<option value=member>"+sortMem[i]+"</option>");
		}
			$('#wmember').append("<option selected value=all>"+all+"</option>");
		//멤버 수 위치에 멤버길이를 세서 표시한다.
		$('#MemCnt').html("<div id=MemCnt>"+"현재 접속인원 : "+sortMem.length+"명</div>")
		
	}
	
	function NickChange() {	//멤버 버튼을 눌러서
		var CNick=prompt()// 사용자에게 입력창을 열어서 문장을 담는다.
		
		if(CNick!=null){//null 아니면
		
		
			var sendChangeNick = "changeNic:"+CNick;
							//서버에 보낼 문장을 조합한다 : changeNic: + 입력내용	
			//alert(sendChangeNick)
			
			//서버에 보내서 처리한다.
			webSocket.send(sendChangeNick);
		}else{
			//null이면 대기상태로 돌아간다.
			return;
		}
	}
	
	function setMyNick(Cnick){
		//닉네임표시 자리에 변경할 닉네임으로 바꿔 채운다.
		$("#NickName").text(Cnick)
	}

	$(function(){//항상실행 된다.
	
		//보내는메세지내용에 키를 누르면
		$('#inputToChat').keydown(function(key){
			
			//key값이 엔터(아스키 코드)면
			if(key.keyCode == 13){
				//보내는 메세지창에 커서를 돌려준다.
				$('#inputToChat').focus();
				
				//서버로 보낸다.
				send();
			}
		});
		
		//전송 버튼을 눌러서 서버로 보낸다.
		$('#sendInputMsg').click(function(){
			send();
		});
		
	})