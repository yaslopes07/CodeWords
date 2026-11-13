package com.example.myapplication.ui.telas

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapplication.viewModel.PainelAdminViewModel

/**
 * Papéis de usuário suportados no sistema.
 * Compatível para mapeamento direto em entidades Room com @TypeConverter ou String.
 */
enum class PapelUsuario(val label: String) {
    ADMIN("Administrador"),
    JOGADOR("Jogador")
}

private const val DATA_NOV_2026 = "Nov. 2026"
private const val DATA_OUT_2026 = "Out. 2026"
private const val DATA_SET_2026 = "Set. 2026"
private const val DATA_AGO_2026 = "Ago. 2026"
private const val DATA_JUL_2026 = "Jul. 2026"
private const val DATA_JUN_2026 = "Jun. 2026"

/**
 * Modelo de dados de Usuário pronto para integração com Room Database.
 * Pode ser diretamente decorado com @Entity(tableName = "usuarios") e @PrimaryKey(autoGenerate = true).
 */
data class UsuarioItem(
    val id: Long = 0L,
    val nome: String,
    val email: String,
    val papel: PapelUsuario = PapelUsuario.JOGADOR,
    val ativo: Boolean = true,
    val dataCadastro: String = DATA_NOV_2026,
    val fotoUri: String? = null
)

/**
 * Lista inicial com 16 usuários para refletir o estado do wireframe
 * ("Usuários cadastrados: 16").
 */
private fun gerarUsuariosIniciais(): List<UsuarioItem> = listOf(
    UsuarioItem(1L, "Admin Mestre", "admin@codewords.com", PapelUsuario.ADMIN, true, DATA_NOV_2026),
    UsuarioItem(2L, "PedroPlay", "pedro@codewords.com", PapelUsuario.JOGADOR, true, DATA_NOV_2026),
    UsuarioItem(3L, "AnaDev", "ana.dev@codewords.com", PapelUsuario.ADMIN, true, DATA_NOV_2026),
    UsuarioItem(4L, "LucasGamer", "lucas.gamer@gmail.com", PapelUsuario.JOGADOR, true, DATA_OUT_2026),
    UsuarioItem(5L, "MarianaCode", "mariana@tech.io", PapelUsuario.JOGADOR, true, DATA_OUT_2026),
    UsuarioItem(6L, "CarlosAdmin", "carlos.adm@codewords.com", PapelUsuario.ADMIN, true, DATA_OUT_2026),
    UsuarioItem(7L, "BeatrizBits", "beatriz@dev.br", PapelUsuario.JOGADOR, true, DATA_OUT_2026),
    UsuarioItem(8L, "GabrielLoop", "gabriel@code.org", PapelUsuario.JOGADOR, true, DATA_SET_2026),
    UsuarioItem(9L, "JulianaByte", "juliana@games.com", PapelUsuario.JOGADOR, false, DATA_SET_2026),
    UsuarioItem(10L, "RafaelStack", "rafael@stack.io", PapelUsuario.JOGADOR, true, DATA_SET_2026),
    UsuarioItem(11L, "FernandaLogic", "fernanda@codewords.com", PapelUsuario.ADMIN, true, DATA_AGO_2026),
    UsuarioItem(12L, "ThiagoPixel", "thiago@pixel.art", PapelUsuario.JOGADOR, true, DATA_AGO_2026),
    UsuarioItem(13L, "CamilaQuery", "camila@data.com", PapelUsuario.JOGADOR, true, DATA_JUL_2026),
    UsuarioItem(14L, "BrunoScript", "bruno@script.dev", PapelUsuario.JOGADOR, false, DATA_JUL_2026),
    UsuarioItem(15L, "LarissaFlow", "larissa@flow.io", PapelUsuario.JOGADOR, true, DATA_JUN_2026),
    UsuarioItem(16L, "DiegoRoot", "diego.root@codewords.com", PapelUsuario.ADMIN, true, DATA_JUN_2026)
)

/**
 * Cores da identidade visual Dark Mode do CodeWords (compatível com a Tela de Perfil).
 */
private object CoresAdmin {
    val Fundo = Color(0xFF192229)
    val Header = Color(0xFF243038)
    val CardFundo = Color(0xFF243038)
    val CardElevado = Color(0xFF2A3742)
    val Borda = Color(0xFF334155)

