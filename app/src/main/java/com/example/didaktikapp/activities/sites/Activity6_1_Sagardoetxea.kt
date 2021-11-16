package com.example.didaktikapp.activities.sites


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.didaktikapp.R
import com.example.didaktikapp.databinding.Activity5MapaBinding
import com.example.didaktikapp.databinding.Activity61SagardoetxeaBinding
import com.example.didaktikapp.fragments.Fragment3_info
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap


class Activity6_1_Sagardoetxea : AppCompatActivity() {

    var bundle:Bundle=Bundle()
    lateinit var fragment:Fragment



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        setContentView(R.layout.activity6_1_sagardoetxea)
        getSupportActionBar()?.hide()


        bundle.putString("titulo_juego", "Sagardoetxea")
        bundle.putString("descripcion", "Esto es una prueba para pasar los datos de activity a fragment")
        fragment= Fragment3_info()
        fragment.arguments=bundle
        CargarFragment(fragment)



    }
    private fun CargarFragment(fragment: Fragment) {
        val transaccion=supportFragmentManager.beginTransaction()
        transaccion.replace(R.id.contenedor, fragment)
        transaccion.addToBackStack(null)
        transaccion.commit() }


}