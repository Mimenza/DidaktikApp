package com.example.didaktikapp.activities

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.example.didaktikapp.R
import com.example.didaktikapp.activities.sites.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.didaktikapp.databinding.Activity5MapaBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener


class Activity5_Mapa : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: Activity5MapaBinding
    private lateinit var fusedLocation: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()

        binding = Activity5MapaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocation = LocationServices.getFusedLocationProviderClient(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        //Check for GPS permisions
        if (ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }

        mMap = googleMap

        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID)

        //Set min and max zoom
        mMap.setMaxZoomPreference(18F)
        mMap.setMinZoomPreference(14F)

        // Constrain the camera target to the Astigarraga bounds.
        val astigarragaBounds = LatLngBounds(
            LatLng(43.269625, -1.959532), LatLng(43.285576, -1.941156)
        )

        mMap.setLatLngBoundsForCameraTarget(astigarragaBounds)

        addMarkers()

        fusedLocation.lastLocation.addOnSuccessListener {
            if (it != null) {
                val ubicacion = LatLng(it.latitude, it.longitude)

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 16F))
            }
        }
    }

    // Add markers in Map
    private fun addMarkers() {
        val astigarragaMarkers = arrayListOf<LatLng>(
            LatLng(43.28124016860453, -1.9469706252948757),
            LatLng(43.28124645002352, -1.9487146619854678),
            LatLng(43.28009128981247, -1.9489651924963394),
            LatLng(43.27989827949647, -1.9495313417852063),
            LatLng(43.27808114110089, -1.9492564399148118),
            LatLng(43.27775413305006, -1.9488400717525682)
        )
        var astigarragaNames = arrayListOf<String>(
            "Sagardoetxea",
            "Murgia jauregia",
            "Foru plaza",
            "Astigar elkartea",
            "Ipintza sagardotegia",
            "Rezola sagardotegia"
        )

        for (i in 0..astigarragaMarkers.size - 1) {
            mMap.addMarker(
                MarkerOptions().position(astigarragaMarkers[i]).title(astigarragaNames[i])
            )
        }

        mMap.setOnInfoWindowClickListener(OnInfoWindowClickListener { marker ->
            val latLon = marker.position

            //Cycle through places array
            for (i in 0..astigarragaMarkers.size - 1) {
                if (latLon == astigarragaMarkers[i]) {

                    lateinit var intent: Intent
                    when (i) {
                        0 -> intent = Intent(this, Activity6_1_Sagardoetxea::class.java)
                        1 -> intent = Intent(this, Activity6_2_Murgia::class.java)
                        2 -> intent = Intent(this, Activity6_3_ForuPlaza::class.java)
                        3 -> intent = Intent(this, Activity6_4_AstigarElkartea::class.java)
                        4 -> intent = Intent(this, Activity6_5_IpintzaSagardotegia::class.java)
                        5 -> intent = Intent(this, Activity6_6_RezolaSagardotegia::class.java)
                    }
                    startActivity(intent)
                    this.overridePendingTransition(0, 0)
                }
            }
        })
    }
}