    val VerdeDestaque = Color(0xFF16D174)
    val VerdeSuave = Color(0x1F16D174)
    val VermelhoAlerta = Color(0xFFE53935)
    val VermelhoSuave = Color(0x1FE53935)
    val LaranjaAdmin = Color(0xFFF59E0B)
    val AzulInfo = Color(0xFF0EA5E9)

    val TextoBranco = Color.White
    val TextoSecundario = Color(0xFF94A3B8)
    val TextoPlaceholder = Color(0xFF64748B)
}

@Composable
fun TelaPainelAdminRoute(
    nomeAdminLogado: String = "Administrador",
    viewModel: PainelAdminViewModel = viewModel(),
    onLogoutClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TelaPainelAdmin(
        nomeAdminLogado = nomeAdminLogado,
        usuariosDoBanco = uiState.usuarios,
        onLogoutClick = onLogoutClick,
        onUsuarioCriadoComSenha = { item, senha ->
            viewModel.criarUsuario(item, senha)
        },
        onUsuarioAtualizado = { item ->
            viewModel.atualizarUsuario(item)
        },
        onUsuarioDeletado = { id ->
            viewModel.deletarUsuario(id)
        }
    )
}

/**
 * Tela do Painel Administrativo com CRUD completo de usuários e administradores.
 */
