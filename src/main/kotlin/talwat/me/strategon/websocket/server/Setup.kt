package talwat.me.strategon.websocket.server

import io.ktor.server.websocket.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import talwat.me.strategon.Global
import talwat.me.strategon.Strategist
import talwat.me.strategon.Team
import talwat.me.strategon.game.Game
import talwat.me.strategon.websocket.Packet
import talwat.me.strategon.websocket.PacketType
import talwat.me.strategon.websocket.Setup

suspend fun setup(): Game {
    val strategists = Global.clients!!.mapIndexed { index, client ->
        val team = if (index == 0) {
            Team.Red
        } else {
            Team.Blue
        }

        Strategist(client.username, team)
    }.toTypedArray()

    val setups: Array<Setup>
    coroutineScope {
        val asks = Global.clients!!.map { client ->
            async {
                client.socket.sendSerialized(Packet(PacketType.SetupRequested, null))
                val setup: Packet<Setup> = client.socket.receiveDeserialized()

                setup.data
            }
        }

        setups = awaitAll(asks[0], asks[1]).requireNoNulls().toTypedArray()
    }

    return Game(strategists, setups)
}