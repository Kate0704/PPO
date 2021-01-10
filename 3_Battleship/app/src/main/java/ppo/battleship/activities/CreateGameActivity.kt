package ppo.battleship.activities

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.gson.Gson
import ppo.battleship.R
import ppo.battleship.adapters.GridAdapter
import ppo.battleship.databinding.ActivityCreateGameBinding
import ppo.battleship.models.Cell
import ppo.battleship.models.FieldReviewer
import ppo.battleship.models.GameInfo
import ppo.battleship.models.Type
import java.util.*


class CreateGameActivity : AppCompatActivity(){
    private val binding by lazy { ActivityCreateGameBinding.inflate(layoutInflater) }
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var cellList: MutableList<Cell> = MutableList(100) { Cell() }
    private lateinit var currentUser: FirebaseUser
    private var database: FirebaseDatabase? = null
    private lateinit var game: DatabaseReference
    private var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser!!
        val joinGame = intent.getBooleanExtra("joinGame", false)
        if (joinGame) binding.idEditText.visibility = View.VISIBLE
        else binding.idEditText.visibility = View.GONE
        binding.progressBarPlay.visibility = View.GONE

        FieldReviewer(cellList).initializeFieldImages(resources, packageName)
        val adapter = GridAdapter(resources, cellList) {cell, pos -> cellClicked(cell, pos)}
        binding.field.layoutManager = GridLayoutManager(this, 10)
        binding.field.adapter = adapter

        binding.btnPlayCreateGameFragment.setOnClickListener {
            if (!FieldReviewer(cellList).fieldOk())
                showError(getString(R.string.incorrect_ship_placement_message))
            else {
                arrangeId(joinGame)
            }
        }
        binding.floatingActionButtonInfo.setOnClickListener{ showInfoDialog() }
        setContentView(binding.root)
    }

    private fun cellClicked(cell: Cell, pos: Int): Boolean {
        if (cell.getCellType() == Type.EMPTY) cell.setCellType(Type.SHIP)
        else cell.setCellType(Type.EMPTY)
        return true
    }

    private fun showInfoDialog() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setMessage(getString(R.string.create_game_info))
        alertDialog.setPositiveButton("OK") { _, _ ->
            Toast.makeText(applicationContext, "U're a curious one. I like u!", Toast.LENGTH_LONG).show()
        }.show()
    }

    private fun arrangeId(joinGame: Boolean) {
        if(joinGame) {
            id = binding.idEditText.text.toString()
            writeInitData(joinGame)
        }
        else {
            id = UUID.randomUUID().toString().replace("-", "")
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setMessage("Your game id:\n$id")
            alertDialog.setPositiveButton("COPY!") { _, _ ->
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("text", id)
                Objects.requireNonNull(clipboard).setPrimaryClip(clip)
                Toast.makeText(applicationContext, "Id copied to Clipboard",Toast.LENGTH_LONG).show()

                val alertDialog2 = AlertDialog.Builder(this)
                alertDialog2.setMessage(getString(R.string.play_now_ad_message))
                alertDialog2.setPositiveButton("PLAY NOW!") { _, _ ->
                    binding.progressBarPlay.visibility = View.VISIBLE
                    writeInitData(joinGame)
                }.show()
            }.show()
        }
    }

    private fun writeInitData(join: Boolean) {
        binding.progressBarPlay.visibility = View.VISIBLE
        database = FirebaseDatabase.getInstance()
        game = database!!.getReference("games").child(id)
        val checkListener: ValueEventListener
        checkListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val gson = Gson()
                val gsonCellList = gson.toJson(cellList)
                val gsonCellListEmpty = gson.toJson(MutableList(100) { Cell() })
                val playerName = currentUser.displayName
                game.removeEventListener(this)
                if (!dataSnapshot.exists() && !join) {
                    game.setValue(GameInfo(playerName, "", gsonCellList, gsonCellListEmpty))
                    startGame(join)
                }
                else if (dataSnapshot.exists() && join){
                    game.child("client").setValue(playerName)
                    game.child("client_field").setValue(gsonCellList)
                    startGame(join)
                }
                else showError(getString(R.string.incorrect_id_message))
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        game.addValueEventListener(checkListener)
    }

    private fun startGame(start: Boolean) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("id", id)
        intent.putExtra("start", !start)
        this.startActivity(intent)
        finish()
    }

    private fun showError(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        val toastContainer = toast.view as LinearLayout?
        val girlImageView = ImageView(this)
        girlImageView.setImageResource(R.drawable.girl_sad)
        toastContainer!!.addView(girlImageView, 0)
        toast.show()
    }

    override fun onBackPressed() {
        if (database != null) database!!.getReference("games").child(id).removeValue()
        super.onBackPressed()
    }
}