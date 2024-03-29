package com.example.didaktikapp.activities

import `in`.codeshuffle.typewriterview.TypeWriterView
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.didaktikapp.R
import com.example.didaktikapp.databinding.Activity5MapaBinding
import com.example.didaktikapp.fragments.Fragment4_ajustes
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity1_principal.*
import kotlinx.android.synthetic.main.activity4_bienvenida.*
import kotlinx.android.synthetic.main.activity5_mapa.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


class Activity5_Mapa : AppCompatActivity(), OnMapReadyCallback, DbHandler.QueryResponseDone {

    companion object {
//        lateinit var instancia: Activity5_Mapa
        var instancia: Activity5_Mapa? = null

    }

    private val thisActivity: Activity = this
    private lateinit var mMap: GoogleMap
    private lateinit var binding: Activity5MapaBinding
    private lateinit var fusedLocation: FusedLocationProviderClient
    private var markerPointList = arrayListOf<Marker>()
    //private var markerPointIcon: Marker? = null
    private var myCurrentPosition: LatLng = LatLng(45.0, 123.0)
    private var lastUserPoint: Int = 0
    private var minimumRadius: Int = 50
    private lateinit var audio: MediaPlayer
    private var fragment :Fragment? = null
    private var ajustesShowing: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instancia = this
        supportActionBar?.hide()
        binding = Activity5MapaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgv5Manzanatutorial.visibility = GONE
        binding.imgv5ManzanatutorialAnimado.visibility = GONE
        binding.imgv5Bocadillo.visibility = GONE
        binding.txtv5Presentacionmapa.visibility = GONE

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocation = LocationServices.getFusedLocationProviderClient(this)


         if (DbHandler.getUser() != null) {
             if (DbHandler.getUser()!!.ultima_puntuacion != null) {
                 lastUserPoint = DbHandler.getUser()!!.ultima_puntuacion!!
             }
             /*
             if(DbHandler.getUser()!!.nombre!=null){
                 userName=DbHandler.getUser()!!.nombre!!
             }
             if(DbHandler.getUser()!!.puntuacion!=null){
                 puntuacion=DbHandler.getUser()!!.puntuacion!!
             }

              */
         }

        //Metemos los datos del usuario en el mapa

      //  txtv_5nombre.text=userName
       // txtv_5puntuacion.text= puntuacion.toString()


        //Typewriter mapa tutorial
        //if (Utils.getUserPreferences(thisActivity,"mapTutorial","finalizado") == null) {
        if (DbHandler.getTutorialFinalizado() == 0) {
            Handler(Looper.getMainLooper()).postDelayed({
                typewriter()
            }, 2000)
            //Typewriter mapa tutorial fin
            audioSound()
        }

