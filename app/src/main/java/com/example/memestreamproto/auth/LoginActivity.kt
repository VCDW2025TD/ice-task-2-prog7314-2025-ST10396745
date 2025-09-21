package com.example.memestreamproto.auth


import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.example.memestreamproto.MainActivity
import com.example.memestreamproto.databinding.ActivityLoginBinding
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val fbWebClientID = "88234076562-gu1p8smvsvf991qtnon2b3r54g29je11.apps.googleusercontent.com"
    private val auth: FirebaseAuth = Firebase.auth
    private lateinit var  credentialManager: CredentialManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //Initialising cred manager
        credentialManager = CredentialManager.create(this)


        binding = ActivityLoginBinding.inflate(layoutInflater)
        FirebaseApp.initializeApp(applicationContext)


        binding.googleSignInButton.setOnClickListener {
            googleSignIn()
        }

        promptBiometric()
        setContentView(binding.root)
    }

    private fun googleSignIn() {
        val credentialManager = CredentialManager.create(this)
        val signInWithGoogleOption = GetSignInWithGoogleOption.Builder(
            serverClientId = fbWebClientID
        ).build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(this@LoginActivity, request)
                handleSignInWithGoogleOption(result)
            } catch (e: GetCredentialException) {
                Log.e(TAG, "Sign-in failed", e)
                // fallback to old GoogleSignInClient flow or show error
            }
        }
    }

    fun handleSignInWithGoogleOption(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        val credential = result.credential

        when (credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        // Use googleIdTokenCredential and extract id to validate and
                        // authenticate on your server.
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)

                        //addeed
                        Log.d(TAG, "Google ID Token: ${googleIdTokenCredential.idToken}")
                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)

                        Log.e(TAG, "Invalid Google ID token", e)
                        Toast.makeText(this, "Invalid Google ID token", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    // Catch any unrecognized credential type here.
                    Log.e(TAG, "Unexpected type of credential")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    Log.d(TAG, "Firebase user: ${user?.uid} / ${user?.email}")
                    // updateUI(user) if you want
                    val biometricManager = BiometricManager.from(this)
                    when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
                        BiometricManager.BIOMETRIC_SUCCESS -> {
                            Log.d("LoginActivity", "Biometric available and enrolled")
                            Toast.makeText(this, "Biometric available", Toast.LENGTH_SHORT).show()

                            //added --changed from og

                            if (checkBiometricAvailability()){
                                promptBiometric()
                            }
                            // Step 2: Proceed to biometric prompt
                            promptBiometric()
                        }
                        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                            Log.e("LoginActivity", "No biometric hardware")
                            Toast.makeText(this, "No biometric hardware detected", Toast.LENGTH_SHORT).show()
                        }
                        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                            Log.e("LoginActivity", "Biometric hardware unavailable")
                            Toast.makeText(this, "Biometric hardware unavailable", Toast.LENGTH_SHORT).show()
                        }
                        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                            Log.e("LoginActivity", "No biometrics enrolled")
                            Toast.makeText(this, "No biometrics enrolled", Toast.LENGTH_SHORT).show()
                        }
                    }
//                    promptBiometric()
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkBiometricAvailability(): Boolean{
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("LoginActivity", "Biometric available and enrolled")
                Toast.makeText(this, "Biometric available", Toast.LENGTH_SHORT).show()
                return true
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.e("LoginActivity", "No biometric hardware")
                Toast.makeText(this, "No biometric hardware detected", Toast.LENGTH_SHORT).show()
                return false
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.e("LoginActivity", "Biometric hardware unavailable")
                Toast.makeText(this, "Biometric hardware unavailable", Toast.LENGTH_SHORT).show()
                return false
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Log.e("LoginActivity", "No biometrics enrolled")
                Toast.makeText(this, "No biometrics enrolled", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return false
    }

    private fun promptBiometric() {
        Log.d("LoginActivity","Prompt Biometric Running!!")
        //added
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this, executor,

            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    // Success → Allow user into app
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    FirebaseAuth.getInstance().signOut() // Kick them out if fail
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric authentication required")
            .setSubtitle("Confirm your identity")
            .setNegativeButtonText("Cancel")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}