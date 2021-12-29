<template>
  <div>
    <div style="width:500px; display:inline-block">
      <h1>채팅방 목록</h1>
      <div v-for="chatRoom in chatRooms" :key="chatRoom.roomId" style="width:300px;" @click="openChat(chatRoom.roomId)">
        채팅 방이름 : {{chatRoom.name}} 
        채팅 번호 : {{chatRoom.roomId}} 
      </div>
    </div>
    <Chat :roomId="this.roomId" style="width:500px; display:inline-block"/>

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

</style>