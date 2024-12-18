package talwat.me.strategon

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
