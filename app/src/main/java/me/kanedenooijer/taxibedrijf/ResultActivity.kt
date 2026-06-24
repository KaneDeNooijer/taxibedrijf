package me.kanedenooijer.taxibedrijf

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val vehicle = intent.getVehicleExtra()
        val fragment = if (vehicle == null) {
            EmptyDetailFragment()
        } else {
            VehicleDetailFragment.newInstance(vehicle)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.detail_fragment_container, fragment)
            .commit()

        findViewById<BottomNavigationView>(R.id.bottom_navigation).apply {
            selectedItemId = R.id.navigation_details
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_home -> {
                        startActivity(Intent(this@ResultActivity, HomeActivity::class.java))
                        true
                    }
                    R.id.navigation_details -> true
                    else -> false
                }
            }
        }
    }

    companion object {
        const val EXTRA_LICENSE_PLATE = "LICENSE_PLATE"
        const val EXTRA_VEHICLE = "VEHICLE"
    }
}

private fun Intent.getVehicleExtra(): Vehicle? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializableExtra(ResultActivity.EXTRA_VEHICLE, Vehicle::class.java)
    } else {
        @Suppress("DEPRECATION")
        getSerializableExtra(ResultActivity.EXTRA_VEHICLE) as? Vehicle
    }
}
