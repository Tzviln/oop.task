import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Position {

   private final int a;
    private final int b;
    private final List<Piece> pieces;
    public void addpiece(Piece piece){
        this.pieces.add(piece);
    }
    public void removpiece(Piece piece){
        this.pieces.remove(piece);
    }
    public List<Piece> getPieces() {
        return pieces;
    }
    public String toString(){
        return "("+this.a+", "+this.b+")";
}

    ConcretePiece AtPosition;
    Position(int x,int y){
        this.a=x;
        this.b=y;
        this.pieces=new LinkedList<>();
    }

    public ConcretePiece getPieceAtPosition  (Position a){
        return a.AtPosition;
    }
    public ConcretePiece getPieceAtPosition (){
        return this.AtPosition;
    }
    public void setPieceAtPosition(ConcretePiece A){
        this.AtPosition=A;
    }
    public int getpositionx(){
        return this.a;
    }
    public int getpositiony(){
        return this.b;
    }
    public int size(){
        return (new HashSet(this.pieces)).size();
    }
}
