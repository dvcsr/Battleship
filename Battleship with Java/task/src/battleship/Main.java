package battleship;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    //in-game
    public static void main(String[] args) {
        //====================== SETUP ===============================
        Scanner scanner = new Scanner(System.in);
        String[][] playerOneBoard = initBoard();
        int[] playerOneShankShipFlag = new int[5];
        Arrays.fill(playerOneShankShipFlag, 0);
        boolean allShipsSankPlayerOne = Arrays.stream(playerOneShankShipFlag).allMatch(x -> x == 1);

        String[][] secondPlayerBoard = initBoard();
        int[] secondSankShipFlag = new int[5];
        Arrays.fill(secondSankShipFlag, 0);
        boolean allShipsSankPlayerTwo = Arrays.stream(secondSankShipFlag).allMatch(x -> x == 1);

        //====================== PLACING SHIP ===============================
        System.out.println("Player 1, place your ships on the game field");
        int[][] playerOneShipsCoords = playerPlaceAllShips(scanner, playerOneBoard);

        passTheGame(scanner);

        System.out.println("Player 2, place your ships to the game field");
        int[][] secondPlayerShipsCoords = playerPlaceAllShips(scanner, secondPlayerBoard);

        passTheGame(scanner);

        //====================== GAME SHOOTING START ===============================
        while (!allShipsSankPlayerOne || !allShipsSankPlayerTwo) {

            getResult(shotAField(scanner, playerOneBoard, "Player 1, it's your turn:"), secondPlayerBoard, secondPlayerShipsCoords, secondSankShipFlag);
            if (checkAllShipIsDestroyed(secondPlayerShipsCoords)) {
                return;
            }

            passTheGame(scanner);

            getResult(shotAField(scanner, secondPlayerBoard, "Player 2, it's your turn:"), playerOneBoard, playerOneShipsCoords, playerOneShankShipFlag);
            scanner.nextLine();
            System.out.println("Press Enter and pass the move to another player");
            System.out.println("...");
            scanner.nextLine();

        }
    }

    //infrastructure
    public static void getResult(int[] playerShot, String[][] enemyBoard, int[][]enemyShipsCoords, int[] enemySankShipFlag) {
        String[][] fogBoard = initBoard();

        boolean firstShipDestroyed = isShipSank(playerShot, enemyShipsCoords[0]);
        boolean secondShipDestroyed = isShipSank(playerShot, enemyShipsCoords[1]);
        boolean thirdShipDestroyed = isShipSank(playerShot, enemyShipsCoords[2]);
        boolean fourthShipDestroyed = isShipSank(playerShot, enemyShipsCoords[3]);
        boolean fifthShipDestroyed = isShipSank(playerShot, enemyShipsCoords[4]);
        boolean allShipDestroyed = firstShipDestroyed && secondShipDestroyed && thirdShipDestroyed && fourthShipDestroyed && fifthShipDestroyed;

        if(allShipDestroyed){
            enemyBoard[playerShot[0]][playerShot[1]] = "X";
            printGameField(enemyBoard);
            System.out.println("You sank the last ship. You won. Congratulations!");
            Arrays.fill(enemySankShipFlag, 1);

        }
        else if (enemyBoard[playerShot[0]][playerShot[1]] == "O" || enemyBoard[playerShot[0]][playerShot[1]] == "X"){
                fogBoard[playerShot[0]][playerShot[1]] = "X";
                printGameField(fogBoard);
                if ((firstShipDestroyed && enemySankShipFlag[0] != 1)){
                    System.out.println("You sank a ship! Specify a new target:");
                    enemySankShipFlag[0] = 1;
                }
                else if (secondShipDestroyed && enemySankShipFlag[1] != 1){
                    System.out.println("You sank a ship! Specify a new target:");
                    enemySankShipFlag[1] = 1;
                }
                else if (thirdShipDestroyed && enemySankShipFlag[2] != 1){
                    System.out.println("You sank a ship! Specify a new target:");
                    enemySankShipFlag[2] = 1;
                }
                else if (fourthShipDestroyed && enemySankShipFlag[3] != 1){
                    System.out.println("You sank a ship! Specify a new target:");
                    enemySankShipFlag[3] = 1;
                }
                else if (fifthShipDestroyed && enemySankShipFlag[4] != 1){
                    System.out.println("You sank a ship! Specify a new target:");
                    enemySankShipFlag[4] = 1;
                }
                else {
                    System.out.println("You hit a ship!");
                }
                enemyBoard[playerShot[0]][playerShot[1]] = "X";
//                printGameField(Board);
            }
            else if (enemyBoard[playerShot[0]][playerShot[1]] != "O" || enemyBoard[playerShot[0]][playerShot[1]] == "M") {
                fogBoard[playerShot[0]][playerShot[1]] = "M";
//                printGameField(fogBoard);
                System.out.println("You missed!");
                enemyBoard[playerShot[0]][playerShot[1]] = "M";
//                printGameField(Board);
            }
        }

    public static boolean checkAllShipIsDestroyed(int[][] shipCarrier) {
        if(Arrays.stream(shipCarrier[0]).allMatch(num -> num == 0) &&
                Arrays.stream(shipCarrier[1]).allMatch(num -> num == 0) &&
                Arrays.stream(shipCarrier[2]).allMatch(num -> num == 0) &&
                Arrays.stream(shipCarrier[3]).allMatch(num -> num == 0) &&
                Arrays.stream(shipCarrier[4]).allMatch(num -> num == 0)){
            return true;
        }
        return false;
    }

    public static boolean isShipSank(int[] target, int[] shipCoords) {
        int[] shot = shipCoords.length > target.length ? target : shipCoords;

        // Check first pair (row1,col1)
        if (shipCoords[0] == shot[0] && shipCoords[1] == shot[1]) {
            shipCoords[0] = 0;
            shipCoords[1] = 0;
        }
        // Check second pair (row2,col2)
        if (shipCoords[2] == shot[0] && shipCoords[3] == shot[1]) {
            shipCoords[2] = 0;
            shipCoords[3] = 0;
        }

        long allCoords = Arrays.stream(shipCoords).filter(c -> c == 0).count();

        if (allCoords == 4) {
            return true;
        }
        return false;
    }

    public static int[] shotAField(Scanner scanner, String[][] playerBoard, String message) {
        int[] coords;
        String[][] fogBoard = initBoard();
        printGameField(fogBoard);
        System.out.print("---------------------");
        printGameField(playerBoard);
        System.out.println(message);

        while (true){
            coords = parsingOneCoordinates(scanner);
            String stringCoords = (char)(coords[0] + 64) + "" + coords[1];
            boolean validInput = (stringCoords.matches("[A-J](10|[1-9])"));
            if (validInput) {
                return new int[]{coords[0], coords[1]} ;
            }
            System.out.println("Error! You entered the wrong coordinates! Try again:");
        }
    }

    public static String[][] initBoard() {
        String[][] board = new String[11][11];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (i == 0 && j == 0) { //top corner
                    board[i][j] = " ";
                } else if (i != 0 && j == 0) {
                    board[i][j] = String.valueOf((char) (64 + i)); //left heading
                } else if (i == 0) {
                    //top heading
                    board[i][j] = String.valueOf(j);
                } else board[i][j] = "~";
            }
        }
        return board;
    }

    public static void printGameField(String[][] board) {
        System.out.println();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static String[][] updateBoard(String[][] board, int[] shipCoords) {
        boolean isHorizontal = Math.abs(shipCoords[0] - shipCoords[2]) == 0;
        boolean isVertical = Math.abs(shipCoords[1] - shipCoords[3]) == 0;

        if (isHorizontal && !isVertical) {
            int staticCoord = shipCoords[0];
            int max = Math.max(shipCoords[1], shipCoords[3]);
            int min = Math.min(shipCoords[1], shipCoords[3]);
            for (int i = min; i <= max; i++) {
                board[staticCoord][i] = "O";
            }
            return board;
        }
        else if (!isHorizontal && isVertical) {
            int staticCoord = shipCoords[1];
            int max = Math.max(shipCoords[0], shipCoords[2]);
            int min = Math.min(shipCoords[0], shipCoords[2]);
            for (int i = min; i <= max; i++) {
                board[i][staticCoord] = "O";
            }
        }
        return board;
    }

    public static boolean checkAdjacentShips(String[][] board, int[] shipCoords) {
        boolean isHorizontal = Math.abs(shipCoords[0] - shipCoords[2]) == 0;
        boolean isVertical = Math.abs(shipCoords[1] - shipCoords[3]) == 0;
        if (isHorizontal && !isVertical) {
            int staticCoord = shipCoords[0];
            int max = Math.max(shipCoords[1], shipCoords[3]);
            int min = Math.min(shipCoords[1], shipCoords[3]);
           //check static adjacent
            for (int i = min; i <= max; i++) {
                if (staticCoord > 1){
                    if (board[staticCoord-1][i].equals("O")) {
                        return false;
                    }
                }
                if (staticCoord < 10){
                    if (board[staticCoord+1][i].equals("O")) {
                        return false;
                    }

                }
                if (i == min && i > 1){
                    if (board[staticCoord][i-1].equals("O")) {
                        return false;
                    }
                }
                if (i == max && i < 10){
                    if (board[staticCoord][i+1].equals("O")) {
                        return false;
                    }
                }
            }
        } else if (!isHorizontal && isVertical) {
            int staticCoord = shipCoords[1];
            int max = Math.max(shipCoords[0], shipCoords[2]);
            int min = Math.min(shipCoords[0], shipCoords[2]);
            for (int i = min; i <= max; i++) {
                if (staticCoord > 1){
                    if (board[i][staticCoord-1].equals("O")) {
                        return false;
                    }
                }
                if (staticCoord < 10){
                    if (board[i][staticCoord+1].equals("O")) {
                        return false;
                    }
                }
                if (i == min && i > 1){
                    if (board[i-1][staticCoord].equals("O")) {
                        return false;
                    }
                }
                if (i == max && i < 10){
                    if (board[i+1][staticCoord].equals("O")) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static int[] inputShip(Scanner scanner, String[][] board, int shipLength) {
        while (true){
            int[] coord = askCoordinates(scanner, shipLength);

            if (coord != null && checkAdjacentShips(board, coord)) {
                updateBoard(board, coord);
                return coord;
            }
            else System.out.println("Error! You placed it too close to another one. Try again:");
        }
    }

    public static int[] askCoordinates(Scanner scanner, int shipLength) {
        while (true) {
            int[] coords = parsingTwoCoordinates(scanner);
            String firstCoords = (char)(coords[0] + 64) + "" + coords[1];
            String secondCoords = (char)(coords[2] + 64) + "" + coords[3];
            boolean validInput = (firstCoords.matches("[A-J](10|[1-9])")) && secondCoords.matches("[A-J](10|[1-9])");

            int row1 = coords[0]; int col1 = coords[1]; int row2 = coords[2]; int col2 = coords[3];
            boolean hasNoDiagonal = Math.abs(row1 - row2) == 0 || Math.abs(col1 - col2) == 0;
            boolean validShipLength = Math.abs(row1 - row2)+1 == shipLength || Math.abs(col1 - col2)+1 == shipLength;

            if (validInput){
                if(!hasNoDiagonal){
                    System.out.println("Error! Wrong ship location! Try again:");
                }
                else if (!validShipLength){
                    System.out.println("Error! Wrong length of the Submarine! Try again:");
                }
                else return coords;
            }
            else {
                System.out.println("Error!");
            }
        }
    }

    public static int[] parsingTwoCoordinates(Scanner scanner) {
        while (true) {
            String input1 = scanner.next(); //a-j 1-1
            String input2 = scanner.next(); //a-j 1-10
            if (input1.length() > 3 || input2.length() > 3){
                System.out.println("Error! Wrong number of coordinates! Try again:");
            }
            else {
                int row1 = input1.charAt(0) - 64;
                int col1 = Integer.parseInt(input1.substring(1));

                int row2 = input2.charAt(0) - 64;
                int col2 = Integer.parseInt(input2.substring(1));
                return new int[]{row1, col1, row2, col2};
            }
        }
    }

    public static int[] parsingOneCoordinates(Scanner scanner) {
        System.out.println();
        while (true) {
            String input1 = scanner.next(); //a-j 1-1
            if (input1.length() > 3){
                System.out.println("Error! Wrong number of coordinates! Try again:");
            }
            else {
                int row1 = input1.charAt(0) - 64;
                int col1 = Integer.parseInt(input1.substring(1));

                return new int[]{row1, col1};
            }
        }
    };

    public static int[][] playerPlaceAllShips(Scanner scanner, String[][] board) {
        int[][] secondPlayerShips = new int[5][4];

        printGameField(board);
        System.out.println("Enter the coordinates of the Aircraft Carrier (5 cells):");
        secondPlayerShips[0] = inputShip(scanner, board, 5);
        printGameField(board);
        System.out.println("Enter the coordinates of the Battleship (4 cells):");
        secondPlayerShips[1] = inputShip(scanner, board, 4);
        printGameField(board);
        System.out.println("Enter the coordinates of the Submarine (3 cells):");
        secondPlayerShips[2] = inputShip(scanner, board, 3);
        printGameField(board);
        System.out.println("Enter the coordinates of the Cruiser (3 cells):");
        secondPlayerShips[3] = inputShip(scanner, board, 3);
        printGameField(board);
        System.out.println("Enter the coordinates of the Destroyer (2 cells):");
        secondPlayerShips[4] = inputShip(scanner, board, 2);
        printGameField(board);

        return secondPlayerShips;
    }

    public static void passTheGame(Scanner scanner) {
        scanner.nextLine();
        System.out.println("Press Enter and pass the move to another player");
        System.out.println("...");
        scanner.nextLine();
    }

    public static void showInformation(int[] arr) {
        String staticCoord = "";
        if (arr[0] - arr[2] == 0){ //if row is static (ship is horizontal)
            int length = Math.abs(arr[1] - arr[3]) + 1;
            staticCoord = String.valueOf((char)(arr[0] + 64));
            if (arr[1] < arr[3]) {
                System.out.println("Length: " + length);
                System.out.print("Parts: ");
                for (int i = arr[1]; i <= arr[3]; i++) {
                    System.out.print(staticCoord + i + " ");
                }
            } else {
                System.out.println("Length: " + length);
                System.out.print("Parts: ");
                for (int i = arr[1]; i >= arr[3]; i--) {
                    System.out.print(staticCoord + i + " ");
                }
            }
            System.out.println();
        }
        else if (arr[1] - arr[3] == 0){ //if col is static (ship is vertical)
            staticCoord = String.valueOf(arr[1]);
            int length = Math.abs(arr[2] - arr[0]) + 1;
            if (arr[0] < arr[2]) {
                System.out.println("Length: " + length );
                System.out.print("Parts: ");
                for (int i = arr[0]; i <= arr[2]; i++) {
                    System.out.print( String.valueOf((char)(64 + i)) + staticCoord + " ");
                }
            } else {
                System.out.println("Length: " + length);
                System.out.print("Parts: ");
                for (int i = arr[0]; i >= arr[2]; i--) {
                    System.out.print( String.valueOf((char)(64 + i)).toUpperCase() + staticCoord + " ");
                }
            }
            System.out.println();
        }
    }

}