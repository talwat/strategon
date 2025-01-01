package talwat.me.strategon.websocket.server

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.bukkit.Bukkit
import talwat.me.strategon.Global
import talwat.me.strategon.Strategist
import talwat.me.strategon.Team
import talwat.me.strategon.game.Game
import talwat.me.strategon.websocket.Packet
import talwat.me.strategon.websocket.recievePacket
import talwat.me.strategon.websocket.sendPacket

suspend fun setup(): Game {
    val strategists = Global.clients!!.mapIndexed { index, client ->
        val team = if (index == 0) {
            Team.Red
        } else {
            Team.Blue
        }

        Strategist(client.username, index, team)
    }.toTypedArray()

    val setups: Array<Packet.Setup>
    coroutineScope {
        val asks = Global.clients!!.map { client ->
            async {
                client.sendPacket(Packet.SetupRequested)
                val setup: Packet.Setup = client.recievePacket() as Packet.Setup
                setup
            }
        }

        setups = awaitAll(asks[0], asks[1]).requireNoNulls().toTypedArray()
    }

    return Game(strategists, setups)
}