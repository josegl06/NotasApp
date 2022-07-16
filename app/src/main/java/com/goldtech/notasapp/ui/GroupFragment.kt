package com.goldtech.notasapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.goldtech.notasapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime


class GroupFragment : Fragment() {


    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var btSave: Button
    private lateinit var etName: EditText
    private lateinit var etDescription: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = auth.currentUser!!

        btSave = view.findViewById(R.id.bt_save)
        etName = view.findViewById(R.id.et_name)
        etDescription = view.findViewById(R.id.et_description)




        btSave.setOnClickListener {
            val name = etName.text.toString()
            val description = etDescription.text.toString()
            if (name.isNotEmpty() && description.isNotEmpty()) {
                publish(name, description)
            } else {
                Toast.makeText(context, "Debe llenar todos los campos", Toast.LENGTH_LONG).show()
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group, container, false)
    }

    private fun publish(
        name: String,
        description: String

    ) {

        val db = Firebase.firestore

        // Create a new user with a first and last name
        val user = hashMapOf(
            //"name" to name,
            "description" to description,
            "user" to user.uid,
            "date" to " ${LocalDateTime.now().monthValue}/${LocalDateTime.now().dayOfMonth}/${LocalDateTime.now().year}"

        )

        // Add a new document with a generated ID
        db.collection("groups")
            .document(name)
            .set(user)
            .addOnSuccessListener {
                //Log.e("TAG", "DocumentSnapshot added with ID: $uid")
                Toast.makeText(context, "Grupo creado correctamente", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_groupFragment_to_homeFragment)
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
                Toast.makeText(context, "Error al crear grupo", Toast.LENGTH_LONG).show()
            }
    }


}