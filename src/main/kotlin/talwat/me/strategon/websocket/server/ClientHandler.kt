package talwat.me.strategon.websocket.server

import io.ktor.server.application.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import org.bukkit.Bukkit
import talwat.me.strategon.Strategist
import talwat.me.strategon.Team
import talwat.me.strategon.websocket.*

suspend fun Application.handler(
    session: WebSocketServerSession,
    strategists: Array<Pair<Strategist, Client>?>
) {
    val username = session.call.request.queryParameters["username"]
    if (username == null) {
        session.close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Username not included in URL params"))
        return
    }

    val (team, index) = if (strategists.first() == null) {
        Team.Red to 0
    } else if (strategists.last() == null) {
        Team.Blue to 1
    } else {
        // Add kicking of Strategists
        session.close(CloseReason(CloseReason.Codes.INTERNAL_ERROR, "Two strategists have already been selected"))
        return
    }

    val strategist = Strategist(username, team)
    val client = Client(session, Channel(Channel.BUFFERED))
    strategists[index] = strategist to client

    Bukkit.getLogger().info("strategist: $strategist, client: $client")

    // Say hello, and notify the strategist of their details.
    session.sendSerialized(Packet(PacketType.Hello, Hello(strategist)))

    if (strategists.all { x -> x != null }) {
        strategists.forEach { x -> x!!.second.channel.send(Signal.Setup) }
    }

    while (true) {
        val signal = client.channel.receive()
        Bukkit.getLogger().info("got signal: $signal")

        if (signal == Signal.Setup) {
            break
        }
    }

    session.sendSerialized(Packet(PacketType.SetupRequested, null))

    val setup: Packet<Setup> = session.receiveDeserialized();
    Bukkit.getLogger().info("$setup")

    session.incoming.consumeEach { frame ->
        if (frame is Frame.Text) {
            val receivedText = frame.readText()
        }
    }
}