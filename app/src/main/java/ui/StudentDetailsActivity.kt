package ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import com.guillaume.mathworld.R
import com.guillaume.mathworld.databinding.ActivityStudentDetailsBinding
import di.MathWorldApplication
import di.MathWorldViewModelFactory
import model.JobWithPower
import model.SealedPower
import model.Student
import services.UiConfigure
import services.UiConfigureImpl
import viewModel.DatabaseCallsViewModel

class StudentDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentDetailsBinding
    private val uiConfigure: UiConfigure = UiConfigureImpl()
    private var student: Student? = null
    private var studentSealedPower: SealedPower? = null
    private var powerList: JobWithPower? = null
    private val databaseCallsVM: DatabaseCallsViewModel by viewModels {
        MathWorldViewModelFactory((application as MathWorldApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureToolbar()
        val bundle = intent.extras
        student = bundle!!.getSerializable("student") as Student

        databaseCallsVM.getSealedPowersByStudent(student!!.id)?.observe(this) { sealedPower ->
                studentSealedPower = sealedPower.sealedPowers
                databaseCallsVM.getPowersInformationsByJob(student!!.job)?.observe(this) { jobPowersList ->
                        powerList = jobPowersList[0]
                        displayData()
                        displayPowersInformation(studentSealedPower!!, powerList!!)
                    }
            }

        clickOnPower()

    }

    private fun configureToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun displayData() {
        uiConfigure.setBelt(student!!.belt, binding.studentDetailBeltImage)
        uiConfigure.displayExperience(
            student!!.experience,
            student!!.xpMax,
            binding.studentDetailExperienceResult
        )
        uiConfigure.displayJobImage(student!!.job, binding.studentDetailJobImage)
        binding.studentDetailExperienceBar.progress = student!!.experience
        binding.studentDetailExperienceBar.max = student!!.xpMax
        uiConfigure.displayLifeNumber(student!!.pointOfLife, binding.studentDetailLifeResult)
        binding.studentDetailLifeBar.progress = student!!.pointOfLife
        binding.studentDetailLifeBar.max = 3
        binding.studentDetailLevelResponse.text = student!!.level.toString()
        binding.studentDetailJob.text = student!!.job
        val nameAndLastname = student!!.firstName + " " + student!!.lastName
        binding.studentDetailName.text = nameAndLastname

    }

    private fun displayPowersInformation(sealedPower: SealedPower?, powersList: JobWithPower) {
        val sealedPowerList: MutableList<Int> = mutableListOf(
            sealedPower!!.power1, sealedPower.power2, sealedPower.power3,
            sealedPower.power4, sealedPower.power5, sealedPower.power6
        )

        sealedPowerList.forEachIndexed { i, powerActivedNumber ->
           /* when (activPowerNumber) {
                true -> configureTruePowersImage(i, powersList)
                false -> configureFalsePowersImage(i)
            }*/
            /*if(powerActivedNumber >= 1)configurePowers(i, powersList, powerActivedNumber)
            else configureFalsePowersImage(i)*/
            configurePowers(i, powersList, powerActivedNumber)
        }
    }

    private fun configurePowers(i: Int, powersList: JobWithPower, powerActivedNumber: Int) {

        when (i) {
            0 -> { displayOrNotPower(binding.studentDetailPower1ActivedNumber, powerActivedNumber,
                binding.studentDetailPower1Text, binding.studentDetailPower1Image, powersList.powers[0].powerName) }
            1 -> displayOrNotPower(binding.studentDetailPower2ActivedNumber, powerActivedNumber,
                binding.studentDetailPower2Text, binding.studentDetailPower2Image, powersList.powers[1].powerName)
            2 -> displayOrNotPower(binding.studentDetailPower3ActivedNumber, powerActivedNumber,
                binding.studentDetailPower3Text, binding.studentDetailPower3Image, powersList.powers[2].powerName)
            3 -> displayOrNotPower(binding.studentDetailPower4ActivedNumber, powerActivedNumber,
                binding.studentDetailPower4Text, binding.studentDetailPower4Image, powersList.powers[3].powerName)
            4 -> displayOrNotPower(binding.studentDetailPower5ActivedNumber, powerActivedNumber,
                binding.studentDetailPower5Text, binding.studentDetailPower5Image, powersList.powers[4].powerName)
            5 -> displayOrNotPower(binding.studentDetailPower6ActivedNumber, powerActivedNumber,
                binding.studentDetailPower6Text, binding.studentDetailPower6Image, powersList.powers[5].powerName)
        }
    }

    /*private fun configureFalsePowersImage(i: Int) {
        when (i) {
            0 -> { binding.studentDetailPower1Image.setImageResource(R.drawable.power_scroll_closed)
                binding.studentDetailPower1Text.isVisible = false }
            1 -> { binding.studentDetailPower2Image.setImageResource(R.drawable.power_scroll_closed)
                binding.studentDetailPower2Text.isVisible = false }
            2 -> { binding.studentDetailPower3Image.setImageResource(R.drawable.power_scroll_closed)
                binding.studentDetailPower3Text.isVisible = false }
            3 -> { binding.studentDetailPower4Image.setImageResource(R.drawable.power_scroll_closed)
                binding.studentDetailPower4Text.isVisible = false }
            4 -> { binding.studentDetailPower5Image.setImageResource(R.drawable.power_scroll_closed)
                binding.studentDetailPower5Text.isVisible = false }
            5 -> { binding.studentDetailPower6Image.setImageResource(R.drawable.power_scroll_closed)
                binding.studentDetailPower6Text.isVisible = false }
        }
    }*/

    private fun displayOrNotPower(powerActivedNumberText: TextView, powerActivedNumber: Int, powerNameText: TextView, powerImage: ImageView, powerName: String){
        if(powerActivedNumber == 0) {
            powerImage.setImageResource(R.drawable.power_scroll_closed)
            powerNameText.isVisible = false
            powerActivedNumberText.isVisible = false
        }
        else {
            powerImage.setImageResource(R.drawable.power_scroll_open)
            powerNameText.text = powerName
            powerActivedNumberText.text = "X$powerActivedNumber"
        }
    }

    private fun clickOnPower(){
        binding.studentDetailPower1Image.setOnClickListener {
            openInformation(1)
        }
        binding.studentDetailPower2Image.setOnClickListener {
            openInformation(2)
        }
        binding.studentDetailPower3Image.setOnClickListener {
            openInformation(3)
        }
        binding.studentDetailPower4Image.setOnClickListener {
            openInformation(4)
        }
        binding.studentDetailPower5Image.setOnClickListener {
            openInformation(5)
        }
        binding.studentDetailPower6Image.setOnClickListener {
            openInformation(6)
        }

    }

    /*val builder = AlertDialog.Builder(requireActivity()).create()
    val dialogView = layoutInflater.inflate(R.layout.add_todo_dialog, null)
    builder.setView(dialogView)
    val title = dialogView.findViewById<EditText>(R.id.add_dialog_title_edit)
    val description = dialogView.findViewById<EditText>(R.id.add_dialog_description_edit)

    builder.setCanceledOnTouchOutside(true)
    builder.show()*/

    //todo finish it
    private fun openInformation(powerId: Int){
        val builder = AlertDialog.Builder(this).create()
        val dialogView = layoutInflater.inflate(R.layout.dialog_power_detail, null)


       /* when(powerId){
            1 ->
        }*/
    }

}