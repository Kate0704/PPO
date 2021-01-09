package ppo.battleship.models

class GameInfo(server: String?, client: String?, server_field: String?,client_field: String?) {
    var who_next: String = "p_1_move"
    var status = GameStatus.CREATION
}

enum class GameStatus {
    CREATION,
    PLAY,
    GAME_OVER
}