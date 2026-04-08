package me.kanedenooijer.taxibedrijf

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        findViewById<TextView>(R.id.license_plate_display).text =
            intent.getStringExtra("LICENSE_PLATE")
    }
}
