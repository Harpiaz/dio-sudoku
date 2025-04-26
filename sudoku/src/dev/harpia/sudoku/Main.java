package dev.harpia.sudoku;

import dev.harpia.sudoku.model.Board;
import dev.harpia.sudoku.model.Space;

import java.util.Scanner;
import java.util.stream.Stream;
import java.util.Map;

import static dev.harpia.sudoku.util.BoardTemplate.BOARD_TEMPLATE;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private final static Scanner scanner = new Scanner(System.in);
    private static Board board;

    public static void main(String[] args) { 
        final var positions = Stream.of(args)
                .collect(toMap(
                        arg -> arg.split(";")[0],
                        arg -> arg.split(";")[1]
                ));

        int option = -1;

        while (true) {
            System.out.println();
            System.out.println("1 - Iniciar um novo Jogo");
            System.out.println("2 - Inserir número");
            System.out.println("3 - Remover número");
            System.out.println("4 - Visualizar jogo");
            System.out.println("5 - Verificar status do jogo");
            System.out.println("6 - Reiniciar jogo");
            System.out.println("7 - Finalizar jogo");
            System.out.println("0 - Sair");

            System.out.println();
            option = scanner.nextInt();
            System.out.println();

            switch (option){
                case 1 -> startGame(positions);
                case 2 -> inputNumber();
                case 3 -> removeNumber();
                case 4 -> showGame();
                case 5 -> showGameStatus();
                case 6 -> resetGame();
                case 7 -> finishGame();
                case 0 -> System.exit(0);
                default -> System.out.println("Opção inválida, selecione uma das opções do menu");
            }
        }

    }

    private static void startGame(final Map<String, String> positions) {
        if (board != null) {
            System.out.println("Um jogo já está em andamento, finalize-o antes de iniciar um novo.");
            return;
        }

        List<List<Space>> spaces = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            spaces.add(new ArrayList<>());
            for (int j = 0; j < 9; j++) {
                String positionConfig = positions.get("%s,%s".formatted(i, j));
                Integer expected = Integer.parseInt(positionConfig.split(",")[0]);
                Boolean fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);
                spaces.get(i).add(new Space(expected, fixed));
            }
        }

        board = new Board(spaces);
        System.out.println("O jogo foi criado com sucesso!");
    }

    private static void inputNumber() {
        if (!hasStartedGame()) return;

        System.out.print("Digite a linha (0-8): ");
        int row = scanner.nextInt();
        System.out.print("Digite a coluna (0-8): ");
        int column = scanner.nextInt();
        System.out.print("Digite o número (1-9): ");
        int number = scanner.nextInt();
        
        if (board.changeSpaceValue(row, column, number))
            System.out.println("\nNúmero inserido com sucesso!");
        else
            System.out.println("\nNão foi possível inserir o número nesta posição.");
    }

    private static void removeNumber() {
        if (!hasStartedGame()) return;

        System.out.print("Digite a linha (0-8): ");
        int row = scanner.nextInt();
        System.out.print("Digite a coluna (0-8): ");
        int column = scanner.nextInt();

        if (board.clearSpaceValue(row, column))
            System.out.println("\nNúmero removido com sucesso!");
        else
            System.out.println("\nNão foi possível remover o número nesta posição.");
    }

    private static void showGame() {
        if (!hasStartedGame()) return;

        var args = new Object[81];
        int argPos = 0;
        for (List<Space> row : board.getSpaces()) {
            for (int col = 0; col < 9; col++) {
                args[argPos++] = " " + ((row.get(col).getActualValue() == null) ? " " : row.get(col).getActualValue());
            }
        }
        System.out.println("Seu jogo se encontra da seguinte forma:\n");
        System.out.printf(BOARD_TEMPLATE, args);
    }

    private static void showGameStatus() {
        if (!hasStartedGame()) return;

        System.out.print("O status do jogo é: ");
        switch (board.getGameStatus()) {
            case NOT_STARTED -> System.out.println("Não iniciado.");
            case INCOMPLETE -> System.out.println("Incompleto.");
            case COMPLETE -> System.out.println("Completo.");
        }

        if (board.hasErrors()) {
            System.out.println("O jogo possui erros.");
        } else {
            System.out.println("O jogo não possui erros.");
        }

        if (board.isGameFinished()) {
            System.out.println("Parabéns! Você completou o jogo!");
        } else {
            System.out.println("O jogo ainda não foi finalizado.");
        }
    }

    private static void resetGame() {
        if (!hasStartedGame()) return;
        
        System.out.print("Tem certeza que deseja reiniciar o jogo (s/n)? ");
        String option = scanner.next();
        if (option.equalsIgnoreCase("s")) {
            board.reset();
            System.out.println("\nO jogo foi reiniciado com sucesso!");
        } else {
            System.out.println("\nO jogo não foi reiniciado.");
        }
    }

    private static void finishGame() {
        if (!hasStartedGame()) return;

        if (board.isGameFinished()) {
            System.out.println("Parabéns! Você completou o jogo!");
            showGame();
            board = null;
        } else if (board.hasErrors()) {
            System.out.println("O jogo possui erros.");
        } else {
            System.out.println("O jogo ainda não foi finalizado.");
        }
    }

    private static boolean hasStartedGame() {
        if (board == null) {
            System.out.println("O jogo ainda não foi iniciado. Inicie um novo jogo.");
            return false;
        } else return true;
    }

}
