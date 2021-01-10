package ppo.battleship.activities

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import ppo.battleship.databinding.ActivityProfileBinding
import ppo.battleship.models.UserInfo
import java.io.File
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private val binding by lazy { ActivityProfileBinding.inflate(layoutInflater) }
    private lateinit var dbUserRef: DatabaseReference
    private val storageRef = FirebaseStorage.getInstance().reference
    private var filePath: Uri? = null
    private val PICK_IMAGE_REQUEST = 1

    private val dbRef = FirebaseDatabase.getInstance().getReference("users")
    private lateinit var userInfo : UserInfo
    private lateinit var mAuth: FirebaseAuth

    var name : MutableLiveData<String> = MutableLiveData("")
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        dbUserRef = FirebaseDatabase.getInstance().getReference("users/${mAuth.currentUser!!.uid}")

        name.observe(this, Observer<String>{
            binding.userNameTextView.text = it
        })
        binding.userImagePreview.setOnClickListener {
            imagePick()
        }
        userInfo = UserInfo()
        binding.userUseGravatarSwitch.setOnCheckedChangeListener{_, isChecked ->
            if(isChecked) downloadFromGravatar()
            else downloadFromFirebase()
            setUseGravatar(isChecked)
        }
        binding.userUseGravatarSwitch.isChecked = isGravatarSource()

        signIn(mAuth.currentUser!!)
        setContentView(binding.root)
    }

    private fun signIn(user: FirebaseUser){
        val checkIfExists = dbRef.orderByKey().equalTo(user.uid)
        checkIfExists.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                if(!snapshot.exists()){
                    userInfo = UserInfo(user.displayName!!, user.email!!, user.uid)
                    dbRef.child(user.uid).setValue(userInfo)
                }
                else{
                    val map = (snapshot.value as HashMap<*, *>)[user.uid] as HashMap<*, *>
                    userInfo = UserInfo()
                    userInfo.name = map["name"].toString()
                    userInfo.email = map["email"].toString()
                    userInfo.avatar = map["avatar"].toString()
                    userInfo.useGravatar = map["useGravatar"].toString().toBoolean()
                    userInfo.uid = map["uid"].toString()
                    if(map["games"] != null)
                        userInfo.games = map["games"] as MutableList<Boolean>
                }
                name.value = userInfo.name
            }

        })
    }

    private fun imagePick() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    private fun downloadFromGravatar(){
        val hash = md5(mAuth.currentUser?.email!!)
        val gravatarUrl = "https://s.gravatar.com/avatar/$hash?s=80"
        Picasso.with(this).load(gravatarUrl).into(binding.userImagePreview)
    }

    private fun md5(mail: String): String? {
        var result: String? = null
        try {
            val digest = MessageDigest.getInstance("MD5")
            digest.reset()
            digest.update(mail.toByteArray())
            val bigInt = BigInteger(1, digest.digest())
            result = bigInt.toString(16)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return result
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null) return
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, filePath)
                binding.userImagePreview.setImageBitmap(bitmap)
                uploadImage(filePath)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    private fun downloadFromFirebase() {
        val path = getImagePath()
        if(path.isEmpty()) return
        val localFile = File.createTempFile("image", ".jpeg")
        storageRef.child("images/${getUID()}/$path").getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.path)
            binding.userImagePreview.setImageBitmap(bitmap)
        }
    }

    private fun uploadImage(filePath : Uri?){
        if(filePath != null){
            val id = UUID.randomUUID().toString()
            val ref = storageRef.child("images/${userInfo.uid}/$id")
            ref.putFile(filePath).addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> {
                Toast.makeText(application, "Image Uploaded", Toast.LENGTH_SHORT).show()
                userInfo.avatar = id
                dbRef.child("${userInfo.uid}/avatar").setValue(id)
            }).addOnFailureListener(OnFailureListener { e ->
                Toast.makeText(application, "Image Uploading Failed " + e.message, Toast.LENGTH_SHORT).show()
            })
        }else{
            Toast.makeText(application, "Please Select an Image", Toast.LENGTH_SHORT).show()
        }
    }
    private fun isGravatarSource() : Boolean = userInfo.useGravatar
    private fun getImagePath() : String = if(userInfo.avatar.isEmpty()) "" else userInfo.avatar
    private fun getUID() = userInfo.uid
    private fun setUseGravatar(isChecked : Boolean){
        userInfo.useGravatar = isChecked
        dbRef.child("${userInfo.uid}/useGravatar").setValue(isChecked)
    }
}