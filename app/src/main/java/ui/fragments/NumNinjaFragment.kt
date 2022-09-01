package ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.guillaume.mathworld.R
import com.guillaume.mathworld.databinding.FragmentNotebookBinding
import com.guillaume.mathworld.databinding.FragmentNumNinjaBinding
import viewModel.MainViewModel


class NumNinjaFragment : Fragment() {

    private lateinit var binding: FragmentNumNinjaBinding
    private lateinit var mainVM: MainViewModel
    private var classID: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNumNinjaBinding.inflate(inflater, container, false)
        mainVM = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        mainVM.classNumber.observe(requireActivity(), Observer {
            classID = it
        })

        return binding.root
    }


}