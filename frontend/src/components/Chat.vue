<template>
  <div>
    <div class="">
				<button @click="disconnect" v-if="status === 'connected'">연결끊기</button>
				<button @click="connect" v-if="status === 'disconnected'">연결</button> {{ status }}
				<br /><br />
				<div v-if="status === 'connected'">
					<form @submit.prevent="sendMessage" action="#">
						<input v-model="message"><button type="submit">메세지 전송</button>
					</form>
					<ul id="logs">
						<li v-for="(log, idx) in logs" class="log" :key="idx">
							{{ log.event }}: {{ log.data }}
						</li>
					</ul>
        <!-- <div>
          <div id="roomNameAlgo">
            <h3>채팅방 입니다.</h3>
          </div>

					<div id="roomBox">
						<div v-for="(obj, index) in receivedMessages" :key="index">
								입퇴장
								<div v-if="obj.senderId === null" id="chatboxcome">
										<div id="Ccome">{{ obj.content }}</div>
								</div>
								남이 보낸거
								<div v-else-if="obj.senderId != userEmail" id="chatboxleft">
										<div id="Cname">
												<a class="link-1"
														><strong @click="toProfile(obj.senderId)">{{ obj.sender }}</strong>
												</a>
										</div>
										<div id="Ctext">{{ obj.content }}</div>
								</div>

								내가 보낸거
								<div v-else id="chatboxright">
										<div id="CMname">
												<strong>{{ obj.sender }}</strong>
										</div>
										<div id="CMtext">{{ obj.content }}</div>
								</div>
						</div>
					</div>
        </div> -->
			</div>
    </div>
  </div>
</template>

<script>

export default {
    name: 'Chat',
    data() {
			return {
				receivedMessages: [],
				message: "",
				logs: [],
				status: "disconnected",
			}
    },
		methods: {
			connect() {
				this.socket = new WebSocket("ws://localhost:8081/ws/chat");
				this.socket.onopen = () => {
				this.status = "connected";
				// this.logs.push({ event: "연결 완료: ", data: 'wss://echo.websocket.org'})
				

				this.socket.onmessage = ({data}) => {
					this.logs.push({ event: "메세지 수신", data });
				};
				};
			},
			disconnect() {
				this.socket.close();
				this.status = "disconnected";
				this.logs = [];
			},
			sendMessage() {
				this.socket.send(this.message);
				this.logs.push({ event: "메시지 전송", data: this.message });
				this.message = "";
			}
		}
   
}
</script>

<style>

</style>