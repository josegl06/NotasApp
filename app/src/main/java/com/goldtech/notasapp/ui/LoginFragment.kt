package com.goldtech.notasapp.ui

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.goldtech.notasapp.R
import com.goldtech.notasapp.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var tvhere: TextView
    private lateinit var btnSignIn: Button
    private lateinit var etEmail: EditText
    private lateinit var etPass: EditText


    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = Firebase.auth

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvhere = view.findViewById(R.id.txt_here)
        btnSignIn = view.findViewById(R.id.btn_sign_in)

        etEmail = view.findViewById(R.id.et_email)
        etPass = view.findViewById(R.id.et_pass)

//        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }

        btnSignIn.setOnClickListener {

            if (etEmail.text.isNullOrEmpty() || etPass.text.isNullOrEmpty()) {
                Snackbar.make(view, "Favor llenar todos los campos", Snackbar.LENGTH_LONG).show()
            } else {
                login(etEmail.text.toString(),  etPass.text.toString(), view)
            }


        }

        tvhere.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

    }

    private fun login(email: String, password: String, view: View) {


        activity?.let {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                it
            ) { task ->
                val activity: Activity? = activity
                activity?.runOnUiThread {
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Session iniciada correctamente", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    } else {
                       // Toast.makeText(context, "ERROR---------", Toast.LENGTH_LONG).show()
                       // Timber.e("ERROR: ----------  ")
                        Snackbar.make(view, "Usuario o clave incorrectos", Snackbar.LENGTH_LONG).show()

//                        Toast.makeText(
//                            context,
//                            "User ID or Password incorrect!",
//                            Toast.LENGTH_LONG
//                        ).show()
                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}