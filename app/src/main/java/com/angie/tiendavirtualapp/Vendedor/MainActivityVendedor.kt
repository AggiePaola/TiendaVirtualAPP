package com.angie.tiendavirtualapp.Vendedor

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angie.tiendavirtualapp.R
import com.angie.tiendavirtualapp.databinding.ActivityMainVendedorBinding

class MainActivityVendedor : AppCompatActivity() {
    private lateinit var binding: ActivityMainVendedorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainVendedorBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main_vendedor)

        }
    }
