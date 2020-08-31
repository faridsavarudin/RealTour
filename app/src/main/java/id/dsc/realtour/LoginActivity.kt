package id.dsc.realtour

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.vr.sdk.widgets.pano.VrPanoramaView
import com.orhanobut.hawk.Hawk
import id.dsc.realtour.data.model.Company
import id.dsc.realtour.ui.BaseActivity
import id.dsc.realtour.utils.Cons
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity() {

    lateinit var glide: RequestManager
    private var panoOptions: VrPanoramaView.Options? = null
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    private var TAG="LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()
        glide = Glide.with(this)
        loadImages()
        mAuth = FirebaseAuth.getInstance()
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        btn_signin.setOnClickListener {
            if (et_email.text.toString().isNotEmpty() && et_password.text.toString().isNotEmpty()) {
                mAuth.signInWithEmailAndPassword(
                    et_email.text.toString(),
                    et_password.text.toString()
                )
                    .addOnCompleteListener(
                        this
                    ) { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "signInWithCredential:success")
                            val user = mAuth.currentUser
                            val company = Company(
                                user?.uid,
                                user?.displayName,
                                user?.email,
                                user?.photoUrl.toString()
                            )
                            Log.d(TAG, "firebaseAuthWithGooglee:" + user?.uid)
                            Hawk.put(Cons.MyProfile, company)
                            onLoggedIn()
                        } else {
                            Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }else {
                Toast.makeText(this, "Mohon masukan email atau password anda", Toast.LENGTH_SHORT).show()
            }
        }

        tv_as_guest.setOnClickListener {
            val company = Company(
                "cc",
                "guest",
                "",
                "")
            Hawk.put(Cons.MyProfile, company)
            val intent = Intent(this, Main2Activity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadImages() {
        pano_view.setInfoButtonEnabled(false)
        pano_view.isClickable = true
        pano_view.setTouchTrackingEnabled(true)
        pano_view.setStereoModeButtonEnabled(false)
        pano_view.setFullscreenButtonEnabled(false)
        panoOptions = VrPanoramaView.Options()
        panoOptions!!.inputType = VrPanoramaView.Options.TYPE_MONO
        glide.asBitmap()
            .load(R.drawable.welcome_screen)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadFailed(errorDrawable: Drawable?) {

                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    pano_view?.loadImageFromBitmap(resource, panoOptions)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    override fun onStart() {
        super.onStart()
        Hawk.get<Company>(Cons.MyProfile)?.let {
            onLoggedIn()
        }
    }

    private fun onLoggedIn(){
        Hawk.get<Company>(Cons.MyProfile)?.let {
            if (it.name.equals("guest")){
                val intent = Intent(this, Main2Activity::class.java)
                startActivity(intent)
                finish()
            }else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
