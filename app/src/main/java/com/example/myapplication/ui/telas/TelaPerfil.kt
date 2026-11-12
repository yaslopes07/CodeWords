package com.example.myapplication.ui.telas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.local.entity.UsuarioEntity
import com.example.myapplication.ui.state.PerfilUiState
import com.example.myapplication.viewModel.PerfilViewModel

@Composable
fun TelaPerfilRoute(
    usuarioLogado: UsuarioEntity?,
    viewModel: PerfilViewModel = viewModel(),
    onSairClick: () -> Unit = {},
    onNavegarFases: () -> Unit = {},
    onNavegarRanking: () -> Unit = {}
) {
    LaunchedEffect(usuarioLogado) {
        viewModel.carregarPerfil(usuarioLogado)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TelaPerfil(
        uiState = uiState,
        onSairClick = onSairClick,
        onNavegarFases = onNavegarFases,
        onNavegarRanking = onNavegarRanking
    )
}

@Composable
fun TelaPerfil(
    uiState: PerfilUiState = PerfilUiState(),
    onSairClick: () -> Unit = {},
    onNavegarFases: () -> Unit = {},
    onNavegarRanking: () -> Unit = {}
) {
    val corFundo = Color(0xFF192229)
    val corHeader = Color(0xFF243038)
    val corVerde = Color(0xFF16D174)
    val corVermelha = Color(0xFFE53935)
    val corCardEstatistica = Color.White
    val corTextoEscuro = Color(0xFF1E293B)
    val corTextoSecundario = Color(0xFF94A3B8)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(corFundo)
    ) {
        // Área com Scroll
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Cabeçalho Superior
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = corHeader,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Perfil do Jogador",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Avatar do Utilizador
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFCBD5E1)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Foto de Perfil",
                    tint = Color.White,
                    modifier = Modifier.size(70.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nome e Tag de Status
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = uiState.nomeUsuario,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(10.dp))
                Surface(
                    color = corVerde,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "Ativo",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = "Entrou em Nov. 2026",
                color = corTextoSecundario,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Progresso de Nível
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Nível ${uiState.nivel}",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { uiState.progressoNivel },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = corVerde,
                    trackColor = Color(0xFF2A3742)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Secção: Minhas estatísticas
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Minhas estatísticas",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Card 1: Melhor Tempo Geral
                    Surface(
                        modifier = Modifier.weight(1f),
                        color = corCardEstatistica,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = Color(0xFFF59E0B),
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Melhor Tempo Geral",
                                color = corTextoEscuro,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = uiState.melhorTempoGeral,
                                color = corTextoEscuro,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Card 2: Fases Concluídas
                    Surface(
                        modifier = Modifier.weight(1f),
                        color = corCardEstatistica,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Extension,
                                contentDescription = null,
                                tint = Color(0xFF0EA5E9),
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Fases Concluídas",
                                color = corTextoEscuro,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = uiState.fasesConcluidasTexto,
                                color = corTextoEscuro,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Secção: Progresso das fases
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Progresso das fases",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                uiState.listaFases.forEach { item ->
                    ItemProgressoFase(
                        fase = item.titulo,
                        nome = item.subtitulo,
                        tempo = item.tempoTexto,
                        concluida = item.concluida,
                        corVerde = corVerde
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Botão Sair da conta
            OutlinedButton(
                onClick = onSairClick,
                modifier = Modifier
                    .width(190.dp)
                    .height(44.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.5.dp, corVermelha),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = corVermelha)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = null,
                        tint = corVermelha,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Sair da conta",
                        color = corVermelha,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Linha divisória e Barra de Navegação Inferior
        HorizontalDivider(color = corVerde, thickness = 1.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(corHeader)
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavegarFases) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.SportsEsports,
                        contentDescription = "Fases",
                        tint = Color.White
                    )
                    Text("Fases", color = Color.White, fontSize = 12.sp)
                }
            }

            IconButton(onClick = onNavegarRanking) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Ranking",
                        tint = Color.White
                    )
                    Text("Ranking", color = Color.White, fontSize = 12.sp)
                }
            }

            IconButton(onClick = { }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Perfil",
                        tint = corVerde
                    )
                    Text("Perfil", color = corVerde, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ItemProgressoFase(
    fase: String,
    nome: String,
    tempo: String,
    concluida: Boolean,
    corVerde: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (concluida) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = corVerde,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFCBD5E1))
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "$fase ",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = nome,
                color = Color.LightGray,
                fontSize = 13.sp
            )
        }

        Text(
            text = tempo,
            color = if (concluida) Color.White else Color(0xFFCBD5E1),
            fontSize = 13.sp,
            fontWeight = if (concluida) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TelaPerfilPreview() {
    TelaPerfil()
}