package com.threelineng.freedomnetwork.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.threelineng.freedomnetwork.R
import com.threelineng.freedomnetwork.databinding.FragmentLoginBinding
import com.tegritech.commons.utils.viewBinding

class LoginFragment : Fragment() {

    private val binding by viewBinding<FragmentLoginBinding>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            continueButton.setOnClickListener {
                findNavController().setGraph(R.navigation.main_nav_graph)
            }
        }
    }
}