@Composable
fun TelaPainelAdmin(
    nomeAdminLogado: String = "Administrador",
    usuariosDoBanco: List<UsuarioItem>? = null,
    onLogoutClick: () -> Unit = {},
    onUsuarioCriado: (UsuarioItem) -> Unit = {},
    onUsuarioCriadoComSenha: (UsuarioItem, String) -> Unit = { _, _ -> },
    onUsuarioAtualizado: (UsuarioItem) -> Unit = {},
    onUsuarioDeletado: (Long) -> Unit = {}
) {
    // Estado local da lista (fallback se não fornecido pelo Room Database)
    val listaUsuariosLocal = remember {
        mutableStateListOf<UsuarioItem>().apply { addAll(gerarUsuariosIniciais()) }
    }

    val listaEfetiva = usuariosDoBanco ?: listaUsuariosLocal

    var termoBusca by remember { mutableStateOf("") }
    var filtroPapel by remember { mutableStateOf<PapelUsuario?>(null) }

    // Estados dos diálogos CRUD
    var usuarioEmEdicao by remember { mutableStateOf<UsuarioItem?>(null) }
    var usuarioParaExcluir by remember { mutableStateOf<UsuarioItem?>(null) }
    var modalCadastroAberto by remember { mutableStateOf(false) }
    var cadastrarComoAdminPredefinido by remember { mutableStateOf(false) }

    // Filtro reativo de busca e papel
    val usuariosFiltrados by remember(termoBusca, filtroPapel, listaEfetiva) {
        derivedStateOf {
            listaEfetiva.filter { usuario ->
                val correspondeBusca = termoBusca.isBlank() ||
                        usuario.nome.contains(termoBusca, ignoreCase = true) ||
                        usuario.email.contains(termoBusca, ignoreCase = true)
                val correspondePapel = filtroPapel == null || usuario.papel == filtroPapel
                correspondeBusca && correspondePapel
            }
        }
    }

    val totalUsuarios = listaEfetiva.size
    val totalAdmins = listaEfetiva.count { it.papel == PapelUsuario.ADMIN }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CoresAdmin.Fundo)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // 1. CABEÇALHO DO PAINEL COM SESSÃO, CONTADOR E LOGOUT
            CabecalhoPainelAdmin(
                nomeAdmin = nomeAdminLogado,
                totalUsuarios = totalUsuarios,
                totalAdmins = totalAdmins,
                onLogoutClick = onLogoutClick
            )

            // CORPO COM SCROLL (LAZY COLUMN)
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // 2. SEÇÃO DESTACADA: CRIAR NOVO LOGIN DE ADMINISTRADOR
                item {
                    SecaoDestaqueNovoAdmin(
                        onCadastrarAdminClick = {
                            cadastrarComoAdminPredefinido = true
                            modalCadastroAberto = true
                        },
                        onCadastrarUsuarioComumClick = {
                            cadastrarComoAdminPredefinido = false
                            modalCadastroAberto = true
                        }
                    )
                }

                // 3. BARRA DE BUSCA COM ÍCONE DE LUPA E FILTROS RÁPIDOS
                item {
                    SecaoBuscaUsuarios(
                        termoBusca = termoBusca,
                        onTermoBuscaChange = { termoBusca = it },
                        filtroPapel = filtroPapel,
                        onFiltroPapelChange = { filtroPapel = it }
                    )
                }

                // 4. SUBTÍTULO E CONTADOR DOS RESULTADOS
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Usuários Cadastrados",
                            color = CoresAdmin.TextoBranco,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${usuariosFiltrados.size} de $totalUsuarios",
                            color = CoresAdmin.TextoSecundario,
                            fontSize = 13.sp
                        )
                    }
                }

                // 5. LISTA DE USUÁRIOS
                if (usuariosFiltrados.isEmpty()) {
                    item {
                        CardVazioBusca(termoBusca = termoBusca)
                    }
                } else {
                    items(
                        items = usuariosFiltrados,
                        key = { it.id }
                    ) { usuario ->
                        ItemUsuarioAdmin(
                            usuario = usuario,
                            onEditarClick = { usuarioEmEdicao = usuario },
                            onExcluirClick = { usuarioParaExcluir = usuario }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }

        // DIÁLOGO DE CRIAÇÃO (NOVO USUÁRIO OU ADMIN)
        if (modalCadastroAberto) {
            DialogoFormularioUsuario(
                usuarioInicial = null,
                papelPadrao = if (cadastrarComoAdminPredefinido) PapelUsuario.ADMIN else PapelUsuario.JOGADOR,
                onDismiss = { modalCadastroAberto = false },
                onSalvar = { novo, senha ->
                    if (usuariosDoBanco == null) {
                        val novoId = (listaUsuariosLocal.maxOfOrNull { it.id } ?: 0L) + 1L
                        val usuarioSalvo = novo.copy(id = novoId)
                        listaUsuariosLocal.add(0, usuarioSalvo)
                    }
                    onUsuarioCriado(novo)
                    onUsuarioCriadoComSenha(novo, senha)
                    modalCadastroAberto = false
                }
            )
        }

        // DIÁLOGO DE EDIÇÃO
        usuarioEmEdicao?.let { usuario ->
            DialogoFormularioUsuario(
                usuarioInicial = usuario,
                onDismiss = { usuarioEmEdicao = null },
                onSalvar = { editado, _ ->
                    if (usuariosDoBanco == null) {
                        val index = listaUsuariosLocal.indexOfFirst { it.id == editado.id }
                        if (index != -1) {
                            listaUsuariosLocal[index] = editado
                        }
                    }
                    onUsuarioAtualizado(editado)
                    usuarioEmEdicao = null
                }
            )
        }

        // DIÁLOGO DE CONFIRMAÇÃO DE EXCLUSÃO
        usuarioParaExcluir?.let { usuario ->
            DialogoConfirmarExclusao(
                usuario = usuario,
                onDismiss = { usuarioParaExcluir = null },
                onConfirmar = {
                    if (usuariosDoBanco == null) {
                        listaUsuariosLocal.removeIf { it.id == usuario.id }
                    }
                    onUsuarioDeletado(usuario.id)
                    usuarioParaExcluir = null
                }
            )
        }
    }
}

/**
 * Cabeçalho do Painel com indicador de sessão ativa, contador de cadastrados e botão de Logout.
 */
