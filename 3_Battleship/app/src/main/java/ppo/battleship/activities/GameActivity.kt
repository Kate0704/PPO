package ppo.battleship.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import ppo.battleship.R
import ppo.battleship.adapters.GridAdapter
import ppo.battleship.databinding.ActivityCreateGameBinding
import ppo.battleship.databinding.ActivityGameBinding
import ppo.battleship.models.Cell
import ppo.battleship.models.FieldReviewer

class GameActivity : AppCompatActivity() {
    private var cellList1: MutableList<Cell> = MutableList(100) { Cell() }
    private var cellList2: MutableList<Cell> = MutableList(100) { Cell() }
    private val binding by lazy { ActivityGameBinding.inflate(layoutInflater) }
    private lateinit var mAuth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.progressBarGAMEWAIT.visibility = View.GONE

        FieldReviewer(cellList1).initializeFieldImages(resources, packageName)
        val adapter1 = GridAdapter(cellList1)
        binding.field1.layoutManager = GridLayoutManager(this, 10)
        binding.field1.adapter = adapter1

        FieldReviewer(cellList2).initializeFieldImages(resources, packageName)
        val adapter2 = GridAdapter(cellList2)
        binding.field2.layoutManager = GridLayoutManager(this, 10)
        binding.field2.adapter = adapter2

        setContentView(binding.root)
    }
}