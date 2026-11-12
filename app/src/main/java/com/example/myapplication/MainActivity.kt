package com.example.myapplication


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.myapplication.ui.telas.TelaCadastroRoute
import com.example.myapplication.ui.telas.TelaFase2Route
import com.example.myapplication.ui.telas.TelaLoginRoute
import com.example.myapplication.ui.theme.MyApplicationTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                var telaAtual by remember { mutableStateOf("login") }


                when (telaAtual) {
                    "login" -> TelaLoginRoute(
                        onLoginSucesso = { telaAtual = "fase2" },
                        onCadastrarClick = { telaAtual = "cadastro" }
                    )
                    "cadastro" -> TelaCadastroRoute(
                        onCadastroSucesso = { telaAtual = "login" },
                        onVoltarLoginClick = { telaAtual = "login" }
                    )
                    "fase2" -> TelaFase2Route(
                        onVoltarClick = { telaAtual = "login" }
                    )
                }
            }
        }
    }
}