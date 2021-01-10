package ppo.battleship.activities

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ppo.battleship.adapters.GridAdapter
import ppo.battleship.databinding.ActivityGameBinding
import ppo.battleship.models.Cell
import ppo.battleship.models.FieldReviewer
import ppo.battleship.models.Type
import java.util.*

class GameActivity : AppCompatActivity() {
    private val binding by lazy { ActivityGameBinding.inflate(layoutInflater) }
    private var database: FirebaseDatabase? = null
    private var player_1_field_db: DatabaseReference? = null
    private var player_2_field_db: DatabaseReference? = null
    private var player_1: DatabaseReference? = null
    private var player_2: DatabaseReference? = null
    private var player_1_score: DatabaseReference? = null
    private var player_2_score: DatabaseReference? = null
    private var currentMove: DatabaseReference? = null
    private var field_1_listener: ValueEventListener? = null
    private var field_2_listener: ValueEventListener? = null
    private var moveChangesListener: ValueEventListener? = null
    private var score_1_changedListener: ValueEventListener? = null
    private var score_2_changedListener: ValueEventListener? = null
    private var scoreView_1_listener: ValueEventListener? = null
    private var scoreView_2_listener: ValueEventListener? = null
    private var gameId: String? = null
    private var started_game = false
    private var secondPlayerJoined = false
    private var mProgressBar: ProgressBar? = null
    private var player_1_field: MutableList<Cell> = MutableList(100) { Cell() }
    var player_2_field: MutableList<Cell> = MutableList(100) { Cell() }
    private lateinit var adapter1: GridAdapter
    private lateinit var adapter2: GridAdapter
    private var score1 = 0
    private var score2 = 0
    private val winPoints = 20
    private var whoMoves = WhoMoves.PLAYER_1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameId = intent.getStringExtra("id")
        started_game = intent.getBooleanExtra("start", false)
        binding.progressBarGAMEWAIT.visibility = View.VISIBLE
        binding.gameHolder.isEnabled = false

        adapter1 = GridAdapter(resources, player_1_field) {cell, pos -> unClickable(cell, pos)}
        adapter2 = GridAdapter(resources, player_2_field) {cell, pos -> cellClicked(cell, pos)}

        FieldReviewer(player_1_field).initializeFieldImages(resources, packageName)
        binding.field1.layoutManager = GridLayoutManager(this, 10)
        binding.field1.adapter = adapter1

        FieldReviewer(player_2_field).initializeFieldImages(resources, packageName)
        binding.field2.layoutManager = GridLayoutManager(this, 10)
        binding.field2.adapter = adapter2

