package org.academiadecodigo.bootcamp;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.List;
import static org.academiadecodigo.bootcamp.AllStrings.*;

public class Server {

    private ServerSocket serverSocket;
    private Socket playerSocket;
    public final static int port = 6666;
    public final static String host = "localhost";
    private List<ServerHelper> players = Collections.synchronizedList(new ArrayList<ServerHelper>());

    public void start(int port) throws RuntimeException, IOException {


        try {
            System.out.println("\nEste jogo funciona com múltiplos jogadores conectados. Para jogar basta juntar-se através do netcat na porta 6666.\n" + "As tuas escolhas importam. Escolhe o teu destino através das teclas 1, 2, 3 e 4. O objectivo é sair do convento com vida.\n\n");
            System.out.println("A conectar com o calabouço... " + port);
            serverSocket = new ServerSocket(port);

            while (true) {
                Socket playerSocket = serverSocket.accept();
                System.out.println(playerSocket);
                ServerHelper player = new ServerHelper(playerSocket);
                players.add(player);

                Thread thread = new Thread(player);
                thread.start();

            }
        } catch (IOException e) {
            System.out.println("Um espertalhão fechou o terminal a ver se dava porcaria.");
        }
    }

    private class ServerHelper implements Runnable {

        private Socket playerSocket;
        PrintStream printStream;
        Prompt prompt;
        MenuInputScanner menu;

        private ServerHelper(Socket playerSocket) throws IOException {
            this.playerSocket = playerSocket;
            printStream = new PrintStream(playerSocket.getOutputStream());
            prompt = new Prompt(playerSocket.getInputStream(), printStream);
        }

        @Override
        public void run() {

            while (!playerSocket.isClosed()) {
                startGame();
            }
        }

        public void startGame() {
            intro();
        }

        public void intro() {
                printStream.println(INSTRUCOES);

            try {
                StringInputScanner introMessage = new StringInputScanner();
                printStream.println(INTRO);

                String[] introAnswer = {
                        "Sim",
                        "Não"
                };
                menu = new MenuInputScanner(introAnswer);
                menu.setMessage("Sentes-te com forças para avançar na aventura?");

                switch (prompt.getUserInput(menu)) {

                    //intro sim ou nao
                    case 1:
                        menu1();
                        break;
                    case 2:
                        textBreak();
                        printStream.println("A vida é dura para quem é mole. \n\n" +
                                "\n" +
                                " ▄▄ •  ▄▄▄· • ▌ ▄ ·. ▄▄▄ .         ▌ ▐·▄▄▄ .▄▄▄  \n" +
                                "▐█ ▀ ▪▐█ ▀█ ·██ ▐███▪▀▄.▀·   ▄█▀▄ ▪█·█▌▀▄.▀·▀▄ █·\n" +
                                "▄█ ▀█▄▄█▀▀█ ▐█ ▌▐▌▐█·▐▀▀▪▄  ▐█▌.▐▌▐█▐█•▐▀▀▪▄▐▀▀▄ \n" +
                                "▐█▄▪▐█▐█▪ ▐▌██ ██▌▐█▌▐█▄▄▌  ▐█▌.▐▌ ███ ▐█▄▄▌▐█•█▌\n" +
                                "·▀▀▀▀  ▀  ▀ ▀▀  █▪▀▀▀ ▀▀▀    ▀█▄▀▪. ▀   ▀▀▀ .▀  ▀\n\n");
                        gameOver();
                        break;
                }
            } catch (Exception e) {
                System.out.println("Um espertalhão fechou o terminal a ver se dava porcaria.");
                gameOver();
            }
        }

        public void menu1() {

            textBreak();

            menu = new MenuInputScanner(FIRST_MENU_ANSWERS);
            menu.setMessage(INTRO_MENU_1);

            switch (prompt.getUserInput(menu)) {
                case 1:
                    textBreak();
                    printStream.println(MENU_1_CHOICE_1);
                    menu2();
                    break;
                case 2:
                    textBreak();
                    printStream.println(MENU_1_CHOICE_2);
                    menu2();
                    break;
                case 3:
                    textBreak();
                    printStream.println(MENU_1_CHOICE_3);
                    menu2();
                    break;
                case 4:
                    textBreak();
                    printStream.println(MENU_1_CHOICE_4);
                    menu2();
                    break;
            }
        }

