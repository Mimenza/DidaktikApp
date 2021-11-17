package com.example.didaktikapp.activities

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.didaktikapp.R
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
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.BitmapDescriptorFactory


class Activity5_Mapa : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: Activity5MapaBinding
    private lateinit var fusedLocation: FusedLocationProviderClient
    private var myCurrentPosition: LatLng = LatLng(45.0, 123.0)
    private var lastUserPoint: Int = 2

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

        if (DbHandler.getUser() != null && DbHandler.getUser()!!.ultima_puntuacion != null) {
            lastUserPoint = DbHandler.getUser()!!.ultima_puntuacion!!
        }
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

        var myCircle: Circle = mMap.addCircle(
            CircleOptions()
            .center(LatLng(43.285576, -1.941156))
            .radius(500.0)
            .strokeColor(getResources().getColor(R.color.white))
            .strokeWidth(2f)
            .fillColor(0x70ff0000))

        mMap.setOnMyLocationChangeListener {
            myCircle.setCenter(LatLng(it.latitude, it.longitude))
            myCurrentPosition = LatLng(it.latitude, it.longitude)
        }

        fusedLocation.lastLocation.addOnSuccessListener {
            if (it != null) {
                val ubicacion = LatLng(it.latitude, it.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 16F))
            }
        }
    }

    // Add markers in Map
    val markerLIst = arrayListOf<LatLng>(
        LatLng(43.28124016860453, -1.9469706252948757),
        LatLng(43.28124645002352, -1.9487146619854678),
        LatLng(43.28009128981247, -1.9489651924963394),
        LatLng(43.2801245169072, -1.94919315370149280),
        LatLng(43.27989827949647, -1.9495313417852063),
        LatLng(43.27808114110089, -1.9492564399148118),
        LatLng(43.27775413305006, -1.9488400717525682),
    )

    var markerNames = arrayListOf<String>(
        "Sagardoetxea",
        "Murgia jauregia",
        "Foru plaza 1",
        "Foru plaza 2",
        "Astigar elkartea",
        "Ipintza sagardotegia",
        "Rezola sagardotegia"
    )

    //var lastUserPoint: Int = 0
    //lastUserPoint = DbHandler.getUser()!!.ultima_puntuacion!!

    //var lastUserPoint: Int = if (DbHandler.getUser()!!.ultima_puntuacion != null) DbHandler.getUser()!!.ultima_puntuacion!! else 0

    private fun addMarkers() {
        var astigarragaMarkers: MutableList<LatLng> = ArrayList()
        var astigarragaNames: MutableList<String> = ArrayList()

        //for (i in 0..lastUserPoint) {
        for (i in 0..markerLIst.size-1) {
            astigarragaMarkers.add(markerLIst[i])
            astigarragaNames.add(markerNames[i])
        }


        for (i in 0..astigarragaMarkers.size - 1) {
            val markerPointIcon = mMap.addMarker(
                MarkerOptions()
                    .position(astigarragaMarkers[i])
                    .title(astigarragaNames[i])
                    .icon(
                        BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_RED)
                    )
            )

            if (markerPointIcon != null) {
                if (i < lastUserPoint) {
                    markerPointIcon.setIcon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                } else if (i == lastUserPoint) {
                    markerPointIcon.setIcon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                }
            }
        }

        mMap.setOnInfoWindowClickListener(OnInfoWindowClickListener { marker ->
            val latLon = marker.position
            var numero:Int=0

            //Cycle through places array
            for (i in 0..astigarragaMarkers.size - 1) {
                if (latLon == astigarragaMarkers[i]) {
                    var distanceToPoint = getDistBetweenPoints(myCurrentPosition, astigarragaMarkers[i])
                    if (distanceToPoint <= 500) {
                        if (i <= lastUserPoint) {
                           var intent:Intent =  Intent(this, Activity6_Site::class.java)
                            when (i) {
                                0 -> numero =0
                                1 -> numero =1
                                2 -> numero =2
                                3 -> numero =3
                                4 -> numero =4
                                5 -> numero =5
                                6 -> numero =6
                            }

                            intent.putExtra("numero",numero)

                            startActivity(intent)
                            this.overridePendingTransition(0, 0)
                        } else {
                            Toast.makeText(this, "Primero debes superar el nivel naranja", Toast.LENGTH_SHORT).show()
                        }


                        break

                    } else {
                        Toast.makeText(this, "No estas suficientemente cerca del punto de juego", Toast.LENGTH_SHORT).show()
                        break
                    }
                }
            }

        })
    }

    fun getDistBetweenPoints(localPoint: LatLng, gamePoint: LatLng): Int {
        val theta: Double = localPoint.longitude - gamePoint.longitude
        var dist = (Math.sin(deg2rad(localPoint.latitude))
                * Math.sin(deg2rad(gamePoint.latitude))
                + (Math.cos(deg2rad(localPoint.latitude))
                * Math.cos(deg2rad(gamePoint.latitude))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        var kms: Double = dist / 0.62137
        var meters: Double = kms * 1000
        return meters.toInt()
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

}
