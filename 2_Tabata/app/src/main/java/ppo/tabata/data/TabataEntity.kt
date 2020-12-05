package ppo.tabata.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class TabataEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var name: String,
    var color: String,
    var warm_up: Int,
    var work: Int,
    var rest: Int,
    var repeats: Int,
    var cycles: Int,
    var cooldown:Int
) : Serializable
