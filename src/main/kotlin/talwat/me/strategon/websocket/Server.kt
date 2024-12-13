package talwat.me.strategon.websocket

import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.serializersModuleOf
import talwat.me.strategon.Strategist
import talwat.me.strategon.websocket.serializers.UUIDSerializer
import talwat.me.strategon.websocket.server.handler
import java.util.*
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

fun startApplication(): EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration> {
    return embeddedServer(
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
            handler(this, strategists)
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