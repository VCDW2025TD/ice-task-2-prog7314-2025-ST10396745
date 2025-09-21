package com.example.memestreamproto.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.memestreamproto.apiService.ApiService
import com.example.memestreamproto.apiService.RetrofitObject
import com.example.memestreamproto.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var imageView: ImageView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        imageView = binding.gifImage

        //site:https://www.geeksforgeeks.org/kotlin/retrofit-with-kotlin-coroutine-in-android/

        val api = RetrofitObject.getInstance().create(ApiService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.getTrending()
                val gifUrl = response.data[0].images.original.url
                Log.d("GIF_URL", gifUrl)


                withContext(Dispatchers.Main){
                    //Glide Documentation: https://bumptech.github.io/glide/doc/getting-started.html
                    Glide.with(requireContext())
                        .load(gifUrl)
                        .into(imageView)

                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}