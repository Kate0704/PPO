package ppo.battleship.models

import android.content.res.Resources
import com.google.firebase.database.FirebaseDatabase
import ppo.battleship.activities.GameActivity
import ppo.battleship.models.Cell
import ppo.battleship.models.Type

class FieldReviewer(private val cellList: MutableList<Cell>) {
    private var battleships = 0
    private var cruisers = 0
    private var submarines = 0
    private var destroyers = 0

    fun initializeFieldImages(res: Resources, packageName: String) {
        for (i in 1..100) {
            cellList[i - 1].emptyRes = res.getIdentifier("image_part_0$i", "drawable", packageName)
        }
    }

    fun fieldOk(): Boolean {
        if (rightShipPlacement() && rightCount())
            return true
        return false
    }

    fun setHitNeighbours(position: Int): MutableList<Cell> {
        var pos = position
        if (horizontal(pos)){
            while (pos % 10 != 9 && cellList[pos + 1].getCellType() == Type.HIT) {
                pos += 1
            }
            if ((pos % 10 != 9 && cellList[pos + 1].getCellType() == Type.SHIP) ||
                (pos % 10 != 0 && (cellList[pos - 1].getCellType() == Type.MISS ||
                        cellList[pos - 1].getCellType() == Type.EMPTY))) return cellList
            pos = position
            while (pos % 10 != 0 && cellList[pos - 1].getCellType() == Type.HIT) {
                pos -= 1
            }
            if (pos % 10 != 0 && cellList[pos - 1].getCellType() == Type.SHIP) return cellList
            if (pos % 10 != 0) cellList[pos - 1].setCellType(Type.MISS)
            if (pos / 10 != 0 && pos % 10 != 0) cellList[pos - 11].setCellType(Type.MISS)
            if (pos / 10 != 9 && pos % 10 != 0) cellList[pos + 9].setCellType(Type.MISS)
            if (pos / 10 != 0) cellList[pos - 10].setCellType(Type.MISS)
            if (pos / 10 != 9) cellList[pos + 10].setCellType(Type.MISS)
            pos += 1
            while (pos % 10 != 9 && cellList[pos - 1].getCellType() == Type.HIT) {
                if (pos / 10 != 0) cellList[pos - 10].setCellType(Type.MISS)
                if (pos / 10 != 9) cellList[pos + 10].setCellType(Type.MISS)
                pos += 1
            }
            if (pos % 10 == 9) {
                if (pos / 10 != 0) cellList[pos - 10].setCellType(Type.MISS)
                if (pos / 10 != 9) cellList[pos + 10].setCellType(Type.MISS)
                if (cellList[pos].getCellType() == Type.EMPTY) cellList[pos].setCellType(Type.MISS)
            }
            pos -= 1
            if (cellList[pos].getCellType() == Type.EMPTY) cellList[pos].setCellType(Type.MISS)
        }
        else if (vertical(pos)){
            while (pos / 10 != 9 && cellList[pos + 10].getCellType() == Type.HIT) {
                pos += 10
            }
            if ((pos / 10 != 9 && cellList[pos + 10].getCellType() == Type.SHIP) ||
                (pos / 10 != 0 && (cellList[pos - 10].getCellType() == Type.MISS ||
                        cellList[pos - 10].getCellType() == Type.EMPTY))) return cellList
            pos = position
            while (pos / 10 != 0 && cellList[pos - 10].getCellType() == Type.HIT) {
                pos -= 10
            }
            if (pos / 10 != 0 && cellList[pos - 10].getCellType() == Type.SHIP) return cellList
            if (pos % 10 != 0) cellList[pos - 1].setCellType(Type.MISS)
            if (pos % 10 != 9) cellList[pos + 1].setCellType(Type.MISS)
            if (pos / 10 != 0) cellList[pos - 10].setCellType(Type.MISS)
            if (pos / 10 != 0 && pos % 10 != 0) cellList[pos - 11].setCellType(Type.MISS)
            if (pos / 10 != 0 && pos % 10 != 9) cellList[pos - 9].setCellType(Type.MISS)
            pos += 10
            while (pos / 10 != 9 && cellList[pos - 10].getCellType() == Type.HIT) {
                if (pos % 10 != 0) cellList[pos - 1].setCellType(Type.MISS)
                if (pos % 10 != 9) cellList[pos + 1].setCellType(Type.MISS)
                pos += 10
            }
            if (pos / 10 == 9) {
                if (pos % 10 != 0) cellList[pos - 1].setCellType(Type.MISS)
                if (pos % 10 != 9) cellList[pos + 1].setCellType(Type.MISS)
                if (cellList[pos].getCellType() == Type.EMPTY) cellList[pos].setCellType(Type.MISS)
            }
            pos -= 10
            if (cellList[pos].getCellType() == Type.EMPTY) cellList[pos].setCellType(Type.MISS)
        }
        else {
            setNeighboursEmpty(pos)
        }
        return cellList
    }

