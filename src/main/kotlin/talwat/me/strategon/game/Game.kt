package talwat.me.strategon.game

import kotlinx.serialization.Serializable
import talwat.me.strategon.Strategist
import java.util.UUID

class Game(
    val strategists: Pair<Strategist, Strategist>,
    val divisions: Pair<List<Division>, List<Division>>
)