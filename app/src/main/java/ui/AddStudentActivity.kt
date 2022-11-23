package ui

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import com.guillaume.mathworld.R
import com.guillaume.mathworld.databinding.ActivityAddStudentBinding
import di.MathWorldApplication
import di.MathWorldViewModelFactory
import model.SealedPower
import model.Student
import services.UiConfigure
import services.UiConfigureImpl
import viewModel.DatabaseCallsViewModel

class AddStudentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStudentBinding
    private val uiConfigure: UiConfigure = UiConfigureImpl()
    private val databaseCallsVM: DatabaseCallsViewModel by viewModels {
        MathWorldViewModelFactory((application as MathWorldApplication).repository)
    }
    private var currentJob = "Bard"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureToolbar()

        val bundle = intent.extras
        val classId = bundle!!.getInt("class_id")
        setClassName(classId)

        onImageJobClick()
        binding.addStudentGroupImage.setOnClickListener {
            changeGroupNumber()
        }
        binding.addStudentSave.setOnClickListener {
            saveStudent(classId)
        }

    }

    private fun configureToolbar(){
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun saveStudent(classId: Int){
        if(binding.addStudentFirstnameEdit.editableText.toString() != ""
            && binding.addStudentLastnameEdit.editableText.toString() != "") {
            val firstname = binding.addStudentFirstnameEdit.editableText.toString()
            val lastname = binding.addStudentLastnameEdit.editableText.toString()
            val job = currentJob
            val group = binding.addStudentGroupResult.text.toString()

            val newStudent = Student(
                classId, firstname, lastname, job, 3, 1,
                0, 45, group.toInt(), 1, 0
            )
            val newStudentSealedPowers = SealedPower(newStudent.id, power1 = false, power2 = false,
                power3 = false, power4 = false, power5 = false, power6 = false
            )
            databaseCallsVM.insertStudent(newStudent)
            databaseCallsVM.insertSealedPowers(newStudentSealedPowers)
            Toast.makeText(this, "$firstname $lastname" + " " + getString(R.string.student_added), Toast.LENGTH_LONG).show()
            resetFields()
        } else {
            Toast.makeText(this, getString(R.string.fill_fields), Toast.LENGTH_LONG).show()
        }
    }


    private fun changeGroupNumber(){
        val groupNumberString = binding.addStudentGroupResult.text.toString()
        var groupNumber: Int = groupNumberString.toInt()

        if(groupNumber < 8){
            groupNumber++
            val newGroupNumber = groupNumber.toString()
            binding.addStudentGroupResult.text = newGroupNumber
        } else {
            groupNumber = 1
            val newGroupNumber = groupNumber.toString()
            binding.addStudentGroupResult.text = newGroupNumber
        }
        uiConfigure.changeGroupImageColor(groupNumber, binding.addStudentGroupImage)
    }

    private fun setJob(job: String){
        setColorJobToGrey()
        when(job){
            "Barde" -> { //binding.addStudentJobBard.setColorFilter(resources.getColor(R.color.orange))
                //binding.addStudentJobBard.setBackgroundResource(R.drawable.round_job_image_yellow)
            binding.addStudentJobBardText.setTextColor(Color.RED)
                binding.addStudentJobBardText.setTypeface(null, Typeface.BOLD)}
            "Changelin" -> {  //binding.addStudentJobShapeshifter.setBackgroundResource(R.drawable.round_job_image_yellow)
                binding.addStudentJobShapeshifterText.setTextColor(Color.RED)
                binding.addStudentJobShapeshifterText.setTypeface(null, Typeface.BOLD)}
            "Empathe" -> { //binding.addStudentJobEmpath.setBackgroundResource(R.drawable.round_job_image_yellow)
                binding.addStudentJobEmpathText.setTextColor(Color.RED)
                binding.addStudentJobEmpathText.setTypeface(null, Typeface.BOLD)}
            "Filou" -> {//binding.addStudentJobRogue.setBackgroundResource(R.drawable.round_job_image_yellow)
                binding.addStudentJobRogueText.setTextColor(Color.RED)
                binding.addStudentJobRogueText.setTypeface(null, Typeface.BOLD)}
            "Parieur" -> { //binding.addStudentJobBettor.setBackgroundResource(R.drawable.round_job_image_yellow)
                binding.addStudentJobBettorText.setTextColor(Color.RED)
                binding.addStudentJobBettorText.setTypeface(null, Typeface.BOLD)}
            "Tisse-sort" -> { //binding.addStudentJobSpellweaver.setBackgroundResource(R.drawable.round_job_image_yellow)
                binding.addStudentJobSpellweaverText.setTextColor(Color.RED)
                binding.addStudentJobSpellweaverText.setTypeface(null, Typeface.BOLD)}
            "Hacker" -> {//binding.addStudentJobHacker.setBackgroundResource(R.drawable.round_job_image_yellow)
                binding.addStudentJobHackerText.setTextColor(Color.RED)
                binding.addStudentJobHackerText.setTypeface(null, Typeface.BOLD)}
        }
    }

    private fun setColorJobToGrey(){
        binding.addStudentJobBard.clearColorFilter()
        //binding.addStudentJobBard.setBackgroundResource(R.drawable.round_job_image_grey)
        binding.addStudentJobBardText.setTextColor(Color.GRAY)
        binding.addStudentJobBardText.setTypeface(null, Typeface.NORMAL)
        binding.addStudentJobShapeshifter.clearColorFilter()
        //binding.addStudentJobShapeshifter.setBackgroundResource(R.drawable.round_job_image_grey)
        binding.addStudentJobShapeshifterText.setTextColor(Color.GRAY)
        binding.addStudentJobShapeshifterText.setTypeface(null, Typeface.NORMAL)
        binding.addStudentJobEmpath.clearColorFilter()
        //binding.addStudentJobEmpath.setBackgroundResource(R.drawable.round_job_image_grey)
        binding.addStudentJobEmpathText.setTextColor(Color.GRAY)
        binding.addStudentJobEmpathText.setTypeface(null, Typeface.NORMAL)
        binding.addStudentJobRogue.clearColorFilter()
        //binding.addStudentJobRogue.setBackgroundResource(R.drawable.round_job_image_grey)
        binding.addStudentJobRogueText.setTextColor(Color.GRAY)
        binding.addStudentJobRogueText.setTypeface(null, Typeface.NORMAL)
        binding.addStudentJobBettor.clearColorFilter()
        //binding.addStudentJobBettor.setBackgroundResource(R.drawable.round_job_image_grey)
        binding.addStudentJobBettorText.setTextColor(Color.GRAY)
        binding.addStudentJobBettorText.setTypeface(null, Typeface.NORMAL)
        binding.addStudentJobSpellweaver.clearColorFilter()
        //binding.addStudentJobSpellweaver.setBackgroundResource(R.drawable.round_job_image_grey)
        binding.addStudentJobSpellweaverText.setTextColor(Color.GRAY)
        binding.addStudentJobSpellweaverText.setTypeface(null, Typeface.NORMAL)
        binding.addStudentJobHacker.clearColorFilter()
        //binding.addStudentJobHacker.setBackgroundResource(R.drawable.round_job_image_grey)
        binding.addStudentJobHackerText.setTextColor(Color.GRAY)
        binding.addStudentJobHackerText.setTypeface(null, Typeface.NORMAL)
    }

    private fun onImageJobClick() {
        currentJob = "Barde"
        setJob(currentJob)

        binding.addStudentJobBard.setOnClickListener {  currentJob = "Barde"
            setJob(currentJob)}
        binding.addStudentJobShapeshifter.setOnClickListener {  currentJob = "Changelin"
            setJob(currentJob)}
        binding.addStudentJobEmpath.setOnClickListener {  currentJob = "Empathe"
            setJob(currentJob)}
        binding.addStudentJobRogue.setOnClickListener {  currentJob = "Filou"
            setJob(currentJob)}
        binding.addStudentJobBettor.setOnClickListener {  currentJob = "Parieur"
            setJob(currentJob)}
        binding.addStudentJobSpellweaver.setOnClickListener {  currentJob = "Tisse-sort"
            setJob(currentJob)}
        binding.addStudentJobHacker.setOnClickListener {  currentJob = "Hacker"
            setJob(currentJob)}
    }

    private fun setClassName(id: Int){
        databaseCallsVM.getClassInformation(id)?.observe(this){
            val className = it.name
            binding.addStudentCurrentClassResponse.text = className
        }
    }

    private fun resetFields(){
        binding.addStudentFirstnameEdit.editableText.clear()
        binding.addStudentLastnameEdit.editableText.clear()
        binding.addStudentGroupResult.text = "1"
        currentJob = "Barde"
        setColorJobToGrey()
        binding.addStudentJobBard.setBackgroundResource(R.drawable.round_job_image_yellow)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}