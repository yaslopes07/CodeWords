package com.example.myapplication.ui.telas

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.state.ItemRankingFase
import com.example.myapplication.ui.state.RankingUiState
import com.example.myapplication.viewModel.RankingViewModel

@Composable
fun TelaRankingRoute(
    usuarioId: Int = 0,
    viewModel: RankingViewModel = viewModel(),
    onFasesClick: () -> Unit = {},
    onPerfilClick: () -> Unit = {}
) {
    LaunchedEffect(usuarioId) {
        viewModel.carregarRanking(usuarioId)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TelaRanking(
        uiState = uiState,
        onFasesClick = onFasesClick,
        onPerfilClick = onPerfilClick
    )
}

@Composable
fun TelaRanking(
    uiState: RankingUiState,
    onFasesClick: () -> Unit = {},
    onPerfilClick: () -> Unit = {}
) {
    val corFundo = Color(0xFF141E26)
    val corBarraInferior = Color(0xFF1C2833)
    val corVerde = Color(0xFF16D174)
    val corDourado = Color(0xFFFACC15)

    Scaffold(
        containerColor = corFundo,
        bottomBar = {
            Column {
                HorizontalDivider(thickness = 1.dp, color = corVerde.copy(alpha = 0.6f))
                NavigationBar(
                    containerColor = corBarraInferior,
                    tonalElevation = 0.dp
                ) {
                    NavigationBarItem(
                        selected = false,
                        onClick = onFasesClick,
                        icon = { Icon(Icons.Filled.SportsEsports, contentDescription = "Fases") },
                        label = { Text("Fases", fontSize = 12.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = corVerde,
                            selectedTextColor = corVerde,
                            indicatorColor = Color.Transparent,
                            unselectedIconColor = Color.White,
                            unselectedTextColor = Color.White
                        )
                    )
                    NavigationBarItem(
                        selected = true,
                        onClick = { /* Já está na tela Ranking */ },
                        icon = { Icon(Icons.Filled.EmojiEvents, contentDescription = "Ranking") },
                        label = { Text("Ranking", fontSize = 12.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = corVerde,
                            selectedTextColor = corVerde,
                            indicatorColor = Color.Transparent,
                            unselectedIconColor = Color.White,
                            unselectedTextColor = Color.White
                        )
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = onPerfilClick,
                        icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
                        label = { Text("Perfil", fontSize = 12.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = corVerde,
                            selectedTextColor = corVerde,
                            indicatorColor = Color.Transparent,
                            unselectedIconColor = Color.White,
                            unselectedTextColor = Color.White
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(corFundo)
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Ranking",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Filled.Leaderboard,
                    contentDescription = "Pódio",
                    tint = corDourado,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(34.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(24.dp),
                color = Color.White
            ) {
                if (uiState.itensRanking.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Você ainda não concluiu nenhuma fase.\nJogue uma fase para registrar seu melhor tempo!",
                            color = Color(0xFF64748B),
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp, vertical = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        uiState.itensRanking.forEach { item ->
                            LinhaRanking(item = item)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun LinhaRanking(item: ItemRankingFase) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.width(36.dp),
                contentAlignment = Alignment.Center
            ) {
                when (item.posicao) {
                    1 -> Icon(
                        imageVector = Icons.Filled.MilitaryTech,
                        contentDescription = "1º Lugar",
                        tint = Color(0xFFEAB308),
                        modifier = Modifier.size(32.dp)
                    )
                    2 -> Icon(
                        imageVector = Icons.Filled.MilitaryTech,
                        contentDescription = "2º Lugar",
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(32.dp)
                    )
                    3 -> Icon(
                        imageVector = Icons.Filled.MilitaryTech,
                        contentDescription = "3º Lugar",
                        tint = Color(0xFFB45309),
                        modifier = Modifier.size(32.dp)
                    )
                    else -> Text(
                        text = item.posicao.toString(),
                        color = Color(0xFF475569),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = item.nomeFase,
                color = Color(0xFF1E293B),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Text(
            text = item.tempoFormatado,
            color = Color(0xFF475569),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}