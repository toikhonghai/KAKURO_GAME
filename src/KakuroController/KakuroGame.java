package KakuroController;

import java.util.*;

public class KakuroGame {
    private static final int WALL = -1;
    private static final int SUM_CELL = -2;
    private static final int EMPTY = 0;

    private int size;
    private int level;
    private int[][] board;
    private int[][] verticalSums;
    private int[][] horizontalSums;
    private Random random;

    public KakuroGame(int size, int level){
        this.size = size;
        this.level = level;
        this.board = new int[size][size];
        this.horizontalSums = new int[size][size];
        this.verticalSums = new int[size][size];
        this.random = new Random();
        initializeBoard();

    }
    private void initializeBoard(){
        for(int i = 0; i<size; i++){
            for(int j = 0; j<size; j++){
                board[i][j] = WALL;
            }
        }
        generateKakuroRandom();
    }

    public void generateKakuroRandom() {

        //Tao cau truc bang ngau nhien
        createRandomBoardStructure();
        fillAllCell(0, 0);
        calculateAndFillSum();
        //Tao goi y
        createSuggest();
    }
    public void createRandomBoardStructure() {
        board[0][0] = WALL; //Luôn luôn là tường ở góc bên trái
        double count = 0;
        for (int i = 1; i < size; i++) {
            for (int j = 1; j < size; j++) {
                if (random.nextDouble() < 0.3) {
                    if((count/(size*size)<=0.3)){
                        count+=1;
                        board[i][j] = WALL;
                    }else board[i][j] = EMPTY;
                } else {
                    board[i][j] = EMPTY;
                }
            }
        }

        // Đảm bảo không có các ô trống đơn lẻ
        for (int i = 1; i < size - 1; i++) {  // Tránh ra ngoài biên
            for (int j = 1; j < size - 1; j++) {
                if (board[i][j] == EMPTY) {
                    if ((board[i - 1][j] == WALL && board[i][j - 1] == WALL) &&
                            (board[i + 1][j] == WALL || board[i][j + 1] == WALL)) {
                        board[i][j] = WALL;
                    }
                }
            }
        }

        // Đặt các ô tổng
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == WALL &&
                        (i < size - 1 && board[i + 1][j] == EMPTY ||
                                j < size - 1 && board[i][j + 1] == EMPTY)) {
                    board[i][j] = SUM_CELL;
                }
            }
        }
    }
    private boolean checkValidMove(int row, int col, int num){
        return checkValidVertical(row, col, num) && checkValidHorizontal(row, col, num);
    }
    private boolean checkValidVertical(int row, int col, int num) {
        int r = row;
        while(r > 0 && board[r-1][col] != WALL && board[r-1][col] != SUM_CELL) {
            r--;
        }
        while(r < size && board[r][col] != WALL && board[r][col] != SUM_CELL) {
            if(board[r][col] == num && r != row) {
                return false;
            }
            r++;
        }
        return true;
    }
    private boolean checkValidHorizontal(int row, int col, int num) {
        int c = col;
        while(c > 0 && board[row][c-1] != WALL && board[row][c-1] != SUM_CELL) {
            c--;
        }
        while(c < size && board[row][c] != WALL && board[row][c] != SUM_CELL) {
            if(board[row][c] == num && c != col) {
                return false;
            }
            c++;
        }
        return true;
    }

    public boolean fillAllCell(int row, int col) {
        if (row == size) return true;
        if (col == size) return fillAllCell(row + 1, 0);
        if (board[row][col] != EMPTY) return fillAllCell(row, col + 1);
        List<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        Collections.shuffle(numbers);

        for (int num : numbers) {
            if (checkValidMove(row, col, num)) {
                board[row][col] = num;
                if (fillAllCell(row, col + 1)) {
                    return true;
                }
                board[row][col] = EMPTY; // Backtrack
            }
        }
        return false;
    }
    private void calculateAndFillSum(){
        for(int i= 0; i<size; i++){
            for(int j = 0; j < size; j++){
                if(board[i][j]==SUM_CELL){
                    verticalSums[i][j] = calculateVerticalSum(i, j);
                    horizontalSums[i][j] = calculateHorizontalSum(i, j);
                    if(verticalSums[i][j]==0 && horizontalSums[i][j]==0) board[i][j] = WALL;
                }
            }
        }
    }
    private int calculateVerticalSum(int row, int col){
        int sum = 0;
        for(int i = row+1; i<size && board[i][col]!=WALL && board[i][col]!=SUM_CELL; i++){
            sum+=board[i][col];
        }
        return sum;
    }
    private int calculateHorizontalSum(int row, int col){
        int sum = 0;
        for(int j = col+1;j<size && board[row][j]!=WALL && board[row][j]!=SUM_CELL; j++){
            sum+=board[row][j];
        }
        return sum;
    }
    private void createSuggest(){
        int cellSuggest = (int) (countValueCell()*0.3);
        double levelSuggets = 0;
        if((level>=4 && level<=6)){
            levelSuggets = 0.1;
        }
        else if(level>=1 && level<=2) levelSuggets = 0;
        else if(level==3) levelSuggets = 0.1;
        for(int i = 0; i<size; i++){
            for(int j = 0; j<size; j++){
                if(board[i][j]!=WALL && board[i][j]!=SUM_CELL){
                    if(random.nextDouble()>=levelSuggets || cellSuggest<=0){
                        board[i][j] = EMPTY;
                    }else cellSuggest--;
                }
            }
        }
    }
    private int countValueCell(){
        int count = 0;
        for(int i = 0; i<size; i++){
            for(int j = 0; j<size; j++){
                if(board[i][j]!=WALL && board[i][j]!=SUM_CELL){
                    count++;
                }
            }
        }
        return count;
    }
    public void printBoardDebug(int row, int col) {
        System.out.println("Board state around position (" + row + "," + col + "):");
        for(int i = Math.max(0, row-1); i <= Math.min(size-1, row+1); i++) {
            for(int j = Math.max(0, col-1); j <= Math.min(size-1, col+1); j++) {
                System.out.print(board[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public boolean checkResult(int row, int col){
        if(board[row][col] != SUM_CELL){
            return true;
        }
        return checkResultVertical(row, col) && checkResultHorizontal(row, col);
    }
    public boolean checkResultVertical(int row, int col){
        if(verticalSums[row][col] <= 0) return true;
        int targetSum = verticalSums[row][col];
        int currentSum = 0;
        int r = row+1;
        boolean[] used = new boolean[10];
        while(r<size && board[r][col]!=WALL && board[r][col]!=SUM_CELL){
            int value = board[r][col];
            if(value<=0 || value>9) return false;
            if(used[value]) return false;
            used[value] = true;
            currentSum+=value;
            r++;
        }
        return targetSum==currentSum;
    }
    public boolean checkResultHorizontal(int row, int col) {
        if(horizontalSums[row][col] <= 0) return true;
        int targetSum = horizontalSums[row][col];
        int currentSum = 0;
        int c = col + 1;
        boolean[] used = new boolean[10];
        while(c<size && board[row][c]!=WALL && board[row][ c]!=SUM_CELL){
            int value = board[row][c];
            if(value<=0 || value>9) return false;
            if(used[value]) return false;
            used[value] = true;
            currentSum += value;
            c++;
        }
        return currentSum == targetSum;
    }
    public void updateBoardValues(int row, int col, int value) {
        board[row][col] = value;
    }

    public int[][] getBoard() {
        return board;
    }

    public int[][] getVerticalSums() {
        return verticalSums;
    }

    public int[][] getHorizontalSums() {
        return horizontalSums;
    }

    public int getSize() {
        return size;
    }

    public int getLevel() {
        return level;
    }
}
