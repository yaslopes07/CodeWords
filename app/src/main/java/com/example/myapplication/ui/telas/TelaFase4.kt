package com.example.myapplication.ui.telas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.state.Fase4UiState
import com.example.myapplication.ui.state.PalavraFase
import com.example.myapplication.ui.state.Posicao
import com.example.myapplication.viewModel.Fase4ViewModel

/*
 * TelaFase4.kt  —  Fase 4 | 8X12  (tema: Análise de Sistemas)
 *
 * Organização do arquivo (de cima para baixo):
 *   1. TelaFase4Route  -> "liga" a tela ao ViewModel (só pega o estado e repassa os eventos)
 *   2. TelaFase4       -> monta a tela juntando os blocos abaixo (não tem lógica, só layout)
 *   3. Blocos da tela  -> Cabecalho, BarraProgresso, Tabuleiro, ListaPalavras, DialogVitoria
 *   4. desenharMarcaTexto -> função que desenha o "marca-texto" em cima das letras
 *
 * Fluxo de dados (MVVM):  ViewModel -> uiState -> Tela   |   Tela -> eventos -> ViewModel
 */

// ============================== 1. ROUTE ==============================

/** Conecta a tela ao ViewModel. A tela em si (TelaFase4) não conhece o ViewModel. */
@Composable
fun TelaFase4Route(
    viewModel: Fase4ViewModel = viewModel(),
    onVoltarClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TelaFase4(
        uiState = uiState,
        onVoltarClick = onVoltarClick,
        onInicioSelecao = { posicao -> viewModel.iniciarSelecao(posicao) },
        onArrastarSelecao = { posicao -> viewModel.arrastarSelecao(posicao) },
        onFimSelecao = { viewModel.finalizarSelecao() },
        onRecomecarClick = { viewModel.reiniciarFase() }
    )
}

// ============================== 2. TELA ==============================

