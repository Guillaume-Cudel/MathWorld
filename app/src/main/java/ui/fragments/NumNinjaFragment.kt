package ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guillaume.mathworld.R
import com.guillaume.mathworld.databinding.FragmentNumNinjaBinding
import di.MathWorldApplication
import di.MathWorldViewModelFactory
import model.Student
import services.BeltManagement
import services.UiConfigure
import services.UiConfigureImpl
import ui.adapters.NumNinjaListAdapter
import ui.adapters.SummaryListAdapter
import viewModel.DatabaseCallsViewModel
import viewModel.MainViewModel


class NumNinjaFragment : Fragment(), BeltManagement {

    private lateinit var binding: FragmentNumNinjaBinding
    private lateinit var mainVM: MainViewModel
    private val databaseCallsVM: DatabaseCallsViewModel by viewModels {
        MathWorldViewModelFactory((requireActivity().application as MathWorldApplication).repository)
    }
    private var classID: Int? = null
    private var classLvl: String? = null
    private val uiConfigure: UiConfigure = UiConfigureImpl()
    private lateinit var adapter: NumNinjaListAdapter
    private var mStudentsList: List<Student> = listOf()
    private val beltXpList: MutableList<Int> = mutableListOf()

    // Barde Pouvoir 4 double l'xp
    private val groupListWithBardPower4 = mutableListOf<Int>()

    // Barde pouvoir 6 donne la ceinture superieur
    private val groupListWithBardPower6 = mutableListOf<Int>()

    //todo relance le fragment quand tu cliques dessus
    // A voir plus tard
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNumNinjaBinding.inflate(inflater, container, false)
        mainVM = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        setHasOptionsMenu(true)

        mainVM.classNumber.observe(requireActivity(), Observer {
            classID = it
            databaseCallsVM.getAllStudentsInClass(classID!!)
                ?.observe(requireActivity(), Observer { studentList ->
                    //val firstStudentName = it[0].students[0].firstName
                    if (studentList.isNotEmpty()) {
                        groupListWithBardPower4.clear()
                        groupListWithBardPower6.clear()
                        val students = studentList[0].students
                        students.let { studentsListSaved ->
                            mStudentsList = studentsListSaved
                            mStudentsList.forEach { student ->
                                checkIfBardPowersAreActived(
                                    student,
                                    groupListWithBardPower4,
                                    groupListWithBardPower6
                                )
                            }
                        }
                        configureRecyclerView(students)
                    }
                })
            databaseCallsVM.getClassInformation(classID!!)?.observe(requireActivity()) { classs ->
                classLvl = classs.level
            }
        })
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        activity?.title = "Temple of Panglossya"

