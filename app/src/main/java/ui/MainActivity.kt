package ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.guillaume.mathworld.R
import com.guillaume.mathworld.databinding.ActivityMainBinding
import di.MathWorldApplication
import di.MathWorldViewModelFactory
import model.RpgClass
import ui.fragments.ClassFragment
import ui.fragments.ClassManagementFragment
import ui.fragments.NotebookFragment
import ui.fragments.NumNinjaFragment
import viewModel.MainViewModel

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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
        //addClass()

        binding.navView.setNavigationItemSelectedListener(this)


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

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

    /*private fun addClass(){
        val addButton = findViewById<FloatingActionButton>(R.id.nav_header_fab_add)

        addButton.setOnClickListener {
            //todo change les boutons du navheader sur le fragment et passe les action des boutons sur le fragments
            createDialog()
        }
    }*/

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        // Handle navigation view item clicks here.
        //var fragment:Fragment = ImportFragment.newInstance()

        when (item.itemId) {
            R.id.header_item_add -> {
                createDialog()

            }
            R.id.header_item_remove -> {
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun createDialog(){
        val builder = AlertDialog.Builder(this).create()
        val view = layoutInflater.inflate(R.layout.dialog_add_class, null)
        val name = view.findViewById<EditText>(R.id.add_class_name_edit)
        val levelChoice = resources.getStringArray(R.array.Level)
        val arrayAdapter = ArrayAdapter(this, R.layout.level_list_item, levelChoice)
        val level: AutoCompleteTextView = view.findViewById(R.id.add_class_level_choice_text)
        level.setAdapter(arrayAdapter)
        val saveButton = view.findViewById<Button>(R.id.add_class_save)
        builder.setView(view)

        saveButton.setOnClickListener {
            if(name.editableText?.toString() != ""){
                val newClass = RpgClass(name.editableText.toString(), null, level.editableText.toString())
                mainVM.insertClass(newClass)
            } else {
                Toast.makeText(this, getString(R.string.enter_name), Toast.LENGTH_LONG).show()
            }
        }
        builder.setCanceledOnTouchOutside(true)
        builder.show()

    }







}