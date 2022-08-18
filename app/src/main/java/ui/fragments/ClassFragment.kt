package ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guillaume.mathworld.R
import com.guillaume.mathworld.databinding.FragmentClassBinding
import di.MathWorldApplication
import di.MathWorldViewModelFactory
import ui.ClassesListAdapter
import viewModel.MainViewModel


class ClassFragment : Fragment() {

    private lateinit var binding: FragmentClassBinding
    private val mainVM: MainViewModel by viewModels {
        MathWorldViewModelFactory((requireActivity().application as MathWorldApplication).repository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClassBinding.inflate(inflater, container, false)

        val recyclerView = binding.navHeaderRecyclerView
        val adapter = ClassesListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        mainVM.allClasses.observe(requireActivity(), Observer { classes ->
            classes?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }


}