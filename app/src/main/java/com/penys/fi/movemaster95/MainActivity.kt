package com.penys.fi.movemaster95

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val manager = supportFragmentManager

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message.text = "Home"
                val homeFragment = HomeFragment()
                val transaction = manager.beginTransaction()

                transaction.replace(R.id.fragment_container,homeFragment)
                transaction.addToBackStack(null)
                transaction.commit()

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_map -> {
                message.text = "Map"
                val mapFragment = MapFragment()
                val transaction = manager.beginTransaction()
<<<<<<< HEAD

=======
              
>>>>>>> 8f33bae62dc294f83143925b721a46e78fe8e030
                transaction.replace(R.id.fragment_container,mapFragment)
                transaction.addToBackStack(null)
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                message.text = "Profile"
                val statsFragment = StatsFragment()
                val transaction = manager.beginTransaction()

                transaction.replace(R.id.fragment_container,statsFragment)
                transaction.addToBackStack(null)
                transaction.commit()

                return@OnNavigationItemSelectedListener true


            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
