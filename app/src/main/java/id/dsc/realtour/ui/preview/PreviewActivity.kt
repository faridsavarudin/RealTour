package id.dsc.realtour.ui.preview

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.gaharitechnology.Ars.util.UrlUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.vr.sdk.widgets.pano.VrPanoramaView
import com.orhanobut.hawk.Hawk
import id.dsc.realtour.MainActivity
import id.dsc.realtour.R
import id.dsc.realtour.data.model.Company
import id.dsc.realtour.data.model.Feed
import id.dsc.realtour.data.model.ParentContentJava
import id.dsc.realtour.ui.BaseActivity
import id.dsc.realtour.ui.home.HomeFragment
import id.dsc.realtour.utils.Cons
import kotlinx.android.synthetic.main.activity_preview.*
import kotlinx.android.synthetic.main.item_feed.*
import java.util.*

class PreviewActivity : BaseActivity() {

    val data by lazy { intent.getStringExtra(HomeFragment.DATA) }
    val isUploadAR by lazy { intent.getBooleanExtra(HomeFragment.UPLOAD_AR, false) }
    private var panoOptions: VrPanoramaView.Options? = null
    var db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()
    var storageRef = storage.reference
    private lateinit var filePath: Uri
    var find = false
    val profile by lazy { Hawk.get<Company>(Cons.MyProfile) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        filePath = Uri.parse(data)

        if (isUploadAR) {
            et_object_3d.visibility=View.VISIBLE
            //et_title.visibility=View.GONE
            iv_preview.setImageURI(filePath)
        } else {
            iv_preview.visibility = View.GONE
            pano_view.visibility = View.VISIBLE
            pano_view.setInfoButtonEnabled(false)
            pano_view.isClickable = true
            pano_view.setTouchTrackingEnabled(true)
            pano_view.setStereoModeButtonEnabled(false)
            pano_view.setFullscreenButtonEnabled(false)
            panoOptions = VrPanoramaView.Options()
            panoOptions!!.inputType = VrPanoramaView.Options.TYPE_MONO
            Glide.with(this).asBitmap()
                .load(data).into(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        pano_view.loadImageFromBitmap(resource, panoOptions)
                    }

                })
        }

        tv_upload.setOnClickListener {
            tv_upload.setBackgroundColor(R.drawable.bg_border_button_gray)
            progress.visibility = View.VISIBLE
            uploadFile()
        }

        et_object_3d.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                for (link in UrlUtils.alphabet) {
                    if (et_object_3d.text.toString().contains(link)) {
                        find = true
                        return
                    }else{
                        find= false
                    }
                }
            }

        })
    }

    private fun uploadFile() {
        val ref: StorageReference = storageRef
            .child("images/" + UUID.randomUUID().toString())
        val uploadTask = ref.putFile(filePath)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                throw task.exception!!
            }
            ref.downloadUrl
        }.addOnCompleteListener(OnCompleteListener<Uri?> { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                uploadToFireStore(downloadUri.toString())
            }
        })
    }

    private fun uploadToFireStore(mediaValue: String) {
        val listParentContent = mutableListOf<ParentContentJava>()
        if (isUploadAR)
            listParentContent.add(ParentContentJava(et_title.text.toString(),et_object_3d.text.toString()))
        else
        listParentContent.add(ParentContentJava(et_title.text.toString(),mediaValue))

        val containerType = if (isUploadAR) "ar-object" else "vr-image"

        val feed = Feed(et_caption.text.toString(),
            listParentContent,
            profile?.photo, profile?.email, containerType, mediaValue, et_price.text.toString(), profile?.CompanyID, et_title.text.toString())

        db.collection("feed")
            .document(UUID.randomUUID().toString())
            .set(feed)
            .addOnSuccessListener {
                progress.visibility=View.GONE
                startActivity(Intent(this, MainActivity::class.java))
            }.addOnFailureListener {
                Log.e("error", it.message)
            }
    }
}
