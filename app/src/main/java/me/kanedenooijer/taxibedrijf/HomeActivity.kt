package me.kanedenooijer.taxibedrijf

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.net.toUri

class HomeActivity : AppCompatActivity() {
    private lateinit var licensePlateInput: EditText
    private lateinit var licensePlateDisplay: TextView
    private lateinit var resultsFragment: VehicleResultsFragment
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        licensePlateInput = findViewById(R.id.license_plate_input)
        licensePlateDisplay = findViewById(R.id.license_plate_display)
        val licensePlateButton = findViewById<Button>(R.id.license_plate_button)

        resultsFragment = VehicleResultsFragment().apply {
            onVehicleSelected = { vehicle -> openVehicleDetail(vehicle) }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.results_fragment_container, resultsFragment)
            .commitNow()

        licensePlateButton.setOnClickListener {
            val query = licensePlateInput.text.toString()
            licensePlateDisplay.text = if (query.isBlank()) {
                getString(R.string.results_title)
            } else {
                getString(R.string.results_for, query.trim().uppercase())
            }
            loadVehicles(query)
        }

        findViewById<BottomNavigationView>(R.id.bottom_navigation).apply {
            selectedItemId = R.id.navigation_home
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_home -> true
                    R.id.navigation_details -> {
                        startActivity(Intent(this@HomeActivity, ResultActivity::class.java))
                        true
                    }
                    else -> false
                }
            }
        }

        findViewById<android.view.View>(R.id.results_fragment_container).post {
            loadVehicles()
        }
    }

    private fun loadVehicles(query: String = "") {
        resultsFragment.showLoading()
        val searchQuery = query.trim().uppercase()

        val request = JsonArrayRequest(
            Request.Method.GET,
            buildRdwUrl(),
            null,
            { response ->
                println("RDW response: $response")
                val vehicles = gson.fromJson<List<Vehicle>>(
                    response.toString(),
                    object : TypeToken<List<Vehicle>>() {}.type
                )
                resultsFragment.showVehicles(vehicles.matchingLicensePlate(searchQuery))
            },
            { error ->
                resultsFragment.showEmpty(
                    getString(
                        R.string.vehicle_data_error,
                        error.localizedMessage ?: getString(R.string.unknown_error)
                    )
                )
            }
        )

        Volley.newRequestQueue(this).add(request)
    }

    private fun buildRdwUrl(): String {
        return "https://opendata.rdw.nl/resource/m9d7-ebf2.json".toUri().buildUpon()
            .appendQueryParameter("\$order", "kenteken")
            .build()
            .toString()
    }

    private fun List<Vehicle>.matchingLicensePlate(searchQuery: String): List<Vehicle> {
        return if (searchQuery.isBlank()) {
            this
        } else {
            filter { vehicle ->
                vehicle.displayLicensePlate.uppercase().contains(searchQuery)
            }
        }
    }

    private fun openVehicleDetail(vehicle: Vehicle) {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(ResultActivity.EXTRA_LICENSE_PLATE, vehicle.displayLicensePlate)
            putExtra(ResultActivity.EXTRA_VEHICLE, vehicle)
        }
        startActivity(intent)
    }
}
