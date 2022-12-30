package ui.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
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
import model.StudentsClass
import ui.AddStudentActivity
import services.StatsUpdater
import services.UiConfigure
import services.UiConfigureImpl
import ui.StudentDetailsActivity
import ui.adapters.StudentsListAdapter
import viewModel.DatabaseCallsViewModel
import viewModel.MainViewModel

class ClassManagementFragment : Fragment(), StatsUpdater {

    private lateinit var binding: FragmentClassManagementBinding
    private lateinit var adapter: StudentsListAdapter
    private val databaseCallsVM: DatabaseCallsViewModel by viewModels {
        MathWorldViewModelFactory((requireActivity().application as MathWorldApplication).repository)
    }
    private lateinit var mainVM: MainViewModel
    private val uiConfigure: UiConfigure = UiConfigureImpl()
    private var classID: Int? = null
    private var currentClass: StudentsClass? = null
    private var experienceGiven: Int = 1
    private var mActionMode: ActionMode? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClassManagementBinding.inflate(inflater, container, false)
        mainVM = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        setHasOptionsMenu(true)

        mainVM.classNumber.observe(requireActivity(), Observer { id ->
            classID = id
            configureRecyclerView(classID!!)
            databaseCallsVM.getClassInformation(classID!!)?.observe(requireActivity(), Observer { cClass ->
                currentClass = cClass
            })
        })
        updateExperienceGiven(experienceGiven)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if(currentClass != null) activity?.title = currentClass!!.name
    }


    private fun configureRecyclerView(classDisplayed: Int) {
        val recyclerView = binding.studentsListRecyclerView
        adapter = StudentsListAdapter(this@ClassManagementFragment, uiConfigure)
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
                        val studentListSorted = studentsList.sortedBy { it.lastName }
                        adapter.submitList(studentListSorted)
                    }
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        //return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.class_management_toolbar_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

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
            R.id.toolbar_edit_class -> {
                editClassDialog(currentClass!!)
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
        databaseCallsVM.updateStudent(student)
    }

    override fun updateLevel(student: Student) {
        student.level += 1
        val text = getString(R.string.level_up)
        Toast.makeText(requireActivity(), student.firstName + " " + text, Toast.LENGTH_SHORT).show()
        databaseCallsVM.updateStudent(student)
    }

    override fun updateLife(student: Student, life: Int) {
        student.pointOfLife = life
        databaseCallsVM.updateStudent(student)
    }

    override fun updateGroup(student: Student, group: Int) {
        student.group = group
        databaseCallsVM.updateStudent(student)
    }

    override fun openDetail(student: Student){
        val intent = Intent(requireActivity(), StudentDetailsActivity::class.java)
        intent.putExtra("student", student)
        intent.putExtra("classId", classID)
        startActivity(intent)
    }

    private fun updateExperienceGiven(xpChoosed: Int) {
        adapter.updateExperienceGiven(xpChoosed)
    }



    private val actionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            mode.menuInflater.inflate(R.menu.action_mode_menu, menu)
            mode.title = getString(R.string.xp_gain_txt)
            mode.subtitle = getString(R.string.what_quantity)

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

    private fun editClassDialog(cClass: StudentsClass){

        val builder = AlertDialog.Builder(requireActivity()).create()
        val view = layoutInflater.inflate(R.layout.dialog_modify_class, null)
        val classNameEditText = view.findViewById<EditText>(R.id.modify_class_name_edit)
        val levelChoice = resources.getStringArray(R.array.Level)
        val arrayAdapter = ArrayAdapter(requireActivity(), R.layout.level_list_item, levelChoice)
        val level: AutoCompleteTextView = view.findViewById(R.id.modify_class_level_choice_text)
        level.setAdapter(arrayAdapter)
        val saveButton = view.findViewById<Button>(R.id.modify_class_save)
        builder.setView(view)

        classNameEditText.setText(cClass.name)

        activateSaveButton(saveButton, level)
        saveButton.setOnClickListener {
            if(classNameEditText.editableText?.toString() != ""){
                cClass.name = classNameEditText.editableText.toString()
                cClass.level = level.editableText.toString()
                databaseCallsVM.updateClass(currentClass!!)
                builder.dismiss()
            } else {
                Toast.makeText(requireActivity(), getString(R.string.enter_name), Toast.LENGTH_LONG).show()
            }
        }
        builder.setCanceledOnTouchOutside(true)
        builder.show()

    }

    private fun activateSaveButton(button: Button, level: AutoCompleteTextView){
        level.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                button.isEnabled = s?.length!! > 0
                button.backgroundTintList =
                    ContextCompat.getColorStateList(requireActivity(), R.color.orange)
            }
        })
    }

}