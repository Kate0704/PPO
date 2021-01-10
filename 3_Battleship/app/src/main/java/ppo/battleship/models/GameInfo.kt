package ppo.battleship.models

class GameInfo(val server: String?, val client: String?, val server_field: String?,
               val client_field: String?) {
    var whoNext: String = "server_move"
}