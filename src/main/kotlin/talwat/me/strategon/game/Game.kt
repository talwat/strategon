package talwat.me.strategon.game

import talwat.me.strategon.Strategist
import talwat.me.strategon.websocket.Packet

class Game(
    val strategists: Array<Strategist>, setup: Array<Packet.Setup>
) {
    val divisions = setup.map { x -> x.divisions.mapNotNull { it.evaluate() } }.toTypedArray()
}