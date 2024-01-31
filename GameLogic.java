import java.util.*;


public class GameLogic implements PlayableLogic {
    //Game data
    private int turn = 0;
    private ArrayList<Position> positionsofkill = new ArrayList<>();
    private ArrayList<Integer> turnofkill = new ArrayList<>();
    private ArrayList<ConcretePiece> killedpieces = new ArrayList<>();
    public static int size = 11;
    ConcretePiece[][] board;
    Position[][] positions;
    private boolean GameFinished;
    private boolean blackturn;
    private ArrayList<Position> backmove = new ArrayList<>();
    private int blackkill = 0;
    private final ArrayList<ConcretePiece> allpieces = new ArrayList<>();
    private final ConcretePlayer white = new ConcretePlayer(true);
    private final ConcretePlayer black = new ConcretePlayer(false);
    private ArrayList<Position> allPositions = new ArrayList<>();

    public GameLogic() {
//making the board
        this.positions = new Position[size][size];
        this.reset();
    }

    @Override
    public boolean move(Position a, Position b) {
        //Checks if movement from A to B is legal and if so makes it
        if (board[a.getpositionx()][a.getpositiony()] == null) return false;
        if (isSecondPlayerTurn() && board[a.getpositionx()][a.getpositiony()].getOwner() == white) return false;
        if (!isSecondPlayerTurn() && board[a.getpositionx()][a.getpositiony()].getOwner() == black) return false;
        if (isGameFinished()) return false;
        if (board[a.getpositionx()][a.getpositiony()].getType() != "♔") {
            if (b.getpositionx() == 0 && b.getpositiony() == 0) return false;
            if (b.getpositionx() == 0 && b.getpositiony() == 10) return false;
            if (b.getpositionx() == 10 && b.getpositiony() == 0) return false;
            if (b.getpositionx() == 10 && b.getpositiony() == 10) return false;
        }

        if (!movehelp(a, b)) return false;
        else {
            // צריך להוסיף את הכלים בשביל פונקצית החזרה
            backmove.add(a);
            backmove.add(b);
            board[b.getpositionx()][b.getpositiony()] = board[a.getpositionx()][a.getpositiony()];
            board[a.getpositionx()][a.getpositiony()] = null;
            board[b.getpositionx()][b.getpositiony()].addHistory(b);

            if (!positions[b.getpositionx()][b.getpositiony()].getPieces().contains(this.board[b.getpositionx()][b.getpositiony()]))
                positions[b.getpositionx()][b.getpositiony()].addpiece(this.board[b.getpositionx()][b.getpositiony()]);
            blackturn = !blackturn;
            if (board[b.getpositionx()][b.getpositiony()] instanceof Pawn) killpiece(b);
            turn++;
            if (b.getpositionx() % 10 == 0 && b.getpositiony() % 10 == 0) {
                // לבן ניצח
                GameFinished = true;
                white.addWin();
                printAllData(false);
            }
            return true;
        }
    }

    private boolean movehelp(Position a, Position b) {
        //Checks that there is no piece in the way
        if (a.getpositiony() == b.getpositiony() && a.getpositionx() == b.getpositionx()) {
            return false;
        }
        if (a.getpositionx() != b.getpositionx() && a.getpositiony() != b.getpositiony()) {
            return false;
        }
        if (a.getpositiony() == b.getpositiony()) {
            int i;
            if (a.getpositionx() < b.getpositionx()) {
                for (i = a.getpositionx() + 1; i < b.getpositionx() + 1; i++) {
                    if (board[i][a.getpositiony()] != null) return false;
                }
            } else {
                for (i = a.getpositionx() - 1; i > b.getpositionx() - 1; i--) {
                    if (board[i][a.getpositiony()] != null) return false;
                }
            }
        } else {
            int i;
            if (a.getpositiony() < b.getpositiony()) {
                for (i = a.getpositiony() + 1; i < b.getpositiony() + 1; i++) {
                    if (board[a.getpositionx()][i] != null) return false;
                }
            } else {
                for (i = a.getpositiony() - 1; i > b.getpositiony() - 1; i--) {
                    if (board[a.getpositionx()][i] != null) return false;
                }
            }
        }

        return true;
    }

