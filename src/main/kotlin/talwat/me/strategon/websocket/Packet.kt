package talwat.me.strategon.websocket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import talwat.me.strategon.Strategist
import talwat.me.strategon.game.Division
import talwat.me.strategon.game.SetupDivision

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
data class Packet<T>(val type: PacketType, val data: T? = null)

@Serializable
class Hello(val strategon: Strategist)

@Serializable
data class Setup(val divisions: List<SetupDivision>)