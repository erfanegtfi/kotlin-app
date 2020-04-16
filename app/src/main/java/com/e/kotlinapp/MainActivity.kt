package com.e.kotlinapp

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.e.kotlinapp.ui.category.CategoryViewModel
import com.e.kotlinapp.ui.base.BaseActivity

class MainActivity : BaseActivity<ViewDataBinding, CategoryViewModel>() {

    override  var layoutRes = R.layout.activity_main
    override  var viewModelClass= CategoryViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

//        viewModel.getCategoryList()
    }


}
