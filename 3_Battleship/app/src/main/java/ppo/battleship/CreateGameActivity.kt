package ppo.battleship

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
import ppo.battleship.databinding.ActivityCreateGameBinding
import java.util.*


class CreateGameActivity : AppCompatActivity(){

    private val binding by lazy { ActivityCreateGameBinding.inflate(layoutInflater) }
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var cellList: MutableList<Cell> = MutableList(100) {Cell()}
    private lateinit var currentUser: FirebaseUser
    private var database: FirebaseDatabase? = null
    private lateinit var game: DatabaseReference
    private var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser!!
        val joinGame = intent.getBooleanExtra("joinGame", false)
        binding.progressBarPlay.visibility = View.GONE

        initializeFieldImages()
        val adapter = GridAdapter (resources, cellList) { cell -> cellClicked(cell) }
        binding.field.layoutManager = GridLayoutManager(this, 10)
        binding.field.adapter = adapter
        binding.floatingActionButtonInfo.setOnClickListener{ showInfoDialog() }

        binding.btnPlayCreateGameFragment.setOnClickListener {
            if (true)
                showError()
            else {
                if (joinGame)
                    enterGameId()
                else
                    getGameId()
            }
        }

        setContentView(binding.root)
    }

    private fun showInfoDialog() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setMessage(getString(R.string.create_game_info))
        alertDialog.setPositiveButton("OK") { _, _ ->
            Toast.makeText(applicationContext, "U're a curious one. I like u!", Toast.LENGTH_LONG).show()
        }
        alertDialog.show()
    }

    private fun initializeFieldImages() {
        for (i in 1..100) {
            var mDrawableName = "image_part_" // файл в папке drawable
            var pos = i.toString()
            if (i > 9 && i != 100)
                pos = "0$pos"
            mDrawableName += pos
            val resID: Int = resources.getIdentifier(mDrawableName, "drawable", packageName)
            cellList[i - 1].imgRes = resID
        }
    }

    private fun cellClicked(cell: Cell) {
    }

    private fun getGameId() {
        id = UUID.randomUUID().toString().replace("-", "")
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setMessage("Your game id:\n$id")
        alertDialog.setPositiveButton("COPY!") { _, _ ->
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("simple text", id)
            Objects.requireNonNull(clipboard).setPrimaryClip(clip)
            Toast.makeText(applicationContext, "Id copied to Clipboard",
                    Toast.LENGTH_LONG).show()

            val alertDialog2 = AlertDialog.Builder(this)
            alertDialog2.setMessage("Be sure your friend is ready to play with you! Ask him, please")
            alertDialog2.setPositiveButton("PLAY NOW!") { _, _ ->
                binding.progressBarPlay.visibility = View.VISIBLE
                connect(true)
            }
            alertDialog2.show()
        }
        alertDialog.show()
    }

    private fun enterGameId() {
        binding.progressBarPlay.visibility = View.VISIBLE
        id = binding.idEditText.text.toString()
        connect(false)
    }

    private fun connect(start: Boolean) {
        database = FirebaseDatabase.getInstance()
        game = database!!.getReference("games").child(id)
        val checkListener: ValueEventListener
        checkListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    game.removeEventListener(this)
                    val gson = Gson()
                    val gsonCellList = gson.toJson(cellList)
                    val gsonCellListEmpty = gson.toJson(MutableList(100) { Cell() })
                    val playerName = currentUser.displayName
                    if (start) {
                        game.setValue(GameInfo(playerName, "", gsonCellList, gsonCellListEmpty))
                        goToGame(true)
                    }
                    else{
                        game.child("player_2").setValue(playerName)
                        game.child("player_2_field").setValue(gsonCellList)
                        goToGame(false)
                    }
                }
                else showIdIncorrectMessage()
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        showIdIncorrectMessage()
        game.addValueEventListener(checkListener)
    }

    private fun goToGame(start: Boolean) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("id", id)
        intent.putExtra("start", start)
        this.startActivity(intent)
        finish()
    }

    private fun showError() {
        val toast = Toast.makeText(this,
                "Incorrect placement for ships... Be careful, honey!",
                Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        val toastContainer = toast.view as LinearLayout?
        val catImageView = ImageView(this)
        catImageView.minimumWidth = 500
        catImageView.maxWidth = 700
        catImageView.setImageResource(R.drawable.girl_sad)
        toastContainer!!.addView(catImageView, 0)
        toast.show()
    }

    private fun showIdIncorrectMessage() {
        val toast = Toast.makeText(this,
                "Unfortunately, id is incorrect... Be careful, honey!",
                Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        val toastContainer = toast.view as LinearLayout?
        val catImageView = ImageView(this)
        catImageView.minimumWidth = 500
        catImageView.maxWidth = 700
        catImageView.setBackgroundResource(R.drawable.girl_sad)
        toastContainer!!.addView(catImageView, 0)
        toast.show()
    }

    override fun onBackPressed() {
        if (database != null) database!!.getReference("games").child(id).removeValue()
        super.onBackPressed()
    }
}