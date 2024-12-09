package talwat.me.strategon.websocket.packets

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.bukkit.Bukkit
import talwat.me.strategon.Strategon
import talwat.me.strategon.Team
import talwat.me.strategon.websocket.packets.info.Player
import kotlin.math.roundToLong

@Serializable
class Info(
    val team: Team,

    // Kotlin is, unfortunately, just as stupid as Java.
    // Now, we have to give it a "default" which will never be used,
    // because this class will never be deserialized.
    @Transient val plugin: Strategon = Strategon()
) {
    val players = Bukkit.getOnlinePlayers().map { player ->
        Player(player.x.roundToLong(), player.z.roundToLong(), player.name, player.uniqueId)
    }

    init {
        plugin.logger.info(players.toString())
    }
}