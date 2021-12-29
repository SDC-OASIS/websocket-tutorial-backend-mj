<template>
  <div style="display:flex;">
    <div class="chat-room-container">
      <h1>채팅방 목록</h1>
      <div class="chat-room-inner-container">
        <div class="chat-room-item" v-for="chatRoom in chatRooms" :key="chatRoom.roomId" style="" @click="openChat(chatRoom.roomId)">
          채팅 방이름 : {{chatRoom.name}}  "입장하려면 클릭!"
          <!-- 채팅 번호 : {{chatRoom.roomId}}  -->
        </div>
      </div>
    </div>
    <Chat :roomId="this.roomId" style="display:inline-block" />

  </div>
</template>

<script>
import axios from 'axios';
import Chat from './Chat.vue'


export default {
  name:'ChatRoom',
  components: {
    Chat,
  },
  data: function(){
    return{
      chatRooms: [],
      roomId: '',
    }
  },
  methods: {
    getRooms: function(){
      axios({
        method: "get",
        url: "http://localhost:8080/chat/rooms"
      })
      .then((res)=> {
        // console.log(res.data)
        this.chatRooms = res.data
      })
      .catch((err)=>{
        console.log(err)
      })
    },
    openChat: function(data){
      this.roomId = data
    }
  },
  created: function(){
    this.getRooms();
  }
}
</script>

<style>
.chat-room-container {
  width:400px; 
  padding: 50px 20px;
  display:inline-block; 
  background-color:#eef8b7;
}
.chat-room-inner-container {
  border: 1px solid #5e5029;
}
.chat-room-item {
  border: 1px solid #5e5029;
  padding: 20px 10px;
}
</style>