        //Ajustes desde el mapa
        binding.btn5Ajustes.setOnClickListener{
            //Push del fragment al activity mapa
            showAjustes()
            //Ocultamos el mapa
            ocultarMapa()
        }
    }

    private fun showAjustes(){
        if (ajustesShowing) {
            return
        }
        ajustesShowing = true
        fragment = Fragment4_ajustes()
        supportFragmentManager.beginTransaction().add(R.id.framelayoutajustes, fragment!!).commit()
        window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
    }

    override fun onBackPressed() {
        if (fragment != null) {
            cerrarAjustes()
        } else {
            super.onBackPressed()
        }
    }

    private fun cerrarAjustes() {
        supportFragmentManager.beginTransaction().remove(fragment!!).commit()
        fragment = null
        ajustesShowing = false
        verMapa()
        window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE)
    }

    private fun verMapa(){
        binding.btn5Ajustes.isVisible=true
        binding.framelayoutmapa.isVisible=true
    }

    private fun ocultarMapa(){
        binding.btn5Ajustes.isVisible=false
        binding.framelayoutmapa.isVisible=false
    }

    private fun typewriter() {
        val typeWriterView = txtv5_presentacionmapa as TypeWriterView
        typeWriterView.setWithMusic(false)
        typeWriterView.animateText(resources.getString(R.string.text_bienvenidamapa))
        typeWriterView.setDelay(70)
    }

    //Este metodo es creado dado que las sentencias que estan dentro de el no pueden estar dentro del hilo de ejecucion del audio.
    private fun actualizarTutorialFinalizado() {
        DbHandler.setTutorialFinalizado()
        // En este caso, no overrideamos la respuesta de esta actualizacion dado que no es algo 'NECESARIO'
        DbHandler().requestDbUserUpdate(this)
    }

    private fun audioSound() {
        //funcion para la reproduccion del sonido
        runBlocking {
            launch {
                audio = MediaPlayer.create(this@Activity5_Mapa, R.raw.mapa_aurkezpena)
                audio.start()
                audio.duration
                audio.setOnCompletionListener {
                    exitAnimationfun()
                    DbHandler.setTutorialFinalizado()
                    actualizarTutorialFinalizado()
                }
            }
        }
        starAnimationfun()
    }

    override fun onDestroy() {
        if(this::audio.isInitialized){
            audio.stop()
        }
        super.onDestroy()
        instancia = null
    }

    private fun starAnimationfun() {
           binding.imgv5Manzanatutorial.visibility = VISIBLE
           binding.imgv5Bocadillo.visibility = VISIBLE
           binding.txtv5Presentacionmapa.visibility = VISIBLE

           val vistaanimadaInicio = TranslateAnimation(-1000f, 0f, 0f, 0f)
           vistaanimadaInicio.duration = 2000

           imgv5_manzanatutorial.startAnimation(vistaanimadaInicio)
           imgv5_manzanatutorial_animado.startAnimation(vistaanimadaInicio)
           imgv5_bocadillo.startAnimation(vistaanimadaInicio)
           txtv5_presentacionmapa.startAnimation(vistaanimadaInicio)

           vistaanimadaInicio.setAnimationListener(object : Animation.AnimationListener {
               override fun onAnimationStart(animation: Animation) {}
               override fun onAnimationEnd(animation: Animation) {
                   talkAnimationfun()
               }
               override fun onAnimationRepeat(animation: Animation) {}
           })
       }

    private fun talkAnimationfun() {
        binding.imgv5Manzanatutorial.visibility = GONE
        binding.imgv5ManzanatutorialAnimado.visibility = VISIBLE
        binding.imgv5ManzanatutorialAnimado.setBackgroundResource(R.drawable.animacion_manzana)
        val ani = binding.imgv5ManzanatutorialAnimado.background as AnimationDrawable
        ani.start()
    }

    private fun exitAnimationfun() {
        binding.imgv5Manzanatutorial.visibility = VISIBLE
        binding.imgv5ManzanatutorialAnimado.visibility = GONE
        //animacion salido upelio
        val vistaanimadaFinal = TranslateAnimation(0f, 1000f, 0f, 0f)
        vistaanimadaFinal.duration = 2000

        imgv5_manzanatutorial.startAnimation(vistaanimadaFinal)
        imgv5_bocadillo.startAnimation(vistaanimadaFinal)
        txtv5_presentacionmapa.startAnimation(vistaanimadaFinal)

        vistaanimadaFinal.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                binding.imgv5ManzanatutorialAnimado.visibility = GONE
                binding.imgv5Bocadillo.visibility = GONE
                binding.txtv5Presentacionmapa.visibility = GONE
                binding.imgv5Manzanatutorial.visibility = GONE
            }
            override fun onAnimationRepeat(animation: Animation) {}
        })

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
        mMap.uiSettings.isCompassEnabled = false
        mMap.uiSettings.isMyLocationButtonEnabled = false
        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID

        //Set min and max zoom
        mMap.setMaxZoomPreference(21F)
        mMap.setMinZoomPreference(14F)

        // Constrain the camera target to the Astigarraga bounds.
        val astigarragaBounds = LatLngBounds(
            LatLng(43.269625, -1.959532), LatLng(43.285576, -1.941156)
        )

        mMap.setLatLngBoundsForCameraTarget(astigarragaBounds)

        //Añadir Markers junto a sus nombres
        addMarkers()

        //Variable para comprobar si la primera animacion se ha realiado correctamente
        var firstFocusAnimation = false
        mMap.setOnMyLocationChangeListener {
            //Metodos para establecer la nueva posicion del radio que rodea la ubicacion del usuario
            myCurrentPosition = LatLng(it.latitude, it.longitude)
            if (!firstFocusAnimation) {
                //Condicion para focalizar al usuario por primera vez
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 16.5F))
                firstFocusAnimation = true
            }
        }
    }

    private val placesList = arrayListOf(
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
                    .title( (i+1).toString() + " - " + item[0] as String)
                    .icon(
                        BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_RED)
                    )
            )

            markerPointList.add(markerPointIcon!!)

            /*
            //We check the kind of user and, in case that is NOT ADMIN
            if (markerPointIcon != null) {
                if (DbHandler.getAdmin()) {
                    markerPointIcon!!.setIcon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                } else {
                    if (i < lastUserPoint) {
                        markerPointIcon!!.setIcon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    } else if (i == lastUserPoint) {
                        markerPointIcon!!.setIcon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    }
                }
            }

             */
        }
        checkPoints()

        mMap.setOnInfoWindowClickListener(OnInfoWindowClickListener { marker ->
            if(DbHandler.getTutorialFinalizado() == 0) {
                Toast.makeText(this, getString(R.string.finishExplanation), Toast.LENGTH_SHORT).show()
                return@OnInfoWindowClickListener
            }
            val latLon = marker.position

            fun irAPunto(puntoSeleccionado: Int) {
                val intent =  Intent(this, Activity6_Site::class.java)
                intent.putExtra("numero",puntoSeleccionado)
                startActivity(intent)
                this.overridePendingTransition(0, 0)
            }

            for ((i,item) in placesList.withIndex()) {
                if (item[1] as LatLng == latLon) {
                    val distanceToPoint = getDistBetweenPoints(myCurrentPosition, latLon)
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

                            //Añadir Area para mostrar al usuario si algun punto esta cerca de su radio o no
                            var myCircle: Circle = mMap.addCircle(
                                CircleOptions()
                                    .center(LatLng(43.285576, -1.941156))
                                    .radius(minimumRadius.toDouble())
                                    .strokeWidth(0f)
                                    .fillColor(0x33ff0000))

                            myCircle.setCenter(LatLng(latLon.latitude, latLon.longitude))

                            Handler(Looper.getMainLooper()).postDelayed({
                                if(myCircle != null){
                                    myCircle.remove()
                                }
                            }, 2000)
                        }
                    }
                    break
                }
            }
        })
    }

    fun checkPoints() {
        for ((i,item) in markerPointList.withIndex()) {
            item!!.setIcon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_RED))
            //We check the kind of user and, in case that is NOT ADMIN
            if (item != null) {
                if (DbHandler.getAdmin()) {
                    item!!.setIcon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                } else {
                    if (i < lastUserPoint) {
                        item!!.setIcon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    } else if (i == lastUserPoint) {
                        item!!.setIcon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    }
                }
            }
        }
    }

    private fun getDistBetweenPoints(localPoint: LatLng, gamePoint: LatLng): Int {
        val theta: Double = localPoint.longitude - gamePoint.longitude
        var dist = (sin(deg2rad(localPoint.latitude))
                * sin(deg2rad(gamePoint.latitude))
                + (cos(deg2rad(localPoint.latitude))
                * cos(deg2rad(gamePoint.latitude))
                * cos(deg2rad(theta))))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515
        val kms: Double = dist / 0.62137
        val meters: Double = kms * 1000
        return meters.toInt()
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

}
