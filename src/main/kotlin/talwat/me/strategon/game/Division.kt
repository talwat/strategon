package talwat.me.strategon.game

import org.bukkit.entity.Player

enum class DivisionType(title: String) {
    Archer("Archer"),
    Melee("Melee")
}

data class Division(
    val leader: Player,
    val players: List<Player>,
    val type: DivisionType,
    val title: String? = null
)