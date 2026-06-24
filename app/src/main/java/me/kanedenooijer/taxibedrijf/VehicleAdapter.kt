package me.kanedenooijer.taxibedrijf

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class VehicleAdapter(context: Context, vehicles: List<Vehicle>) :
    ArrayAdapter<Vehicle>(context, 0, vehicles) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item_vehicle, parent, false)
        val vehicle = getItem(position)

        view.findViewById<TextView>(R.id.license_plate_text).text =
            context.getString(R.string.vehicle_license_plate, vehicle?.displayLicensePlate.orEmpty())
        view.findViewById<TextView>(R.id.vehicle_type_text).text =
            context.getString(R.string.vehicle_type, vehicle?.displayType.orEmpty())

        return view
    }
}
