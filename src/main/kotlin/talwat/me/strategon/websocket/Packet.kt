package talwat.me.strategon.websocket

import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonClassDiscriminator
import talwat.me.strategon.game.SetupDivision

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
sealed class Packet {
    @Serializable
    @SerialName("hello")
    class Hello(val username: String) : Packet()

    @Serializable
    @SerialName("setupRequested")
    data object SetupRequested : Packet()

    @Serializable
    @SerialName("setup")
    class Setup(val divisions: List<SetupDivision>) : Packet()

    @Serializable
    @SerialName("test")
    data object Test : Packet()
}

suspend fun Client.sendPacket(packet: Packet) {
    this.socket.send(Frame.Text(Json.encodeToString(packet)))
}

suspend fun Client.recievePacket(): Packet {
    return this.socket.receiveDeserialized()
}
