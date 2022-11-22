package ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.guillaume.mathworld.R
import com.guillaume.mathworld.databinding.ActivityStudentDetailsBinding
import di.MathWorldApplication
import di.MathWorldViewModelFactory
import model.JobWithPower
import model.Student
import services.UiConfigure
import services.UiConfigureImpl
import viewModel.DatabaseCallsViewModel

class StudentDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentDetailsBinding
    private val uiConfigure: UiConfigure = UiConfigureImpl()
    private var student: Student? = null
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

        databaseCallsVM.getPowersByJob(student!!.job)?.observe(this, Observer {
            //val jobPowersList = it
            displayData(it)
        })

        //displayData()

    }

    private fun configureToolbar(){
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

    private fun displayData(jobPowersList: List<JobWithPower>){
        uiConfigure.setBelt(student!!.belt, binding.studentDetailBeltImage)
        uiConfigure.displayExperience(student!!.experience, student!!.xpMax, binding.studentDetailExperienceResult)
        binding.studentDetailExperienceBar.progress = student!!.experience
        binding.studentDetailExperienceBar.max = student!!.xpMax
        uiConfigure.displayLifeNumber(student!!.pointOfLife, binding.studentDetailLifeResult)
        binding.studentDetailLifeBar.progress = student!!.pointOfLife
        binding.studentDetailLifeBar.max = 3
        binding.studentDetailLevelResponse.text = student!!.level.toString()
        binding.studentDetailJob.text = student!!.job
        binding.studentDetailName.text = student!!.firstName

        jobPowersList.let {
           //todo affiche le nom du pouvoir s'il est actif
        }
    }

    // todo Set job image
}