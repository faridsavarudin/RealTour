package id.dsc.realtour.ui.profile

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.orhanobut.hawk.Hawk
import id.dsc.realtour.R
import id.dsc.realtour.data.model.Company
import id.dsc.realtour.data.model.Feed
import id.dsc.realtour.utils.Cons
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    val profile by lazy { Hawk.get<Company>(Cons.MyProfile) }
    var db = FirebaseFirestore.getInstance()
    private var listFeeds: MutableList<Feed> = mutableListOf()
    var adapter: ProfileAdapter?=null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Glide.with(this).load(profile.photo).into(iv_profile)
        tv_username.text = profile.name
        tv_email.text = profile.email

        initFireStore()
    }

    private fun initFireStore() {
        db.collection("feed")
            .whereEqualTo("companyID", profile.CompanyID)
            .get().addOnCompleteListener {
                for (document in it.result){
                    val feeds = document.toObject(Feed::class.java)
                    listFeeds.add(feeds)
                }
                adapter = ProfileAdapter(context, listFeeds)
                recycler?.also {
                    progress.visibility=View.GONE
                    it.layoutManager= GridLayoutManager(context, 2)
                    it.isNestedScrollingEnabled= false
                    it.setHasFixedSize(true)
                    it.adapter = adapter
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}
