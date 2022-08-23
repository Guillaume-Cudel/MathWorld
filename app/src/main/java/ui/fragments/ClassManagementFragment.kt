package ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.guillaume.mathworld.R
import com.guillaume.mathworld.databinding.FragmentClassManagementBinding
import di.MathWorldApplication
import di.MathWorldViewModelFactory
import viewModel.ClassManagementViewModel

class ClassManagementFragment : Fragment() {

    private lateinit var binding: FragmentClassManagementBinding
    private val classManagementVM: ClassManagementViewModel by viewModels {
        MathWorldViewModelFactory((requireActivity().application as MathWorldApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClassManagementBinding.inflate(inflater, container, false)

        val textField = binding.cmText

        classManagementVM.getAllStudentsInClass(1)?.observe(requireActivity(), Observer {
            val firstStudentName = it[0].students[0].firstName
            textField.text = firstStudentName
        })


        return binding.root
    }



}