@Composable
fun TelaFase4(
    uiState: Fase4UiState,
    onVoltarClick: () -> Unit = {},
    onInicioSelecao: (Posicao) -> Unit = {},
    onArrastarSelecao: (Posicao) -> Unit = {},
    onFimSelecao: () -> Unit = {},
    onRecomecarClick: () -> Unit = {}
) {
    val corFundo = Color(0xFF141E26)
    val corCardGrid = Color(0xFFEBF3FB)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(corFundo)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Cabecalho(
            numeroFase = uiState.numeroFase,
            tempo = uiState.tempoDecorrido,
            onVoltarClick = onVoltarClick
        )

        BarraProgresso(
            progresso = uiState.progresso,
            texto = "${uiState.encontradasCount}/${uiState.totalPalavras}"
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = uiState.tema,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Cartão do tabuleiro: ocupa todo o espaço que sobrar (weight = 1f)
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            color = corCardGrid,
            shape = RoundedCornerShape(20.dp)
        ) {
            Tabuleiro(
                uiState = uiState,
                onInicioSelecao = onInicioSelecao,
                onArrastarSelecao = onArrastarSelecao,
                onFimSelecao = onFimSelecao,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        ListaPalavras(palavras = uiState.palavras)

        if (uiState.mensagemVitoria) {
            DialogVitoria(
                numeroFase = uiState.numeroFase,
                tempo = uiState.tempoDecorrido,
                onRecomecarClick = onRecomecarClick,
                onVoltarClick = onVoltarClick
            )
        }
    }
}

// ============================== 3. BLOCOS ==============================

/** Linha do topo: seta de voltar + nome da fase à esquerda, cronômetro à direita. */
@Composable
private fun Cabecalho(
    numeroFase: String,
    tempo: String,
    onVoltarClick: () -> Unit
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
                text = numeroFase,
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
                text = tempo,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/** Barra verde que enche conforme as palavras são encontradas (progresso vai de 0f a 1f). */
@Composable
private fun BarraProgresso(progresso: Float, texto: String) {
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
                .fillMaxWidth(progresso)
                .fillMaxHeight()
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF00E676))
        )
        Text(
            text = texto,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

/**
 * Tabuleiro do caça-palavras. É feito em 2 camadas empilhadas:
 *   - atrás : Canvas que desenha os traços do marca-texto
 *   - frente: as letras (Column de Rows)
 * O gesto de arrastar é detectado no Box de fora, que converte o ponto tocado em (linha, coluna).
 */
@Composable
private fun Tabuleiro(
    uiState: Fase4UiState,
    onInicioSelecao: (Posicao) -> Unit,
    onArrastarSelecao: (Posicao) -> Unit,
    onFimSelecao: () -> Unit,
    modifier: Modifier = Modifier
) {
    val corMarcaEncontrada = Color(0xFF00E676).copy(alpha = 0.35f) // verde: palavra já achada
    val corMarcaSelecao = Color(0xFF2979FF).copy(alpha = 0.35f)    // azul: seleção em andamento

    val matriz = uiState.matrizLetras
    val totalLinhas = matriz.size
    val totalColunas = matriz.firstOrNull()?.size ?: 0
    if (totalLinhas == 0 || totalColunas == 0) return // dados ainda não carregaram

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        // Tamanho (em pixels) de cada célula: área total dividida pela quantidade de linhas/colunas
        val celulaLargura = constraints.maxWidth / totalColunas.toFloat()
        val celulaAltura = constraints.maxHeight / totalLinhas.toFloat()

        // Transforma um ponto da tela (x, y) na célula (linha, coluna) correspondente
        fun posicaoDe(ponto: Offset) = Posicao(
            linha = (ponto.y / celulaAltura).toInt().coerceIn(0, totalLinhas - 1),
            coluna = (ponto.x / celulaLargura).toInt().coerceIn(0, totalColunas - 1)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(celulaLargura, celulaAltura) {
                    // Um gesto completo: encostar -> arrastar -> soltar
                    awaitEachGesture {
                        val toque = awaitFirstDown()
                        onInicioSelecao(posicaoDe(toque.position))
                        drag(toque.id) { mudanca ->
                            mudanca.consume()
                            onArrastarSelecao(posicaoDe(mudanca.position))
                        }
                        onFimSelecao() // dedo levantou
                    }
                }
        ) {
            // Camada de trás: marca-texto
            Canvas(modifier = Modifier.fillMaxSize()) {
                uiState.palavras
                    .filter { it.encontrada }
                    .forEach { palavra ->
                        desenharMarcaTexto(
                            inicio = palavra.posicoes.first(),
                            fim = palavra.posicoes.last(),
                            celulaLargura = celulaLargura,
                            celulaAltura = celulaAltura,
                            cor = corMarcaEncontrada
                        )
                    }

                val selecao = uiState.posicoesSelecionada
                if (selecao.isNotEmpty()) {
                    desenharMarcaTexto(
                        inicio = selecao.first(),
                        fim = selecao.last(),
                        celulaLargura = celulaLargura,
                        celulaAltura = celulaAltura,
                        cor = corMarcaSelecao
                    )
                }
            }

            // Camada da frente: letras (cada linha/célula divide o espaço igualmente com weight)
            Column(modifier = Modifier.fillMaxSize()) {
                matriz.forEach { linha ->
                    Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        linha.forEach { letra ->
                            Box(
                                modifier = Modifier.weight(1f).fillMaxHeight(),
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
            }
        }
    }
}

/** Lista horizontal (com scroll) de todas as palavras; as já encontradas ficam riscadas. */
@Composable
private fun ListaPalavras(palavras: List<PalavraFase>) {
    val corAzulChip = Color(0xFFD6E8FA)

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
                text = "Palavras Encontradas:",
                color = Color.Black,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(palavras) { item ->
                    Surface(color = corAzulChip, shape = CircleShape) {
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
}

/** Pop-up que aparece quando todas as palavras foram encontradas. */
@Composable
private fun DialogVitoria(
    numeroFase: String,
    tempo: String,
    onRecomecarClick: () -> Unit,
    onVoltarClick: () -> Unit
) {
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
                    text = "Você concluiu a $numeroFase com sucesso!",
                    color = Color.LightGray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Tempo total: $tempo",
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

// ============================== 4. DESENHO ==============================

/**
 * Desenha um traço arredondado (estilo marca-texto) do centro da célula inicial
 * até o centro da célula final.
 */
private fun DrawScope.desenharMarcaTexto(
    inicio: Posicao,
    fim: Posicao,
    celulaLargura: Float,
    celulaAltura: Float,
    cor: Color
) {
    val espessura = minOf(celulaLargura, celulaAltura) * 0.8f
    val pontoInicial = Offset((inicio.coluna + 0.5f) * celulaLargura, (inicio.linha + 0.5f) * celulaAltura)
    val pontoFinal = Offset((fim.coluna + 0.5f) * celulaLargura, (fim.linha + 0.5f) * celulaAltura)

    if (inicio == fim) {
        // Seleção de uma célula só: desenha uma bolinha
        drawCircle(color = cor, radius = espessura / 2f, center = pontoInicial)
    } else {
        drawLine(
            color = cor,
            start = pontoInicial,
            end = pontoFinal,
            strokeWidth = espessura,
            cap = StrokeCap.Round
        )
    }
}