        public void menu2() {

            menu = new MenuInputScanner(SECOND_MENU_ANSWERS);
            menu.setMessage(INTRO_MENU_2);

            switch (prompt.getUserInput(menu)) {
                case 1:
                    //direita/cantina
                    menu2Direita();
                    break;
                case 2:
                    //ESQUERDA
                    menu2Esquerda();
                    break;
                case 3:
                    menu2Frente();
                    break;
                case 4:
                    printStream.println(MENU_2_OPTION_4);//tras
                    //morreu -- done
                    gameOver();
                    break;
            }

        }
        public void menu2Frente() {

            textBreak();
            menu = new MenuInputScanner(SECOND_MENU_FRENTE_ANSWERS);
            menu.setMessage(MENU_2_OPTION_3);

            switch (prompt.getUserInput(menu)) {
                case 1:
                    textBreak();
                    menu2Voltar();
                    break;
                case 2:
                    printStream.println(MENU_2_FRENTE_OPTION_2);
                    gameOver();
                    break;
            }
        }

        public void menu2Voltar(){

            menu = new MenuInputScanner(SECOND_MENU_ANSWERS);
            menu.setMessage(MENU_2_VOLTAR);

            switch (prompt.getUserInput(menu)) {
                case 1:
                    //direita/cantina
                    menu2Direita();
                    break;
                case 2:
                    //ESQUERDA
                    menu2Esquerda();
                    break;
                case 3:
                    menu2Frente();
                    break;
                case 4:
                    printStream.println(MENU_2_OPTION_4);//tras
                    //morreu -- done
                    gameOver();
                    break;
            }
        }
        public void menu2Esquerda() {

            textBreak();

            menu = new MenuInputScanner(SECOND_MENU_ESQUERDA_ANSWERS);

            menu.setMessage(MENU_2_OPTION_2);

            switch (prompt.getUserInput(menu)) {
                case 1:
                    menuCorredor(); //escadas corredor
                    break;
                case 2:
                    menuEscadasDireita();//escadas direita
                    break;
            }
        }

        public void menu2Direita() {

            textBreak();

            menu = new MenuInputScanner(SECOND_MENU_DIREITA_ANSWERS);

            menu.setMessage(MENU_2_OPTION_1);

            switch (prompt.getUserInput(menu)) {
                case 1:
                    printStream.println(MENU_2_CANTINA_OPTION_1);//despensa NAO ABRIR
                    //morre -- done
                    gameOver();
                    break;
                case 2:
                    printStream.println(MENU_2_CANTINA_OPTION_2);//outra despensa
                    menu2Esquerda();
                    break;
                case 3:
                    printStream.println(MENU_2_CANTINA_OPTION_3);//arrombar a porta
                    gameOver();
                    break;
                case 4:
                    printStream.println(MENU_2_CANTINA_OPTION_4);//gritar socorro
                    gameOver();
                    break;
            }
        }
        public void menuEscadasDireita() {

            textBreak();

            menu = new MenuInputScanner(SECOND_MENU_ESCADAS_DIREITA_ANSWERS);

            menu.setMessage(MENU_3_ESCADAS_DIREITA);

            switch (prompt.getUserInput(menu)) {
                case 1:
                    printStream.println(MENU_3_ESCADAS_DIREITA_OPTION_1);//morres
                    gameOver();
                    break;
                case 2:
                    menu5EscadaDireita();
                    break;
                case 3:
                    textBreak();
                    printStream.println(MENU_3_ESCADAS_DIREITA_OPTION_3);
                    menuEscadasDireitaOption3(); //reza e volta ao mesmo sitio
                    break;
            }
        }

        public void menuEscadasDireitaOption3() {

            menu = new MenuInputScanner(SECOND_MENU_ESCADAS_DIREITA_OPTION_3_ANSWERS);

            menu.setMessage(MENU_3_ESCADAS_DIREITA);

            switch (prompt.getUserInput(menu)) {
                case 1:
                    printStream.println(MENU_3_ESCADAS_DIREITA_OPTION_1);//morres
                    gameOver();
                    break;
                case 2:
                    printStream.println(MENU_3_ESCADAS_DIREITA_OPTION_2);
                    menu5EscadaDireita();
                    break;
            }
        }
        public void menuCorredor() {

            textBreak();

            menu = new MenuInputScanner(SECOND_MENU_CORREDOR_ANSWERS);

            menu.setMessage(MENU_3_ESCADAS_CORREDOR);

            switch (prompt.getUserInput(menu)) {
                case 1:
                    menuCorredor1();
                    break;
                case 2:
                    menuCorredor2();
                    break;
                case 3:
                    menuCorredor3();
                    break;
            }
        }

        public void menuCorredor1() {
            textBreak();
            printStream.println(MENU_3_ESCADAS_CORREDOR_OPTION_1);
            menu4();
        }

        public void menuCorredor2() {
            textBreak();
            printStream.println(MENU_3_ESCADAS_CORREDOR_OPTION_2);
            menu4();
        }

        public void menuCorredor3() {
            textBreak();
            printStream.println(MENU_3_ESCADAS_CORREDOR_OPTION_3);
            menu4();
        }

