package ppo.battleship

class Cell {
    private var type = Type.EMPTY
    fun getCellType() = type
    fun setCellType(type: Type) { this.type = type }
}

enum class Type {
    SHIP,
    EMPTY,
    HIT,
    MISS
}

