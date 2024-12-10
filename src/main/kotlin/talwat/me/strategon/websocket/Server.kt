package talwat.me.strategon.websocket

import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import org.bukkit.Bukkit
import talwat.me.strategon.Strategist
import talwat.me.strategon.Team
import kotlin.time.Duration.Companion.seconds

enum class Signal {
    Lobby,
    Setup,
    GameRunning
}

data class Client(
    val socket: WebSocketServerSession,
    val channel: Channel<Signal>
)

class Server {
    val application = embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = false)
}

fun Application.configureSockets() {
    val strategists: Array<Pair<Strategist, Client>?> = Array(2) { null };

    routing {
        webSocket("/play") {
            val username = call.request.queryParameters["username"]
            if (username == null) {
                close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Username not included in URL params"))
                return@webSocket
            }

            val (team, index) = if (strategists.first() == null) {
                Team.Red to 0
            } else if (strategists.last() == null) {
                Team.Blue to 1
            } else {
                // Add kicking of Strategists
                close(CloseReason(CloseReason.Codes.INTERNAL_ERROR, "Two strategists have already been selected"))
                return@webSocket
            }

            val strategist = Strategist(username, team)
            val client = Client(this, Channel(Channel.BUFFERED))
            strategists[index] = strategist to client

            Bukkit.getLogger().info("strategist: $strategist, client: $client")

            // Say hello, and notify the strategist of their details.
            sendSerialized(Packet(PacketType.Hello, Hello(strategist)))

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

            sendSerialized(Packet(PacketType.SetupRequested, null))

            val frame = incoming.receive();
            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    val receivedText = frame.readText()
                }
            }
        }
    }
}

fun Application.module() {
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json {
            encodeDefaults = true
        })

        pingPeriod = 15.seconds
        timeout = 15.seconds
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    configureSockets()
}