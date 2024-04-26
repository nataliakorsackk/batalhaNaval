import java.util.Scanner;

public class BatalhaNaval {
    /**
     * Tabuleiro do jogo. Cada posição do tabuleiro pode conter:
     * - '-' para água
     * - 'X' para acerto
     * - 'O' para erro
     * - 'B' para barco
     */
    private char[][] tabuleiro;
    /**
     * Nome do jogador
     */
    private String nome;

    /**
     * Tamanho do tabuleiro
     */
    private final int tamanho = 10;
    /**
     * Tamanhos dos barcos
     */
    private final int[] tamanhosBarcos = {4,3,3,2,2,2,1,1,1,1};

    /**
     * Construtor da classe BatalhaNaval
     * @param nome
     */
    public BatalhaNaval(String nome) {
        tabuleiro = new char[tamanho][tamanho];
        inicializarTabuleiro();
        this.nome = nome;
    }

    /**
     * Inicializa o tabuleiro com água
     */
    private void inicializarTabuleiro() {
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                tabuleiro[i][j] = '-';
            }
        }
    }

    /**
     * Mostra o tabuleiro do jogo
     *
     * @param ocultarNavios se true, os navios não são mostrados
     */
    public void mostrarTabuleiro(boolean ocultarNavios) {
        // Mostra a primeira linha com os números
        System.out.println("   1 2 3 4 5 6 7 8 9 10");

        // Mostra o tabuleiro
        for (int i = 0; i < tamanho; i++) {
            System.out.print((char) ('A' + i) + "  ");
            for (int j = 0; j < tamanho; j++) {
                if (ocultarNavios && tabuleiro[i][j] != '-' && tabuleiro[i][j] != 'X' && tabuleiro[i][j] != 'O') {
                    System.out.print('-' + " ");
                } else {
                    System.out.print(tabuleiro[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Aloca os barcos no tabuleiro
     *
     * @param automatico se true, aloca os barcos automaticamente
     */
    public void alocarBarcos(boolean automatico) {
        if (automatico) {
            alocarBarcosAutomaticamente();
        } else {
            alocarBarcosManualmente();
        }

        mostrarTabuleiro(false);
    }

    /**
     * Aloca os barcos no tabuleiro de forma automática e aleatória
     */
    private void alocarBarcosAutomaticamente() {
        for (int i = 0; i < tamanhosBarcos.length; i++) {
            int tamanhoBarco = tamanhosBarcos[i];
            boolean alocado = false;

            while (!alocado) {
                //Gera posições aleatórias para o barco
                int linha = (int) (Math.random() * tamanho);
                int coluna = (int) (Math.random() * tamanho);
                boolean horizontal = Math.random() < 0.5;

                if (podeAlocar(linha, coluna, tamanhoBarco, horizontal)) {
                    alocar(linha, coluna, tamanhoBarco, horizontal, 'B');
                    alocado = true;
                }
            }
        }
    }

    /**
     * Aloca os barcos no tabuleiro de forma manual lendo a entrada do usuário
     */
    private void alocarBarcosManualmente() {
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < tamanhosBarcos.length; i++) {
            int tamanhoBarco = tamanhosBarcos[i];
            boolean alocado = false;

            while (!alocado) {
                mostrarTabuleiro(false);
                System.out.println("Digite a posição inicial do barco de tamanho " + tamanhoBarco + ": ");
                String posicao = scanner.nextLine().toUpperCase();
                int linha = posicao.charAt(0) - 'A';
                int coluna = Integer.parseInt(posicao.substring(1)) - 1;

                System.out.println("Horizontal (H) ou Vertical (V)?");
                String orientacao = scanner.nextLine().toUpperCase();

                boolean horizontal = orientacao.equals("H");
                if (podeAlocar(linha, coluna, tamanhoBarco, horizontal)) {
                    alocar(linha, coluna, tamanhoBarco, horizontal, 'B');
                    alocado = true;
                } else {
                    System.out.println("Posição inválida! Tente novamente.");
                }
            }
        }
    }

    /**
     * Verifica se é possível alocar um barco na posição especificada
     *
     * @param linha
     * @param coluna
     * @param tamanhoBarco
     * @param horizontal
     *
     * @return true se for possível alocar o barco, false caso contrário
     */
    private boolean podeAlocar(int linha, int coluna, int tamanhoBarco, boolean horizontal) {
        if (horizontal) {
            if (coluna + tamanhoBarco > tamanho) {
                return false;
            }
            for (int i = coluna; i < coluna + tamanhoBarco; i++) {
                if (tabuleiro[linha][i] != '-') {
                    return false;
                }
            }
        } else {
            if (linha + tamanhoBarco > tamanho) {
                return false;
            }
            for (int i = linha; i < linha + tamanhoBarco; i++) {
                if (tabuleiro[i][coluna] != '-') {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Aloca um barco na posição especificada
     *
     * @param linha
     * @param coluna
     * @param tamanhoBarco
     * @param horizontal
     * @param simbolo
     */
    private void alocar(int linha, int coluna, int tamanhoBarco, boolean horizontal, char simbolo) {
        if (horizontal) {
            for (int i = coluna; i < coluna + tamanhoBarco; i++) {
                tabuleiro[linha][i] = simbolo;
            }
        } else {
            for (int i = linha; i < linha + tamanhoBarco; i++) {
                tabuleiro[i][coluna] = simbolo;
            }
        }
    }

    /**
     * Realiza a jogada do jogador
     *
     * @param atual
     * @param adversario
     *
     * @return true se o jogador deve continuar jogando, false caso contrário
     */
    public boolean jogar(BatalhaNaval atual, BatalhaNaval adversario) {
        Scanner scanner = new Scanner(System.in);
        Boolean continua = false;
        int linha = 0;
        int coluna = 0;

        System.out.println("Vez do jogador " + atual.nome);

        if (atual.nome == "Computador") {
            System.out.println("Computador está pensando...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Boolean isValida = false;

            while (!isValida) {
                linha = (int) (Math.random() * tamanho);
                coluna = (int) (Math.random() * tamanho);

                if (adversario.tabuleiro[linha][coluna] == 'X' || adversario.tabuleiro[linha][coluna] == 'O') {
                    isValida = false;
                } else {
                    isValida = true;
                }
            }

        } else {
            System.out.println("Seu tabuleiro:");
            atual.mostrarTabuleiro(false);

            System.out.println("Tabuleiro do adversário:");
            adversario.mostrarTabuleiro(true);

            System.out.println("Digite a posição da jogada: ");
            String posicao = scanner.nextLine().toUpperCase();
            linha = posicao.charAt(0) - 'A';
            coluna = Integer.parseInt(posicao.substring(1)) - 1;
        }

        if (linha < 0 || linha >= tamanho || coluna < 0 || coluna >= tamanho) {
            System.out.println("Posição inválida! Tente novamente.");
            continua = true;

        } else if (adversario.tabuleiro[linha][coluna] == 'X' || adversario.tabuleiro[linha][coluna] == 'O') {
            System.out.println("Jogada inválida! Tente novamente.");
            continua = true;

        } else if (adversario.tabuleiro[linha][coluna] != '-') {
            System.out.println("Você acertou um navio inimigo!");
            adversario.tabuleiro[linha][coluna] = 'X'; // Acerto
            continua = true;

        } else {
            System.out.println("Você atingiu a água.");
            adversario.tabuleiro[linha][coluna] = 'O'; // Água
        }

        System.out.println("Pressione Enter para continuar...");
        scanner.nextLine(); // Aguarda a entrada do teclado
        return continua;
    }

    /**
     * Verifica se o jogo terminou
     *
     * @return true se o jogo terminou, false caso contrário
     */
    private boolean jogoTerminou() {
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                if (tabuleiro[i][j] != '-' && tabuleiro[i][j] != 'X' && tabuleiro[i][j] != 'O') {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Limpa a tela do console
     */
    public static void limpaTela() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                Runtime.getRuntime().exec("cmd /c cls");
            } else {
                // No MacOS e no Linux apliquei uma sequência de escape para limpar o console
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (final Exception e) {
            // Tratar exceções
            System.out.println("Erro ao limpar o console: " + e.getMessage());
        }
    }

    /**
     * Método principal
     *
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bem-vindo ao Jogo de Batalha Naval!");
        System.out.println("Modo de jogo:");
        System.out.println("1 - Jogador vs Jogador");
        System.out.println("2 - Jogador vs Computador");

        int modo = scanner.nextInt();
        scanner.nextLine();

        String nomeJogador1;

        System.out.println("Digite o nome do Jogador 1:");
        nomeJogador1 = scanner.nextLine();

        BatalhaNaval jogador1 = new BatalhaNaval(nomeJogador1);

        System.out.println(jogador1.nome + ", escolha o modo de alocação de barcos:");
        System.out.println("1 - Automático");
        System.out.println("2 - Manual");
        int opcao1 = scanner.nextInt();
        boolean automatico1 = opcao1 == 1;
        jogador1.alocarBarcos(automatico1);

        scanner.nextLine();
        System.out.println("Pressione Enter para continuar...");
        scanner.nextLine(); // Aguarda a entrada do teclado

        limpaTela();

        BatalhaNaval jogador2;
        if (modo == 1){
            String nomeJogador2;

            System.out.println("Digite o nome do Jogador 2:");
            nomeJogador2 = scanner.nextLine();

            jogador2 = new BatalhaNaval(nomeJogador2);

            System.out.println(jogador2 + ", escolha o modo de alocação de barcos:");
            System.out.println("1 - Automático");
            System.out.println("2 - Manual");
            int opcao2 = scanner.nextInt();
            boolean automatico2 = opcao2 == 1;
            jogador2.alocarBarcos(automatico2);

            scanner.nextLine();
            System.out.println("Pressione Enter para continuar...");
            scanner.nextLine();

        } else {
            jogador2 = new BatalhaNaval("Computador");

            jogador2.alocarBarcos(true);
        }

        limpaTela();

        BatalhaNaval jogadorAtual = jogador1;
        BatalhaNaval jogadorAdversario = jogador2;

        while (!jogador1.jogoTerminou() && !jogador2.jogoTerminou()) {
            limpaTela();
            boolean continua = jogadorAtual.jogar(jogadorAtual, jogadorAdversario);

            if (!continua) {
                BatalhaNaval temp = jogadorAtual;
                jogadorAtual = jogadorAdversario;
                jogadorAdversario = temp;
            }
        }

        limpaTela();
        System.out.println("Fim de jogo!");
        if (jogador1.jogoTerminou()) {
            System.out.println(jogador2.nome + " venceu!");
        } else {
            System.out.println(jogador1.nome + " venceu!");
        }
    }
}
