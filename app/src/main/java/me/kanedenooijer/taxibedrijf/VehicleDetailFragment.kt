package me.kanedenooijer.taxibedrijf

import android.os.Bundle
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

private const val ARG_VEHICLE = "vehicle"

class VehicleDetailFragment : Fragment(R.layout.fragment_vehicle_detail) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val vehicle = arguments?.getVehicle()

        view.findViewById<TextView>(R.id.detail_license_plate).text =
            vehicle?.displayLicensePlate.orEmpty()
        view.findViewById<TextView>(R.id.detail_vehicle_type).text =
            vehicle?.displayType ?: getString(R.string.unknown_type)
        view.findViewById<TextView>(R.id.detail_data).text = vehicle?.detailText().orEmpty()
    }

    private fun Vehicle.detailText(): String = listOf(
        getString(R.string.detail_brand) to merk,
        getString(R.string.detail_trade_name) to handelsbenaming,
        getString(R.string.detail_color) to eersteKleur,
        getString(R.string.detail_second_color) to tweedeKleur,
        getString(R.string.detail_body) to inrichting,
        getString(R.string.detail_seats) to aantalZitplaatsen,
        getString(R.string.detail_first_registration) to datumEersteToelating?.formatRdwDate(),
        getString(R.string.detail_apk_expiry) to vervaldatumApk?.formatRdwDate()
    ).joinToString(separator = "\n") { (label, value) ->
        getString(
            R.string.detail_row,
            label,
            value?.ifBlank { getString(R.string.missing_value) } ?: getString(R.string.missing_value)
        )
    }

    private fun String.formatRdwDate(): String {
        return if (length == 8) {
            "${substring(6, 8)}-${substring(4, 6)}-${substring(0, 4)}"
        } else {
            this
        }
    }

    companion object {
        fun newInstance(vehicle: Vehicle): VehicleDetailFragment {
            return VehicleDetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_VEHICLE, vehicle)
                }
            }
        }
    }
}

private fun Bundle.getVehicle(): Vehicle? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(ARG_VEHICLE, Vehicle::class.java)
    } else {
        @Suppress("DEPRECATION")
        getSerializable(ARG_VEHICLE) as? Vehicle
    }
}
