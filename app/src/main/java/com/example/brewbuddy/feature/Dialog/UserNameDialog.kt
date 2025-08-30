package com.example.brewbuddy.feature.Dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.brewbuddy.core.prefs.UserPrefs
import com.example.brewbuddy.databinding.DialogUserNameBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UserNameDialog : DialogFragment() {

    private var _binding: DialogUserNameBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var userPrefs: UserPrefs

    companion object {
        const val KEY_USER_NAME = "user_name"
        fun newInstance(): UserNameDialog = UserNameDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogUserNameBinding.inflate(inflater, container, false)

//        // Start disabled until user types something valid
//        binding.btnContinue.isEnabled = false

//        // Enable button only when input is not blank
//        binding.etUserName.addTextChangedListener { editable ->
//            binding.btnContinue.isEnabled = !editable.isNullOrBlank()
//        }

//        binding.btnContinue.setOnClickListener {
//            val name = binding.etUserName.text?.toString()?.trim().orEmpty()
//            if (name.isNotEmpty()) {
//                // Save using lifecycleScope for coroutine context
//                lifecycleScope.launch {
//                    userPrefs.saveData(KEY_USER_NAME, name)
//                    dismiss()
//                }
//            }
//        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}