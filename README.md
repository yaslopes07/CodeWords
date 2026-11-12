# 🔤 CodeWords

O **CodeWords** é um jogo de caça-palavras desenvolvido para dispositivos Android, no qual os jogadores devem encontrar palavras escondidas em 
matrizes de letras. Com diferentes fases e desafios, o aplicativo proporciona uma experiência divertida, estimulando a concentração, a percepção
visual e o raciocínio.

---

## 📌 Sobre o Projeto

Este projeto foi desenvolvido como parte de uma atividade acadêmica voltada ao desenvolvimento de aplicativos móveis para Android.

O objetivo principal é criar um jogo de caça-palavras interativo, no qual os usuários possam testar suas habilidades de observação e encontrar 
palavras relacionadas a diferentes conceitos, especialmente do universo da programação e tecnologia.

## 🛠️ Tecnologias e Arquitetura

O projeto foi desenvolvido utilizando ferramentas e recursos do ecossistema moderno de desenvolvimento Android:

- **Linguagem de Programação:** Kotlin.
- **Interface Gráfica:** Jetpack Compose e Material 3.
- **Arquitetura:** organização baseada em ViewModel.
- **Gerenciamento de Estado:** Kotlin Coroutines e StateFlow.
- **Persistência Local:** Room Database (SQLite).
- **Processamento de Código:** KSP (Kotlin Symbol Processing).
- **Build:** Gradle com Kotlin DSL.

---

## ✨ Principais Funcionalidades

- 🔐 **Cadastro e Login:** criação de contas e acesso ao aplicativo.
- 🔤 **Jogo de Caça-Palavras:** encontre palavras escondidas em uma matriz de letras.
- 🎮 **Fases e Desafios:** avance por quatro fases diferentes do jogo.
- ⏱️ **Cronômetro:** acompanhe o tempo gasto para concluir cada fase.
- 🏆 **Registro de Desempenho:** acompanhe os tempos e resultados das partidas.
- 👤 **Perfil do Jogador:** consulte suas informações e seu progresso.
- 📊 **Melhores Tempos:** visualize seus melhores resultados em cada fase.
- 💾 **Salvamento Local:** mantenha os dados dos usuários e resultados armazenados no dispositivo.

### 🧩 Fases do Jogo

O CodeWords possui quatro fases:

| Fase | Nome |
|---|---|
| 1 | Primeiros Passos |
| 2 | Conexões |
| 3 | Precisão |
| 4 | Desafio |

Cada fase apresenta um conjunto de palavras que devem ser encontradas pelo jogador na matriz de letras.

---

## 💻 Como Executar o Projeto

### Pré-requisitos

- Android Studio instalado.
- JDK 11 ou versão compatível com o Gradle.
- Android SDK configurado.
- Emulador Android ou dispositivo físico disponível.
- Conexão com a internet para sincronização das dependências.

### Passo a Passo

**1. Abra o Android Studio**

Com o Android Studio aberto, acesse:

`File → New → Project from Version Control...`

**2. Clone o repositório**

Na janela **Clone Repository**, selecione **Git** em *Version control*.

No campo **URL**, cole o endereço do repositório:

```text
https://github.com/yaslopes07/CodeWords.git
```

Selecione a pasta onde o projeto será salvo e clique em **Clone**.

**3. Sincronize o projeto**

Aguarde o Android Studio carregar os arquivos e sincronizar as dependências do Gradle.

**4. Execute o aplicativo**

Selecione um emulador Android ou conecte um dispositivo físico com a depuração USB ativada.

Clique no botão **Run ▶** (Shift + F10) para compilar e iniciar o CodeWords.

## 💾 Armazenamento de Dados

O aplicativo utiliza o **Room Database**, baseado em SQLite, para armazenar informações localmente no dispositivo.

Entre os dados armazenados estão:

- Informações dos usuários cadastrados.
- Progresso e conclusão das fases.
- Tempos registrados durante as partidas.
- Melhores resultados de cada jogador.

> **⚠️ Observação:** o projeto possui finalidade acadêmica e demonstrativa. A autenticação e o armazenamento são realizados localmente.

---

## 👥 Equipe de Desenvolvimento

- **[Ana Júlia Nicolack](https://github.com/anajulianicolack-hub)**
- **[Eduardo Fogaça Bader](https://github.com/pardalzincwb)**
- **[Pedro Henrique Alves](https://github.com/alvespedroo)**
- **[Vitor Matheus Gauer](https://github.com/Vitor-Gauer)**
- **[Yasmin da Rocha Lopes](https://github.com/yaslopes07)**

