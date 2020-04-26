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
            val signInIntent: Intent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, 101)
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
            .load(R.drawable.building)
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
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }
    ////
//
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            when (requestCode) {
                101 -> try {
                    val task =
                        GoogleSignIn.getSignedInAccountFromIntent(data)
                    val account =
                        task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account)
                } catch (e: ApiException) { // The ApiException status code indicates the detailed failure reason.
                    Log.w("LoginActivity", "signInResult:failed code=" + e.statusCode)
                }
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = mAuth.currentUser
                    val company =Company(acct.id, user?.displayName,user?.email, user?.photoUrl.toString())
                    Log.d(TAG, "firebaseAuthWithGooglee:" + user?.uid)
                    Hawk.put(Cons.MyProfile, company)
                    onLoggedIn()
                } else {
                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
