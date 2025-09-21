package com.example.memestreamproto.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.memestreamproto.auth.LoginActivity
import com.example.memestreamproto.databinding.FragmentProfilePageBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class ProfilePageFragment : Fragment() {

    //Followed viewbinding documentation for fragments for recommended approach
    // Site: https://developer.android.com/topic/libraries/view-binding#fragments
    private var _binding: FragmentProfilePageBinding? = null
    private val binding get() = _binding!!

    private val auth: FirebaseAuth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfilePageBinding.inflate(inflater, container, false)

        binding.mbLogout.setOnClickListener{
            logout()
        }

//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_profile_page, container, false)

        return binding.root
    }

//    private fun logout() {
//        clearCredentialState()
//    }

    private fun logout() {
        // Firebase sign out
        auth.signOut()
        lifecycleScope.launch {
            try {

                val credentialManager = CredentialManager.create(requireContext())
                val request = ClearCredentialStateRequest()
                credentialManager.clearCredentialState(request)

                //Log out --> then go back from whence we came
                 startActivity(Intent(requireContext(), LoginActivity::class.java))
                 requireActivity().finish()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //Part of viewbinding
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}