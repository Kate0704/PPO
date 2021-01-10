package ppo.battleship.activities

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ppo.battleship.R
import ppo.battleship.adapters.StatAdapter
import ppo.battleship.databinding.ActivityStatisticsBinding
import java.util.*

class StatisticsActivity : AppCompatActivity() {

    private val binding by lazy { ActivityStatisticsBinding.inflate(layoutInflater) }
    private var currentUser: FirebaseUser? = null
    private lateinit var statistics: ArrayList<StatItem>
    private lateinit var mRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
        val database = FirebaseDatabase.getInstance()
        val statsRef = database.getReference("stats")
        statistics = ArrayList<StatItem>()
        binding.statsList.layoutManager = LinearLayoutManager(this)
        statsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    val value: StatItem? = StatItem(ds.child("server").getValue(String::class.java)!!,
                        ds.child("client").getValue(String::class.java)!!,
                        ds.child("server_score").getValue(Int::class.java)!!,
                        ds.child("client_score").getValue(Int::class.java)!!)
                    if (Objects.requireNonNull<StatItem>(value).getPlayer1()
                            .equals(currentUser?.displayName) ||
                        value?.getPlayer2().equals(currentUser?.displayName))
                        statistics.add(value!!)
                }
                if (statistics.size != 0) {
                    binding.statsList.adapter = StatAdapter(statistics)
                    binding.progressBarStats.visibility = View.GONE
                }
                else {
                    val toast = Toast.makeText(applicationContext,
                        (getString(R.string.no_stat_message)), Toast.LENGTH_LONG)
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    val toastContainer = toast.view as LinearLayout?
                    val girlImageView = ImageView(applicationContext)
                    girlImageView.setImageResource(R.drawable.girl_sad)
                    toastContainer!!.addView(girlImageView, 0)
                    toast.show()
                    onBackPressed()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        setContentView(binding.root)
    }

    class StatItem () {
        private var serverNme: String = ""
        private var clientName: String = ""
        private var serverScore: Int = 0
        private var clientScore: Int = 0
        constructor(serverNme: String, clientName: String, serverScore: Int, clientScore: Int): this() {
            this.serverNme = serverNme
            this.clientName = clientName
            this.serverScore = serverScore
            this.clientScore = clientScore
        }
        fun getPlayer1(): String? = serverNme
        fun getPlayer2(): String? = clientName
        fun getScore1(): Int = serverScore
        fun getScore2(): Int = clientScore
    }
}