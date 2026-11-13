package com.example.myapplication.ui.telas

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.ui.state.CadastroUiState
import com.example.myapplication.viewModel.CadastroViewModel

@Composable
fun TelaCadastroRoute(
    viewModel: CadastroViewModel = viewModel(),
    onCadastroSucesso: () -> Unit = {},
    onVoltarLoginClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.cadastroConcluido) {
        if (uiState.cadastroConcluido) {
            onCadastroSucesso()
        }
    }

    TelaCadastro(
        uiState = uiState,
        onEmailOuUsuarioChange = viewModel::onEmailOuUsuarioChange,
        onSenhaChange = viewModel::onSenhaChange,
        onConfirmarSenhaChange = viewModel::onConfirmarSenhaChange,
        onFotoUriChange = viewModel::onFotoUriChange,
        onCadastrarClick = viewModel::cadastrar,
        onVoltarLoginClick = onVoltarLoginClick
    )
}

@Composable
fun TelaCadastro(
    uiState: CadastroUiState,
    onEmailOuUsuarioChange: (String) -> Unit = {},
    onSenhaChange: (String) -> Unit = {},
    onConfirmarSenhaChange: (String) -> Unit = {},
    onFotoUriChange: (String?) -> Unit = {},
    onCadastrarClick: () -> Unit = {},
    onVoltarLoginClick: () -> Unit = {}
) {
    val corFundo = Color(0xFFFFFFFF)
    val corVerde = Color(0xFF16D174)
    val corTextoEscuro = Color(0xFF1E293B)
    val corBordaField = Color(0xFF334155)

    val launcherFoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onFotoUriChange(uri?.toString())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(corFundo)
            .padding(horizontal = 28.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_codewords),
                contentDescription = "Logo CodeWords",
                modifier = Modifier
                    .width(170.dp)
                    .height(110.dp)
            )

            Text(
                text = "Criar Conta",
                color = corTextoEscuro,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Seletor de Foto de Perfil
            Box(
                modifier = Modifier
                    .size(86.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE2E8F0))
                    .clickable { launcherFoto.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (!uiState.fotoUri.isNullOrBlank()) {
                    AsyncImage(
                        model = uiState.fotoUri,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = "Adicionar Foto",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (!uiState.fotoUri.isNullOrBlank()) "Alterar foto de perfil" else "Escolha sua foto de perfil",
                color = corVerde,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { launcherFoto.launch("image/*") }
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = uiState.emailOuUsuario,
                onValueChange = onEmailOuUsuarioChange,
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

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.senha,
                onValueChange = onSenhaChange,
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

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.confirmarSenha,
                onValueChange = onConfirmarSenhaChange,
                placeholder = { Text("Confirmar Senha", color = Color(0xFF475569), fontSize = 14.sp) },
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

            if (uiState.mensagemErro != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = uiState.mensagemErro,
                    color = Color(0xFFDC2626),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = onCadastrarClick,
                modifier = Modifier
                    .width(170.dp)
                    .height(46.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = corVerde)
            ) {
                Text(
                    text = "Cadastrar",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Já possui uma conta?",
                color = corTextoEscuro,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onVoltarLoginClick,
                modifier = Modifier
                    .width(180.dp)
                    .height(44.dp),
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, corTextoEscuro),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = corTextoEscuro)
            ) {
                Text(
                    text = "Fazer Login",
                    color = corTextoEscuro,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TelaCadastroPreview() {
    TelaCadastro(uiState = CadastroUiState())
}
