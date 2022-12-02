package ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.guillaume.mathworld.R
import com.guillaume.mathworld.databinding.FragmentNotebookBinding
import com.guillaume.mathworld.databinding.FragmentNumNinjaBinding
import di.MathWorldApplication
import di.MathWorldViewModelFactory
import services.UiConfigure
import services.UiConfigureImpl
import ui.adapters.NumNinjaListAdapter
import ui.adapters.StudentsListAdapter
import viewModel.DatabaseCallsViewModel
import viewModel.MainViewModel


class NumNinjaFragment : Fragment() {

    private lateinit var binding: FragmentNumNinjaBinding
    private lateinit var mainVM: MainViewModel
    private val databaseCallsVM: DatabaseCallsViewModel by viewModels {
        MathWorldViewModelFactory((requireActivity().application as MathWorldApplication).repository)
    }
    private var classID: Int? = null
    private val uiConfigure: UiConfigure = UiConfigureImpl()
    private lateinit var adapter: NumNinjaListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNumNinjaBinding.inflate(inflater, container, false)
        mainVM = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        mainVM.classNumber.observe(requireActivity(), Observer {
            classID = it
            configureRecyclerView(classID!!)
        })

        return binding.root
    }

    //todo ajoute le bouton sauvegarder dans la bare de menu

    private fun configureRecyclerView(classDisplayed: Int){
        val recyclerView = binding.numninjaListRecyclerView
        adapter = NumNinjaListAdapter(uiConfigure)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireActivity(),
                DividerItemDecoration.VERTICAL
            )
        )

        databaseCallsVM.getAllStudentsInClass(classDisplayed)
            ?.observe(requireActivity(), Observer {
                //val firstStudentName = it[0].students[0].firstName
                if (it.isNotEmpty()) {
                    val students = it[0].students
                    students.let { studentsList ->
                        adapter.submitList(studentsList)
                    }
                }
            })
    }
}