        database = FirebaseDatabase.getInstance()
        val game = database!!.getReference("games").child(gameId!!)
        currentMove = game.child("whoNext")
        player_1_field_db = game.child("server_field")
        player_2_field_db = game.child("client_field")
        player_1_score = game.child("server_score")
        game.child("server_score").setValue(0)
        player_2_score = game.child("client_score")
        game.child("client_score").setValue(0)
        player_1 = game.child("server")
        player_2 = game.child("client")
        player_2!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.value != "") {
                    initFirstPlayerField()
                    initSecondPlayerField()
                    trackCurrentMove()
                    trackScore1Update()
                    trackScore2Update()
                    initStatsView()
                    binding.progressBarGAMEWAIT.visibility = View.GONE
                    binding.gameHolder.isEnabled = true
                    player_2!!.removeEventListener(this)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
        setContentView(binding.root)
    }

    private fun initFirstPlayerField() {
        field_1_listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.getValue(String::class.java) == null) {
                    onBackPressed()
                    return
                }
                if (Objects.requireNonNull<String>(dataSnapshot.getValue(String::class.java)).isEmpty()) {
                    finish()
                    return
                }
                val gson = Gson()
                val type = object : TypeToken<MutableList<Cell>>(){}.type
                val value: String = dataSnapshot.getValue(String::class.java) as String
                val cellList = gson.fromJson(value, type) as MutableList<Cell>
                secondPlayerJoined = true
                if (started_game) {
                    player_1_field = cellList
                    adapter1 = GridAdapter(resources, player_1_field) {cell, pos -> unClickable(cell, pos)}
                    binding.field1.layoutManager = GridLayoutManager(applicationContext, 10)
                    binding.field1.adapter = adapter1
                }
                else {
                    player_2_field = cellList
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        player_1_field_db!!.addValueEventListener(field_1_listener!!)
    }

    private fun initSecondPlayerField() {
        field_2_listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.getValue(String::class.java) == null) {
                    onBackPressed()
                    return
                }
                if (Objects.requireNonNull<String>(dataSnapshot.getValue(String::class.java))
                        .isEmpty() && secondPlayerJoined) {
                    finish()
                    return
                }
                val value = dataSnapshot.getValue(String::class.java)!!
                if (Objects.requireNonNull(value).isNotEmpty()) {
                    val gson = Gson()
                    val type = object : TypeToken<MutableList<Cell>>(){}.type
                    val cellList = gson.fromJson(value, type) as MutableList<Cell>
                    secondPlayerJoined = true
                    if (!started_game) {
                        player_1_field = cellList
                        adapter1 = GridAdapter(resources, player_1_field) {cell, pos -> unClickable(cell, pos)}
                        binding.field1.layoutManager = GridLayoutManager(applicationContext, 10)
                        binding.field1.adapter = adapter1
                    }
                    else {
                        player_2_field = cellList
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        player_2_field_db!!.addValueEventListener(field_2_listener!!)
    }

    private fun unClickable (cell: Cell, pos: Int): Boolean {
        return false
    }

    private fun cellClicked(cell: Cell, pos: Int): Boolean {
        if (whoMoves == WhoMoves.PLAYER_1) {
            val type = player_2_field[pos].getCellType()
            cell.setCellType(type)
            if (type == Type.HIT || type == Type.MISS) return false
            else if (type == Type.EMPTY) {
                cell.setCellType(Type.MISS)
                player_2_field[pos].setCellType(Type.MISS)
                if (started_game) currentMove!!.setValue("client_move")
                else currentMove!!.setValue("server_move")
            }
            else if (type == Type.SHIP) {
                cell.setCellType(Type.HIT)
                player_2_field[pos].setCellType(Type.HIT)
                player_2_field = FieldReviewer(player_2_field).setHitNeighbours(pos)
                val oldCellList = adapter2.cellList
                for (i in 0..99) {
                    if (player_2_field[i].getCellType() != Type.SHIP)
                        oldCellList[i].setCellType(player_2_field[i].getCellType())
                }
                adapter2.submitList(oldCellList)
                hitMessage()
                if (started_game) {
                    score1++
                    player_1_score!!.setValue(score1)
                } else {
                    score2++
                    player_2_score!!.setValue(score2)
                }
            }
            val gson = Gson()
            val jsonField1 = gson.toJson(player_1_field)
            val jsonField2 = gson.toJson(player_2_field)
            if (started_game) {
                player_1_field_db!!.setValue(jsonField1)
                player_2_field_db!!.setValue(jsonField2)
            } else {
                player_1_field_db!!.setValue(jsonField2)
                player_2_field_db!!.setValue(jsonField1)
            }
            return true
        }
        return false
    }

    private fun trackCurrentMove() {
        moveChangesListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!secondPlayerJoined) return
                if (dataSnapshot.getValue(String::class.java) == null) {
                    onBackPressed()
                    return
                }
                val value = dataSnapshot.getValue(String::class.java)!!
                if (started_game) {
                    if (Objects.requireNonNull(value) == "server_move") {
                        whoMoves = WhoMoves.PLAYER_1
                        binding.whoMovesView.text = "MY TURN"
                    }
                    else {
                        whoMoves = WhoMoves.PLAYER_2
                        binding.whoMovesView.text = "OPPONENT's TURN"
                    }
                } else {
                    if (Objects.requireNonNull(value) == "server_move") {
                        whoMoves = WhoMoves.PLAYER_2
                        binding.whoMovesView.text = "OPPONENT's TURN"
                    }
                    else {
                        whoMoves = WhoMoves.PLAYER_1
                        binding.whoMovesView.text = "MY TURN"
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        currentMove!!.addValueEventListener(moveChangesListener!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (moveChangesListener != null) currentMove!!.removeEventListener(moveChangesListener!!)
        if (field_1_listener != null) player_1_field_db!!.removeEventListener(field_1_listener!!)
        if (field_2_listener != null) player_2_field_db!!.removeEventListener(field_2_listener!!)
        if (scoreView_1_listener != null) player_1!!.removeEventListener(scoreView_1_listener!!)
        if (scoreView_2_listener != null) player_2!!.removeEventListener(scoreView_2_listener!!)
        if (score_2_changedListener != null) player_2_score!!.removeEventListener(
            score_2_changedListener!!
        )
        if (score_1_changedListener != null) player_1_score!!.removeEventListener(
            score_1_changedListener!!
        )
    }

    private fun initStatsView() {
        scoreView_1_listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(String::class.java)
                if (value == null) {
                    onBackPressed()
                    return
                }
                if (started_game) binding.player1FieldName.text = value
                else binding.player2FieldName.text = value
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        player_1!!.addValueEventListener(scoreView_1_listener!!)
        scoreView_2_listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(String::class.java)
                if (value == null) {
                    onBackPressed()
                    return
                }
                if (started_game) binding.player2FieldName.text = value
                else binding.player1FieldName.text = value
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        player_2!!.addValueEventListener(scoreView_2_listener!!)
    }

    override fun onBackPressed() {
        database!!.getReference("games").child(gameId!!).removeValue()
        super.onBackPressed()
    }

    private fun hitMessage() {
        Snackbar.make(binding.gameHolder,"Carry on my wayward son!",
                BaseTransientBottomBar.LENGTH_SHORT).show()
    }

    private fun gameEnded(win: Boolean) {
        val mes: String = if (win) "CONGRATULATIONS!\n YOU ARE THE WINNER!" else "U loose :c\n Unlucky in games, lucky in love!"
        saveStats()

        val alertDialog2 = AlertDialog.Builder(this)
        alertDialog2.setPositiveButton("OK") { _, _ ->
            onBackPressed()
        }
        alertDialog2.setOnDismissListener{
            onBackPressed()
        }
        val adContainer = LinearLayout(applicationContext)
        val textView = TextView(applicationContext)
        textView.text = mes
        textView.gravity = Gravity.CENTER
        textView.textSize = 32F
        adContainer.addView(textView, 0)
        val girlImageView = ImageView(this)
        if (win) girlImageView.setImageResource(ppo.battleship.R.drawable.girl_win)
        else girlImageView.setImageResource(ppo.battleship.R.drawable.girl_loose)
        adContainer.addView(girlImageView, 1)
        adContainer.gravity = Gravity.CENTER
        adContainer.orientation = LinearLayout.VERTICAL
        girlImageView.maxHeight = 300
        alertDialog2.setView(adContainer)
        alertDialog2.show()
    }

    private fun saveStats() {
        val stats = database!!.getReference("stats").child(gameId!!)
        stats.child("server").setValue(binding.player1FieldName.text)
        stats.child("server_score").setValue(score1)
        stats.child("client").setValue(binding.player2FieldName.text)
        stats.child("client_score").setValue(score2)
    }

    private fun trackScore1Update() {
        score_1_changedListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.getValue(Int::class.java) == null) {
                    onBackPressed()
                    return
                }
                val value = dataSnapshot.getValue(Int::class.java)!!
                score1 = value
                if (started_game) {
                    binding.scorePlayer1.text = score1.toString()
                } else {
                    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    if (Objects.requireNonNull(vibrator).hasVibrator()) {
                        vibrator.vibrate(VibrationEffect.createOneShot(100L, 5))
                    }
                    binding.scorePlayer2.text = score1.toString()
                }
                if (value == winPoints) {
                    if (started_game) gameEnded(true) else gameEnded(false)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        player_1_score!!.addValueEventListener(score_1_changedListener!!)
    }

    private fun trackScore2Update() {
        score_2_changedListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.getValue(Int::class.java) == null) {
                    onBackPressed()
                    return
                }
                val value = dataSnapshot.getValue(Int::class.java)!!
                score2 = value
                if (started_game) {
                    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    if (Objects.requireNonNull(vibrator).hasVibrator()) {
                        vibrator.vibrate(VibrationEffect.createOneShot(100L, 5))
                    }
                    binding.scorePlayer2.text = score2.toString()
                } else {
                    binding.scorePlayer1.text = score2.toString()
                }
                if (value == 1) {
                    if (!started_game) gameEnded(true) else gameEnded(false)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        player_2_score!!.addValueEventListener(score_2_changedListener!!)
    }

    private enum class WhoMoves {
        PLAYER_1,
        PLAYER_2
    }
}

//        player_2_field_db!!.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (dataSnapshot.getValue(String::class.java) == null) {
//                    onBackPressed()
//                    return
//                }
//                val value = dataSnapshot.getValue(String::class.java)!!
//                if (Objects.requireNonNull(value).isNotEmpty() && started_game) {
//                    secondPlayerJoined = true
////                    player_2_field.setFieldMode(CurrentFieldMode.OPPONENT)
//                    player_2_field_db!!.removeEventListener(this)
//                }
//            }
//            override fun onCancelled(databaseError: DatabaseError) {}
//        })