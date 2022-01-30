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

    private lateinit var recyclerViewUsers: RecyclerView
    private lateinit var binding: Activity3LoadBinding
    private lateinit var listUsers: MutableList<User>
    private lateinit var context: Context
    private lateinit var usuario: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        val sharedPreferences = this.getSharedPreferences("com.example.didaktikapp_preferences", 0)
        val darkStatus = sharedPreferences?.getInt("io.github.manuelernesto.DARK_STATUS",0)

        if(darkStatus == 0){
            window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }

        binding = Activity3LoadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        comprobarPermisos()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.overridePendingTransition(0, 0)
    }

    private fun comprobarPermisos() {
        if (!Utils.comprobarPermisosMap(this)) {
            askForMapPermission()
        } else {
            initViews()
            initObjects()
        }
    }

    private fun initViews() {
        recyclerViewUsers = rview3_usuarios as RecyclerView

    }

    private fun initObjects() {
        listUsers = ArrayList()
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        context = this

        getUsersData(db)
    }

    private fun getUsersData(db: FirebaseFirestore) {

        db.collection("Usuarios").get().addOnSuccessListener { documentos ->
            for (documento in documentos) {
                usuario = User()
                usuario.id = documento.data["id"].toString().replace("u", "").toInt()
                usuario.nombre = documento.data["nombre"].toString()
                usuario.tutorialFinalizado =
                    documento.data["tutorialFinalizado"].toString().toInt()
                usuario.ultima_puntuacion = documento.data["ultimo_punto"].toString().toInt()
                usuario.puntuacion = documento.data["puntuacion"].toString().toInt()
                listUsers.add(usuario)
            }

            cargarAdapter()

        }

    }

    private fun cargarAdapter() {

        //Cargar el adapter después de llamar a la bbdd
        val adapter = UsersRecyclerAdapter(this, listUsers, context)

        recyclerViewUsers.layoutManager = LinearLayoutManager(context)
        recyclerViewUsers.adapter = adapter
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] != -1) {
            initViews()
            initObjects()
        } else {
            finish()
        }
    }

    private fun askForMapPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            1
        )
    }
}