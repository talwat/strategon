package talwat.me.strategon

import io.ktor.server.websocket.*
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import talwat.me.strategon.websocket.Signal

@Serializable
enum class Team {
    @SerialName("red")
    Red,

    @SerialName("blue")
    Blue
}

@Serializable
data class Strategist(
    val username: String,
    val team: Team
)
