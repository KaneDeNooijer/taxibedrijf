package me.kanedenooijer.taxibedrijf

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val licensePlateInput = findViewById<TextInputLayout>(R.id.license_plate_input)
        val licensePlateButton = findViewById<Button>(R.id.license_plate_button)
        val licensePlateDisplay = findViewById<TextView>(R.id.license_plate_display)

        licensePlateButton.setOnClickListener {
            val licensePlate = licensePlateInput.editText?.text.toString()
            licensePlateDisplay.text = licensePlate
        }
    }
}
