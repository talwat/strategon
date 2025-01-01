package talwat.me.strategon

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import talwat.me.strategon.game.Game
import talwat.me.strategon.websocket.Client
import talwat.me.strategon.websocket.server.setup
import talwat.me.strategon.websocket.startApplication

// Rust enums would make this SO MUCH better...
// God, I miss Rust :(
enum class Signal {
    Lobby,
    SetupStart,
}

// Global data, primarily used to bridge the gap between
// web server code & minecraft code.
object Global {
    var game: Game? = null
    var clients: Array<Client>? = null
    var channel: Channel<Signal> = Channel(Channel.BUFFERED)
}

class Strategon : JavaPlugin(), Listener {
    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this)
        val app = startApplication()

        this.logger.info("Initialized!")

        runBlocking {
            for (signal in Global.channel) {
                when (signal) {
                    Signal.Lobby -> TODO()
                    Signal.SetupStart -> {
                        Global.game = setup()
                        Bukkit.getLogger().info("Setup done!")git a
                    }
                }
            }
        }

        this.logger.info("Ending?")
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.player.sendMessage(Component.text("Hello, " + event.player.name + "!"))
    }
}
