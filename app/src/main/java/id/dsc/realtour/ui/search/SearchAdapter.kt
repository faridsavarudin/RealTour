package id.dsc.realtour.ui.search

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import id.dsc.realtour.R
import id.dsc.realtour.data.model.Feed
import id.dsc.realtour.ui.ar.ARActivity
import id.dsc.realtour.ui.vr.VRFullScreenActivity
import kotlinx.android.synthetic.main.item_search.view.*

class SearchAdapter(private val context: Context?, private val listFeed: MutableList<Feed>):
RecyclerView.Adapter<SearchAdapter.MyViewHolder>(){

    lateinit var glide: RequestManager

    interface OnItemClickListener {
        fun onItemClickListener(feed: Feed)
    }
    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: Feed) {
            view.apply {
                Glide.with(context).load(data.mediaValue).into(iv_profile)
                tv_title.text = data.title

                if (data.containerType.equals("ar-object")){
                    tv_try_ar.visibility=View.VISIBLE
                }else {
                    tv_try_ar.visibility=View.GONE
                }

                itemView.setOnClickListener {
                    val intent = Intent(context, VRFullScreenActivity::class.java)
                    intent.putExtra("data", data)
                    context.startActivity(intent)
                }
                tv_try_ar.setOnClickListener {
                    context.startActivity(
                        Intent(context, ARActivity::class.java)
                        .also { intent: Intent -> intent.putExtra("feed", data) })
                }
            }
        }

    }

    fun clear(){
        listFeed?.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val typeFollowing = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search, parent, false)
        return MyViewHolder(typeFollowing)
    }

    override fun getItemCount(): Int = listFeed.size


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val feed = listFeed[position]
        glide = Glide.with(context!!)
        holder.bind(feed)
    }
}