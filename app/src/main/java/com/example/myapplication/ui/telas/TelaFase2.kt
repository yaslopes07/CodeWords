package com.example.myapplication.ui.telas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.state.Fase2UiState
import com.example.myapplication.ui.state.Posicao
import com.example.myapplication.viewModel.Fase2ViewModel

@Composable
fun TelaFase2Route(
    usuarioId: Int = 0,
    viewModel: Fase2ViewModel = viewModel(),
    onVoltarClick: () -> Unit = {}
) {
    androidx.compose.runtime.LaunchedEffect(usuarioId) {
        viewModel.definirUsuario(usuarioId)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TelaFase2(
        uiState = uiState,
        onVoltarClick = onVoltarClick,
        onLetraClick = { posicao -> viewModel.selecionarPosicao(posicao) },
        onRecomecarClick = { viewModel.reiniciarFase() }
    )
}
@Composable
fun TelaFase2(
    uiState: Fase2UiState,
    onVoltarClick: () -> Unit = {},
    onLetraClick: (Posicao) -> Unit = {},
    onRecomecarClick: () -> Unit = {}
){
    val corFundo = Color(0xFF141E26)
    val corCardGrid = Color(0xFFEBF3FB)
    val corDoProgresso = Color(0xFF00E676)
    val corVerdeDestaque = Color(0xFFA5D6A7)
    val corAzulChip = Color(0xFFD6E8FA)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(corFundo)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onVoltarClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.White
                    )
                }
                Text(
                    text = uiState.numeroFase,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Timer,
                    contentDescription = "Tempo",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = uiState.tempoDecorrido,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF263238)),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(uiState.progresso)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(10.dp))
                    .background(corDoProgresso)
            )
            Text(
                text = "${uiState.encontradasCount}/${uiState.totalPalavras}",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = uiState.tema,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            color = corCardGrid,
            shape = RoundedCornerShape(20.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(8),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                val totalLinhas = uiState.matrizLetras.size
                val totalColunas = if (totalLinhas > 0) uiState.matrizLetras[0].size else 0

                itemsIndexed(uiState.matrizLetras.flatten()){ index, letra ->
                    val l = index / totalColunas
                    val c = index % totalColunas
                    val posicao = Posicao(l, c)

                    val estaSelecionada = uiState.posicoesSelecionada.contains(posicao)
                    val estaEncontrada = uiState.posicoesEncontradas.contains(posicao)

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(2.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                when{
                                    estaEncontrada -> corVerdeDestaque
                                    estaSelecionada -> Color(0xFF93C5FD)
                                    else -> Color.Transparent
                                }
                            )
                            .clickable{onLetraClick(posicao)},
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = letra.toString(),
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            color = Color.White,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Palavras Encontradas: ",
                    color = Color.Black,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(uiState.palavras){item ->
                        Surface(
                            color = corAzulChip,
                            shape = CircleShape
                        ) {
                            Text(
                                text = item.texto,
                                color = Color(0xFF1565C0),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                textDecoration = if (item.encontrada) TextDecoration.LineThrough else TextDecoration.None,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
        if (uiState.mensagemVitoria) {
            AlertDialog(
                onDismissRequest = { /* Mantém aberto até clicar num botão */ },
                containerColor = Color(0xFF1F2C34),
                title = {
                    Text(
                        text = "🎉 Parabéns!",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Você concluiu a ${uiState.numeroFase} com sucesso!",
                            color = Color.LightGray,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Tempo total: ${uiState.tempoDecorrido}",
                            color = Color(0xFF00E676),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = onRecomecarClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676))
                    ) {
                        Text("Recomeçar", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = onVoltarClick,
                        border = BorderStroke(1.dp, Color.White)
                    ) {
                        Text("Menu de Fases", color = Color.White)
                    }
                }
            )
        }
    }
}

