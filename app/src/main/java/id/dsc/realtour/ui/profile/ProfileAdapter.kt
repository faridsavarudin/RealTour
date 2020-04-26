package id.dsc.realtour.ui.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.vr.sdk.widgets.pano.VrPanoramaView
import id.dsc.realtour.R
import id.dsc.realtour.data.model.Feed
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.item_profile.view.*

class ProfileAdapter(private val context: Context?, private val listFeed: MutableList<Feed>):
RecyclerView.Adapter<ProfileAdapter.MyViewHolder>(){

    private var panoOptions: VrPanoramaView.Options? = null
    lateinit var glide: RequestManager

    interface OnItemClickListener {
        fun onItemClickListener(feed: Feed)
    }
    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: Feed) {
            view.apply {
                if (data.containerType=="image"){
                    iv_image.visibility = View.VISIBLE
                    vr_panorama.visibility = View.GONE
                    rel_ar.visibility = View.GONE
                    Glide.with(context).asBitmap().load(data.mediaValue)
                        .into(object :CustomTarget<Bitmap>(){
                            override fun onLoadCleared(placeholder: Drawable?) {

                            }

                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                iv_image.setImageBitmap(resource)
                            }

                        })
                }
                if (data.containerType=="ar-object"){
                    iv_image.visibility = View.GONE
                    vr_panorama.visibility = View.GONE
                    rel_ar.visibility = View.VISIBLE
                    Glide.with(context).asBitmap().load(data.mediaValue)
                        .into(object :CustomTarget<Bitmap>(){
                            override fun onLoadCleared(placeholder: Drawable?) {

                            }

                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                iv_ar.setImageBitmap(resource)
                            }

                        })
                }
                if (data.containerType=="vr-image"){
                    iv_image.visibility = View.GONE
                    vr_panorama.visibility = View.VISIBLE
                    rel_ar.visibility = View.GONE

                    //load vr
                    vr_panorama.setInfoButtonEnabled(false)
                    vr_panorama.isClickable = true
                    vr_panorama.setTouchTrackingEnabled(true)
                    vr_panorama.setStereoModeButtonEnabled(false)
                    vr_panorama.setFullscreenButtonEnabled(false)
                    panoOptions = VrPanoramaView.Options()
                    panoOptions!!.inputType = VrPanoramaView.Options.TYPE_MONO
                    Log.e("halooo",data.mediaValue)
                    Glide.with(context).asBitmap()
                        .load(data.mediaValue).into(object : CustomTarget<Bitmap>(){
                            override fun onLoadCleared(placeholder: Drawable?) {

                            }

                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?) {
                                vr_panorama.loadImageFromBitmap(resource, panoOptions)
                            }

                        })
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val typeFollowing = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_profile, parent, false)
        return MyViewHolder(typeFollowing)
    }

    override fun getItemCount(): Int = listFeed.size


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val feed = listFeed[position]
        glide = Glide.with(context!!)
        holder.bind(feed)
    }
}