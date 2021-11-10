package com.example.didaktikapp.activities

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import com.example.didaktikapp.R

import com.example.didaktikapp.databinding.Activity1PrincipalBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

import com.google.firebase.firestore.QuerySnapshot

import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnCompleteListener


class Activity1_Principal : AppCompatActivity() {

    private lateinit var binding: Activity1PrincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        setContentView(R.layout.activity1_principal)

        binding = Activity1PrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData(db)
        menu()
        ocultarbtn()

        binding.btn1Hasi.setOnClickListener() {
            binding.btn1Hasi.isVisible = false
            binding.btn1Cargar.isVisible = true
            binding.btn1Nuevo.isVisible = true

            binding.btn1Cargar.setOnClickListener() {
                ocultartodo()
            }
        }
    }

    fun ocultarbtn() {
        binding.btn1Cargar.isVisible = false
        binding.btn1Nuevo.isVisible = false
        binding.btnMapa.isVisible = false
        binding.btnLogin.isVisible = false
        binding.btnLoad.isVisible = false
        binding.btnBienvenida.isVisible = false
    }

    fun menu() {
        binding.btnBienvenida.setOnClickListener() {
            var i = Intent(this, Activity4_bienvenida::class.java)
            startActivity(i)
            this.overridePendingTransition(0, 0)
        }
        binding.btnLoad.setOnClickListener() {
            var i = Intent(this, Activity3_Load::class.java)
            startActivity(i)
            this.overridePendingTransition(0, 0)
        }
        binding.btnLogin.setOnClickListener() {
            var i = Intent(this, Activity2_Login::class.java)
            startActivity(i)
            this.overridePendingTransition(0, 0)
        }
        binding.btnMapa.setOnClickListener() {
            var i = Intent(this, Activity5_Mapa::class.java)
            startActivity(i)
            this.overridePendingTransition(0, 0)
        }
    }

    fun ocultartodo() {
        //ocultamos lo que no nos interesa
        binding.btn1Nuevo.isVisible = false
        binding.btn1Cargar.isVisible = false
        binding.btn1Hasi.isVisible = false
        binding.img1Upelio.isVisible = false
        binding.txtv1Gruponombre.isVisible = false
        binding.txtv1Titulo.isVisible = false

        //sacamos lo que nos interesa
        binding.btnMapa.isVisible = true
        binding.btnLogin.isVisible = true
        binding.btnLoad.isVisible = true
        binding.btnBienvenida.isVisible = true
    }

    fun getData(db: FirebaseFirestore) {

        db.collection("Usuarios")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d(TAG, document.id + " => " + document.data)
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }

}
