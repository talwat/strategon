package talwat.me.strategon.websocket.packets.info

import kotlinx.serialization.Serializable
import talwat.me.strategon.websocket.serializers.UUIDSerializer
import java.util.*

@Serializable
data class Player(
    val x: Long,
    val z: Long,
    val name: String,

    @Serializable(with = UUIDSerializer::class)
    val uuid: UUID
)