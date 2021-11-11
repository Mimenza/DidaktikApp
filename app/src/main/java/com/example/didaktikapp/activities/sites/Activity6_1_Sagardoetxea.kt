package com.example.didaktikapp.activities.sites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.didaktikapp.R
import com.example.didaktikapp.activities.Activity4_bienvenida
import com.example.didaktikapp.databinding.Activity61SagardoetxeaBinding

class Activity6_1_Sagardoetxea : AppCompatActivity() {
    private lateinit var binding:Activity61SagardoetxeaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity6_1_sagardoetxea)
        getSupportActionBar()?.hide()


        binding = Activity61SagardoetxeaBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}