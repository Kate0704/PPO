package ppo.battleship.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import ppo.battleship.R
import ppo.battleship.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity(), View.OnClickListener {

    private val binding by lazy { ActivityMenuBinding.inflate(layoutInflater) }
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.menuUsername.text = currentUser?.displayName
        binding.menuProfile.setOnClickListener(this)
        binding.menuStartGame.setOnClickListener(this)
        binding.menuJoinGame.setOnClickListener(this)
        binding.menuStats.setOnClickListener(this)
        binding.menuLogOut.setOnClickListener(this)
    }

    private fun logout() {
        mAuth.signOut()
        googleSignInClient.signOut().addOnCompleteListener {
            val intent = Intent(this, LoginActivity::class.java)
            this.startActivity(intent)
            finish()
        }
    }

    private fun goToStats() {
        val intent = Intent(this, StatisticsActivity::class.java)
        this.startActivity(intent)
    }

    private fun joinGame() {
        val intent = Intent(this, CreateGameActivity::class.java)
        intent.putExtra("joinGame", true)
        this.startActivity(intent)
    }

    private fun createGame() {
        val intent = Intent(this, CreateGameActivity::class.java)
        intent.putExtra("joinGame", false)
        this.startActivity(intent)
    }

    private fun goToProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        this.startActivity(intent)
    }

    override fun onClick(v: View?) {
        when(v){
            binding.menuProfile -> goToProfile()
            binding.menuStartGame -> createGame()
            binding.menuJoinGame -> joinGame()
            binding.menuStats -> goToStats()
            binding.menuLogOut -> logout()
        }
    }
}