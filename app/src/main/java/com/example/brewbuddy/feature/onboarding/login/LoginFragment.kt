package com.example.brewbuddy.feature.onboarding.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.brewbuddy.R
import com.example.brewbuddy.feature.main.MainActivity
import com.example.brewbuddy.feature.onboarding.pref.PreferenceHelper


class LoginFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editText = view.findViewById<EditText>(R.id.editTextName)
        val button = view.findViewById<Button>(R.id.buttonContinue)

        button.setOnClickListener {
            val name = editText.text.toString().trim()
            if (name.isNotEmpty()) {
                PreferenceHelper.saveUserName(requireContext(), name)
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            } else {
                editText.error = "Enter your name"
            }
        }
    }
}