@Composable
private fun CabecalhoPainelAdmin(
    nomeAdmin: String,
    totalUsuarios: Int,
    totalAdmins: Int,
    onLogoutClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = CoresAdmin.Header,
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 14.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            // Linha superior: Título e Botão de Logout
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(CoresAdmin.VerdeSuave),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Ícone de Segurança",
                            tint = CoresAdmin.VerdeDestaque,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Painel Administrativo",
                        color = CoresAdmin.TextoBranco,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Botão de Sair com visual consistente com o da tela de perfil
                OutlinedButton(
                    onClick = onLogoutClick,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, CoresAdmin.VermelhoAlerta),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = CoresAdmin.VermelhoSuave,
                        contentColor = CoresAdmin.VermelhoAlerta
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Sair",
                        tint = CoresAdmin.VermelhoAlerta,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Sair",
                        color = CoresAdmin.VermelhoAlerta,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Linha de status de sessão e contadores
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Indicador de Sessão ("Login como: administrador")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(CoresAdmin.VerdeDestaque)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Login como: ",
                        color = CoresAdmin.TextoSecundario,
                        fontSize = 13.sp
                    )
                    Surface(
                        color = CoresAdmin.CardElevado,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = nomeAdmin.lowercase(),
                            color = CoresAdmin.VerdeDestaque,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Contador "Usuários cadastrados: 16"
                Surface(
                    color = CoresAdmin.CardElevado,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, CoresAdmin.Borda)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Usuários cadastrados: ",
                            color = CoresAdmin.TextoSecundario,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "$totalUsuarios",
                            color = CoresAdmin.TextoBranco,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (totalAdmins > 0) {
                            Text(
                                text = " • $totalAdmins admins",
                                color = CoresAdmin.LaranjaAdmin,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Seção em destaque para criação rápida de novos logins de Administrador e Usuários.
 */
@Composable
private fun SecaoDestaqueNovoAdmin(
    onCadastrarAdminClick: () -> Unit,
    onCadastrarUsuarioComumClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CoresAdmin.CardFundo),
        border = BorderStroke(1.dp, CoresAdmin.VerdeDestaque.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(CoresAdmin.VerdeSuave),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = null,
                        tint = CoresAdmin.VerdeDestaque,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Gestão de Credenciais",
                        color = CoresAdmin.TextoBranco,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Cadastre novos administradores com acesso ao painel ou novos jogadores.",
                        color = CoresAdmin.TextoSecundario,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Botão destacado para Novo Administrador
                Button(
                    onClick = onCadastrarAdminClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CoresAdmin.VerdeDestaque,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Novo Admin",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Botão secundário para Novo Jogador/Usuário
                OutlinedButton(
                    onClick = onCadastrarUsuarioComumClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, CoresAdmin.Borda),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = CoresAdmin.TextoBranco
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = CoresAdmin.TextoBranco
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Novo Jogador",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

/**
 * Seção de busca com ícone de lupa oficial do Material Design e filtro por papel.
 */
@Composable
private fun SecaoBuscaUsuarios(
    termoBusca: String,
    onTermoBuscaChange: (String) -> Unit,
    filtroPapel: PapelUsuario?,
    onFiltroPapelChange: (PapelUsuario?) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = termoBusca,
            onValueChange = onTermoBuscaChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Buscar por nome ou e-mail...",
                    color = CoresAdmin.TextoPlaceholder,
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar usuário",
                    tint = if (termoBusca.isNotBlank()) CoresAdmin.VerdeDestaque else CoresAdmin.TextoSecundario
                )
            },
            trailingIcon = {
                if (termoBusca.isNotEmpty()) {
                    IconButton(onClick = { onTermoBuscaChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Limpar busca",
                            tint = CoresAdmin.TextoSecundario
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CoresAdmin.CardFundo,
                unfocusedContainerColor = CoresAdmin.CardFundo,
                focusedBorderColor = CoresAdmin.VerdeDestaque,
                unfocusedBorderColor = CoresAdmin.Borda,
                focusedTextColor = CoresAdmin.TextoBranco,
                unfocusedTextColor = CoresAdmin.TextoBranco,
                cursorColor = CoresAdmin.VerdeDestaque
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Chips de filtro rápido
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = null,
                tint = CoresAdmin.TextoSecundario,
                modifier = Modifier.size(16.dp)
            )

            ChipFiltro(
                label = "Todos",
                selecionado = filtroPapel == null,
                onClick = { onFiltroPapelChange(null) }
            )

            ChipFiltro(
                label = "Admins",
                selecionado = filtroPapel == PapelUsuario.ADMIN,
                onClick = {
                    onFiltroPapelChange(if (filtroPapel == PapelUsuario.ADMIN) null else PapelUsuario.ADMIN)
                }
            )

            ChipFiltro(
                label = "Jogadores",
                selecionado = filtroPapel == PapelUsuario.JOGADOR,
                onClick = {
                    onFiltroPapelChange(if (filtroPapel == PapelUsuario.JOGADOR) null else PapelUsuario.JOGADOR)
                }
            )
        }
    }
}

@Composable
private fun ChipFiltro(
    label: String,
    selecionado: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selecionado,
        onClick = onClick,
        label = { Text(label, fontSize = 12.sp, fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Normal) },
        shape = RoundedCornerShape(50),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = CoresAdmin.CardFundo,
            labelColor = CoresAdmin.TextoSecundario,
            selectedContainerColor = CoresAdmin.VerdeSuave,
            selectedLabelColor = CoresAdmin.VerdeDestaque
        ),
        border = BorderStroke(
            1.dp,
            if (selecionado) CoresAdmin.VerdeDestaque else CoresAdmin.Borda
        )
    )
}

/**
 * Item individual de usuário na lista com nome, papel, status e ações de Editar e Excluir.
 */
@Composable
private fun ItemUsuarioAdmin(
    usuario: UsuarioItem,
    onEditarClick: () -> Unit,
    onExcluirClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CoresAdmin.CardFundo),
        border = BorderStroke(1.dp, CoresAdmin.Borda)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar estilizado (distingue Admin de Jogador ou exibe Foto)
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(
                        if (usuario.papel == PapelUsuario.ADMIN) Color(0xFF3B2F17) else Color(0xFF1E2E38)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (!usuario.fotoUri.isNullOrBlank()) {
                    AsyncImage(
                        model = usuario.fotoUri,
                        contentDescription = "Foto de ${usuario.nome}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = if (usuario.papel == PapelUsuario.ADMIN) Icons.Default.AdminPanelSettings else Icons.Default.Person,
                        contentDescription = null,
                        tint = if (usuario.papel == PapelUsuario.ADMIN) CoresAdmin.LaranjaAdmin else CoresAdmin.AzulInfo,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Informações do Usuário
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = usuario.nome,
                        color = CoresAdmin.TextoBranco,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = usuario.email,
                    color = CoresAdmin.TextoSecundario,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Tags de Papel e Status
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tag Papel
                    Surface(
                        color = if (usuario.papel == PapelUsuario.ADMIN) CoresAdmin.LaranjaAdmin.copy(alpha = 0.15f) else Color(0x200EA5E9),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = usuario.papel.label,
                            color = if (usuario.papel == PapelUsuario.ADMIN) CoresAdmin.LaranjaAdmin else CoresAdmin.AzulInfo,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }

                    // Tag Ativo/Inativo
                    Surface(
                        color = if (usuario.ativo) CoresAdmin.VerdeSuave else CoresAdmin.VermelhoSuave,
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = if (usuario.ativo) "Ativo" else "Inativo",
                            color = if (usuario.ativo) CoresAdmin.VerdeDestaque else CoresAdmin.VermelhoAlerta,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Ações: Editar (Lápis) e Deletar (Lixeira)
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Botão Editar
                IconButton(
                    onClick = onEditarClick,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(CoresAdmin.CardElevado)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar usuário",
                        tint = CoresAdmin.AzulInfo,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                // Botão Deletar
                IconButton(
                    onClick = onExcluirClick,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(CoresAdmin.CardElevado)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Excluir usuário",
                        tint = CoresAdmin.VermelhoAlerta,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}

/**
 * Diálogo de formulário para Criar ou Atualizar Usuário/Administrador.
 */
@Composable
private fun DialogoFormularioUsuario(
    usuarioInicial: UsuarioItem? = null,
    papelPadrao: PapelUsuario = PapelUsuario.JOGADOR,
    onDismiss: () -> Unit,
    onSalvar: (UsuarioItem, String) -> Unit
) {
    val isEdicao = usuarioInicial != null

    var nome by remember { mutableStateOf(usuarioInicial?.nome ?: "") }
    var email by remember { mutableStateOf(usuarioInicial?.email ?: "") }
    var papel by remember { mutableStateOf(usuarioInicial?.papel ?: papelPadrao) }
    var ativo by remember { mutableStateOf(usuarioInicial?.ativo ?: true) }
    var fotoUri by remember { mutableStateOf(usuarioInicial?.fotoUri) }
    var senha by remember { mutableStateOf("") }
    var erroNome by remember { mutableStateOf(false) }
    var erroEmail by remember { mutableStateOf(false) }

    val launcherFoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        fotoUri = uri?.toString()
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = CoresAdmin.Header,
            border = BorderStroke(1.dp, CoresAdmin.Borda),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Título do Diálogo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isEdicao) "Editar Cadastro" else "Novo Usuário / Admin",
                        color = CoresAdmin.TextoBranco,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = CoresAdmin.TextoSecundario
                        )
                    }
                }

                HorizontalDivider(
                    color = CoresAdmin.Borda,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                // Seleção de Foto de Perfil
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(CoresAdmin.CardElevado)
                            .clickable { launcherFoto.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (!fotoUri.isNullOrBlank()) {
                            AsyncImage(
                                model = fotoUri,
                                contentDescription = "Foto de perfil",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AddAPhoto,
                                contentDescription = "Foto",
                                tint = CoresAdmin.TextoSecundario,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Foto de Perfil",
                            color = CoresAdmin.TextoBranco,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = if (!fotoUri.isNullOrBlank()) "Alterar foto" else "Selecionar foto de perfil",
                            color = CoresAdmin.VerdeDestaque,
                            fontSize = 11.sp,
                            modifier = Modifier.clickable { launcherFoto.launch("image/*") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Campo: Nome
                CampoFormularioAdmin(
                    label = "Nome Completo",
                    valor = nome,
                    onValorChange = {
                        nome = it
                        erroNome = false
                    },
                    placeholder = "Ex: Carlos Silva",
                    isError = erroNome
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Campo: E-mail
                CampoFormularioAdmin(
                    label = "E-mail de Acesso",
                    valor = email,
                    onValorChange = {
                        email = it
                        erroEmail = false
                    },
                    placeholder = "Ex: carlos@empresa.com",
                    isError = erroEmail,
                    keyboardType = KeyboardType.Email
                )

                // Se for novo cadastro, campo de senha
                if (!isEdicao) {
                    Spacer(modifier = Modifier.height(10.dp))
                    CampoFormularioAdmin(
                        label = "Senha Provisória",
                        valor = senha,
                        onValorChange = { senha = it },
                        placeholder = "Mínimo 6 caracteres",
                        keyboardType = KeyboardType.Password
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tipo de Conta (Papel)
                SelecaoPapelUsuario(
                    papelSelecionado = papel,
                    onPapelSelecionado = { papel = it }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Switch de Ativo/Inativo
                ControleStatusUsuario(
                    ativo = ativo,
                    onAtivoChange = { ativo = it }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Botões de Ação do Diálogo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, CoresAdmin.Borda),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CoresAdmin.TextoSecundario)
                    ) {
                        Text("Cancelar", fontSize = 14.sp)
                    }

                    Button(
                        onClick = {
                            val nomeInvalido = nome.isBlank()
                            val emailInvalido = email.isBlank() || !email.contains("@")
                            erroNome = nomeInvalido
                            erroEmail = emailInvalido

                            if (!nomeInvalido && !emailInvalido) {
                                val itemFinal = (usuarioInicial ?: UsuarioItem(
                                    nome = nome.trim(),
                                    email = email.trim(),
                                    papel = papel,
                                    ativo = ativo,
                                    fotoUri = fotoUri
                                )).copy(
                                    nome = nome.trim(),
                                    email = email.trim(),
                                    papel = papel,
                                    ativo = ativo,
                                    fotoUri = fotoUri
                                )
                                onSalvar(itemFinal, senha)
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CoresAdmin.VerdeDestaque,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = if (isEdicao) "Atualizar" else "Salvar",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CampoFormularioAdmin(
    label: String,
    valor: String,
    onValorChange: (String) -> Unit,
    placeholder: String,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = CoresAdmin.TextoSecundario,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = valor,
            onValueChange = onValorChange,
            isError = isError,
            placeholder = { Text(placeholder, color = CoresAdmin.TextoPlaceholder, fontSize = 13.sp) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CoresAdmin.Fundo,
                unfocusedContainerColor = CoresAdmin.Fundo,
                focusedBorderColor = CoresAdmin.VerdeDestaque,
                unfocusedBorderColor = CoresAdmin.Borda,
                focusedTextColor = CoresAdmin.TextoBranco,
                unfocusedTextColor = CoresAdmin.TextoBranco
            )
        )
    }
}

@Composable
private fun SelecaoPapelUsuario(
    papelSelecionado: PapelUsuario,
    onPapelSelecionado: (PapelUsuario) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Nível de Acesso",
            color = CoresAdmin.TextoSecundario,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FilterChip(
                selected = papelSelecionado == PapelUsuario.JOGADOR,
                onClick = { onPapelSelecionado(PapelUsuario.JOGADOR) },
                label = { Text("Jogador") },
                modifier = Modifier.weight(1f),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = CoresAdmin.Fundo,
                    labelColor = CoresAdmin.TextoSecundario,
                    selectedContainerColor = Color(0x200EA5E9),
                    selectedLabelColor = CoresAdmin.AzulInfo
                ),
                border = BorderStroke(
                    1.dp,
                    if (papelSelecionado == PapelUsuario.JOGADOR) CoresAdmin.AzulInfo else CoresAdmin.Borda
                )
            )

            FilterChip(
                selected = papelSelecionado == PapelUsuario.ADMIN,
                onClick = { onPapelSelecionado(PapelUsuario.ADMIN) },
                label = { Text("Administrador") },
                modifier = Modifier.weight(1f),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = CoresAdmin.Fundo,
                    labelColor = CoresAdmin.TextoSecundario,
                    selectedContainerColor = CoresAdmin.LaranjaAdmin.copy(alpha = 0.2f),
                    selectedLabelColor = CoresAdmin.LaranjaAdmin
                ),
                border = BorderStroke(
                    1.dp,
                    if (papelSelecionado == PapelUsuario.ADMIN) CoresAdmin.LaranjaAdmin else CoresAdmin.Borda
                )
            )
        }
    }
}

@Composable
private fun ControleStatusUsuario(
    ativo: Boolean,
    onAtivoChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Status da Conta",
                color = CoresAdmin.TextoBranco,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = if (ativo) "Usuário ativo e autorizado" else "Usuário desativado",
                color = CoresAdmin.TextoSecundario,
                fontSize = 11.sp
            )
        }

        Switch(
            checked = ativo,
            onCheckedChange = onAtivoChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = CoresAdmin.VerdeDestaque,
                uncheckedThumbColor = CoresAdmin.TextoSecundario,
                uncheckedTrackColor = CoresAdmin.Fundo
            )
        )
    }
}

/**
 * Diálogo seguro de confirmação antes de deletar um usuário.
 */
@Composable
private fun DialogoConfirmarExclusao(
    usuario: UsuarioItem,
    onDismiss: () -> Unit,
    onConfirmar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CoresAdmin.Header,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = CoresAdmin.VermelhoAlerta,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Confirmar Exclusão",
                    color = CoresAdmin.TextoBranco,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Text(
                text = "Tem certeza de que deseja remover permanentemente o usuário \"${usuario.nome}\" (${usuario.email})? Esta ação não pode ser desfeita.",
                color = CoresAdmin.TextoSecundario,
                fontSize = 14.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirmar,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CoresAdmin.VermelhoAlerta,
                    contentColor = Color.White
                )
            ) {
                Text("Excluir", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = CoresAdmin.TextoSecundario)
            }
        }
    )
}

/**
 * Estado vazio exibido quando a busca não retorna resultados.
 */
@Composable
private fun CardVazioBusca(termoBusca: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CoresAdmin.CardFundo),
        border = BorderStroke(1.dp, CoresAdmin.Borda)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = CoresAdmin.TextoSecundario,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Nenhum usuário encontrado",
                color = CoresAdmin.TextoBranco,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (termoBusca.isNotBlank()) "Não encontramos resultados para \"$termoBusca\"" else "Nenhum registro corresponde ao filtro selecionado.",
                color = CoresAdmin.TextoSecundario,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TelaPainelAdminPreview() {
    TelaPainelAdmin()
}
