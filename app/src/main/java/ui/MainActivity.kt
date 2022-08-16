package ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationBarView
import com.guillaume.mathworld.R
import com.guillaume.mathworld.databinding.ActivityMainBinding
import dao.ClassDAO
import database.AppDatabase
import di.MathWorldApplication
import di.MathWorldViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ui.fragments.ClassManagementFragment
import ui.fragments.NotebookFragment
import ui.fragments.NumNinjaFragment
import viewModel.MainViewModel
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private val managementFragment = ClassManagementFragment()
    private val notebookFragment = NotebookFragment()
    private val numNinjaFragment = NumNinjaFragment()
    private val mainVM: MainViewModel by viewModels {
        MathWorldViewModelFactory((application as MathWorldApplication).repository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureNavigation()
        replaceFragment(managementFragment)

        val recyclerView = findViewById<RecyclerView>(R.id.nav_header_recycler_view)
        val adapter = ClassesListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        mainVM.allClasses.observe(this, Observer { classes ->
            classes?.let {
                adapter.submitList(it)
            }
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configureNavigation(){
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val bottomNavigation: NavigationBarView = findViewById(R.id.bottom_navigation)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_class_management -> replaceFragment(managementFragment)
                R.id.item_notebook ->  replaceFragment(notebookFragment)
                R.id.item_num_ninja -> replaceFragment(numNinjaFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }


}