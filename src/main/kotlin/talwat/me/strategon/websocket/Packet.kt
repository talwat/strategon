package talwat.me.strategon.websocket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import talwat.me.strategon.Strategist

@Serializable
enum class PacketType {
    @SerialName("info")
    Info,

    @SerialName("hello")
    Hello,

    @SerialName("setupRequested")
    SetupRequested,
}

@Serializable
class Packet<T>(val type: PacketType, val data: T? = null)

@Serializable
class Hello(val strategon: Strategist)