    private void killpiece(Position a) {
        //Checks if piece is eaten
        int x = a.getpositionx();
        int y = a.getpositiony();
        a.setPieceAtPosition(this.board[x][y]);

        // right
        if (x >= 1) {
            if (this.board[x - 1][y] instanceof King && a.AtPosition.getOwner() == black) killking(x - 1, y);
            if (this.board[x - 1][y] instanceof Pawn) {
                if (a.AtPosition.getOwner() != getPieceAtPosition(x - 1, y).getOwner()) {
                    if (x == 1) {
                        killedpieces.add(this.board[x - 1][y]);
                        if (this.board[x - 1][y].getOwner() == black) blackkill++;
                        board[x - 1][y] = null;
                        ((Pawn) this.board[x][y]).addKill();
                        turnofkill.add(turn);
                        positionsofkill.add(new Position(x - 1, y));
                    } else {
                        if (this.board[x - 2][y] instanceof Pawn) {
                            if (a.AtPosition.getOwner() == getPieceAtPosition(x - 2, y).getOwner()) {
                                killedpieces.add(this.board[x - 1][y]);
                                if (this.board[x - 1][y].getOwner() == black) blackkill++;
                                board[x - 1][y] = null;
                                ((Pawn) this.board[x][y]).addKill();
                                turnofkill.add(turn);
                                positionsofkill.add(new Position(x - 1, y));
                            }
                        }
                    }
                }
            }
        }
        //left
        if (x <= 9) {
            if (this.board[x + 1][y] instanceof King && a.AtPosition.getOwner() == black) killking(x + 1, y);
            if (this.board[x + 1][y] instanceof Pawn) {
                if (a.AtPosition.getOwner() != getPieceAtPosition(x + 1, y).getOwner()) {
                    if (x == 9) {
                        killedpieces.add(this.board[x + 1][y]);
                        if (this.board[x + 1][y].getOwner() == black) blackkill++;
                        board[x + 1][y] = null;
                        ((Pawn) this.board[x][y]).addKill();
                        turnofkill.add(turn);
                        positionsofkill.add(new Position(x + 1, y));
                    } else {
                        if (this.board[x + 2][y] instanceof Pawn) {
                            if (a.AtPosition.getOwner() == getPieceAtPosition(x + 2, y).getOwner()) {
                                killedpieces.add(this.board[x + 1][y]);
                                if (this.board[x + 1][y].getOwner() == black) blackkill++;
                                board[x + 1][y] = null;
                                ((Pawn) this.board[x][y]).addKill();
                                turnofkill.add(turn);
                                positionsofkill.add(new Position(x + 1, y));
                            }
                        }
                    }
                }
            }
        }
        // up
        if (y >= 1) {
            if (this.board[x][y - 1] instanceof King && a.AtPosition.getOwner() == black) killking(x, y - 1);
            if (this.board[x][y - 1] instanceof Pawn) {
                if (a.AtPosition.getOwner() != getPieceAtPosition(x, y - 1).getOwner()) {
                    if (y == 1) {
                        killedpieces.add(this.board[x][y - 1]);
                        if (this.board[x][y - 1].getOwner() == black) blackkill++;
                        board[x][y - 1] = null;
                        ((Pawn) this.board[x][y]).addKill();
                        turnofkill.add(turn);
                        positionsofkill.add(new Position(x, y - 1));
                    } else {
                        if (this.board[x][y - 2] instanceof Pawn) {
                            if (a.AtPosition.getOwner() == getPieceAtPosition(x, y - 2).getOwner()) {
                                killedpieces.add(this.board[x][y - 1]);
                                if (this.board[x][y - 1].getOwner() == black) blackkill++;
                                board[x][y - 1] = null;
                                ((Pawn) this.board[x][y]).addKill();
                                turnofkill.add(turn);
                                positionsofkill.add(new Position(x, y - 1));
                            }
                        }
                    }
                }
            }
        }
        // down
        if (y <= 9) {
            if (this.board[x][y + 1] instanceof King && a.AtPosition.getOwner() == black) killking(x, y + 1);
            if (this.board[x][y + 1] instanceof Pawn) {
                if (a.AtPosition.getOwner() != getPieceAtPosition(x, y + 1).getOwner()) {
                    if (y == 9) {
                        killedpieces.add(this.board[x][y + 1]);
                        if (this.board[x][y + 1].getOwner() == black) blackkill++;
                        board[x][y + 1] = null;
                        ((Pawn) this.board[x][y]).addKill();
                        turnofkill.add(turn);
                        positionsofkill.add(new Position(x, y + 1));
                    } else {
                        if (this.board[x][y + 2] instanceof Pawn) {
                            if (a.AtPosition.getOwner() == getPieceAtPosition(x, y + 2).getOwner()) {
                                killedpieces.add(this.board[x][y + 1]);
                                if (this.board[x][y + 1].getOwner() == black) blackkill++;
                                board[x][y + 1] = null;
                                ((Pawn) this.board[x][y]).addKill();
                                turnofkill.add(turn);
                                positionsofkill.add(new Position(x, y + 1));
                            }
                        }
                    }
                }
            }
        }
        if (blackkill == 24) {
            GameFinished = true;
            white.addWin();
            printAllData(false);
        }
    }
//Checks if the king is eaten
    private void killking(int x, int y) {
        if (x == 0) {
            if (board[0][y - 1] != null && board[0][y + 1] != null && board[1][y] != null)
                if (board[0][y - 1].getOwner() == black && board[0][y + 1].getOwner() == black && board[1][y].getOwner() == black) {
                    // שחור ניצח להוסיף פה ניצחון
                    GameFinished = true;
                    black.addWin();
                    printAllData(true);
                    // reset();
                }
            return;
        }
        if (x == 10) {
            if (board[10][y - 1] != null && board[10][y + 1] != null && board[9][y] != null)
                if (board[10][y - 1].getOwner() == black && board[10][y + 1].getOwner() == black && board[9][y].getOwner() == black) {
                    // שחור ניצח להוסיף פה ניצחון
                    GameFinished = true;
                    black.addWin();
                    printAllData(true);
                    //  reset();
                }
            return;
        }
        if (y == 0) {
            if (board[x - 1][0] != null && board[x + 1][0] != null && board[x][1] != null)
                if (board[x - 1][0].getOwner() == black && board[x + 1][0].getOwner() == black && board[x][1].getOwner() == black) {
                    // שחור ניצח
                    GameFinished = true;
                    black.addWin();
                    printAllData(true);
                }
            return;
        }
        if (y == 10) {
            if (board[x - 1][10] != null && board[x + 1][10] != null && board[x][9] != null)
                if (board[x - 1][10].getOwner() == black && board[x + 1][10].getOwner() == black && board[x][9].getOwner() == black) {
                    // שחור ניצח
                    GameFinished = true;
                    black.addWin();
                    printAllData(true);
                }
            return;
        }
        if (board[x - 1][y] != null && board[x + 1][y] != null && board[x][y - 1] != null && board[x][y + 1] != null)
            if (board[x - 1][y].getOwner() == black && board[x + 1][y].getOwner() == black && board[x][y - 1].getOwner() == black && board[x][y + 1].getOwner() == black) {
                // שחור ניצח
                GameFinished = true;
                black.addWin();
                printAllData(true);
            }
    }

