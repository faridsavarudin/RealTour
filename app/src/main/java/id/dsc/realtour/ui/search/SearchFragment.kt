package id.dsc.realtour.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

import id.dsc.realtour.R
import id.dsc.realtour.data.model.Feed
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    var db = FirebaseFirestore.getInstance()
    private var listFeeds: MutableList<Feed> = mutableListOf()
    val storage = FirebaseStorage.getInstance()
    var storageRef = storage.reference
    var adapter : SearchAdapter?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                initFireStore(p0.toString())
            }

        })
    }


    private fun initFireStore(query : String) {
        progress.visibility=View.VISIBLE
        listFeeds.clear()
        adapter?.clear()
        db.collection("feed").whereGreaterThanOrEqualTo("title",query)
            .get().addOnCompleteListener {
                for (document in it.result){
                    val feeds = document.toObject(Feed::class.java)
                    listFeeds.add(feeds)
                }
                adapter = SearchAdapter(context, listFeeds)
                recycler?.also {
                    it.layoutManager= LinearLayoutManager(context)
                    it.setHasFixedSize(true)
                    it.adapter = adapter
                }

                for (datas in listFeeds){
                    for (da in datas.parentContent){
                    }

                }
            }
        progress.visibility=View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

}
