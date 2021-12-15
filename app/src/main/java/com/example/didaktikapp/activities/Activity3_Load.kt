package com.example.didaktikapp.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.didaktikapp.Adapter.UsersRecyclerAdapter
import com.example.didaktikapp.databinding.Activity3LoadBinding
import com.example.reto01.Model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity3_load.*


class Activity3_Load : AppCompatActivity() {
    private val activity = this
    private lateinit var recyclerViewUsers:RecyclerView
    lateinit private var Usuario : User
    private lateinit var listUsers: MutableList<User>
    private lateinit var usersRecyclerAdapter:UsersRecyclerAdapter
    private lateinit var context:Context
    private lateinit var binding:Activity3LoadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

        binding= Activity3LoadBinding.inflate(layoutInflater)
        setContentView(binding.root)


        comprobarPermisos()
    }

    private fun comprobarPermisos() {
        if (!Utils.comprobarPermisosMap(this)) {
            askForMapPermission()
        } else {
            initViews()
            initObjects()
        }
    }

    private fun initViews(){
        recyclerViewUsers =rview3_usuarios as RecyclerView

    }
    private fun initObjects(){
        listUsers = ArrayList()
        var db:FirebaseFirestore = FirebaseFirestore.getInstance()
        context =this

        getUsersData(db)


    }

    private fun  getUsersData(db:FirebaseFirestore){


        db.collection("Usuarios").get().
        addOnSuccessListener { documentos->
            for (documento in documentos){
                Usuario = User()
                Usuario.id = documento.data.get("id").toString().replace("u","").toInt()
                Usuario.nombre=documento.data.get("nombre").toString()
                Usuario.tutorialFinalizado=documento.data.get("tutorialFinalizado").toString().toInt()
                Usuario.ultima_puntuacion=documento.data.get("ultimo_punto").toString().toInt()
                Usuario.puntuacion=documento.data.get("puntuacion").toString().toInt()
                listUsers.add(Usuario)
            }

            cargarAdapter()

        }

    }
    private fun cargarAdapter(){

        //Cargar el adapter después de llamar a la bbdd
        val adapter = UsersRecyclerAdapter(this,listUsers, context)

        recyclerViewUsers.layoutManager = LinearLayoutManager(context)
        recyclerViewUsers.adapter = adapter
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0 && grantResults[0] != -1 ) {
            initViews()
            initObjects()
        } else {
            finish()
        }
    }

    private fun askForMapPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
    }
}