        public void menu4() {

            menu = new MenuInputScanner(MENU_TRASEIRAS_ANSWERS);

            menu.setMessage(INTRO_MENU_4);

            switch (prompt.getUserInput(menu)) {
                case 1:
                    menu5();
                    break;
                case 2:
                    menuWC();
            }
        }

        public void menuWC() {

            textBreak();

            menu = new MenuInputScanner(MENU_WC_ANSWERS);

            menu.setMessage(MENU_4_OPTION_2);

            switch (prompt.getUserInput(menu)) {
                case 1:
                    printStream.println(MENU_4_WC_1);//morres
                    gameOver();
                    break;
                case 2:
                    printStream.println(MENU_4_WC_2);
                    menu5WC();
            }
        }
        public void menu5WC(){

                textBreak();

                menu = new MenuInputScanner(MENU_CULTO_ANSWERS);

                menu.setMessage(INTRO_MENU_5_WC);

                switch (prompt.getUserInput(menu)) {
                    case 1:
                        menuCantico();
                        break;
                    case 2:
                        menuDanca();
                        break;
                    case 3:
                        menuContorcionismo();
                        break;
                    case 4:
                        menuInvocacao();
                        break;
                }
        }
        public void menu5() {

            textBreak();

            menu = new MenuInputScanner(MENU_CULTO_ANSWERS);

            menu.setMessage(INTRO_MENU_5);

            switch (prompt.getUserInput(menu)) {
                case 1:
                    menuCantico();
                    break;
                case 2:
                    menuDanca();
                    break;
                case 3:
                    menuContorcionismo();
                    break;
                case 4:
                    menuInvocacao();
                    break;
            }
        }
        public void menu5EscadaDireita() {

            textBreak();

            menu = new MenuInputScanner(MENU_CULTO_ANSWERS);

            menu.setMessage(INTRO_MENU_5_ESCADAS_DIREITA);

            switch (prompt.getUserInput(menu)) {
                case 1:
                    menuCantico();
                    break;
                case 2:
                    menuDanca();
                    break;
                case 3:
                    menuContorcionismo();
                    break;
                case 4:
                    menuInvocacao();
                    break;
            }
        }
        public void menuCantico() {

            textBreak();

            menu = new MenuInputScanner(MENU_CANTICO_ANSWERS);

            menu.setMessage(MENU_5_CANTICO);

            switch (prompt.getUserInput(menu)) {
                case 1:
                    textBreak();
                    printStream.println(MENU_5_CANTICO_OPTION_1);
                    menu6();
                    break;
                case 2:
                    printStream.println(MENU_5_CANTICO_OPTION_2);
                    gameOver();
                    break;
            }
        }
        public void menuDanca() {

            textBreak();

            menu = new MenuInputScanner(MENU_DANCA_ANSWERS);

            menu.setMessage(MENU_5_DANCA);

            switch (prompt.getUserInput(menu)) {
                case 1:
                    textBreak();
                    printStream.println(MENU_5_DANCA_OPTION_1);
                    menu6();
                    break;
                case 2:
                    printStream.println(MENU_5_DANCA_OPTION_2);
                    gameOver();
                    break;
            }
        }
        public void menuContorcionismo() {

            textBreak();

            menu = new MenuInputScanner(MENU_CONTORCIONISMO_ANSWERS);

            menu.setMessage(MENU_5_CONTORCIONISMO);

            switch (prompt.getUserInput(menu)) {
                case 1:
                    textBreak();
                    printStream.println(MENU_5_CONTORCIONISMO_OPTION_1);
                    menu6();
                    break;
                case 2:
                    printStream.println(MENU_5_CONTORCIONISMO_OPTION_2);
                    gameOver();
                    break;
            }
        }
        public void menuInvocacao() {

            textBreak();

            menu = new MenuInputScanner(MENU_INVOCACAO_ANSWERS);

            menu.setMessage(MENU_5_INVOCACAO);

            switch (prompt.getUserInput(menu)) {
                case 1:
                    textBreak();
                    printStream.println(MENU_5_INVOCACAO_OPTION_1);
                    menu6();
                    break;
                case 2:
                    printStream.println(MENU_5_INVOCACAO_OPTION_2);
                    gameOver();
                    break;
            }
        }
        public void menu6() {

            menu = new MenuInputScanner(MENU_PROPOSTA_ANSWERS);

            menu.setMessage(MENU_6);

            switch (prompt.getUserInput(menu)) {
                case 1:
                    textBreak();
                    printStream.println(MENU_6_OPTION_1);
                    gameOver();
                    break;
                case 2:
                    textBreak();
                    printStream.println(MENU_6_OPTION_2);//morre
                    gameOver();
                    break;
            }
        }

        public void gameOver() {
            try {
                playerSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        public void textBreak(){
            //adds an Ascii text break between menus for better reading
            printStream.println(TEXT_BREAK);
        }
    }
}