    public Piece getPieceAtPosition(Position position) {
        return this.board[position.getpositionx()][position.getpositiony()];
    }

    private Piece getPieceAtPosition(int x, int y) {
        return this.board[x][y];
    }

    public Player getFirstPlayer() {
        return this.white;
    }

    public Player getSecondPlayer() {
        return this.black;
    }

    public boolean isGameFinished() {
        return this.GameFinished;
    }

    public boolean isSecondPlayerTurn() {
        return this.blackturn;
    }

    public void undoLastMove() {
        //Returns the last movement
        if (turn == 0) return;
        blackturn = !blackturn;
        Position a = backmove.removeLast();
        Position b = backmove.removeLast();
        this.board[b.getpositionx()][b.getpositiony()] = board[a.getpositionx()][a.getpositiony()];
        this.board[a.getpositionx()][a.getpositiony()] = null;
        positions[a.getpositionx()][a.getpositiony()].removpiece(this.board[b.getpositionx()][b.getpositiony()]);
        this.board[b.getpositionx()][b.getpositiony()].minosHistory();
        //Returns recent kills
        if (turnofkill.size() > 0) {
            while (turnofkill.getLast() == turn - 1) {
                turnofkill.removeLast();
                Position c = positionsofkill.removeLast();
                this.board[c.getpositionx()][c.getpositiony()] = killedpieces.removeLast();
                c.setPieceAtPosition(this.board[c.getpositionx()][c.getpositiony()]);
                ((Pawn) this.board[b.getpositionx()][b.getpositiony()]).minusKill();
                if (this.board[c.getpositionx()][c.getpositiony()].getOwner() == black) blackkill--;
                if (turnofkill.size() == 0) {
                    return;
                }
            }
        }
        turn--;
    }

