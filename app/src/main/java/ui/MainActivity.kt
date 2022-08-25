package ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.guillaume.mathworld.R
import com.guillaume.mathworld.databinding.ActivityMainBinding
import androidx.appcompat.widget.Toolbar
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
    private var classNumber = 1

    private lateinit var mainVM: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainVM = ViewModelProvider(this)[MainViewModel::class.java]

        configureNavigation()
        replaceFragment(managementFragment)
        mainVM.setClass(classNumber)


        binding.navView.setNavigationItemSelectedListener(this)
        binding.navView.setCheckedItem(R.id.header_item_first)


    }

    private fun configureNavigation(){
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val bottomNavigation: NavigationBarView = findViewById(R.id.bottom_navigation)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        //var currentItem: MenuItem = item
        when (item.itemId) {
            R.id.header_item_first -> {
                classNumber = 1
            }
            R.id.header_item_second -> {
                classNumber = 2
            }
            R.id.header_item_third -> {
                classNumber = 3
            }
            R.id.header_item_fourth -> {
                classNumber = 4
            }
            R.id.header_item_fifth -> {
                classNumber = 5
            }
        }

        mainVM.setClass(classNumber)
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }



    private fun createDialog(item: Int, rpgClassId: Int){
        val navView: NavigationView = binding.navView
        val menu: Menu = navView.menu
        val menuItem: MenuItem = menu.findItem(item)


        val builder = AlertDialog.Builder(this).create()
        val view = layoutInflater.inflate(R.layout.dialog_add_class, null)
        val name = view.findViewById<EditText>(R.id.add_class_name_edit)
        val levelChoice = resources.getStringArray(R.array.Level)
        val arrayAdapter = ArrayAdapter(this, R.layout.level_list_item, levelChoice)
        val level: AutoCompleteTextView = view.findViewById(R.id.add_class_level_choice_text)
        level.setAdapter(arrayAdapter)
        val saveButton = view.findViewById<Button>(R.id.add_class_save)
        builder.setView(view)

        activateSaveButton(saveButton, level)
        saveButton.setOnClickListener {
            if(name.editableText?.toString() != ""){
                /*val newClass = RpgClass(name.editableText.toString(),  level.editableText.toString())
                mainVM.insertClass(newClass)*/
                menuItem.title = name.editableText.toString()
                builder.dismiss()
            } else {
                Toast.makeText(this, getString(R.string.enter_name), Toast.LENGTH_LONG).show()
            }
        }
        builder.setCanceledOnTouchOutside(true)
        builder.show()

    }

    private fun activateSaveButton(button: Button, level: AutoCompleteTextView){
        level.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                button.isEnabled = s?.length!! > 0
                button.backgroundTintList =
                    ContextCompat.getColorStateList(this@MainActivity, R.color.orange)
            }
        })

    }








}