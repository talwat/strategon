package talwat.me.strategon.game

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
enum class StrategistColor {
    Red,
    Blue
}

class Strategist(val uuid: UUID, val color: StrategistColor)

class Game(val strategists: Pair<UUID, UUID>) {

}