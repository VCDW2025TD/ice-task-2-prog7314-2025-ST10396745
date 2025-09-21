package com.example.memestreamproto.ui

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.memestreamproto.apiService.MemeRetrofitObject
import com.example.memestreamproto.data.MemePost
import com.example.memestreamproto.databinding.FragmentCreateMemeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import ja.burhanrashid52.photoeditor.PhotoEditor
import kotlinx.coroutines.launch

class CreateMemeFragment : Fragment() {

    private var _binding: FragmentCreateMemeBinding? = null
//    private lateinit var imageView: ImageView
// This property is only valid between onCreateView and
// onDestroyView.

        //added
//private lateinit var fusedLocation: FusedLocationProviderClient
    private var lastLat: Double? = null
    private var lastLng: Double? = null

    private lateinit var mMap: GoogleMap

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    companion object {
        private const val PERMISSION_REQUEST_LOCATION = 1
    }

    private lateinit var photoEditor: PhotoEditor
private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Init location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateMemeBinding.inflate(inflater, container, false)

//        val editorView = binding.photoEditorView

        // Load bitmap first
//        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.baseline_image_24)
//        editorView.source.setImageBitmap(bitmap)
//
//
//        editorView.post{
//            // 1. Load image into PhotoEditorView
//            editorView.source.setImageResource(R.drawable.baseline_image_24)
//        }
//


//        // 2. Wait until view is ready
//        editorView.post {
//            // Set bitmap once
//            editorView.source.setImageBitmap(bitmap)
//
//            // 3. Build PhotoEditor AFTER bitmap is set
//            photoEditor = PhotoEditor.Builder(requireContext(), editorView)
//                .setPinchTextScalable(true)
//                .build()
//        }


//
//        photoEditor = PhotoEditor.Builder(requireContext(), editorView)
//            .setPinchTextScalable(true)
//            .build() // build photo editor sdk
////



//        lifecycleScope.launch {
//            val file = File(requireContext().cacheDir, "final_${System.currentTimeMillis()}.jpg")
//            val result = if (ActivityCompat.checkSelfPermission(
//                    requireContext(),
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                Toast.makeText(requireContext(), "no", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(requireContext(), "no", Toast.LENGTH_SHORT).show()
//
//            }
//
//            if (result is SaveFileResult.Success) {
//                // Option 1: show inside the editor itself
//                binding.photoEditorView.source.setImageURI(Uri.fromFile(file))
//
//                // Option 2: show in a normal ImageView
//                binding.imageViewPost.setImageURI(Uri.fromFile(file))
//            }
//        }


        // 3. Use a button (or ImageView) to finalize edits
        binding.imageViewPost.setOnClickListener {
//            Log.d(TAG, "Listener is activated")
//            editorView.post {
//                saveImage()
//            }
            openGallery()

        }

        binding.buttonAddLocation.setOnClickListener {
            checkLocationPermissionAndFetchLocation()
        }

        binding.buttonUploadMeme.setOnClickListener {
            val caption  = binding.editTextTextMultiLine.text.toString()

            val memePost = MemePost(
                userId = "ryeye",
                imageUrl = "test for now",
                caption = caption,
                lat = lastLat,
                lng = lastLng,
                timestamp = System.currentTimeMillis().toString(),
            )
            sendMeme(memePost)
        }

        return binding.root
    }

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
            binding.photoEditorView.source.setImageBitmap(bitmap)

            // Initialize PhotoEditor AFTER bitmap is loaded
            photoEditor = PhotoEditor.Builder(requireContext(), binding.photoEditorView)
                .setPinchTextScalable(true)
                .build()
        }
    }

    // Call this when user clicks "Choose Image"
    fun openGallery() {
        pickImageLauncher.launch("image/*")
    }


    private fun saveImage() {
        lifecycleScope.launch {
            val editedBitmap = photoEditor.saveAsBitmap()
            if (editedBitmap != null) {
                // Save to gallery, upload, or display
            }
        }
    }

    private fun checkLocationPermissionAndFetchLocation() {
        val fineLocationGranted = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineLocationGranted) {
            getCurrentLocation()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_LOCATION
            )
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "Permission not granted.")
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    Log.d(TAG, "Location: $location")
                    //added for loc when memeing
                    lastLat = location.latitude
                    lastLng = location.longitude

                    Log.d("LOCATION", "Tthese valiues be LONG: ${lastLng},LAST: ${lastLat}")
                    val currentLatLng = LatLng(location.latitude, location.longitude)
//                    mMap.addMarker(MarkerOptions().position(currentLatLng).title("You are here"))
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                } else {
                    Log.d(TAG, "Location is null, showing fallback location")
                    // Fallback location
                    val fallbackLocation = LatLng(40.7128, -74.0060)  // Example: New York
//                    mMap.addMarker(MarkerOptions().position(fallbackLocation).title("Fallback location"))
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fallbackLocation, 15f))
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Error fetching location")
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_LOCATION &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        } else {
            Log.d(TAG, "Location permission denied.")
        }
    }


    private fun sendMeme(memePost: MemePost){
        lifecycleScope.launch {
            try{
                val response = MemeRetrofitObject.instance.postMeme(memePost)

                if (response.isSuccessful){
                    Toast.makeText(requireContext(), "IS SUCCESSSSS", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(), "WHYYYYYYYYY!", Toast.LENGTH_SHORT).show()

                }
            }catch (e: Exception){
                Toast.makeText(requireContext(), "This is why I guess: ${e.message}", Toast.LENGTH_SHORT).show()

            }
        }
    }

//    private fun saveImage(){
//        Log.d(TAG, "Saveimage is activated")
//
//        lifecycleScope.launch {
//            val bitmap = photoEditor.saveAsBitmap()
//
////            // Show the edited image in another ImageView
//            binding.imageViewPost.setImageBitmap(bitmap)
//
//
//            // Or replace the editor's base image with the result
//            binding.photoEditorView.source.setImageBitmap(bitmap)
//        }
//    }

//    private fun saveImage() {
//        lifecycleScope.launch {
//            val resultBitmap = photoEditor.saveAsBitmap()
//            if (resultBitmap != null) {
//                binding.imageViewPost.setImageBitmap(resultBitmap)
//                binding.photoEditorView.source.setImageBitmap(resultBitmap)
//            } else {
//                Log.e(TAG, "PhotoEditor returned null bitmap!")
//            }
//        }
//    }



}
