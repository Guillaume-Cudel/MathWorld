package ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.guillaume.mathworld.R
import com.guillaume.mathworld.databinding.FragmentClassManagementBinding
import di.MathWorldApplication
import di.MathWorldViewModelFactory
import model.RpgClass
import model.Student
import ui.AddStudentActivity
import ui.StatsUpdater
import ui.adapters.StudentsListAdapter
import viewModel.ClassManagementViewModel
import viewModel.MainViewModel

class ClassManagementFragment : Fragment(), StatsUpdater {

    private lateinit var binding: FragmentClassManagementBinding
    private val classManagementVM: ClassManagementViewModel by viewModels {
        MathWorldViewModelFactory((requireActivity().application as MathWorldApplication).repository)
    }
    private lateinit var mainVM: MainViewModel
    private var classID: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClassManagementBinding.inflate(inflater, container, false)
        mainVM = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        setHasOptionsMenu(true)

        mainVM.classNumber.observe(requireActivity(), Observer {
            classID = it
            configureRecyclerView(classID!!)
        })


        return binding.root
    }

    private fun configureRecyclerView(classDisplayed: Int){
        val recyclerView = binding.studentsListRecyclerView
        val adapter = StudentsListAdapter(this@ClassManagementFragment)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireActivity(),
                DividerItemDecoration.VERTICAL
            )
        )

        classManagementVM.getAllStudentsInClass(classDisplayed)?.observe(requireActivity(), Observer {
            // Exemple de recuperation
            //val firstStudentName = it[0].students[0].firstName
            if(it.isNotEmpty()) {
                val students = it[0].students
                students.let { studentsList ->
                    adapter.submitList(studentsList)
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        //return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.toolbar_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.toolbar_add_student -> {
                val intent = Intent(requireActivity(), AddStudentActivity::class.java)
                intent.putExtra("class_id", classID)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun updateExperience(student: Student, experience: Int) {
        student.experience = experience
        classManagementVM.updateStudent(student)
    }

    override fun updateLevel(student: Student) {
        student.level += 1
        classManagementVM.updateStudent(student)

    }

    override fun updateLife(life: Int) {
        TODO("Not yet implemented")
    }

    override fun updateGroup(group: Int) {
        TODO("Not yet implemented")
    }


}