package com.kiss.mickandrorty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kiss.mickandrorty.databinding.FragmentFirstBinding
import com.skydoves.bindables.BindingFragment

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : BindingFragment<FragmentFirstBinding>(R.layout.fragment_first) {

    internal val viewModel:FirstFragmentViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding {
            adapter = CharacterAdapter()
            vm = viewModel

            buttonFirst.setOnClickListener {
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            }
        }
    }

}