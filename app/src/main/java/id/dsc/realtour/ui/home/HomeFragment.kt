package id.dsc.realtour.ui.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.orhanobut.hawk.Hawk
import id.dsc.realtour.R
import id.dsc.realtour.data.model.Company
import id.dsc.realtour.data.model.Feed
import id.dsc.realtour.ui.preview.PreviewActivity
import id.dsc.realtour.utils.Cons
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    var db = FirebaseFirestore.getInstance()
    private var listFeeds: MutableList<Feed> = mutableListOf()
    val storage = FirebaseStorage.getInstance()
    var storageRef = storage.reference
    private lateinit var filePath: Uri
    var adapter : HomeAdapter?=null
    val profile by lazy { Hawk.get<Company>(Cons.MyProfile) }
    var isFABOpen=false
    var uploadImage = true
    companion object{
        const val DATA ="data"
        const val UPLOAD_AR = "upload_image"
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (profile.name.equals("guest"))
            fab.visibility=View.GONE
        initView()
        initFireStore()
    }

    private fun initView() {
        fab.setOnClickListener {
            if(!isFABOpen){
                showFABMenu()
            }else{
                closeFABMenu()
            }
        }
        tv_camera_360.text = "Upload 360" + 0x00B0.toChar().plus(" Virtual Reality")
        tv_upload.text = getString(R.string.upload_photo)
        lin_fab2.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .start()
            uploadImage = true
        }
        lin_fab1.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop(2F, 1F)
                .start()
            uploadImage = false
        }
    }

    private fun initFireStore() {
        db.collection("feed")
            .get().addOnCompleteListener {
                for (document in it.result){
                    val feeds = document.toObject(Feed::class.java)
                    listFeeds.add(feeds)
                }
                adapter = HomeAdapter(context, listFeeds)
                recycler?.also {
                    it.layoutManager= LinearLayoutManager(context)
                    it.setHasFixedSize(true)
                    it.adapter = adapter
                }
                progress?.visibility=View.GONE

                for (datas in listFeeds){
                    for (da in datas?.parentContent){
                    }

                }
            }
    }


    private fun showFABMenu() {
        isFABOpen = true
        lin_fab1.animate().translationY(-resources.getDimension(R.dimen.standard_65))
        lin_fab1.visibility=View.VISIBLE
        lin_fab2.visibility=View.VISIBLE
        lin_fab2.animate().translationY(-resources.getDimension(R.dimen.standard_115))
    }

    private fun closeFABMenu() {
        isFABOpen = false
        lin_fab1.visibility=View.GONE
        lin_fab2.visibility=View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            filePath = data?.data!!
            val intent = Intent(context, PreviewActivity::class.java)
            intent.putExtra(DATA, filePath.toString())
            intent.putExtra(UPLOAD_AR, uploadImage)
            startActivity(intent)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        }
    }


}