    private fun setNeighboursEmpty(pos: Int) {
        if (pos % 10 != 0) cellList[pos - 1].setCellType(Type.MISS)
        if (pos % 10 != 9) cellList[pos + 1].setCellType(Type.MISS)
        if (pos / 10 != 0) cellList[pos - 10].setCellType(Type.MISS)
        if (pos / 10 != 9) cellList[pos + 10].setCellType(Type.MISS)
        if (pos / 10 != 0 && pos % 10 != 0) cellList[pos - 11].setCellType(Type.MISS)
        if (pos / 10 != 0 && pos % 10 != 9) cellList[pos - 9].setCellType(Type.MISS)
        if (pos / 10 != 9 && pos % 10 != 0) cellList[pos + 9].setCellType(Type.MISS)
        if (pos / 10 != 9 && pos % 10 != 9) cellList[pos + 11].setCellType(Type.MISS)
    }

    private fun rightShipPlacement(): Boolean {
        for (i in 0..99)
            if (!cellOk(i)) return false
        return true
    }

    private fun rightCount(): Boolean {
        if (battleships / 4 == 1 && cruisers / 3 == 2 && submarines / 2 == 3 && destroyers == 4)
            return true
        return false
    }

    private fun cellOk(pos: Int): Boolean {
        if (cellList[pos].getCellType() == Type.SHIP && rightLength(pos) && nearbyOk(pos)) return true
        if (cellList[pos].getCellType() == Type.EMPTY) return true
        return false
    }

    private fun horizontal (pos: Int): Boolean {
        if (pos % 10 != 9 && (cellList[pos + 1].getCellType() == Type.SHIP ||
            cellList[pos + 1].getCellType() == Type.HIT)) return true
        if (pos % 10 != 0 && (cellList[pos - 1].getCellType() == Type.SHIP ||
            cellList[pos - 1].getCellType() == Type.HIT)) return true
        return false
    }

    private fun vertical (pos: Int): Boolean {
        if (pos / 10 != 9 && (cellList[pos + 10].getCellType() == Type.SHIP ||
            cellList[pos + 10].getCellType() == Type.HIT)) return true
        if (pos / 10 != 0 && (cellList[pos - 10].getCellType() == Type.SHIP ||
            cellList[pos - 10].getCellType() == Type.HIT)) return true
        return false
    }

    private fun rightLength(position: Int): Boolean {
        var length = 1
        var pos = position
        if (horizontal(position)) {
            while (pos % 10 != 9 && cellList[pos + 1].getCellType() == Type.SHIP) {
                length += 1
                pos += 1
            }
            pos = position
            while (pos % 10 != 0 && cellList[pos - 1].getCellType() == Type.SHIP) {
                length += 1
                pos -= 1
            }
        }
        else if (vertical(position)) {
            while (pos / 10 != 9 && cellList[pos + 10].getCellType() == Type.SHIP) {
                length += 1
                pos += 10
            }
            pos = position
            while (pos / 10 != 0 && cellList[pos - 10].getCellType() == Type.SHIP) {
                length += 1
                pos -= 10
            }
        }
        if (length > 4) return false
        when (length) {
            4 -> battleships += 1
            3 -> cruisers += 1
            2 -> submarines += 1
            1 -> destroyers += 1
        }
        return true
    }

    private fun nearbyOk (pos: Int): Boolean {
        if ((pos / 10 != 9 && pos % 10 != 9 && cellList[pos + 11].getCellType() == Type.SHIP) ||
            (pos / 10 != 9 && pos % 10 != 0 && cellList[pos + 9].getCellType() == Type.SHIP) ||
            (pos / 10 != 0 && pos % 10 != 9 && cellList[pos - 9].getCellType() == Type.SHIP) ||
            (pos / 10 != 0 && pos % 10 != 0 && cellList[pos - 11].getCellType() == Type.SHIP))
            return false
        if (horizontal(pos) || (!horizontal(pos) && !vertical(pos))) {
            if ((pos / 10 != 0 && cellList[pos - 10].getCellType() == Type.SHIP) ||
                (pos / 10 != 9 && cellList[pos + 10].getCellType() == Type.SHIP))
                return false
        }
        else if (vertical(pos) || (!horizontal(pos) && !vertical(pos))) {
            if ((pos % 10 != 9 && cellList[pos + 1].getCellType() == Type.SHIP) ||
                (pos % 10 != 0 && cellList[pos - 1].getCellType() == Type.SHIP))
                return false
        }
        return true
    }
}