package talwat.me.strategon.websocket

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PacketType {
    @SerialName("info")
    Info,

    @SerialName("hello")
    Hello,
}

@Serializable
class Packet<T>(val type: PacketType, val data: T? = null)