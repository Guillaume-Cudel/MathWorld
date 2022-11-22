package ui

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
import model.Student
import viewModel.DatabaseCallsViewModel

class AddStudentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStudentBinding
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
            databaseCallsVM.insertStudent(newStudent)
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
    }

    private fun setJob(job: String){
        setColorJobToGrey()
        when(job){
            "bard" -> { //binding.addStudentJobBard.setColorFilter(resources.getColor(R.color.orange))
                binding.addStudentJobBard.setBackgroundResource(R.drawable.round_job_image_yellow)}
            "shapeshifter" -> {  binding.addStudentJobShapeshifter.setBackgroundResource(R.drawable.round_job_image_yellow)}
            "empath" -> { binding.addStudentJobEmpath.setBackgroundResource(R.drawable.round_job_image_yellow)}
            "rogue" -> {binding.addStudentJobRogue.setBackgroundResource(R.drawable.round_job_image_yellow)}
            "bettor" -> { binding.addStudentJobBettor.setBackgroundResource(R.drawable.round_job_image_yellow)}
            "spellweaver" -> { binding.addStudentJobSpellweaver.setBackgroundResource(R.drawable.round_job_image_yellow)}
            "hacker" -> {binding.addStudentJobHacker.setBackgroundResource(R.drawable.round_job_image_yellow)}
        }
    }

    private fun setColorJobToGrey(){
        binding.addStudentJobBard.clearColorFilter()
        binding.addStudentJobBard.setBackgroundResource(R.drawable.round_job_image_grey)
        binding.addStudentJobShapeshifter.clearColorFilter()
        binding.addStudentJobShapeshifter.setBackgroundResource(R.drawable.round_job_image_grey)
        binding.addStudentJobEmpath.clearColorFilter()
        binding.addStudentJobEmpath.setBackgroundResource(R.drawable.round_job_image_grey)
        binding.addStudentJobRogue.clearColorFilter()
        binding.addStudentJobRogue.setBackgroundResource(R.drawable.round_job_image_grey)
        binding.addStudentJobBettor.clearColorFilter()
        binding.addStudentJobBettor.setBackgroundResource(R.drawable.round_job_image_grey)
        binding.addStudentJobSpellweaver.clearColorFilter()
        binding.addStudentJobSpellweaver.setBackgroundResource(R.drawable.round_job_image_grey)
        binding.addStudentJobHacker.clearColorFilter()
        binding.addStudentJobHacker.setBackgroundResource(R.drawable.round_job_image_grey)
    }

    private fun onImageJobClick() {
        binding.addStudentJobBard.setOnClickListener {  currentJob = "bard"
            setJob(currentJob)}
        binding.addStudentJobShapeshifter.setOnClickListener {  currentJob = "shapeshifter"
            setJob(currentJob)}
        binding.addStudentJobEmpath.setOnClickListener {  currentJob = "empath"
            setJob(currentJob)}
        binding.addStudentJobRogue.setOnClickListener {  currentJob = "rogue"
            setJob(currentJob)}
        binding.addStudentJobBettor.setOnClickListener {  currentJob = "bettor"
            setJob(currentJob)}
        binding.addStudentJobSpellweaver.setOnClickListener {  currentJob = "spellweaver"
            setJob(currentJob)}
        binding.addStudentJobHacker.setOnClickListener {  currentJob = "hacker"
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
        currentJob = "Bard"
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