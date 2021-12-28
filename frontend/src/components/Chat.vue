<template>
  <div id="app">
    {{this.roomId}}
    유저이름: 
    <input
      v-model="userName"
      type="text"
    >
    내용: <input
      v-model="message"
      type="text"
      @keyup="sendMessage"
    >
    <!-- <div
      v-for="(item, idx) in recvList"
      :key="idx"
    >
      <h3>유저이름: {{ item.userName }}</h3>
      <h3>내용: {{ item.content }}</h3>
    </div> -->
  </div>
</template>

<script>
// import axios from 'axios'
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'
// var sock = new SockJS("/ws-stomp");
// var ws = Stomp.over(sock);
// var reconnect = 0;

export default {
  name: 'App',
  props: {
    roomId: {
      type: String,
      default: 'basic',
    }
  },
  data() {
    return {
      userName: "",
      // message: "",
      recvList: [],
    
      room: {},
      sender: '',
      message: '',
      messages: []
    }
  },
  created() {
    // App.vue가 생성되면 소켓 연결을 시도합니다.
    console.log(this.roomId)
    this.connect()
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
          userName: this.userName,
          content: this.message 
        };
        this.stompClient.send("/receive", JSON.stringify(msg), {});
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