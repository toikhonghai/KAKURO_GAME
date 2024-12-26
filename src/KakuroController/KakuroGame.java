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
        double maxWall = 0.3 * size * size;
        // Tạo tường ngẫu nhiên
        for (int i = 1; i < size; i++) {
            for (int j = 1; j < size; j++) {
                if ((count<=maxWall) && random.nextDouble() < 0.3) {
                    // Thử đặt tường và kiểm tra kết nối
                    board[i][j] = WALL;
                    if (!checkConnectivity()) {
                        board[i][j] = EMPTY;
                        continue;
                    }
                    count+=1.0;
                } else {
                    board[i][j] = EMPTY;
                }
            }
        }

        // Đảm bảo không có các ô trống đơn lẻ
        for (int i = 1; i < size - 1; i++) {
            for (int j = 1; j < size - 1; j++) {
                if (board[i][j] == EMPTY) {
                    if ((board[i - 1][j] == WALL && board[i][j - 1] == WALL) &&
                            (board[i + 1][j] == WALL || board[i][j + 1] == WALL)) {
                        board[i][j] = WALL;
                        // Kiểm tra nếu việc đặt tường tạo ra vùng cô lập
                        if (!checkConnectivity()) {
                            board[i][j] = EMPTY;
                        }
                    }
                }
            }
        }

        // Đặt các ô tổng
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == WALL &&
                        ((i < size - 1 && board[i + 1][j] == EMPTY) ||
                                (j < size - 1 && board[i][j + 1] == EMPTY))) {
                    // Thử chuyển thành ô tổng và kiểm tra kết nối
                    int oldValue = board[i][j];
                    board[i][j] = SUM_CELL;
                    if (!checkConnectivity()) {
                        board[i][j] = oldValue;
                    }
                }
            }
        }

        // Xử lý các góc có 2 ô tổng liền kề
        for(int i = 1; i < size; i++) {
            for(int j = 1; j < size; j++) {
                boolean hasTopSum = (board[i-1][j] == SUM_CELL);
                boolean hasLeftSum = (board[i][j-1] == SUM_CELL);

                if(hasTopSum && hasLeftSum) {
                    boolean hasRightPath = (j+1 < size && board[i][j+1] == EMPTY);
                    boolean hasBottomPath = (i+1 < size && board[i+1][j] == EMPTY);
                    boolean isDeadEnd = !hasRightPath && !hasBottomPath;

                    if(isDeadEnd) {
                        // Thử mở đường và kiểm tra kết nối
                        if(random.nextBoolean()) {
                            if(i+1==size && j+1<size) {
                                board[i][j+1]=EMPTY;
                                if (!checkConnectivity()) board[i][j+1]=WALL;
                            } else {
                                board[i+1][j] = EMPTY;
                                if (!checkConnectivity()) board[i+1][j]=WALL;
                            }
                        } else {
                            if(j+1==size && i+1<size) {
                                board[i+1][j] = EMPTY;
                                if (!checkConnectivity()) board[i+1][j]=WALL;
                            } else {
                                board[i][j+1] = EMPTY;
                                if (!checkConnectivity()) board[i][j+1]=WALL;
                            }
                        }
                    }
                }
            }
        }
        //Loai bo truong hop khong co o tong
        boolean sumCellCheck = false;
        outer:
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == WALL &&
                        ((i < size - 1 && board[i + 1][j] == EMPTY) ||
                                (j < size - 1 && board[i][j + 1] == EMPTY))) {
                    sumCellCheck = true;
                    break outer;
                }
            }
        }
        if (sumCellCheck) {
            initializeBoard();
        }
    }

    private boolean checkConnectivity() {
        //Tim o EMPTY dau tien
        int startI = -1, startJ = -1;
        outer: for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == EMPTY) {
                    startI = i;
                    startJ = j;
                    break outer;
                }
            }
        }
        if (startI == -1) return true; //Khong co o EMPTY nao
        boolean[][] visited = new boolean[size][size];
        floodFill(startI, startJ, visited);
        //kiem tra xem co o EMPTY nao chua duoc tham khong
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == EMPTY && !visited[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }
    private void floodFill(int i, int j, boolean[][] visited) {
        if (i < 0 || i >= size || j < 0 || j >= size ||
                visited[i][j] || board[i][j] != EMPTY) {
            return;
        }
        visited[i][j] = true;
        //De quy sang 4 huong
        floodFill(i - 1, j, visited);
        floodFill(i + 1, j, visited);
        floodFill(i, j - 1, visited);
        floodFill(i, j + 1, visited);
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
                board[row][col] = EMPTY; //Backtrack
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
            levelSuggets = 0;
        }
        else if(level>=1 && level<=2) levelSuggets = 0;
        else if(level==3) levelSuggets = 0;
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
        boolean[] used = new boolean[11];
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
        boolean[] used = new boolean[11];
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