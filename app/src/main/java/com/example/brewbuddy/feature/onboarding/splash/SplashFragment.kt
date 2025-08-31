package com.example.brewbuddy.feature.onboarding.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.brewbuddy.R
import com.example.brewbuddy.feature.main.MainActivity
import com.example.brewbuddy.feature.onboarding.pref.PreferenceHelper


class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val hasName = PreferenceHelper.getUserName(requireContext()) != null

        Handler(Looper.getMainLooper()).postDelayed({
            if (hasName) {
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            } else {
                findNavController().navigate(R.id.action_splash_to_enterName)
            }
        }, 2000)
    }
}