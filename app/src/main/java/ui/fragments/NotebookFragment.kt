package ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.guillaume.mathworld.R
import com.guillaume.mathworld.databinding.FragmentNotebookBinding
import di.MathWorldApplication
import di.MathWorldViewModelFactory
import model.Student
import services.UiConfigure
import services.UiConfigureImpl
import services.XpByGroupUpdater
import ui.adapters.GroupListAdapter
import viewModel.DatabaseCallsViewModel
import viewModel.MainViewModel


class NotebookFragment : Fragment(), XpByGroupUpdater {

    private lateinit var binding: FragmentNotebookBinding
    private lateinit var adapter: GroupListAdapter
    private lateinit var mainVM: MainViewModel
    private val databaseCallsVM: DatabaseCallsViewModel by viewModels {
        MathWorldViewModelFactory((requireActivity().application as MathWorldApplication).repository)
    }
    private var classID: Int? = null
    private val uiConfigure: UiConfigure = UiConfigureImpl()
    private lateinit var students: List<Student>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotebookBinding.inflate(inflater, container, false)
        mainVM = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        mainVM.classNumber.observe(requireActivity(), Observer { classId ->
            classID = classId
            configureRecyclerView()
            databaseCallsVM.getAllStudentsInClass(classID!!)?.observe(requireActivity(), Observer {
                if(it.isNotEmpty()){
                    students = it[0].students
                }
            })
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.notebook)
    }

    private fun configureRecyclerView(){
        val recyclerView = binding.groupsListRecyclerView
        adapter = GroupListAdapter(this@NotebookFragment, uiConfigure)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireActivity(),
                DividerItemDecoration.VERTICAL
            )
        )

            val groupList = listOf<Int>(1,2,3,4,5,6,7,8)
        groupList.let {
            adapter.submitList(it)
        }
    }

    override fun addXpByGroup(group: Int, xp: Int) {
        students.forEach { student->

            if(student.group == group){
                addExperience(student, xp)
            }
        }
        Toast.makeText(requireActivity(), "+$xp d'xp pour le groupe $group", Toast.LENGTH_LONG).show()

    }

    private fun addExperience(student: Student, xp: Int){
        var xpMax = student.xpMax
        var experience = student.experience
        val newExperience = experience + xp

        if(newExperience >= xpMax){
            experience = newExperience - xpMax
            xpMax += 5
            student.experience = experience
            student.xpMax = xpMax
            student.level += 1
        } else {
            student.experience = newExperience
        }

        databaseCallsVM.updateStudent(student)
    }

}