package com.example.myapplication.ui.telas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

@Composable
fun TelaLogin(
    onEntrarClick: () -> Unit = {},
    onCadastrarClick: () -> Unit = {}
) {
    var emailOuUsuario by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    val corFundo = Color(0xFFFFFFFF)
    val corVerde = Color(0xFF16D174)
    val corTextoEscuro = Color(0xFF1E293B)
    val corBordaField = Color(0xFF334155)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(corFundo)
            .padding(horizontal = 28.dp, vertical = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Imagem da Logo Oficial
            Image(
                painter = painterResource(id = R.drawable.logo_codewords),
                contentDescription = "Logo CodeWords",
                modifier = Modifier
                    .width(240.dp)
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Campo: Email ou Usuário
            OutlinedTextField(
                value = emailOuUsuario,
                onValueChange = { emailOuUsuario = it },
                placeholder = { Text("Email ou Usuário", color = Color(0xFF475569), fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedBorderColor = corBordaField,
                    unfocusedBorderColor = corBordaField,
                    focusedTextColor = corTextoEscuro,
                    unfocusedTextColor = corTextoEscuro
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo: Senha
            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                placeholder = { Text("Senha", color = Color(0xFF475569), fontSize = 14.sp) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedBorderColor = corBordaField,
                    unfocusedBorderColor = corBordaField,
                    focusedTextColor = corTextoEscuro,
                    unfocusedTextColor = corTextoEscuro
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Botão Entrar
            Button(
                onClick = onEntrarClick,
                modifier = Modifier
                    .width(150.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = corVerde)
            ) {
                Text(
                    text = "Entrar",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Secção Inferior: Pergunta + Botão Cadastrar
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Ainda não é um jogador?",
                color = corTextoEscuro,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onCadastrarClick,
                modifier = Modifier
                    .width(180.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, corTextoEscuro),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = corTextoEscuro)
            ) {
                Text(
                    text = "Cadastrar",
                    color = corTextoEscuro,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TelaLoginPreview() {
    TelaLogin()
}
