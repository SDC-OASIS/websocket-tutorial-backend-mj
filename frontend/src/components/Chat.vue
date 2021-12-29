<template>
  <div class="chat-container">
    <div v-if="this.roomId ==''">
      <div style="margin-top:200px">
        채팅에 참여하시려면 채팅방을 클릭해주세요.
      </div>
    </div>
    <div v-else>
      <!-- <div>{{this.roomId}}</div> -->
      <div v-for="(item, idx) in recvList" :key="idx">
        <div v-if="item.type == 'JOIN'">
          <h5>{{ item.message }}</h5>
        </div>
        <div v-else style="display:inline-block;  margin:5px;">
          <span style="padding-right:10px;">{{ item.sender }}</span>
          <span style="width:300px; background-color:#FFFFFF; padding:3px;">{{ item.message }}</span>
        </div>
      </div>
      <div class="input-container">
        유저: <input v-model="userName" type="text" style="width:70px">
        내용: <input v-model="message" type="text" @keyup="sendMessage" style="width:200px">
      </div>
    </div>
    
    
  </div>
</template>

<script>
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'

export default {
  name: 'App',
  props: {
    roomId: {
      type: String,
      default: 'basic', //왜 default가 안먹히는지 추후 확인
    }
  },
  data() {
    return {
      userName: "희은",
      message: "",
      recvList: [],
    }
  },
  created() {
    this.connect()
  },
  watch: {
    roomId: function(){
      this.recvList=[] // 새로운 방 입장시 초기화
      this.connect() // 새로운 방 다시 연결
    }
  },
  methods: {
    // findRoom: function() {
    //     axios.get('/chat/room/'+this.roomId).then(response => { this.room = response.data; });
    // },
    // sendMessage: function() {
    //     ws.send("/pub/chat/message", {}, JSON.stringify({type:'TALK', roomId:this.roomId, sender:this.sender, message:this.message}));
    //     this.message = '';
    // },
    // recvMessage: function(recv) {
    //     this.messages.unshift({"type":recv.type,"sender":recv.type=='ENTER'?'[알림]':recv.sender,"message":recv.message})
    // },
    // connect:function() {
    //     // pub/sub event
    //     ws.connect({}, function() {
    //         ws.subscribe("/sub/chat/room/"+this.roomId, function(message) {
    //             var recv = JSON.parse(message.body);
    //             this.recvMessage(recv);
    //         });
    //         ws.send("/pub/chat/message", {}, JSON.stringify({type:'ENTER', roomId: this.roomId, sender:this.sender}));
    //     }, function() {
    //         if(reconnect++ <= 5) {
    //             setTimeout(function() {
    //                 console.log("connection reconnect");
    //                 sock = new SockJS("/ws-stomp");
    //                 ws = Stomp.over(sock);
    //                 this.connect();
    //             },10*1000);
    //         }
    //     });
    // },
    sendMessage (e) {
      console.log(e)
      if(e.keyCode === 13 && this.userName !== '' && this.message !== ''){
        this.send()
        this.message = ''
      }
    },    
    send() {
      console.log("Send message:" + this.message);
      if (this.stompClient && this.stompClient.connected) {
        const msg = { 
          type:'TALK',
          roomId: this.roomId,
          sender: this.userName,
          message: this.message 
        };
        this.stompClient.send("/pub/chat/message", JSON.stringify(msg), res=>{
          console.log('메세지 보내기', res)
        });
      }
    },    
    connect() {
      const serverURL = "http://localhost:8080/ws-stomp"
      let socket = new SockJS(serverURL); //소켓을 만든다.
      console.log(socket)
      this.stompClient = Stomp.over(socket); //소켓을 연결한다.
      console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`)
      this.stompClient.connect(
        {},
        frame => {
          // 소켓 연결 성공
          this.connected = true;
          console.log('소켓 연결 성공', frame);
          // 서버의 메시지 전송 endpoint를 구독합니다.
          // 이런형태를 pub sub 구조라고 합니다.
          this.stompClient.subscribe("/sub/chat/room/"+this.roomId, res => {
            console.log('구독으로 받은 메시지 입니다.', res.body);

            // 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
            this.recvList.push(JSON.parse(res.body))
          });
          const msg = {
            type:'JOIN', 
            roomId: this.roomId, 
            sender: this.userName,
          }
          this.stompClient.send("/pub/chat/message", JSON.stringify(msg), {});
        },
        error => {
          // 소켓 연결 실패
          console.log('소켓 연결 실패', error);
          this.connected = false;
        }
      );        
    }
  }
}
</script>
<style scoped>
.chat-container {
  width:500px; 
  height: 500px;
  display:inline-block;
  background-color: rgb(175, 206, 241);
}
.input-container{
  position: absolute;
  top: 70vh;
  right:20vw;
  display: inline-block;
  width:500px;
}
</style>