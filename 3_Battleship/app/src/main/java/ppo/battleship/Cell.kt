package ppo.battleship

import android.graphics.Color

class Cell {
    private var type = Type.EMPTY
    var imgRes = Color.BLACK
    fun getCellType() = type
    fun setCellType(type: Type) { this.type = type }
}

enum class Type {
    SHIP,
    EMPTY,
    HIT,
    MISS
}

