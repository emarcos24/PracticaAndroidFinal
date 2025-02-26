package com.euroformac.practicafinal

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // Bloquear orientación en vertical

        enableEdgeToEdge()
        setContentView(R.layout.activity_liga)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_liga)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as? NavHostFragment
            ?: throw IllegalStateException("NavHostFragment not found")
        val controller = navController.navController

        bottomNavigationView.setupWithNavController(controller)

        bottomNavigationView.setOnItemSelectedListener { item ->
            if (controller.currentDestination?.id != item.itemId) {
                controller.navigate(item.itemId)
            }
            true
        }
    }
}
