package ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import android.widget.Toolbar
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
import model.Student
import ui.AddStudentActivity
import ui.StatsUpdater
import ui.adapters.StudentsListAdapter
import viewModel.ClassManagementViewModel
import viewModel.MainViewModel
import java.util.*

class ClassManagementFragment : Fragment(), StatsUpdater {

    private lateinit var binding: FragmentClassManagementBinding
    private lateinit var adapter: StudentsListAdapter
    private val classManagementVM: ClassManagementViewModel by viewModels {
        MathWorldViewModelFactory((requireActivity().application as MathWorldApplication).repository)
    }
    private lateinit var mainVM: MainViewModel
    private var classID: Int? = null
    private var experienceGiven: Int = 1
    private var mActionMode: ActionMode? = null

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

        updateExperienceGiven(experienceGiven)


        return binding.root
    }


    private fun configureRecyclerView(classDisplayed: Int) {
        val recyclerView = binding.studentsListRecyclerView
        adapter = StudentsListAdapter(this@ClassManagementFragment)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireActivity(),
                DividerItemDecoration.VERTICAL
            )
        )

        classManagementVM.getAllStudentsInClass(classDisplayed)
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
            R.id.toolbar_configure_xp -> {
                mActionMode = requireActivity().startActionMode(actionModeCallback)!!
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun updateExperience(student: Student, experience: Int, xpMax: Int) {
        student.experience = experience
        if (student.xpMax != xpMax) {
            student.xpMax = xpMax
        }

        classManagementVM.updateStudent(student)
    }

    override fun updateLevel(student: Student) {
        student.level += 1
        val text = getString(R.string.level_up)
        Toast.makeText(requireActivity(), student.firstName + " " + text, Toast.LENGTH_SHORT).show()
        classManagementVM.updateStudent(student)

    }

    override fun updateLife(student: Student, life: Int) {
        student.pointOfLife = life
        classManagementVM.updateStudent(student)
    }

    override fun updateGroup(student: Student, group: Int) {
        student.group = group
        classManagementVM.updateStudent(student)
    }

    private fun updateExperienceGiven(xpChoosed: Int) {
        adapter.updateExperienceGiven(xpChoosed)
    }

    private val actionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            mode.menuInflater.inflate(R.menu.action_mode_menu, menu)
            mode.title = "Xp gain"
            mode.subtitle = "What quantity ?"

            //todo met en surlignance le gain d'xp choisi
            val xp1 = menu.findItem(R.id.action_mode_xp1)
            /*when(experienceGiven){
                1 -> xp1.
            }*/
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu?): Boolean {
            return false
        }


        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.action_mode_xp1 -> {
                    experienceGiven = 1
                    mode.finish()
                    true
                }
                R.id.action_mode_xp5 -> {
                    experienceGiven = 5
                    mode.finish()
                    true
                }
                R.id.action_mode_xp10 -> {
                    experienceGiven = 10
                    mode.finish()
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            val xpGain = getString(R.string.xp_gain) + experienceGiven.toString()
            Toast.makeText(
                requireActivity(),
                xpGain,
                Toast.LENGTH_SHORT
            ).show()
            adapter.giveExperience = experienceGiven
            mActionMode = null
        }
    }

}