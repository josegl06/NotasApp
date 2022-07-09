package com.goldtech.notasapp.ui


import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.goldtech.notasapp.R
import com.goldtech.notasapp.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime


class SignUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private lateinit var txtHere: TextView
    private lateinit var btnSignUp: Button
    private lateinit var etName: EditText
    private lateinit var etsurname: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPass: EditText
    private lateinit var etPassRepeat: EditText


    private var _binding: FragmentSignUpBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = Firebase.auth

        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtHere = view.findViewById(R.id.txt_here)
        btnSignUp = view.findViewById(R.id.btn_sign_in)
        etName = view.findViewById(R.id.et_name)
        etsurname = view.findViewById(R.id.et_last_name)
        etEmail = view.findViewById(R.id.et_email)
        etPass = view.findViewById(R.id.et_pass)
        etPassRepeat = view.findViewById(R.id.et_pass_repeat)


        txtHere.setOnClickListener {

            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        btnSignUp.setOnClickListener {

            val mail = etEmail.text.toString()
            val pass = etPass.text.toString()


            if (mail.isNotEmpty() && mail.isNotBlank() && pass.isNotBlank() && pass.isNotEmpty()) {
                if (pass == etPassRepeat.text.toString()) {

                    if (etName.text.toString().isNotEmpty() && etsurname.text.isNotEmpty()) {
                        signUp(mail, pass)
                    } else {
                        Toast.makeText(context, "Debe llenar todos los campos", Toast.LENGTH_LONG)
                            .show()
                    }

                } else {
                    Toast.makeText(context, "Las claves no son iguales", Toast.LENGTH_LONG).show()
                }

            } else {
                Toast.makeText(context, "Debe llenar correo y clave", Toast.LENGTH_LONG).show()
            }

        }


    }


    private fun signUp(email: String, password: String) {


        activity?.let {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                it
            ) { task ->
                val activity: Activity? = activity
                activity?.runOnUiThread {
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        userSave(email, user!!.uid)

                    } else {
                        //    Toast.makeText(context, "ERROR---------", Toast.LENGTH_LONG).show()
                        // Timber.e("ERROR: ----------  ")
                        Log.e("Error", "createUserWithEmail:failure ${task.exception}")
                        Log.e("Error", "  ${task.exception!!.message}")

                        Toast.makeText(
                            context,
                            " Error ${task.exception!!.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

    }


    private fun userSave(email: String, uid: String) {
        val db = Firebase.firestore

        // Create a new user with a first and last name
        val user = hashMapOf(
            "first" to etName.text.toString(),
            "last" to etsurname.text.toString(),
            "email" to email,
            "active" to true,
            "date" to LocalDateTime.now()
        )

        // Add a new document with a generated ID
        db.collection("users")
            .document(uid)
            .set(user)
            .addOnSuccessListener {
                Log.e("TAG", "DocumentSnapshot added with ID: $uid")
                findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}