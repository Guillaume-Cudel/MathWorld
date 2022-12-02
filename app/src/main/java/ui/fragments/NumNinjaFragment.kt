package ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
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
    private val uiConfigure: UiConfigure = UiConfigureImpl()
    private lateinit var adapter: NumNinjaListAdapter
    private var studentsList: List<Student> = listOf()
    private val beltXpList: MutableList<Int> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNumNinjaBinding.inflate(inflater, container, false)
        mainVM = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        setHasOptionsMenu(true)



        mainVM.classNumber.observe(requireActivity(), Observer {
            classID = it
            configureRecyclerView(classID!!)
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        activity?.title = "Num Ninja"
    }


    private fun configureRecyclerView(classDisplayed: Int){
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

        databaseCallsVM.getAllStudentsInClass(classDisplayed)
            ?.observe(requireActivity(), Observer {
                //val firstStudentName = it[0].students[0].firstName
                if (it.isNotEmpty()) {
                    val students = it[0].students
                    students.let { studentsListSaved ->
                        studentsList = studentsListSaved
                        adapter.submitList(studentsList)
                    }
                }
            })
    }


    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.num_ninja_toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.toolbar_save_num_ninja -> {

                openVerificationWindows()


                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openVerificationWindows() {
        val builder = AlertDialog.Builder(requireActivity()).create()
        val dialogView = layoutInflater.inflate(R.layout.dialog_save_new_belts, null)
        builder.setView(dialogView)
        builder.setTitle(getString(R.string.summary))
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.summary_list_recycler_view)
        val saveButton = dialogView.findViewById<Button>(R.id.summary_list_save)
        val cancelButton = dialogView.findViewById<Button>(R.id.summary_list_cancel)

        configureDialogRecyclerView(recyclerView)


        builder.setCanceledOnTouchOutside(true)
        builder.show()
    }

    override fun addBeltXp(beltXp: Int) {
        beltXpList.add(beltXp)
    }


    private fun configureDialogRecyclerView(recyclerView: RecyclerView) {

        var dialogAdapter = SummaryListAdapter(uiConfigure)
        recyclerView.adapter = dialogAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireActivity(),
                DividerItemDecoration.VERTICAL
            ))

        dialogAdapter.submitList(studentsList)
    }
}