package com.example.stalkit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.stalkit.ui.main.StalkitApp
import com.example.stalkit.ui.theme.StalkitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StalkitTheme {
                StalkitApp()
            }
        }
    }
}