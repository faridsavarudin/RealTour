package id.dsc.realtour.ui.vr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.google.vr.sdk.widgets.common.VrWidgetView
import com.google.vr.sdk.widgets.pano.VrPanoramaView
import id.dsc.realtour.R
import id.dsc.realtour.data.model.Feed
import id.dsc.realtour.ui.BaseActivity
import id.dsc.realtour.ui.home.VRImageLoader
import kotlinx.android.synthetic.main.activity_v_r_full_screen.*

class VRFullScreenActivity : BaseActivity() {

    val data by lazy { intent.getParcelableExtra<Feed>("data") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_v_r_full_screen)


        val loadVRImage = VRImageLoader(this)
        Log.e("dataaa", data.companyName)
        Log.e("dataaa", data.mediaValue)
        loadVRImage.loadVR(data.parentContent, pano_view,
            rvVR = rv_vr_image)
    }
}
