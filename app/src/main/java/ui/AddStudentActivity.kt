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
    private var currentJob = ""


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
                0, 45, group.toInt(), 1, 0, 0, 0
            )
            val newStudentSealedPowers = SealedPower(newStudent.id,
                power1 = 0,
                power1Actived = false,
                power2 = 0,
                power2Actived = false,
                power3 = 0,
                power3Actived = false,
                power4 = 0,
                power4Actived = false,
                power5 = 0,
                power5Actived = false,
                power6 = 0,
                power6Actived = false,
                powerToAssign = 0
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
            MathWorldApplication.context.getString(R.string.bard) -> { binding.addStudentJobBardText.setTextColor(Color.RED)
                binding.addStudentJobBardText.setTypeface(null, Typeface.BOLD)}
            MathWorldApplication.context.getString(R.string.shapeshifter) -> { binding.addStudentJobShapeshifterText.setTextColor(Color.RED)
                binding.addStudentJobShapeshifterText.setTypeface(null, Typeface.BOLD)}
            MathWorldApplication.context.getString(R.string.empath) -> { binding.addStudentJobEmpathText.setTextColor(Color.RED)
                binding.addStudentJobEmpathText.setTypeface(null, Typeface.BOLD)}
            MathWorldApplication.context.getString(R.string.rogue) -> { binding.addStudentJobRogueText.setTextColor(Color.RED)
                binding.addStudentJobRogueText.setTypeface(null, Typeface.BOLD)}
            MathWorldApplication.context.getString(R.string.bettor) -> { binding.addStudentJobBettorText.setTextColor(Color.RED)
                binding.addStudentJobBettorText.setTypeface(null, Typeface.BOLD)}
            MathWorldApplication.context.getString(R.string.spellweaver) -> {  binding.addStudentJobSpellweaverText.setTextColor(Color.RED)
                binding.addStudentJobSpellweaverText.setTypeface(null, Typeface.BOLD)}
            MathWorldApplication.context.getString(R.string.hacker) -> {binding.addStudentJobHackerText.setTextColor(Color.RED)
                binding.addStudentJobHackerText.setTypeface(null, Typeface.BOLD)}
        }
    }

    private fun setColorJobToGrey(){
        binding.addStudentJobBard.clearColorFilter()
        binding.addStudentJobBardText.setTextColor(Color.GRAY)
        binding.addStudentJobBardText.setTypeface(null, Typeface.NORMAL)
        binding.addStudentJobShapeshifter.clearColorFilter()
        binding.addStudentJobShapeshifterText.setTextColor(Color.GRAY)
        binding.addStudentJobShapeshifterText.setTypeface(null, Typeface.NORMAL)
        binding.addStudentJobEmpath.clearColorFilter()
        binding.addStudentJobEmpathText.setTextColor(Color.GRAY)
        binding.addStudentJobEmpathText.setTypeface(null, Typeface.NORMAL)
        binding.addStudentJobRogue.clearColorFilter()
        binding.addStudentJobRogueText.setTextColor(Color.GRAY)
        binding.addStudentJobRogueText.setTypeface(null, Typeface.NORMAL)
        binding.addStudentJobBettor.clearColorFilter()
        binding.addStudentJobBettorText.setTextColor(Color.GRAY)
        binding.addStudentJobBettorText.setTypeface(null, Typeface.NORMAL)
        binding.addStudentJobSpellweaver.clearColorFilter()
        binding.addStudentJobSpellweaverText.setTextColor(Color.GRAY)
        binding.addStudentJobSpellweaverText.setTypeface(null, Typeface.NORMAL)
        binding.addStudentJobHacker.clearColorFilter()
        binding.addStudentJobHackerText.setTextColor(Color.GRAY)
        binding.addStudentJobHackerText.setTypeface(null, Typeface.NORMAL)
    }

    private fun onImageJobClick() {
        currentJob = MathWorldApplication.context.getString(R.string.bard)
        setJob(currentJob)

        binding.addStudentJobBard.setOnClickListener {  currentJob = MathWorldApplication.context.getString(R.string.bard)
            setJob(currentJob)}
        binding.addStudentJobShapeshifter.setOnClickListener {  currentJob = MathWorldApplication.context.getString(R.string.shapeshifter)
            setJob(currentJob)}
        binding.addStudentJobEmpath.setOnClickListener {  currentJob = MathWorldApplication.context.getString(R.string.empath)
            setJob(currentJob)}
        binding.addStudentJobRogue.setOnClickListener {  currentJob = MathWorldApplication.context.getString(R.string.rogue)
            setJob(currentJob)}
        binding.addStudentJobBettor.setOnClickListener {  currentJob = MathWorldApplication.context.getString(R.string.bettor)
            setJob(currentJob)}
        binding.addStudentJobSpellweaver.setOnClickListener {  currentJob = MathWorldApplication.context.getString(R.string.spellweaver)
            setJob(currentJob)}
        binding.addStudentJobHacker.setOnClickListener {  currentJob = MathWorldApplication.context.getString(R.string.hacker)
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
        currentJob = R.string.bard.toString()
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