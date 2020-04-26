package id.dsc.realtour.ui.home

import android.content.Context
import android.content.Intent
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
import id.dsc.realtour.ui.vr.VRFullScreenActivity
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.item_feed.view.*

class HomeAdapter(private val context: Context?, private val listFeed: MutableList<Feed>):
RecyclerView.Adapter<HomeAdapter.MyViewHolder>(){

    lateinit var glide: RequestManager

    interface OnItemClickListener {
        fun onItemClickListener(feed: Feed)
    }
    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: Feed) {
            view.apply {
                Glide.with(context).load(data.companyLogo).into(iv_profile)
                tv_username.text = data.companyName
                tv_caption.text = data.caption

                //price
                if (data.price!=null){
                    tv_price.visibility=View.VISIBLE
                    tv_price.text = data.price
                }else {
                    tv_price.visibility=View.GONE
                }

                if (data.containerType=="image"){
                    iv_image.visibility = View.VISIBLE
                    rel_vr.visibility = View.GONE
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
                    rel_vr.visibility = View.GONE
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
                    val loadVRImage = VRImageLoader(context)
                    loadVRImage.loadVR(data.parentContent, vr_panorama,
                        rvVR = rv_vr_image)
                    iv_image.visibility = View.GONE
                    rel_vr.visibility = View.VISIBLE
                    rel_ar.visibility = View.GONE
                    iv_fullscreen.setOnClickListener {
                        val intent = Intent(context, VRFullScreenActivity::class.java)
                        intent.putExtra("data", data)
                        context.startActivity(intent)
                    }
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val typeFollowing = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_feed, parent, false)
        return MyViewHolder(typeFollowing)
    }

    override fun getItemCount(): Int = listFeed.size


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val feed = listFeed[position]
        glide = Glide.with(context!!)
        holder.bind(feed)
    }
}