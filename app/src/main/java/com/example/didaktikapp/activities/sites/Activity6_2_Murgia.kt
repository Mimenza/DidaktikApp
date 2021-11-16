package com.example.didaktikapp.activities.sites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.didaktikapp.R
import com.example.didaktikapp.fragments.Fragment3_info

class Activity6_2_Murgia : AppCompatActivity() {
    var bundle:Bundle=Bundle()
    lateinit var fragment:Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity6_2_murgia)
        getSupportActionBar()?.hide()


        //Poner  datos y pasar a fragment
        bundle.putString("titulo", resources.getString(R.string.titulo2_juego))
        bundle.putString("imagen",R.drawable.murgiajauregia.toString())
        bundle.putString("descripcion",resources.getString(R.string.text_2juego))

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