package ui

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
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

        if (student != null) {
            setStudentData(student!!)
            currentJob = student!!.job
            setClassName(classId!!)
        }

        onImageJobClick()
        configureButtons()
    }

    private fun configureButtons() {
        binding.editStudentExperienceButtonOne.setOnClickListener {
            dicreaseExperience(1, student!!)
        }
        binding.editStudentExperienceButtonFive.setOnClickListener {
            dicreaseExperience(5, student!!)
        }
        binding.editStudentLevelButtonPlus.setOnClickListener {
            editLevel(1, student!!)
        }
        binding.editStudentLevelButtonMinus.setOnClickListener {
            editLevel(-1, student!!)
        }
        binding.editStudentBeltPlus.setOnClickListener {
            editBelt(1, student!!)
        }
        binding.editStudentBeltMinus.setOnClickListener {
            editBelt(-1, student!!)
        }
        binding.editStudentSave.setOnClickListener {
            saveEditingStudent()
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

    private fun setStudentData(student: Student) {
        binding.editStudentFirstnameEdit.setText(student.firstName)
        binding.editStudentLastnameEdit.setText(student.lastName)
        setJob(student.job)
        binding.editStudentProgressbarExperience.progress = student.experience
        val studentExperience = "${student.experience}/${student.xpMax}"
        binding.editStudentProgressbarExperienceResult.text = studentExperience
        binding.editStudentLevelResult.text = student.level.toString()
        uiConfigure.setBelt(student.bestBelt, binding.editStudentBeltImage)
    }

    private fun setJob(job: String) {
        setColorJobToGrey()
        when (job) {
            MathWorldApplication.context.getString(R.string.bard) -> {
                binding.editStudentJobBardText.setTextColor(Color.RED)
                binding.editStudentJobBardText.setTypeface(null, Typeface.BOLD)
            }
            MathWorldApplication.context.getString(R.string.shapeshifter) -> {
                binding.editStudentJobShapeshifterText.setTextColor(Color.RED)
                binding.editStudentJobShapeshifterText.setTypeface(null, Typeface.BOLD)
            }
            MathWorldApplication.context.getString(R.string.empath) -> {
                binding.editStudentJobEmpathText.setTextColor(Color.RED)
                binding.editStudentJobEmpathText.setTypeface(null, Typeface.BOLD)
            }
            MathWorldApplication.context.getString(R.string.rogue) -> {
                binding.editStudentJobRogueText.setTextColor(Color.RED)
                binding.editStudentJobRogueText.setTypeface(null, Typeface.BOLD)
            }
            MathWorldApplication.context.getString(R.string.bettor) -> {
                binding.editStudentJobBettorText.setTextColor(Color.RED)
                binding.editStudentJobBettorText.setTypeface(null, Typeface.BOLD)
            }
            MathWorldApplication.context.getString(R.string.spellweaver) -> {
                binding.editStudentJobSpellweaverText.setTextColor(Color.RED)
                binding.editStudentJobSpellweaverText.setTypeface(null, Typeface.BOLD)
            }
            MathWorldApplication.context.getString(R.string.hacker) -> {
                binding.editStudentJobHackerText.setTextColor(Color.RED)
                binding.editStudentJobHackerText.setTypeface(null, Typeface.BOLD)
            }
        }
    }

    private fun setColorJobToGrey() {
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

        binding.editStudentJobBard.setOnClickListener {
            currentJob = MathWorldApplication.context.getString(R.string.bard)
            setJob(currentJob)
        }
        binding.editStudentJobShapeshifter.setOnClickListener {
            currentJob = MathWorldApplication.context.getString(R.string.shapeshifter)
            setJob(currentJob)
        }
        binding.editStudentJobEmpath.setOnClickListener {
            currentJob = MathWorldApplication.context.getString(R.string.empath)
            setJob(currentJob)
        }
        binding.editStudentJobRogue.setOnClickListener {
            currentJob = MathWorldApplication.context.getString(R.string.rogue)
            setJob(currentJob)
        }
        binding.editStudentJobBettor.setOnClickListener {
            currentJob = MathWorldApplication.context.getString(R.string.bettor)
            setJob(currentJob)
        }
        binding.editStudentJobSpellweaver.setOnClickListener {
            currentJob = MathWorldApplication.context.getString(R.string.spellweaver)
            setJob(currentJob)
        }
        binding.editStudentJobHacker.setOnClickListener {
            currentJob = MathWorldApplication.context.getString(R.string.hacker)
            setJob(currentJob)
        }
    }

    private fun setClassName(id: Int) {
        databaseCallsVM.getClassInformation(id)?.observe(this) {
            val className = it.name
            binding.editStudentCurrentClassResponse.text = className
        }
    }

    private fun saveEditingStudent() {
        if (binding.editStudentFirstnameEdit.editableText.toString() != ""
            && binding.editStudentLastnameEdit.editableText.toString() != ""
        ) {
            val firstname = binding.editStudentFirstnameEdit.editableText.toString()
            val lastname = binding.editStudentLastnameEdit.editableText.toString()
            //currentJob
            if (student!!.firstName != firstname) student!!.firstName = firstname
            if (student!!.lastName != lastname) student!!.lastName = lastname
            if (student!!.job != currentJob) student!!.job = currentJob


            databaseCallsVM.updateStudent(student!!)
            Toast.makeText(
                this,
                "$firstname $lastname " + getString(R.string.updated),
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(this, getString(R.string.fill_fields), Toast.LENGTH_LONG).show()
        }

        val intent = Intent()
        intent.putExtra("student", student)
        intent.putExtra("classId", classId)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun editBelt(number: Int, student: Student) {
        var currentBelt = student.bestBelt

        when (number) {
            1 -> {
                currentBelt++
                student.bestBelt = currentBelt
            }
            -1 -> {
                if (currentBelt > 1) {
                    currentBelt--
                    student.bestBelt = currentBelt
                } else Toast.makeText(this, "Ceinture minimale atteinte !", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        databaseCallsVM.updateStudent(student)
        setStudentData(student)
    }

    private fun dicreaseExperience(xpToDecrease: Int, student: Student) {
        var newExperience = student.experience
        if (newExperience > 0) {
            newExperience = newExperience.minus(xpToDecrease)
            student.experience = newExperience
            databaseCallsVM.updateStudent(student)
            setStudentData(student)
        } else Toast.makeText(this, "Il peut pas sous-pex gros !", Toast.LENGTH_SHORT).show()
    }

    private fun editLevel(editingLevel: Int, student: Student) {
        var newLevel = student.level
        var newXpMax = student.xpMax

        if (editingLevel > 0) {
            newLevel = newLevel.plus(1)
            newXpMax = newXpMax.plus(5)
            student.level = newLevel
            student.xpMax = newXpMax
        } else {
            if (newLevel > 1) {
                newLevel = newLevel.minus(1)
                newXpMax = newXpMax.minus(5)
                student.level = newLevel
                student.xpMax = newXpMax
            } else Toast.makeText(this, "Niveau minimum atteint !", Toast.LENGTH_LONG).show()
        }
        databaseCallsVM.updateStudent(student)
        setStudentData(student)
    }
}