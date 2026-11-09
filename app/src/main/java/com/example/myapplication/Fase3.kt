package com.exemplo.cacapalavras.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
private val DarkBackground = Color(0xFF18222A)

@Composable
fun Fase3Screen(
    onVoltarClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Fase3ScreenPreview() {
    Fase3Screen()
}