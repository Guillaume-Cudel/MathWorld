package ui

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.guillaume.mathworld.R
import com.guillaume.mathworld.databinding.ActivityEditStudentBinding
import di.MathWorldApplication
import di.MathWorldViewModelFactory
import model.Student
import services.UiConfigureImpl
import viewModel.DatabaseCallsViewModel
import viewModel.MainViewModel

class EditStudentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditStudentBinding
    private lateinit var mainVM: MainViewModel
    private val uiConfigure: UiConfigureImpl = UiConfigureImpl()
    private val databaseCallsVM: DatabaseCallsViewModel by viewModels {
        MathWorldViewModelFactory((application as MathWorldApplication).repository)
    }
    private var student: Student? = null
    private var classId: Int? = null
    private var currentJob: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureToolbar()
        mainVM = ViewModelProvider(this)[MainViewModel::class.java]

        val recoveStudentBundle = intent.extras
        student = recoveStudentBundle?.get("student") as Student
        classId = recoveStudentBundle.getInt("classId")

        if(student != null){
            setStudentData(student!!)
            currentJob = student!!.job
        }


        onImageJobClick()

        binding.editStudentSave.setOnClickListener {
            editStudent(classId!!, student!!)
        }

    }

    private fun configureToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setStudentData(student: Student){
        binding.editStudentFirstnameEdit.setText(student.firstName)
        binding.editStudentLastnameEdit.setText(student.lastName)
        setJob(student.job)
        binding.editStudentProgressbarExperience.progress = student.experience
        val studentExperience = "${student.experience}/${student.xpMax}"
        binding.editStudentProgressbarExperienceResult.text = studentExperience
        binding.editStudentLevelResult.text = student.level.toString()
        uiConfigure.setBelt(student.bestBelt, binding.editStudentBeltImage)
    }

    private fun setJob(job: String){
        setColorJobToGrey()
        when(job){
            MathWorldApplication.context.getString(R.string.bard) -> { binding.editStudentJobBardText.setTextColor(Color.RED)
                binding.editStudentJobBardText.setTypeface(null, Typeface.BOLD)}
            MathWorldApplication.context.getString(R.string.shapeshifter) -> { binding.editStudentJobShapeshifterText.setTextColor(Color.RED)
                binding.editStudentJobShapeshifterText.setTypeface(null, Typeface.BOLD)}
            MathWorldApplication.context.getString(R.string.empath) -> { binding.editStudentJobEmpathText.setTextColor(Color.RED)
                binding.editStudentJobEmpathText.setTypeface(null, Typeface.BOLD)}
            MathWorldApplication.context.getString(R.string.rogue) -> { binding.editStudentJobRogueText.setTextColor(Color.RED)
                binding.editStudentJobRogueText.setTypeface(null, Typeface.BOLD)}
            MathWorldApplication.context.getString(R.string.bettor) -> { binding.editStudentJobBettorText.setTextColor(Color.RED)
                binding.editStudentJobBettorText.setTypeface(null, Typeface.BOLD)}
            MathWorldApplication.context.getString(R.string.spellweaver) -> {  binding.editStudentJobSpellweaverText.setTextColor(Color.RED)
                binding.editStudentJobSpellweaverText.setTypeface(null, Typeface.BOLD)}
            MathWorldApplication.context.getString(R.string.hacker) -> {binding.editStudentJobHackerText.setTextColor(Color.RED)
                binding.editStudentJobHackerText.setTypeface(null, Typeface.BOLD)}
        }
    }

    private fun setColorJobToGrey(){
        binding.editStudentJobBard.clearColorFilter()
        binding.editStudentJobBardText.setTextColor(Color.GRAY)
        binding.editStudentJobBardText.setTypeface(null, Typeface.NORMAL)
        binding.editStudentJobShapeshifter.clearColorFilter()
        binding.editStudentJobShapeshifterText.setTextColor(Color.GRAY)
        binding.editStudentJobShapeshifterText.setTypeface(null, Typeface.NORMAL)
        binding.editStudentJobEmpath.clearColorFilter()
        binding.editStudentJobEmpathText.setTextColor(Color.GRAY)
        binding.editStudentJobEmpathText.setTypeface(null, Typeface.NORMAL)
        binding.editStudentJobRogue.clearColorFilter()
        binding.editStudentJobRogueText.setTextColor(Color.GRAY)
        binding.editStudentJobRogueText.setTypeface(null, Typeface.NORMAL)
        binding.editStudentJobBettor.clearColorFilter()
        binding.editStudentJobBettorText.setTextColor(Color.GRAY)
        binding.editStudentJobBettorText.setTypeface(null, Typeface.NORMAL)
        binding.editStudentJobSpellweaver.clearColorFilter()
        binding.editStudentJobSpellweaverText.setTextColor(Color.GRAY)
        binding.editStudentJobSpellweaverText.setTypeface(null, Typeface.NORMAL)
        binding.editStudentJobHacker.clearColorFilter()
        binding.editStudentJobHackerText.setTextColor(Color.GRAY)
        binding.editStudentJobHackerText.setTypeface(null, Typeface.NORMAL)
    }

    private fun onImageJobClick() {
        setJob(currentJob)

        binding.editStudentJobBard.setOnClickListener {  currentJob = MathWorldApplication.context.getString(R.string.bard)
            setJob(currentJob)}
        binding.editStudentJobShapeshifter.setOnClickListener {  currentJob = MathWorldApplication.context.getString(R.string.shapeshifter)
            setJob(currentJob)}
        binding.editStudentJobEmpath.setOnClickListener {  currentJob = MathWorldApplication.context.getString(R.string.empath)
            setJob(currentJob)}
        binding.editStudentJobRogue.setOnClickListener {  currentJob = MathWorldApplication.context.getString(R.string.rogue)
            setJob(currentJob)}
        binding.editStudentJobBettor.setOnClickListener {  currentJob = MathWorldApplication.context.getString(R.string.bettor)
            setJob(currentJob)}
        binding.editStudentJobSpellweaver.setOnClickListener {  currentJob = MathWorldApplication.context.getString(R.string.spellweaver)
            setJob(currentJob)}
        binding.editStudentJobHacker.setOnClickListener {  currentJob = MathWorldApplication.context.getString(R.string.hacker)
            setJob(currentJob)}
    }

    private fun setClassName(id: Int){
        databaseCallsVM.getClassInformation(id)?.observe(this){
            val className = it.name
            binding.editStudentCurrentClassResponse.text = className
        }
    }

    //todo modifie l activity pour modifier ce qui peut l etre
    private fun editStudent(classId: Int, student: Student){
        if(binding.editStudentFirstnameEdit.editableText.toString() != ""
            && binding.editStudentLastnameEdit.editableText.toString() != "") {
            val firstname = binding.editStudentFirstnameEdit.editableText.toString()
            val lastname = binding.editStudentLastnameEdit.editableText.toString()
            val job = currentJob



            //databaseCallsVM.insertStudent(newStudent)
            Toast.makeText(this, "$firstname $lastname" + getString(R.string.updated), Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, getString(R.string.fill_fields), Toast.LENGTH_LONG).show()
        }
    }

    //todo ajoute la fonctionnalite des boutons
}