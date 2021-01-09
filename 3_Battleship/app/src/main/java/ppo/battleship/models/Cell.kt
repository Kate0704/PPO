package ppo.battleship.models

import android.graphics.Color
import ppo.battleship.R

class Cell() {
    private var type = Type.EMPTY
    var emptyRes = Color.BLACK
    val hitRes = R.drawable.hit
    fun getCellType(): Type = type
    fun setCellType(type: Type) { this.type = type }
}

enum class Type {
    SHIP,
    EMPTY,
    HIT,
    MISS
}

