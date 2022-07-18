package com.goldtech.notasapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.goldtech.notasapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private lateinit var ivCreateGroup:ImageView
    private lateinit var ivCreateSubjects:ImageView
    private lateinit var ivStudent:ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        ivCreateGroup = view.findViewById(R.id.iv_create_group)
        ivCreateSubjects = view.findViewById(R.id.iv_subjects)
        ivStudent = view.findViewById(R.id.iv_student)


        // addListItems(ClientsRoute.getClientsList(originalList) as ArrayList<ClientsRoute>)

        ivCreateGroup.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_groupFragment)
        }

        ivCreateSubjects.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_subjectsFragment)
        }

        ivStudent.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_studentFragment)
        }


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }



}