import java.util.Scanner;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Pexeso {
    private static final int BOARD_SIZE = 4; // Velikost hrací desky (4x4)
    private static char[][] board;
    private static char[][] revealed;
    private static char[] symbols = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H' };
    private static int pairsFoundPlayer1 = 0;
    private static int pairsFoundPlayer2 = 0;
    private static boolean isPlayer1Turn = true;

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        boolean playAgain;

        do {
            initializeGame();
            printRules();
            boolean gameOver = false;

            while (!gameOver) {
                printBoard();
                if (isPlayer1Turn) {
                    System.out.println("Hráč 1, je váš tah.");
                } else {
                    System.out.println("Hráč 2, je váš tah.");
                }

                int[] firstCard = getCoordinates(scanner, "Vyberte první kartu (např. 1;1): ");
                int[] secondCard = getCoordinates(scanner, "Vyberte druhou kartu (např. 2;2): ");

                revealCards(firstCard, secondCard);
                printBoard();

                if (board[firstCard[0]][firstCard[1]] == board[secondCard[0]][secondCard[1]]) {
                    System.out.println("Nalezli jste pár!");
                    if (isPlayer1Turn) {
                        pairsFoundPlayer1++;
                    } else {
                        pairsFoundPlayer2++;
                    }
                    revealed[firstCard[0]][firstCard[1]] = board[firstCard[0]][firstCard[1]];
                    revealed[secondCard[0]][secondCard[1]] = board[secondCard[0]][secondCard[1]];
                } else {
                    System.out.println("Karty nejsou stejné, budou skryty.");
                    TimeUnit.SECONDS.sleep(2); // Krátká pauza
                }

                gameOver = checkGameOver();

                if (!gameOver) {
                    isPlayer1Turn = !isPlayer1Turn;
                }
            }

            announceWinner();
            System.out.println("Chcete hrát znovu? (ano/ne)");
            playAgain = scanner.next().equalsIgnoreCase("ano");

        } while (playAgain);

        System.out.println("Děkujeme za hraní!");
    }

    private static void initializeGame() {
        board = new char[BOARD_SIZE][BOARD_SIZE];
        revealed = new char[BOARD_SIZE][BOARD_SIZE];

        // Naplnění herní desky dvojicemi karet
        char[] temp = new char[BOARD_SIZE * BOARD_SIZE];
        for (int i = 0; i < symbols.length; i++) {
            temp[i * 2] = symbols[i];
            temp[i * 2 + 1] = symbols[i];
        }

        // Zamíchání karet
        shuffleArray(temp);

        // Umístění karet na desku
        int index = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = temp[index++];
                revealed[i][j] = '*'; // Skryté karty
            }
        }
    }

    private static void printRules() {
        System.out.println("Vítejte ve hře Pexeso!");
        System.out.println("Vaším úkolem je najít všechny dvojice stejných karet.");
        System.out.println("Karty jsou reprezentovány symboly. Zadejte souřadnice karet, které chcete otočit.");
    }

    private static void printBoard() {
        System.out.println("Stav hrací desky:");
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(revealed[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static int[] getCoordinates(Scanner scanner, String prompt) {
        int row = -1, col = -1;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            String input = scanner.next();
            String[] parts = input.split(";");
            try {
                row = Integer.parseInt(parts[0]) - 1;
                col = Integer.parseInt(parts[1]) - 1;
                if (row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE && revealed[row][col] == '*') {
                    valid = true;
                } else {
                    System.out.println("Neplatné souřadnice, zkuste to znovu.");
                }
            } catch (Exception e) {
                System.out.println("Neplatný formát, zkuste to znovu.");
            }
        }
        return new int[] { row, col };
    }

    private static void revealCards(int[] firstCard, int[] secondCard) {
        revealed[firstCard[0]][firstCard[1]] = board[firstCard[0]][firstCard[1]];
        revealed[secondCard[0]][secondCard[1]] = board[secondCard[0]][secondCard[1]];
    }

    private static boolean checkGameOver() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (revealed[i][j] == '*') {
                    return false;
                }
            }
        }
        return true;
    }

    private static void announceWinner() {
        System.out.println("Konec hry!");
        System.out.println("Hráč 1 nalezl " + pairsFoundPlayer1 + " párů.");
        System.out.println("Hráč 2 nalezl " + pairsFoundPlayer2 + " párů.");
        if (pairsFoundPlayer1 > pairsFoundPlayer2) {
            System.out.println("Gratulujeme, Hráč 1 vyhrál!");
        } else if (pairsFoundPlayer2 > pairsFoundPlayer1) {
            System.out.println("Gratulujeme, Hráč 2 vyhrál!");
        } else {
            System.out.println("Remíza!");
        }
    }

    private static void shuffleArray(char[] array) {
        Random rand = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            char temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
}
