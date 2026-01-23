package com.ims.activesubscriptionsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ims.activesubscriptionsapp.ui.navigation.MainNavigation
import com.ims.activesubscriptionsapp.ui.theme.ActiveSubscriptionsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ActiveSubscriptionsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF8F8F8)
                ) {
                    // Chamada correta à função de navegação
                    MainNavigation()
                }
            }
        }
    }
}
