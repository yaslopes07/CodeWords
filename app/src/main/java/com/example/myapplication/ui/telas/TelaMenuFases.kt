package com.example.myapplication.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Extension
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TelaMenuFases(
    fasesConcluidas: Set<Int> = emptySet(),
    onFaseClick: (Int) -> Unit = {},
    onRankingClick: () -> Unit = {},
    onPerfilClick: () -> Unit = {}
) {
    val corFundo = Color(0xFF141E26)
    val corBarraInferior = Color(0xFF1C2833)
    val corVerde = Color(0xFF16D174)
    val corCardBloqueado = Color(0xFF23303B)

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
                        selected = true,
                        onClick = {},
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
                        selected = false,
                        onClick = onRankingClick,
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
            Text(
                text = "Fases",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(28.dp))

            CardFase(
                titulo = "Fase 1",
                subtitulo = "Primeiros Passos",
                desbloqueada = false,
                concluida = false,
                corCardBloqueado = corCardBloqueado,
                corVerde = corVerde,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            CardFase(
                titulo = "Fase 2",
                subtitulo = "Conexões",
                desbloqueada = true,
                concluida = fasesConcluidas.contains(2),
                corCardBloqueado = corCardBloqueado,
                corVerde = corVerde,
                onClick = { onFaseClick(2) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            CardFase(
                titulo = "Fase 3",
                subtitulo = "Precisão",
                desbloqueada = true,
                concluida = fasesConcluidas.contains(3),
                corCardBloqueado = corCardBloqueado,
                corVerde = corVerde,
                onClick = { onFaseClick(3) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            CardFase(
                titulo = "Fase 4",
                subtitulo = "Desafio",
                desbloqueada = true,
                concluida = fasesConcluidas.contains(4),
                corCardBloqueado = corCardBloqueado,
                corVerde = corVerde,
                onClick = { onFaseClick(4) }
            )
        }
    }
}

@Composable
private fun CardFase(
    titulo: String,
    subtitulo: String,
    desbloqueada: Boolean,
    concluida: Boolean,
    corCardBloqueado: Color,
    corVerde: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
            .clickable(enabled = desbloqueada, onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = if (desbloqueada) Color.White else corCardBloqueado
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Extension,
                    contentDescription = null,
                    tint = if (desbloqueada) corVerde else Color(0xFF94A3B8),
                    modifier = Modifier.size(30.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = titulo,
                        color = if (desbloqueada) Color(0xFF1E293B) else Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitulo,
                        color = if (desbloqueada) Color(0xFF64748B) else Color(0xFF94A3B8),
                        fontSize = 13.sp
                    )
                }
            }

            if (!desbloqueada) {
                Text(
                    text = "Em breve",
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp
                )
            } else if (concluida) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Concluída",
                        tint = corVerde,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Concluída",
                        color = corVerde,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


