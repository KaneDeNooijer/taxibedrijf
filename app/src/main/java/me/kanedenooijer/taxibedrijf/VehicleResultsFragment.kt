package me.kanedenooijer.taxibedrijf

import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment

class VehicleResultsFragment : Fragment(R.layout.fragment_vehicle_results) {
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var statusText: TextView
    private lateinit var vehicleList: ListView
    private var vehicles: List<Vehicle> = emptyList()

    var onVehicleSelected: ((Vehicle) -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadingIndicator = view.findViewById(R.id.loading_indicator)
        statusText = view.findViewById(R.id.status_text)
        vehicleList = view.findViewById(R.id.vehicle_list)

        vehicleList.setOnItemClickListener { _, _, position, _ ->
            vehicles.getOrNull(position)?.let { onVehicleSelected?.invoke(it) }
        }

        showEmpty(getString(R.string.no_results_yet))
    }

    fun showLoading() {
        loadingIndicator.visibility = View.VISIBLE
        statusText.visibility = View.GONE
        vehicleList.visibility = View.GONE
    }

    fun showVehicles(newVehicles: List<Vehicle>) {
        vehicles = newVehicles
        loadingIndicator.visibility = View.GONE

        if (newVehicles.isEmpty()) {
            showEmpty(getString(R.string.no_vehicles_found))
            return
        }

        statusText.visibility = View.GONE
        vehicleList.adapter = VehicleAdapter(requireContext(), newVehicles)
        vehicleList.visibility = View.VISIBLE
    }

    fun showEmpty(message: String) {
        vehicles = emptyList()
        loadingIndicator.visibility = View.GONE
        vehicleList.visibility = View.GONE
        statusText.text = message
        statusText.visibility = View.VISIBLE
    }
}
