package talwat.me.strategon.websocket

import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.serialization.json.Json
import talwat.me.strategon.Global
import talwat.me.strategon.Signal
import talwat.me.strategon.websocket.server.handle
import kotlin.time.Duration.Companion.seconds

data class Client(
    val socket: WebSocketServerSession,
    val username: String,
)

fun startApplication(): EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration> {
    return embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = false)
}

fun Application.configureSockets() {
    val clients: Array<Client?> = Array(2) { null }

    routing {
        webSocket("/play") {
            val index = handle(this, clients) ?: return@webSocket

            // Send the signal if all clients have joined.
            if (clients.all { x -> x != null }) {
                Global.clients = clients.requireNoNulls()
                Global.channel.send(Signal.SetupStart)
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