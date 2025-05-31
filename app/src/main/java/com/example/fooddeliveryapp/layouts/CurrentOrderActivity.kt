package com.example.fooddeliveryapp.layouts

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.fooddeliveryapp.databinding.ActivityCurrentOrderBinding
import com.example.fooddeliveryapp.network.NominatimRetrofitClient
import com.example.fooddeliveryapp.network.NominatimSearchResult
import com.example.fooddeliveryapp.network.OsrmRetrofitClient
import com.example.fooddeliveryapp.network.OsrmRouteResponse
import com.google.android.gms.location.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import kotlin.math.round

class CurrentOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCurrentOrderBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mapView: MapView

    private var currentLatitude: Double? = null
    private var currentLongitude: Double? = null

    companion object {
        private const val REQUEST_LOCATION_PERM = 2001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(
            applicationContext,
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )
        binding = ActivityCurrentOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mapView = binding.osmMapview
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_LOCATION_PERM
            )
        } else {
            getDeviceLocation()
        }

        binding.btnFindDistance.setOnClickListener {
            val restaurantNameInput = binding.etRestaurantName.text.toString().trim()
            if (restaurantNameInput.isEmpty()) {
                Toast.makeText(this, "Introduceti numele restaurantului", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (currentLatitude == null || currentLongitude == null) {
                getDeviceLocation {
                    if (currentLatitude != null && currentLongitude != null) {
                        searchRestaurantAndShowDistance(restaurantNameInput)
                    } else {
                        Toast.makeText(this, "Nu am putut obtine locatia reala", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                searchRestaurantAndShowDistance(restaurantNameInput)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERM) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getDeviceLocation()
            } else {
                Toast.makeText(this, "Permisiunea de locatie nu a fost acordata", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation(onLocationAcquired: (() -> Unit)? = null) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    currentLatitude = location.latitude
                    currentLongitude = location.longitude
                    onLocationAcquired?.invoke()
                } else {
                    val locationRequest =
                        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 0)
                            .setMinUpdateIntervalMillis(1000)
                            .build()
                    val locationCallback = object : LocationCallback() {
                        override fun onLocationResult(result: LocationResult) {
                            fusedLocationClient.removeLocationUpdates(this)
                            val loc = result.lastLocation
                            currentLatitude = loc?.latitude
                            currentLongitude = loc?.longitude
                            onLocationAcquired?.invoke()
                        }
                    }
                    fusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.getMainLooper()
                    )
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Eroare la obtinerea locatiei: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun searchRestaurantAndShowDistance(restaurantName: String) {
        //coordonate pentru testare emulator
        val originLat = 44.4325
        val originLon = 26.1039
//        val originLat = currentLatitude!!
//        val originLon = currentLongitude!!

        binding.progressBar.visibility = android.view.View.VISIBLE
        binding.tvRestaurantResultName.text = "Nume restaurant: –"
        binding.tvRestaurantResultAddress.text = "Adresa: –"
        binding.tvDistance.text = "Distanta: –"
        binding.tvDuration.text = "Durata estimata: –"

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val query = "$restaurantName, Bucuresti"
                val nominatimResults: List<NominatimSearchResult> =
                    NominatimRetrofitClient.apiService.searchPlace(query)

                if (nominatimResults.isNotEmpty()) {
                    val res = nominatimResults[0]
                    val destLat = res.lat.toDoubleOrNull()
                    val destLon = res.lon.toDoubleOrNull()
                    val displayAddress = res.displayName

                    if (destLat != null && destLon != null) {
                        val coords = "$originLon,$originLat;$destLon,$destLat"
                        val osrmResponse: OsrmRouteResponse =
                            OsrmRetrofitClient.apiService.getRoute(coords)

                        if (osrmResponse.routes.isNotEmpty() &&
                            osrmResponse.routes[0].legs.isNotEmpty()
                        ) {
                            val leg = osrmResponse.routes[0].legs[0]
                            val distMeters = leg.distance
                            val durSec = leg.duration

                            val distKm = round(distMeters / 1000 * 100) / 100
                            val durMin = round(durSec / 60 * 100) / 100

                            withContext(Dispatchers.Main) {
                                binding.progressBar.visibility = android.view.View.GONE
                                binding.tvRestaurantResultName.text =
                                    "Nume restaurant: $restaurantName"
                                binding.tvRestaurantResultAddress.text =
                                    "Adresa: $displayAddress"
                                binding.tvDistance.text = "Distanta: ${distKm} km"
                                binding.tvDuration.text = "Durata estimata: ${durMin} min"

                                mapView.overlays.clear()
                                val marker = Marker(mapView)
                                marker.position = GeoPoint(destLat, destLon)
                                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                mapView.overlays.add(marker)
                                mapView.controller.setZoom(15.0)
                                mapView.controller.setCenter(GeoPoint(destLat, destLon))
                                mapView.invalidate()
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                binding.progressBar.visibility = android.view.View.GONE
                                Toast.makeText(
                                    this@CurrentOrderActivity,
                                    "OSRM nu a returnat o ruta valida",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            binding.progressBar.visibility = android.view.View.GONE
                            Toast.makeText(
                                this@CurrentOrderActivity,
                                "Coordonate invalide de la Nominatim",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        binding.progressBar.visibility = android.view.View.GONE
                        Toast.makeText(
                            this@CurrentOrderActivity,
                            "Nu am gasit restaurantul \"$restaurantName\"",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = android.view.View.GONE
                    Toast.makeText(
                        this@CurrentOrderActivity,
                        "Eroare la apeluri: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}