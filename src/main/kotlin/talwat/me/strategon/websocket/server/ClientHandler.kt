package talwat.me.strategon.websocket.server

import io.ktor.server.application.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import talwat.me.strategon.websocket.Client
import talwat.me.strategon.websocket.Packet
import talwat.me.strategon.websocket.sendPacket

/**
 * Responsible for getting the initial client & strategist from both players.
 */
suspend fun Application.handle(
    session: WebSocketServerSession,
    clients: Array<Client?>
): Int? {
    val username = session.call.request.queryParameters["username"]
    if (username == null) {
        session.close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Username not included in URL params"))
        return null
    }

    val index = if (clients.first() == null) {
        0
    } else if (clients.last() == null) {
        1
    } else {
        session.close(CloseReason(CloseReason.Codes.INTERNAL_ERROR, "Two strategists have already been selected"))
        return null
    }

    val client = Client(session, username)
    clients[index] = client

    // Say hello, and notify the strategist of their details.
    client.sendPacket(Packet.Hello(username))

    return index
}