package com.example.memestreamproto.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memestreamproto.R
import com.example.memestreamproto.apiService.ApiService
import com.example.memestreamproto.apiService.RetrofitObject
import com.example.memestreamproto.data.Gif
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A fragment representing a list of Items.
 */
class FeedFragment : Fragment() {

//    private lateinit var recyclerView: RecyclerView
//    private lateinit var adapter: GifRecyclerViewAdapter

    private var columnCount = 2


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feed_list, container, false)

        // Find the RecyclerView inside the layout
        val recyclerView = view.findViewById<RecyclerView>(R.id.list)


//        if (view is RecyclerView) {
//            with(view) {
//                layoutManager = when {
//                    columnCount <= 1 -> LinearLayoutManager(context)
//                    else -> GridLayoutManager(context, columnCount)
//                }
////                adapter = MyItemRecyclerViewAdapter(PlaceholderContent.ITEMS)
////                  ada
        recyclerView.layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount)
        }
                // Launch coroutine to fetch data
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val api = RetrofitObject.getInstance().create(ApiService::class.java)
                        val response = api.getTrending()
                        val gifs = response.data.map { Gif(it.title, it.id, it.images) }

                        withContext(Dispatchers.Main) {
                            recyclerView.adapter = GifRecyclerViewAdapter(gifs)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                return view
            }
        }






