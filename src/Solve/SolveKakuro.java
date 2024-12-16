//package Solve;
//
//public class SolveKakuro {
//    private static final int WALL = -1;
//    private static final int SUM_CELL = -2;
//    private static final int EMPTY = 0;
//
//    private int size;
//    private int[][] board;
//    public void solveKakuro(int row, int col) {
//        if (row == size) {
//            printBoard();
//            System.out.println();
//            return;
//        }
//        if (col == size) {
//            solveKakuro(row + 1, 0);
//            return;
//        }
//        if (board[row][col] != EMPTY) {
//            solveKakuro(row, col + 1);
//            return;
//        }
//        for (int num = 1; num <= 9; num++) {
//            if (isValidMove(row, col, num)) {
//                board[row][col] = num;
//                solveKakuro(row, col + 1);
//                board[row][col] = EMPTY;
//            }
//        }
//    }
//
//
//    public void printBoard(){
//        for(int i = 0; i<size; i++){
//            for(int j = 0; j<size; j++){
//                if(board[i][j]==WALL) System.out.printf("%-7s", "🧱");
//                else if(board[i][j]==SUM_CELL){
//                    String str = "";
//                    if(verticalSums[i][j]>0) str+=verticalSums[i][j] + "\\";
//                    if(horizontalSums[i][j]>0) str+=horizontalSums[i][j];
//                    System.out.printf("%-7s", str);
//                }
//                else System.out.printf("%-7d", board[i][j]);
////            }
//            System.out.println();
//        }
//    }
//    private boolean isValidMove(int row, int col, int num){
//        return  isValidHorizontal(row, col, num) && isValidVertical(row, col, num);
//    }
//    private boolean isValidHorizontal(int row, int col, int num){
//        int sum = 0;
//        int targetSum = 0;
//        int c = col;
//        int notEmptyNums = 0;
//        int cnt = 0;
//        while(c > 0 && board[row][c-1] != WALL && board[row][c-1] != SUM_CELL) c--;
//        if(c > 0 && board[row][c-1]==SUM_CELL) targetSum = horizontalSums[row][c-1];
//
//        while(c < size && board[row][c] != WALL && board[row][c] != SUM_CELL){
//            if(board[row][c] == num && c != col) return false;
//            if(board[row][c] != EMPTY){
//                sum+=board[row][c];
//                notEmptyNums++;
//            }
//            cnt++;
//            c++;
//        }
//        if(notEmptyNums==(cnt-1) && (sum+num) == targetSum) return true;
//        else if(notEmptyNums==(cnt-1) && (sum+num) != targetSum) return false;
//        return sum+num < targetSum;
//    }
//}


