package talwat.me.strategon.websocket

import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import talwat.me.strategon.Strategist
import talwat.me.strategon.Team
import talwat.me.strategon.websocket.packets.Hello
import kotlin.time.Duration.Companion.seconds

enum class State {
    WaitingForPlayers,
    WaitingForSetup,
    Running
}

class Server {
    val application = embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = false)
}

fun Application.configureSockets() {
    var strategists: Array<Strategist?> = Array(2) { null };

    routing {
        webSocket("/play") {
            var strategist: Strategist = if (strategists.first() == null) {
                strategists[0] = Strategist("test", Team.Red)

                strategists[0]
            } else {
                strategists[1] = Strategist("test", Team.Blue)

                strategists[1]
            }!!

            sendSerialized(Packet(PacketType.Hello, Hello(strategist.team)))

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