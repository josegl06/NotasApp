package com.goldtech.notasapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.goldtech.notasapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime

private lateinit var auth: FirebaseAuth
private lateinit var user: FirebaseUser
private lateinit var btSave: Button
private lateinit var etName: EditText
private lateinit var etLastName: EditText
private lateinit var etEmail: EditText
private lateinit var etDni: EditText

private lateinit var spGroup: Spinner
private lateinit var etDescription: EditText
private lateinit var groupList: ArrayList<String>
private lateinit var group: String



class StudentFragment : Fragment(), AdapterView.OnItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = Firebase.auth
        groupList = ArrayList()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = auth.currentUser!!

        btSave = view.findViewById(R.id.bt_save)
        etName = view.findViewById(R.id.et_name)
        etLastName = view.findViewById(R.id.et_last_name)
        etDni = view.findViewById(R.id.et_dni)
        etEmail = view.findViewById(R.id.et_email)


        spGroup = view.findViewById(R.id.sp_groups)

        spGroup.onItemSelectedListener = this


        getGroups()


        btSave.setOnClickListener {
            val name = etName.text.toString()
            val surname = etLastName.text.toString()
            val dni = etDni.text.toString()
            val email = etEmail.text.toString()
            if (name.isNotEmpty() && surname.isNotEmpty() && dni.isNotEmpty() && email.isNotEmpty()) {
                publish(name, surname, dni, email)
            } else {
                Toast.makeText(context, "Debe llenar todos los campos", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun getGroups() {
        val db = Firebase.firestore


        db.collection("groups")
            //.whereEqualTo("sold", true)
            .get()
            .addOnSuccessListener { documents ->

                //  aptList = object

                for (document in documents) {
                    //  map  = document.data ,

                    groupList.add(
                        document.id
                    )


                    Log.w("TAG", "${document.id} => ${document.data}")
                }
                Log.e("TAG   ---------  ", groupList.toString())


                // Create an ArrayAdapter using a simple spinner layout and languages array
                val aa = ArrayAdapter<String>(
                    requireActivity(),
                    android.R.layout.simple_spinner_item,
                    groupList
                )
                // Set layout to use when the list of choices appear
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Set Adapter to Spinner
                spGroup.adapter = aa
            }

            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }

    }


    private fun publish(
        name: String,
        surname: String,
        email: String,
        dni: String

    ) {

        val db = Firebase.firestore

        // Create a new user with a first and last name
        val user = hashMapOf(
            "name" to name,
            "group" to group,
            "surname" to surname,
            "dni" to dni,
            "email" to email,
            "user" to user.uid,
            "date" to " ${LocalDateTime.now().monthValue}/${LocalDateTime.now().dayOfMonth}/${LocalDateTime.now().year}"

        )

        // Add a new document with a generated ID
        db.collection("students")
            //.document(name)
            .add(user)
            .addOnSuccessListener {
                //Log.e("TAG", "DocumentSnapshot added with ID: $uid")
                Toast.makeText(context, "La materia ah sido creada correctamente", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_studentFragment_to_homeFragment)
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
                Toast.makeText(context, "Error al crear materia", Toast.LENGTH_LONG).show()
            }
    }


    override fun onItemSelected(p0: AdapterView<*>, arg1: View, position: Int, id: Long) {
        group = groupList[position]

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

}