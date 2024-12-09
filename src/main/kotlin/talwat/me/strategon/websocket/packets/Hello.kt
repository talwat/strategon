package talwat.me.strategon.websocket.packets

import kotlinx.serialization.Serializable
import talwat.me.strategon.Team

@Serializable
class Hello(val team: Team)