    public int getBoardSize() {
        return size;
    }

    public void reset() {
        //Resets the data and builds a board
        this.board = new ConcretePiece[size][size];
        for (int i=0;i<11;i++){
            for (int j=0;j<11;j++){
                positions[i][j]=new Position(i,j);
            }
        }
        this.allPositions.clear();
        this.GameFinished = false;
        this.blackturn = true;
        killedpieces.clear();
        positionsofkill.clear();
        turnofkill.clear();
        backmove.clear();
        turn = 0;
        blackkill = 0;
        allpieces.clear();
        //Add the pieces
        for (int i = 3; i < 8; i++) {
            this.board[0][i] = new Pawn(this.black);
            this.board[0][i].setNumber(i - 2);
            this.board[0][i].addHistory(new Position(0, i));
            positions[0][i].addpiece(this.board[0][i]);
            allpieces.add(this.board[0][i]);
        }

        this.board[1][5] = new Pawn(this.black);
        this.board[1][5].setNumber(6);
        this.board[1][5].addHistory(new Position(1, 5));
        positions[1][5].addpiece(this.board[1][5]);
        allpieces.add(this.board[1][5]);
        for (int i = 3; i < 8; i++) {
            this.board[10][i] = new Pawn(this.black);
            this.board[10][i].setNumber(i + 4);
            this.board[10][i].addHistory(new Position(10, i));
            positions[10][i].addpiece(this.board[10][i]);
            allpieces.add(this.board[10][i]);
        }

        this.board[9][5] = new Pawn(this.black);
        this.board[9][5].setNumber(12);
        this.board[9][5].addHistory(new Position(9, 5));
        positions[9][5].addpiece(this.board[9][5]);
        allpieces.add(this.board[9][5]);
        for (int i = 3; i < 8; i++) {
            this.board[i][0] = new Pawn(this.black);
            this.board[i][0].setNumber(i + 10);
            this.board[i][0].addHistory(new Position(i, 0));
            positions[i][0].addpiece(this.board[i][0]);
            allpieces.add(this.board[i][0]);
        }
        this.board[5][1] = new Pawn(this.black);
        this.board[5][1].setNumber(18);
        this.board[5][1].addHistory(new Position(5, 1));
        positions[5][1].addpiece(this.board[5][1]);
        allpieces.add(this.board[5][1]);
        for (int i = 3; i < 8; i++) {
            this.board[i][10] = new Pawn(this.black);
            this.board[i][10].setNumber(i + 17);
            this.board[i][10].addHistory(new Position(i, 10));
            positions[i][10].addpiece(this.board[i][10]);
            allpieces.add(this.board[i][10]);
        }

        this.board[5][9] = new Pawn(this.black);
        this.board[5][9].setNumber(19);
        this.board[5][9].addHistory(new Position(5, 9));
        positions[5][9].addpiece(this.board[5][9]);
        allpieces.add(this.board[5][9]);

        this.board[5][3] = new Pawn(this.white);
        this.board[5][3].setNumber(1);
        this.board[5][3].addHistory(new Position(5, 3));
        positions[5][3].addpiece(this.board[5][3]);
        allpieces.add(this.board[5][3]);
        for (int i = 4; i < 7; i++) {
            this.board[i][4] = new Pawn(this.white);
            this.board[i][4].setNumber(i - 2);
            this.board[i][4].addHistory(new Position(i, 4));
            positions[i][4].addpiece(this.board[i][4]);
            allpieces.add(this.board[i][4]);
        }

        for (int i = 3; i < 8; i++) {
            if (i != 5) {
                this.board[i][5] = new Pawn(this.white);
                this.board[i][5].setNumber(i + 2);
                this.board[i][5].addHistory(new Position(i, 5));
                positions[i][5].addpiece(this.board[i][5]);
                allpieces.add(this.board[i][5]);
            }
        }
        this.board[5][5] = new King(this.white);
        this.board[5][5].setNumber(7);
        this.board[5][5].addHistory(new Position(5, 5));
        positions[5][5].addpiece(this.board[5][5]);
        allpieces.add(this.board[5][5]);

        for (int i = 4; i < 7; i++) {
            this.board[i][6] = new Pawn(this.white);
            this.board[i][6].setNumber(i + 6);
            this.board[i][6].addHistory(new Position(i, 6));
            positions[i][6].addpiece(this.board[i][6]);
            allpieces.add(this.board[i][6]);
        }

        this.board[5][7] = new Pawn(this.white);
        this.board[5][7].setNumber(13);
        this.board[5][7].addHistory(new Position(5, 7));
        positions[5][7].addpiece(this.board[5][7]);
        allpieces.add(this.board[5][7]);

        this.board[3][0].setNumber(7);
        this.board[4][0].setNumber(9);
        this.board[5][0].setNumber(11);
        this.board[6][0].setNumber(15);
        this.board[7][0].setNumber(17);
        this.board[5][1].setNumber(12);
        this.board[5][9].setNumber(13);
        this.board[5][10].setNumber(14);
        this.board[3][10].setNumber(8);
        this.board[4][10].setNumber(10);
        this.board[6][10].setNumber(16);
        this.board[7][10].setNumber(18);
    }

