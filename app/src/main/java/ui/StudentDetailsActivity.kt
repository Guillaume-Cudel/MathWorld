package ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
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
                        val powerList = jobPowersList[0]
                        displayData()
                        displayPowersInformation(studentSealedPower!!, powerList)
                    }
            }


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
        val sealedPowerList: MutableList<Boolean> = mutableListOf(
            sealedPower!!.power1, sealedPower.power2, sealedPower.power3,
            sealedPower.power4, sealedPower.power5, sealedPower.power6
        )

        sealedPowerList.forEachIndexed { i, bool ->
            when (bool) {
                true -> configureTruePowersImage(i, powersList)
                false -> configureFalsePowersImage(i)
            }
        }
    }

    private fun configureTruePowersImage(i: Int, powersList: JobWithPower) {
        when (i) {
            0 -> { binding.studentDetailPower1Image.setImageResource(R.drawable.power_scroll_open)
                binding.studentDetailPower1Text.text = powersList.powers[0].powerName }
            1 -> { binding.studentDetailPower2Image.setImageResource(R.drawable.power_scroll_open)
                binding.studentDetailPower2Text.text = powersList.powers[1].powerName }
            2 -> { binding.studentDetailPower3Image.setImageResource(R.drawable.power_scroll_open)
                binding.studentDetailPower3Text.text = powersList.powers[2].powerName }
            3 -> { binding.studentDetailPower4Image.setImageResource(R.drawable.power_scroll_open)
                binding.studentDetailPower4Text.text = powersList.powers[3].powerName }
            4 -> { binding.studentDetailPower5Image.setImageResource(R.drawable.power_scroll_open)
                binding.studentDetailPower5Text.text = powersList.powers[4].powerName }
            5 -> { binding.studentDetailPower6Image.setImageResource(R.drawable.power_scroll_open)
                binding.studentDetailPower6Text.text = powersList.powers[5].powerName }
        }
    }

    private fun configureFalsePowersImage(i: Int) {
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
    }

}