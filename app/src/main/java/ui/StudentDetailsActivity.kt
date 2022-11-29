package ui

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.ImageViewCompat
import com.google.android.material.button.MaterialButton
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
    private var sealedPowerList: MutableList<Int> = mutableListOf()
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
                        displayPowersInformation(studentSealedPower!!, powerList!!)
                        displayData()
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
        sealedPowerList = mutableListOf(
            sealedPower!!.power1, sealedPower.power2, sealedPower.power3,
            sealedPower.power4, sealedPower.power5, sealedPower.power6
        )

        sealedPowerList.forEachIndexed { i, powerActivedNumber ->
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


    private fun displayOrNotPower(powerActivedNumberText: TextView, powerActivedNumber: Int, powerNameText: TextView, powerImage: ImageView, powerName: String){
        if(powerActivedNumber == 0) {
            powerImage.setImageResource(R.drawable.power_scroll_closed)
            powerNameText.isVisible = false
            powerActivedNumberText.isVisible = false
        } else {
            powerImage.setImageResource(R.drawable.power_scroll_open)
            powerNameText.isVisible = true
            powerActivedNumberText.isVisible = true
            powerNameText.text = powerName
            powerActivedNumberText.text = "X$powerActivedNumber"
        }
    }

    private fun clickOnPower(){
        binding.studentDetailPower1Image.setOnClickListener {
            openInformation(0)
        }
        binding.studentDetailPower2Image.setOnClickListener {
            openInformation(1)
        }
        binding.studentDetailPower3Image.setOnClickListener {
            openInformation(2)
        }
        binding.studentDetailPower4Image.setOnClickListener {
            openInformation(3)
        }
        binding.studentDetailPower5Image.setOnClickListener {
            openInformation(4)
        }
        binding.studentDetailPower6Image.setOnClickListener {
            openInformation(5)
        }

    }

    private fun openInformation(powerId: Int){
        if(sealedPowerList.size > 0 && powerList != null) {
        val currentPower = powerList!!.powers[powerId]
        val currentSealedPower = sealedPowerList[powerId]
        var newCurrentSealedPower = currentSealedPower

        val builder = AlertDialog.Builder(this).create()
        val dialogView = layoutInflater.inflate(R.layout.dialog_power_detail, null)
        builder.setView(dialogView)
        val title = dialogView.findViewById<TextView>(R.id.power_detail_title)
        val description = dialogView.findViewById<TextView>(R.id.power_detail_text)
        val padlock = dialogView.findViewById<ImageView>(R.id.power_detail_padlock)
        val plusButton = dialogView.findViewById<MaterialButton>(R.id.power_detail_plus)
        val minusButton = dialogView.findViewById<MaterialButton>(R.id.power_detail_minus)
        val activedNumberText = dialogView.findViewById<TextView>(R.id.power_detail_actived_number)

        title.text = currentPower.powerName
        description.text = currentPower.description
        activedNumberText.text = "X$currentSealedPower"
        if(currentSealedPower == 0) {
            activedNumberText.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.little_grey)))
            padlock.setImageResource(R.drawable.padlock)
            ImageViewCompat.setImageTintList(padlock,
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)))
        } else {
            activedNumberText.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green_light)))
            padlock.setImageResource(R.drawable.unlock)
            ImageViewCompat.setImageTintList(padlock,
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green_light)))
        }

        plusButton.setOnClickListener {
            newCurrentSealedPower++
            updateSealedPower(powerId, newCurrentSealedPower)
            builder.cancel()
        }
        minusButton.setOnClickListener {
            if(newCurrentSealedPower > 0){
                newCurrentSealedPower--
                updateSealedPower(powerId, newCurrentSealedPower)
                builder.cancel()
            }
        }

        builder.setCanceledOnTouchOutside(true)
        builder.show()
        }
    }

    private fun updateSealedPower(position: Int, number: Int){
        when(position){
            0 ->  studentSealedPower!!.power1 = number
            1 ->  studentSealedPower!!.power2 = number
            2 ->  studentSealedPower!!.power3 = number
            3 ->  studentSealedPower!!.power4 = number
            4 ->  studentSealedPower!!.power5 = number
            5 ->  studentSealedPower!!.power6 = number
        }
       databaseCallsVM.updateSealedPower(studentSealedPower!!)
    }

}