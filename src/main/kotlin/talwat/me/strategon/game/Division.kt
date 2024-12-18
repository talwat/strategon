package talwat.me.strategon.game

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bukkit.entity.Player
import talwat.me.strategon.websocket.serializers.UUIDSerializer
import java.util.*

enum class DivisionType(title: String) {
    @SerialName("archer")
    Archer("Archer"),

    @SerialName("melee")
    Melee("Melee")
}

@Serializable
data class SetupDivision(
    @Serializable(with = UUIDSerializer::class)
    val leader: UUID,
    val players: Set<@Serializable(with = UUIDSerializer::class) UUID>,
    val type: DivisionType,
    val title: String? = null
)

data class Division(
    val leader: Player,
    val players: Set<Player>,
    val type: DivisionType,
    val title: String? = null
)