       /* mStudentsList.forEach { student ->
            checkIfBardPowersAreActived(
                student,
                groupListWithBardPower4,
                groupListWithBardPower6
            )
        }*/

    }


    private fun configureRecyclerView(studentList: List<Student>) {
        val recyclerView = binding.numninjaListRecyclerView
        adapter = NumNinjaListAdapter(uiConfigure, this@NumNinjaFragment)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireActivity(),
                DividerItemDecoration.VERTICAL
            )
        )
        adapter.submitList(studentList)

    }


    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.num_ninja_toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.toolbar_save_num_ninja -> {
                if (classLvl != null) {
                    assignXpBelt()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun assignXpBelt() {
        if (beltXpList.size > 0) {
            mStudentsList.forEachIndexed { i, student ->

                student.beltXp = beltXpList[i]
                databaseCallsVM.updateStudent(student)
            }
            verifyPowersToUpBelt(groupListWithBardPower4, groupListWithBardPower6)
        }
    }

    private fun checkIfBardPowersAreActived(
        student: Student,
        powerList4: MutableList<Int>,
        powerList6: MutableList<Int>
    ) {
        val bardJob = getString(R.string.bard)
        if (student.job == bardJob) {
            databaseCallsVM.getSealedPowersByStudent(student.id)
                ?.observe(requireActivity()) { powerStudent ->

                    // Verifie si le pouvoir 4 est actif
                    if (powerStudent.sealedPowers.power4Actived) {
                        var groupHaveBardPower4 = false
                        powerList4.forEach { groupNumberWithBard ->
                            if (student.group == groupNumberWithBard) groupHaveBardPower4 = true
                        }
                        if (!groupHaveBardPower4) powerList4.add(student.group)
                    }
                    // Verifie si le pouvoir 6 est actif
                    if (powerStudent.sealedPowers.power6Actived) {
                        var groupHaveBardPower6 = false
                        powerList6.forEach { groupNumberWithBard ->
                            if (student.group == groupNumberWithBard) groupHaveBardPower6 = true
                        }
                        if (!groupHaveBardPower6) powerList6.add(student.group)
                    }
                }
        }
    }

    private fun verifyPowersToUpBelt(powerList4: MutableList<Int>, powerList6: MutableList<Int>) {
        mStudentsList.forEach { student ->
            if (powerList6.isNotEmpty() && powerList4.isNotEmpty()) {
                powerList6.forEach { g ->
                    powerList4.forEach { group ->
                        addXpByPowerActived(student, g, group)
                    }
                }
            } else if (powerList4.isNotEmpty()) {
                powerList4.forEach { group ->
                    addXpByPowerActived(student, 0, group)
                }
            } else if (powerList6.isNotEmpty()) {
                powerList6.forEach { g ->
                    addXpByPowerActived(student, g, 0)
                }
            } else addXpByPowerActived(student, 0, 0)

        }
        openVerificationWindows(powerList4, powerList6)
    }

    private fun addXpByPowerActived(student: Student, g: Int, group: Int) {
        var xpGained = 0
        var newBelt = 0
        // Ajuste le gain avec le pouvoir 6 actif
        if (g == student.group) {
            xpGained = adjustXpGainedByBeltXpIfPower6(student.beltXp, classLvl!!)
            newBelt = saveBeltWithPower6(student.beltXp)
        } else {
            xpGained = adjustXpGainedByBeltXp(student.beltXp, classLvl!!)
            newBelt = saveBelt(student.beltXp)
        }
        // Double l'xp avec le pouvoir 4
        if (group == student.group) {
            xpGained += xpGained
        }
        student.numNinjaXp = xpGained
        student.currentBelt = newBelt
        databaseCallsVM.updateStudent(student)
    }

    private fun openVerificationWindows(
        powerList4: MutableList<Int>,
        powerList6: MutableList<Int>
    ) {
        val builder = AlertDialog.Builder(requireActivity()).create()
        val dialogView = layoutInflater.inflate(R.layout.dialog_save_new_belts, null)
        builder.setView(dialogView)
        builder.setTitle(getString(R.string.summary))
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.summary_list_recycler_view)
        val saveButton = dialogView.findViewById<Button>(R.id.summary_list_save)
        val cancelButton = dialogView.findViewById<Button>(R.id.summary_list_cancel)

        configureDialogRecyclerView(recyclerView)

        saveButton.setOnClickListener {
            saveXpAndBelt()
            clearAllList(powerList4, powerList6)
            builder.cancel()
            Toast.makeText(requireActivity(), getString(R.string.save_num_ninja), Toast.LENGTH_LONG)
                .show()
        }
        cancelButton.setOnClickListener {
            clearAllList(powerList4, powerList6)
            resetNumNinjaData()
            builder.cancel()
        }
        builder.show()
    }

    private fun resetNumNinjaData() {
        mStudentsList.forEach { student ->
            resetSummaryData(student)
        }
    }

    private fun clearAllList(
        powerList4: MutableList<Int>,
        powerList6: MutableList<Int>
    ) {
        powerList4.clear()
        powerList6.clear()
        beltXpList.clear()
    }

    private fun saveXpAndBelt() {
        for (student in mStudentsList) {
            addExperience(student, student.numNinjaXp)
            upgradeBelt(student)
            resetSummaryData(student)
        }
    }

    private fun resetSummaryData(student: Student) {
        student.numNinjaXp = 0
        student.currentBelt = 0
        student.beltXp = 0
        databaseCallsVM.updateStudent(student)
    }

    private fun addExperience(student: Student, xp: Int) {
        var xpMax = student.xpMax
        var experience = student.experience
        val newExperience = experience + xp

        if (newExperience >= xpMax) {
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

    private fun upgradeBelt(student: Student) {
        if (student.currentBelt > student.bestBelt) {
            student.bestBelt = student.currentBelt
            databaseCallsVM.updateStudent(student)
        }
    }

    override fun addBeltXp(beltXp: Int) {
        beltXpList.add(beltXp)
    }


    private fun configureDialogRecyclerView(recyclerViewD: RecyclerView) {

        val dialogAdapter = SummaryListAdapter(uiConfigure)
        recyclerViewD.adapter = dialogAdapter
        recyclerViewD.layoutManager = LinearLayoutManager(activity)
        recyclerViewD.addItemDecoration(
            DividerItemDecoration(
                requireActivity(),
                DividerItemDecoration.VERTICAL
            )
        )
        dialogAdapter.submitList(mStudentsList)
    }

    private fun saveBelt(xpBelt: Int): Int {
        var newBelt = 0
        when (xpBelt) {
            in 0..3 -> newBelt = 1
            in 4..6 -> newBelt = 2
            in 7..9 -> newBelt = 3
            in 10..13 -> newBelt = 4
            in 14..17 -> newBelt = 5
            in 18..21 -> newBelt = 6
            in 22..25 -> newBelt = 7
            in 26..29 -> newBelt = 8
            30 -> newBelt = 9
        }
        return newBelt
    }

    private fun saveBeltWithPower6(xpBelt: Int): Int {
        var newBelt = 0
        when (xpBelt) {
            in 0..3 -> newBelt = 2
            in 4..6 -> newBelt = 3
            in 7..9 -> newBelt = 4
            in 10..13 -> newBelt = 5
            in 14..17 -> newBelt = 6
            in 18..21 -> newBelt = 7
            in 22..25 -> newBelt = 8
            in 26..29 -> newBelt = 9
            30 -> newBelt = 10
        }
        return newBelt
    }

    private fun adjustXpGainedByBeltXp(beltXp: Int, classLvl: String): Int {
        var xpResult = 0

        when (classLvl) {
            "6e" -> {
                when (beltXp) {
                    in 0..3 -> xpResult = 0
                    in 4..6 -> xpResult = 0
                    in 7..9 -> xpResult = 2
                    in 10..13 -> xpResult = 2
                    in 14..17 -> xpResult = 5
                    in 18..21 -> xpResult = 5
                    in 22..25 -> xpResult = 10
                    in 26..29 -> xpResult = 10
                    30 -> xpResult = 20
                }
            }
            "5e" -> {
                when (beltXp) {
                    in 0..3 -> xpResult = 0
                    in 4..6 -> xpResult = 0
                    in 7..9 -> xpResult = 0
                    in 10..13 -> xpResult = 0
                    in 14..17 -> xpResult = 2
                    in 18..21 -> xpResult = 5
                    in 22..25 -> xpResult = 5
                    in 26..29 -> xpResult = 10
                    30 -> xpResult = 20
                }
            }
            "4e" -> {
                when (beltXp) {
                    in 0..3 -> xpResult = 0
                    in 4..6 -> xpResult = 0
                    in 7..9 -> xpResult = 0
                    in 10..13 -> xpResult = 0
                    in 14..17 -> xpResult = 0
                    in 18..21 -> xpResult = 1
                    in 22..25 -> xpResult = 3
                    in 26..29 -> xpResult = 5
                    30 -> xpResult = 10
                }
            }
            "3e" -> {
                when (beltXp) {
                    in 0..3 -> xpResult = 0
                    in 4..6 -> xpResult = 0
                    in 7..9 -> xpResult = 0
                    in 10..13 -> xpResult = 0
                    in 14..17 -> xpResult = 0
                    in 18..21 -> xpResult = 1
                    in 22..25 -> xpResult = 3
                    in 26..29 -> xpResult = 5
                    30 -> xpResult = 10
                }
            }
        }
        return xpResult
    }

    private fun adjustXpGainedByBeltXpIfPower6(beltXp: Int, classLvl: String): Int {
        var xpResult = 0

        when (classLvl) {
            "6e" -> {
                when (beltXp) {
                    in 0..3 -> xpResult = 0
                    in 4..6 -> xpResult = 2
                    in 7..9 -> xpResult = 2
                    in 10..13 -> xpResult = 5
                    in 14..17 -> xpResult = 5
                    in 18..21 -> xpResult = 10
                    in 22..25 -> xpResult = 10
                    in 26..29 -> xpResult = 20
                    30 -> xpResult = 30
                }
            }
            "5e" -> {
                when (beltXp) {
                    in 0..3 -> xpResult = 0
                    in 4..6 -> xpResult = 0
                    in 7..9 -> xpResult = 0
                    in 10..13 -> xpResult = 2
                    in 14..17 -> xpResult = 5
                    in 18..21 -> xpResult = 10
                    in 22..25 -> xpResult = 10
                    in 26..29 -> xpResult = 20
                    30 -> xpResult = 30
                }
            }
            "4e" -> {
                when (beltXp) {
                    in 0..3 -> xpResult = 0
                    in 4..6 -> xpResult = 0
                    in 7..9 -> xpResult = 0
                    in 10..13 -> xpResult = 0
                    in 14..17 -> xpResult = 1
                    in 18..21 -> xpResult = 3
                    in 22..25 -> xpResult = 5
                    in 26..29 -> xpResult = 10
                    30 -> xpResult = 20
                }
            }
            "3e" -> {
                when (beltXp) {
                    in 0..3 -> xpResult = 0
                    in 4..6 -> xpResult = 0
                    in 7..9 -> xpResult = 0
                    in 10..13 -> xpResult = 0
                    in 14..17 -> xpResult = 1
                    in 18..21 -> xpResult = 3
                    in 22..25 -> xpResult = 5
                    in 26..29 -> xpResult = 10
                    30 -> xpResult = 15
                }
            }
        }
        return xpResult
    }
}