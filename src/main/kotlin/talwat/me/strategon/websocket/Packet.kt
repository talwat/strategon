package talwat.me.strategon.websocket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import talwat.me.strategon.game.SetupDivision

@Serializable
enum class PacketType {
    @SerialName("info")
    Info,

    @SerialName("hello")
    Hello,

    @SerialName("setupRequested")
    SetupRequested,

    @SerialName("setup")
    Setup,
}

@Serializable
data class Packet<T>(val type: PacketType, val data: T? = null)

@Serializable
class Hello(val username: String)

@Serializable
data class Setup(val divisions: List<SetupDivision>)