    private void printAllData(boolean blackwon) {
        Comparator<ConcretePiece> won;
        pieceCompare1 compare1 = new pieceCompare1();
        if (blackwon) {
            won = new isblackwon().thenComparing(compare1);
        } else won = new iswhitewon().thenComparing(compare1);
        Collections.sort(allpieces, won);
        print1();
        print75();
        pieceCompare2 compare2 = new pieceCompare2();
        if (blackwon) won = new isblackwon();
        else won = new iswhitewon();
        won = new pieceCompare2().thenComparing(won);
        Collections.sort(allpieces, won);
        print2();
        print75();
        pieceCompare3 compare3 = new pieceCompare3();
        if (blackwon) won = new isblackwon();
        else won = new iswhitewon();
        won = new pieceCompare3().thenComparing(won);
        Collections.sort(allpieces, won);
        print3();
        print75();
        positionCompare compare4 = new positionCompare();
        for (int i = 0; i < this.positions.length; i++) {
            for (int j = 0; j < this.positions.length; j++) {
            if (this.positions[i][j]!=null)   this.allPositions.add(this.positions[i][j]);
            }
        }
        Collections.sort(allPositions, compare4);
        print4();
    }

    private void print75() {
        for (int i = 0; i < 75; i++) {
            System.out.print("*");
        }
        System.out.println("");
    }

