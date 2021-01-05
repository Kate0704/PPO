package ppo.battleship

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import ppo.battleship.databinding.ActivityCreateGameBinding


class CreateGameActivity : AppCompatActivity(){

    private val binding by lazy { ActivityCreateGameBinding.inflate(layoutInflater) }
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}