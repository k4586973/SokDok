//socket.io불러오기
"use strict"
const socket = io();


// html 태그 아이디 클래스 연결 
const nickname = document.querySelector("#nickname");
const chatList = document.querySelector(".chatting-list");
const chatInput = document.querySelector("#chatting-input");
const sendButton = document.querySelector(".send-button");
const displayContainer = document.querySelector(".display-container")


// 앤터 누르면 값 전송
chatInput.addEventListener("keypress", (event)=>{
   if(event.keyCode===13){
        send()
   }
   
})

// 글저 전송시 보내내는 값 
function send(){
    const param = {
        name : nickname.value,
        msg : chatInput.value
    }
    socket.emit("chatting",param);

    // 가져왔으니 데이터 빈칸으로 변경
  document.getElementById('chatting-input').value =''

}

//닉네임과 내용을 서버에 전송 전송 버튼시
sendButton.addEventListener("click",send)

// 체팅창에 보여지는 이름 메시지 시간 연결
socket.on("chatting", (data) => {
    const { name, msg, time } = data;
    const item = new LiModel(name, msg, time);
    item.makeLi();

    var className = ''
  // 타입에 따라 적용할 클래스를 다르게 지정
  switch(data.type) {
    case 'disconnect':
      className = 'disconnect'
      break
  }

  msg.classList.add(className)

    // 화면이 다 차면 자동으로 스크롤 설정
    displayContainer.scrollTo(0, displayContainer.scrollHeight)

})

// 시간 메시지 시간 데이터 전송 값 설정
// append 하기 
function LiModel(name,msg,time){
    this.name = name;
    this.msg = msg;
    this.time = time;
    
    // li 태그 안에 값을 보내고 받을때 할때 설정하기 

    // 메서드애서 값을 벋아오기 위함 
    this.makeLi = ()=>{
        const li = document.createElement("li");
        // === 은  두 값이 같은지 비교 하는 거 이다 
        li.classList.add(nickname.value === this.name ? "sent" : "received")
        // 블로오는 html 형식을 html 파일대신 여기에 작성 
        // 내용을 여기에 적어서 innerHTML로 넣어줌 
        const dom = `  <span class="profile">
        <span class="user">${this.name}</span>
        <img class="image" src="upbang/속닥.png" >
    </span>
    <span class="message">${this.msg}</span>
    <span class="time">${this.time}</span>`;
    li.innerHTML = dom;
    // 만든chatList를 appendChild로 li를 집어넣음 
    chatList.appendChild(li)

    }
}