    private void print1() {
        for (int i = 0; i < allpieces.size(); i++) {
            if (allpieces.get(i).getHistory().size() > 1) {
                System.out.println(allpieces.get(i).toString() + "[" + allpieces.get(i).getHistoryString() + "]");

            }
        }

    }

    private void print2() {
        for (int i = 1; i < allpieces.size(); i++) {
            if (((Pawn) allpieces.get(i)).getKill() != 0) {
                System.out.println(allpieces.get(i).toString() + "" + ((Pawn) allpieces.get(i)).stringkill() + "");
            }
        }
    }

    private void print3() {
        for (int i = 0; i < allpieces.size(); i++) {
            if (allpieces.get(i).getHistory().size() > 1) {
                System.out.println(allpieces.get(i).toString() + "" + allpieces.get(i).getDistance() + " squares");

            }
        }
    }

    private void print4() {
int i=0;
        for (int j = 0; j < this.allPositions.size(); j++) {

            if (allPositions.get(j).size() > 1) {
                System.out.print("(" + allPositions.get(j).getpositionx() + "," + allPositions.get(j).getpositiony() + ")");
                System.out.println(allPositions.get(j).size() + " pieces");
            }

    }
}}
class pieceCompare1 implements Comparator<ConcretePiece> {
    public int compare(ConcretePiece piece1, ConcretePiece piece2) {

        if (piece1.getHistory().size() > piece2.getHistory().size()) return 1;
        if (piece1.getHistory().size() < piece2.getHistory().size()) return -1;
        if (piece1.number > piece2.number) return 1;
        if (piece1.number < piece2.number) return -1;

        return 0;
    }
}
class pieceCompare2 implements Comparator<ConcretePiece>{
    public int compare(ConcretePiece piece1,ConcretePiece piece2){
        if (piece1.getType()=="♔")return -1;
        if (piece2.getType()=="♔")return 1;
        if (((Pawn)piece1).getKill()>((Pawn)piece2).getKill())return -1;
        if (((Pawn)piece1).getKill()<((Pawn)piece2).getKill())return 1;
        if (piece1.number>piece2.number)return 1;
        if (piece1.number<piece2.number)return -1;
        return 0;
    }
}
class pieceCompare3 implements Comparator<ConcretePiece>{
    public int compare(ConcretePiece piece1,ConcretePiece piece2){
        if (piece1.getDistance()>piece2.getDistance())return -1;
        if (piece1.getDistance()<piece2.getDistance())return 1;
        if (piece1.number>piece2.number)return 1;
        if (piece1.number<piece2.number)return -1;
    return 0;
}
}
class positionCompare implements Comparator<Position>{
    public int compare(Position position1,Position position2){
        if (position1==null)return 1;
        if (position2==null)return -1;
        if (position1.size()>position2.size())return -1;
        if (position1.size()<position2.size())return 1;
        if (position1.getpositionx()>position2.getpositionx())return -1;
        if (position1.getpositionx()<position2.getpositionx())return 1;
        if (position1.getpositiony()>position2.getpositiony())return -1;
        if (position1.getpositiony()<position2.getpositiony())return 1;
        return 0;
    }
}
class isblackwon implements Comparator<ConcretePiece>{
    public int compare(ConcretePiece piece1,ConcretePiece piece2){
        if (piece1.getOwner().isPlayerOne()&&piece2.getOwner().isPlayerOne())return 0;
        if (piece1.getOwner().isPlayerOne())return 1;
        if (piece2.getOwner().isPlayerOne())return -1;
        return 0;
    }
}
class iswhitewon implements Comparator<ConcretePiece>{
    public int compare(ConcretePiece piece1,ConcretePiece piece2){
        if (piece1.getOwner().isPlayerOne()&&piece2.getOwner().isPlayerOne())return 0;
        if (piece1.getOwner().isPlayerOne())return -1;
        if (piece2.getOwner().isPlayerOne())return 1;
        return 0;
    }
}