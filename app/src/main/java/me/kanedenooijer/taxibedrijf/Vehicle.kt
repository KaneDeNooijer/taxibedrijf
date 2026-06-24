package me.kanedenooijer.taxibedrijf

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Vehicle(
    val kenteken: String? = null,
    val voertuigsoort: String? = null,
    val merk: String? = null,
    val handelsbenaming: String? = null,
    @SerializedName("eerste_kleur")
    val eersteKleur: String? = null,
    @SerializedName("tweede_kleur")
    val tweedeKleur: String? = null,
    val inrichting: String? = null,
    @SerializedName("aantal_zitplaatsen")
    val aantalZitplaatsen: String? = null,
    @SerializedName("datum_eerste_toelating")
    val datumEersteToelating: String? = null,
    @SerializedName("vervaldatum_apk")
    val vervaldatumApk: String? = null
) : Serializable {
    val displayLicensePlate: String
        get() = kenteken?.formatLicensePlate().orEmpty()

    val displayType: String
        get() = voertuigsoort?.replaceFirstChar { it.uppercase() }.orEmpty()
}

fun String.formatLicensePlate(): String {
    val normalized = filter { it.isLetterOrDigit() }.uppercase()
    return when {
        normalized.length == 6 -> "${normalized.substring(0, 2)}-${normalized.substring(2, 5)}-${normalized.substring(5)}"
        else -> normalized
    }
}

fun String.toRdwLicensePlate(): String = filter { it.isLetterOrDigit() }.uppercase()
