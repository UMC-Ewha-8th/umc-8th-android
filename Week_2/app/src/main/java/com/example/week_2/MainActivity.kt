package com.example.week_2

import androidx.fragment.app.Fragment
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        replaceFragment(HomeFragment(), false)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_category -> replaceFragment(CategoryFragment(), true)
                R.id.nav_snap -> replaceFragment(SnapFragment(), true)
                R.id.nav_home -> replaceFragment(HomeFragment(), true)
                R.id.nav_like -> replaceFragment(LikeFragment(), true)
                R.id.nav_my -> replaceFragment(MyFragment(), true)
            }
            true
        }
    }
    private fun replaceFragment(fragmentL:Fragment, addToBackStack:Boolean){
        val transaction=supportFragmentManager.beginTransaction()

        transaction.setCustomAnimations(
            android.R.anim.slide_in_left,
            android.R.anim.slide_out_right
        )

        transaction.replace(R.id.fragment_container, fragmentL)

        if(addToBackStack){
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

}