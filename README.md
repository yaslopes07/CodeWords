
# 🔤 CodeWords

**CodeWords** é um jogo de caça-palavras para Android com temática de tecnologia e programação. O jogador procura palavras escondidas em matrizes de letras, conclui fases e acompanha seu desempenho por meio de tempos e recordes.

> Projeto acadêmico desenvolvido para a disciplina de **Desenvolvimento de Aplicativos Móveis (Android)**.

---

## 🎥 Demonstração

Assista à demonstração do aplicativo:

[**Abrir vídeo de demonstração**](https://drive.google.com/file/d/1iBJGm_dWZC59Mfr6mbkKnhn1B0Z_d77x/view?usp=sharing)

Acesse o desenho inicial das telas: 

[**Abrir desenho das telas**](https://drive.google.com/drive/folders/1EsIhhV7BFaS6aYo577g55-r7ScetdpQ3?usp=sharing)

---

## Funcionalidades

-  **Cadastro e login:** criação de conta e autenticação local.
-  **Caça-palavras:** seleção de palavras escondidas em matrizes de letras.
-  **Quatro fases:** desafios com palavras relacionadas à tecnologia e programação.
-  **Cronômetro:** medição do tempo de conclusão das fases.
-  **Recordes e ranking:** consulta aos melhores tempos registrados pelo jogador.
-  **Perfil:** visualização de informações e progresso nas fases.
-  **Painel administrativo:** área para gerenciamento dos usuários.
-  **Persistência local:** armazenamento de usuários e progresso no banco de dados do dispositivo.

### 🧩 Fases

| Fase | Nome |
|:---:|---|
| 1 | Primeiros Passos |
| 2 | Conexões |
| 3 | Precisão |
| 4 | Desafio |

---

## 🛠️ Tecnologias

- **Kotlin** — linguagem principal.
- **Jetpack Compose** — construção das interfaces.
- **Material 3** — componentes visuais.
- **ViewModel** — organização da lógica de apresentação.
- **Kotlin Coroutines e StateFlow** — operações assíncronas e gerenciamento de estado.
- **Room (SQLite)** — persistência de dados local.
- **KSP (Kotlin Symbol Processing)** — processamento de anotações do Room.
- **Coil** — carregamento de imagens.
- **Gradle Kotlin DSL** — configuração e build do projeto.

---

##  Organização do projeto

O código-fonte está organizado principalmente em:

```text
CodeWords/
├── app/
│   └── src/
│       └── main/
│           ├── java/com/example/myapplication/
│           │   ├── data/
│           │   │   ├── local/          # Banco Room, entidades e DAOs
│           │   │   └── repository/     # Repositórios
│           │   ├── ui/
│           │   │   ├── state/         # Estados das telas
│           │   │   ├── telas/         # Interfaces do aplicativo
│           │   │   └── theme/         # Tema visual
│           │   ├── viewModel/         # Lógica e estado das telas
│           │   └── MainActivity.kt
│           └── res/                   # Recursos visuais
└── README.md
```

---

## 💻 Como executar

### Pré-requisitos

- Android Studio.
- Android SDK configurado.
- JDK compatível com a versão do Android Gradle Plugin utilizada pelo projeto.
- Emulador Android ou dispositivo físico com depuração USB habilitada.

### Passos

1. **Clone o repositório:**

   ```bash
   git clone https://github.com/yaslopes07/CodeWords.git
   ```

2. **Abra o projeto** no Android Studio.

3. **Aguarde a sincronização** do Gradle e o download das dependências.

4. **Execute o aplicativo:** selecione um emulador ou dispositivo Android e clique em **Run ▶**.

---

## 💾 Armazenamento e observações

O CodeWords utiliza o Room, baseado em SQLite, para armazenar localmente os dados dos usuários e o progresso nas fases.

> [!WARNING]
> A autenticação e a persistência dos dados são realizadas localmente no dispositivo. O projeto possui finalidade acadêmica e demonstrativa, não oferecendo serviços de autenticação ou sincronização em nuvem.

---

## 👥 Equipe de desenvolvimento

- [Ana Júlia Nicolack](https://github.com/anajulianicolack-hub)
- [Eduardo Fogaça Bader](https://github.com/pardalzincwb)
- [Pedro Henrique Alves](https://github.com/alvespedroo)
- [Vitor Matheus Gauer](https://github.com/Vitor-Gauer)
- [Yasmin da Rocha Lopes](https://github.com/yaslopes07)
