package talwat.me.strategon.websocket.packets
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Player(
    val x: Long,
    val z: Long,
    val name: String,
    @Serializable(with = UUIDSerializer::class)
    val uuid: UUID
)