//package KakuroController;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//
//public class KakuroGUI extends JFrame {
//    private static final int CELL_SIZE = 60;
//    private KakuroGame game;
//    private JPanel boardPanel;
//    private JTextField[][] cells;
//    private JButton newGameButton;
//    private JButton checkButton;
//
//    public KakuroGUI(int size) {
//        game = new KakuroGame(size);
//        cells = new JTextField[size][size];
//
//        // Tao frame
//        setTitle("Kakuro Game");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        // Tao panel chinh
//        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
//        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));// Khoang trong giua cac duong vien ngoai cua mainpanel voi cac phan ben trong
//        // Tao panel cho bang game
//        createBoardPanel(size);
//        mainPanel.add(boardPanel, BorderLayout.CENTER);
//        // Tao panel cho nut dieu khien
//        createControlPanel();
//        mainPanel.add(createControlPanel(), BorderLayout.SOUTH);
//
//        add(mainPanel);
//        pack();
//        setLocationRelativeTo(null);
//    }
//
//    private void createBoardPanel(int size) {
//        boardPanel = new JPanel(new GridLayout(size, size, 2, 2));
//        boardPanel.setBackground(Color.BLACK);
//
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < size; j++) {
//                if (game.getBoard()[i][j] == -1) { // WALL
//                    JPanel wallCell = new JPanel();
//                    wallCell.setBackground(Color.BLACK);
//                    wallCell.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
//                    boardPanel.add(wallCell);
//                }
//                else if (game.getBoard()[i][j] == -2) { // SUM_CELL
//                    JPanel sumCell = createSumCell(i, j);
//                    boardPanel.add(sumCell);
//                }
//                else {
//                    cells[i][j] = createInputCell(i, j);
//                    boardPanel.add(cells[i][j]);
//                }
//            }
//        }
//    }
//
//    private JPanel createSumCell(int row, int col) {
//        JPanel sumCell = new JPanel() {
//            @Override
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//                g.setColor(Color.LIGHT_GRAY);
//                g.fillRect(0, 0, getWidth(), getHeight());
//                g.setColor(Color.BLACK);
//                g.drawLine(0, 0, getWidth(), getHeight());
//
//                int verticalSum = game.getVerticalSums()[row][col];
//                int horizontalSum = game.getHorizontalSums()[row][col];
//                if (verticalSum > 0) {
//                    g.drawString(String.valueOf(verticalSum), 5, getHeight() - 5);
//                }
//                if (horizontalSum > 0) {
//                    g.drawString(String.valueOf(horizontalSum), getWidth() - 20, 15);
//                }
//            }
//        };
//        sumCell.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
//        return sumCell;
//    }
//
//    private JTextField createInputCell(int row, int col) {
//        JTextField cell = new JTextField();
//        cell.setHorizontalAlignment(JTextField.CENTER);
//        cell.setFont(new Font("Arial", Font.BOLD, 20));
//
//        // Nếu là ô gợi ý, hiển thị giá trị và disable
//        if (game.getBoard()[row][col] > 0) {
//            cell.setText(String.valueOf(game.getBoard()[row][col]));
//            cell.setEditable(false);
//            cell.setBackground(new Color(230, 230, 230));
//        } else {
//            cell.setBackground(Color.WHITE);
//        }
//
//        // Chỉ cho phép nhập số từ 1-9
//        cell.addKeyListener(new KeyAdapter() {
//            public void keyTyped(KeyEvent e) {
//                char c = e.getKeyChar();
//                if (!Character.isDigit(c) || c == '0' ||
//                        cell.getText().length() >= 1) {
//                    e.consume();
//                }
//            }
//        });
//
//        cell.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
//        return cell;
//    }
//
//    private JPanel createControlPanel() {
//        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
//
//        newGameButton = new JButton("New Game");
//        newGameButton.addActionListener(e -> startNewGame());
//
//        checkButton = new JButton("Check Solution");
//        checkButton.addActionListener(e -> checkSolution());
//
//        controlPanel.add(newGameButton);
//        controlPanel.add(checkButton);
//
//        return controlPanel;
//    }
//
//    private void startNewGame() {
//        game.generateKakuroRandom();
//        updateBoard();
//    }
//
//    private void updateBoard() {
//        boardPanel.removeAll();
//        createBoardPanel(game.getSize());
//        boardPanel.revalidate();
//        boardPanel.repaint();
//    }
//
//    private void checkSolution() {
//        boolean isCorrect = true;
//        // TODO: Implement solution checking logic
//        if (isCorrect) {
//            JOptionPane.showMessageDialog(this, "Congratulations! Solution is correct!",
//                    "Success", JOptionPane.INFORMATION_MESSAGE);
//        } else {
//            JOptionPane.showMessageDialog(this, "Solution is incorrect. Keep trying!",
//                    "Incorrect", JOptionPane.ERROR_MESSAGE);
//        }
//    }
////
//    // Thêm getters cho game
//    public KakuroGame getGame() {
//        return game;
//    }
//
//}





//package KakuroController;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//
//public class KakuroGUI extends JFrame{
//    private static final int CELL_SIZE = 60;
//    private KakuroGame game;
//    private JPanel boardPanel;
//    private JTextField[][] cells;
//    private JPanel mainPanel;
//    public KakuroGUI(int size){
//        game = new KakuroGame(size);
//        cells = new JTextField[size][size];
//        // Tao frame
//        setTitle("Kakuro Game");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(800, 700);
//        // Tao panel chinh
//        mainPanel = new JPanel(new BorderLayout(10, 10));
//        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        // Tao panel cho bang game
//        createBoardPanel(size);
//        mainPanel.add(boardPanel, BorderLayout.CENTER);
//        add(mainPanel);
//        // Tao panel cho nut dieu khien
//        JPanel createControl = createControlPanel(size);
//        mainPanel.add(createControl, BorderLayout.SOUTH);
//        //pack(); // Can chinh kich thuoc frame cho phu hop
//        setLocationRelativeTo(null); // Dat frame o giua man hinh
//    }
//    public void createBoardPanel(int size){
//        boardPanel = new JPanel(new GridLayout(size, size, 2, 2));
//        boardPanel.setBackground(Color.BLACK);
//        for(int i = 0; i<size; i++){
//            for(int j = 0; j<size; j++){
//                if(game.getBoard()[i][j]==-1){
//                    JPanel wallCell = new JPanel();
//                    wallCell.setBackground(Color.BLACK);
//                    wallCell.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
//                    boardPanel.add(wallCell);
//                }else if(game.getBoard()[i][j]==-2){
//                    JPanel sumCell = createSumCell(i, j);
//                    boardPanel.add(sumCell);
//                }else{
//                    cells[i][j] = createInputCell(i, j);
//                    boardPanel.add(cells[i][j]);
//                }
//            }
//        }
//
//    }
//    public JPanel createSumCell(int row, int col){
//        JPanel sumCell = new JPanel(){
//            @Override
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//                g.setColor(Color.LIGHT_GRAY);
//                g.fillRect(0, 0, getWidth(), getHeight());
//                g.setColor(Color.BLACK);
//                g.drawLine(0, 0, getWidth(), getHeight());
//                int verticalSum = game.getVerticalSums()[row][col];
//                int horizontalSum = game.getHorizontalSums()[row][col];
//                if(verticalSum>0) g.drawString(String.valueOf(verticalSum), 5, getHeight()-15);
//                if(horizontalSum>0) g.drawString(String.valueOf(horizontalSum), getWidth()-20, 15);
//            }
//        };
//        sumCell.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
//        return sumCell;
//    }
//    public JTextField createInputCell(int row, int col){
//        JTextField cell = new JTextField();
//        cell.setHorizontalAlignment(JTextField.CENTER);
//        cell.setFont(new Font("Arial", Font.BOLD, 20));
//        if(game.getBoard()[row][col]>0){
//            cell.setText(String.valueOf(game.getBoard()[row][col]));
//            cell.setEditable(false);
//            cell.setBackground(new Color(230, 230, 230));
//        }else cell.setBackground(Color.WHITE);
//        cell.addKeyListener(new KakuroKeylistener(cell, game, row, col));
//        return cell;
//    }
//    private JPanel createControlPanel(int size){
//        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
//        JButton newGameButton = new JButton("New Game");
//        newGameButton.addActionListener(e->startNewGame());
//        controlPanel.add(newGameButton);
//        JButton checkButton = new JButton("Check Solution");
//        checkButton.addActionListener(e->checkSolution(size));
//        controlPanel.add(checkButton);
//        return controlPanel;
//    }
//    private void checkSolution(int size){
//        boolean isCorrect = true;
//        for(int i = 0; i<size; i++){
//            for(int j = 0; j<size; j++){
//                if(game.getBoard()[i][j]==-2){
//                    if(!game.checkResult(i, j)) isCorrect = false;
//                }
//            }
//        }
//        if(isCorrect){
//            JOptionPane.showMessageDialog(this, "Congratulations! Solution is correct!", "Success", JOptionPane.INFORMATION_MESSAGE);
//        }else{
//            JOptionPane.showMessageDialog(this, "Solution is incorrect. Keep trying!", "Incorrect", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//    private void startNewGame(){
//        int size = game.getSize();
//        game = new KakuroGame(size);
//        game.generateKakuroRandom();
//        updateBoard();
//    }
//    private void updateBoard(){
//        mainPanel.remove(boardPanel);
//        boardPanel.removeAll();
//        int size = game.getSize();
//        cells = new JTextField[size][size];
//        createBoardPanel(size);
//        mainPanel.add(boardPanel, BorderLayout.CENTER);
//        boardPanel.revalidate();
//        boardPanel.repaint();
//    }
//}