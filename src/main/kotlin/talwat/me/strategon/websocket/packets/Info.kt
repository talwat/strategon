package talwat.me.strategon.websocket.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.bukkit.Bukkit
import talwat.me.strategon.game.StrategistColor
import talwat.me.strategon.Strategon
import kotlin.math.roundToLong

@Serializable
class Info(
    val strategist: StrategistColor,

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