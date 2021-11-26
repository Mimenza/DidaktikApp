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
import com.example.didaktikapp.databinding.Activity5MapaBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener
import com.google.android.gms.maps.model.*


class Activity5_Mapa : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: Activity5MapaBinding
    private lateinit var fusedLocation: FusedLocationProviderClient
    private var myCurrentPosition: LatLng = LatLng(45.0, 123.0)
    private var lastUserPoint: Int = 7
    private var minimumRadius: Int = 50

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

        if (DbHandler.getUser() != null) {
            if (DbHandler.getUser()!!.ultima_puntuacion != null) {
                lastUserPoint = DbHandler.getUser()!!.ultima_puntuacion!!
            }
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
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }

        mMap = googleMap
        mMap.clear()

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

        //Añadir Markers junto a sus nombres
        addMarkers()

        //Añadir Area para mostrar al usuario si algun punto esta cerca de su radio o no
        var myCircle: Circle = mMap.addCircle(
            CircleOptions()
            .center(LatLng(43.285576, -1.941156))
            .radius(minimumRadius.toDouble())
            .strokeColor(getResources().getColor(R.color.white))
            .strokeWidth(2f)
            .fillColor(0x70ff0000))

        //Variable para comprobar si la primera animacion se ha realiado correctamente
        var firstFocusAnimation = false
        mMap.setOnMyLocationChangeListener {
            //Metodos para establecer la nueva posicion del radio que rodea la ubicacion del usuario
            myCircle.setCenter(LatLng(it.latitude, it.longitude))
            myCurrentPosition = LatLng(it.latitude, it.longitude)
            if (!firstFocusAnimation) {
                //Condicion para focalizar al usuario por primera vez
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 16F))
                firstFocusAnimation = true
            }
        }
    }

    val placesList = arrayListOf<ArrayList<Any>>(
        arrayListOf("Sagardoetxea",LatLng(43.28124016860453, -1.9469706252948757)),
        arrayListOf("Murgia jauregia",LatLng(43.28124645002352, -1.9487146619854678)),
        arrayListOf("Foru plaza 1",LatLng(43.28009128981247, -1.9489651924963394)),
        arrayListOf("Foru plaza 2",LatLng(43.2801245169072, -1.94919315370149280)),
        arrayListOf("Astigar elkartea",LatLng(43.27989827949647, -1.9495313417852063)),
        arrayListOf("Ipintza sagardotegia",LatLng(43.27808114110089, -1.9492564399148118)),
        arrayListOf("Rezola sagardotegia",LatLng(43.27775413305006, -1.9488400717525682)),
    )

    private fun addMarkers() {
        for ((i,item) in placesList.withIndex()) {
            // We create the Marker and their options
            val markerPointIcon = mMap.addMarker(
                MarkerOptions()
                    .position(item[1] as LatLng)
                    .title(item[0] as String)
                    .icon(
                        BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_RED)
                    )
            )

            //We check the kind of user and, in case that is NOT ADMIN
            if (markerPointIcon != null) {
                if (DbHandler.getAdmin()) {
                    markerPointIcon.setIcon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                } else {
                    if (i < lastUserPoint) {
                        markerPointIcon.setIcon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    } else if (i == lastUserPoint) {
                        markerPointIcon.setIcon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    }
                }
            }
        }

        mMap.setOnInfoWindowClickListener(OnInfoWindowClickListener { marker ->
            val latLon = marker.position

            fun irAPunto(puntoSeleccionado: Int) {
                val intent:Intent =  Intent(this, Activity6_Site::class.java)
                intent.putExtra("numero",puntoSeleccionado)
                startActivity(intent)
                this.overridePendingTransition(0, 0)
            }

            for ((i,item) in placesList.withIndex()) {
                if (item[1] as LatLng == latLon) {
                    var distanceToPoint = getDistBetweenPoints(myCurrentPosition, latLon)
                    if (DbHandler.getAdmin()) {
                        irAPunto(i)
                    } else {
                        if (distanceToPoint <= minimumRadius) {
                            if (i <= lastUserPoint) {
                                irAPunto(i)
                            } else {
                                Toast.makeText(this, "Primero debes superar el nivel naranja", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this, "No estas suficientemente cerca del punto de juego", Toast.LENGTH_SHORT).show()
                        }
                    }
                    break
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
