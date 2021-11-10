package com.example.didaktikapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.didaktikapp.R
import com.example.didaktikapp.databinding.Activity2LoginBinding
import com.google.firebase.firestore.FirebaseFirestore


class Activity2_Login : AppCompatActivity() {
    private lateinit var binding:Activity2LoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        setContentView(R.layout.activity2_login)

        binding= Activity2LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Firebase
        var db:FirebaseFirestore = FirebaseFirestore.getInstance()

        //Botón alta Usuario
        binding.btn1Hasi.setOnClickListener{

            guardardatos(db)
        }
    }

    private fun guardardatos(db:FirebaseFirestore){
        //Si los datos están rellenos
        if (binding.txt2Nombre.text.isNotBlank()){

            val dato= hashMapOf(
                "admin" to 0,
                "id" to "u3",
                "nombre" to binding.txt2Nombre.text.toString(),
                 "puntuacion" to 0,
                 "ultimo_punto" to 0)


            db.collection("Usuarios").document("3").set(dato)
                .addOnSuccessListener {

                    Toast.makeText(this, "Usuario insertado", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
        }

    }
}