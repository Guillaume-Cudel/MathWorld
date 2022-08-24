package ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.guillaume.mathworld.databinding.FragmentClassManagementBinding
import di.MathWorldApplication
import di.MathWorldViewModelFactory
import ui.adapters.StudentsListAdapter
import viewModel.ClassManagementViewModel
import viewModel.MainViewModel

class ClassManagementFragment : Fragment() {

    private lateinit var binding: FragmentClassManagementBinding
    private val classManagementVM: ClassManagementViewModel by viewModels {
        MathWorldViewModelFactory((requireActivity().application as MathWorldApplication).repository)
    }
    private lateinit var mainVM: MainViewModel
    private var classDisplayed = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClassManagementBinding.inflate(inflater, container, false)

        //val textField = binding.cmText
        mainVM = ViewModelProvider(requireActivity())[MainViewModel::class.java]


        mainVM.classNumber.observe(requireActivity(), Observer {
            classDisplayed = it
            configureRecyclerView(classDisplayed)
        })

        //configureRecyclerView(classDisplayed)


        return binding.root
    }

    private fun configureRecyclerView(classDisplayed: Int){
        val recyclerView = binding.studentsListRecyclerView
        val adapter = StudentsListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        classManagementVM.getAllStudentsInClass(classDisplayed)?.observe(requireActivity(), Observer {
            // Exemple de recuperation
            //val firstStudentName = it[0].students[0].firstName
            val students = it[0].students

            students.let { studentsList ->
                adapter.submitList(studentsList)
            }
        })
    }





}