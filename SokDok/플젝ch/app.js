//express 를 모듈에서 불러옴
const express = require("express");
const http = require("http");
//express의 실행 내용을 app에 담음
const app = express();
const path = require("path")
//express가 http로 실행될 수 있도록
const server = http.createServer(app);
//소켓 사용하기
const socketIO = require("socket.io");
//변수에 서버 담기
const io = socketIO(server);
//모먼트 사용(시간)
var moment = require("moment");
//한국시간으로 모먼트 설정
require('moment-timezone'); 
moment.tz.setDefault("Asia/Seoul");

//서버가 실행이 되면 보여줄 폴더를 지정 
//path.join : 운영체제마다 경로를 나타내주는 기호가 다르기 때문에
// __dirname 은 현재 실행 중인 폴더 경로
app.use(express.static(path.join(__dirname, "src")));

//프로세스 환경에 포트가 지정되어 있으면 그것을 실행 아니면 8080실행
const PORT = process.env.PORT || 8080;

//연결이 되면 그것의 정보를 소켓에 담음
//소켓의 정보에 접근
io.on("connection", (socket) => {
   
         
  
    //"채팅아이디"
    socket.on("chatting", (data) => {
        const { name, msg } = data;
        io.emit("chatting", {
            name,
            msg,
            time : moment(new Date()).format("h:mm A") 
        })


 

        /* 접속 종료 */
  socket.on('disconnect', function() {
    console.log(` ${name} 님이 나갔습니다`)

  })
    })
    

      
    
});
//app.listen(포트,명령) 포트라는 변수를 사용하기 위해서 사용
server.listen(PORT, ()=> console.log(`서버 접속중 : ${PORT}`));

//서버 ctrl c 중지
//nodemon라이브러리를 사용하면 js파일에 변경이 있을 때 마다 자동으로 서버 재실행 
// nodemon app.js 으로 실행하면 자동으로 해줌!