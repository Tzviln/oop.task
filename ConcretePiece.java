import java.lang.reflect.Array;
import java.util.ArrayList;

public abstract class ConcretePiece implements Piece {
    protected int number;
    public Player getOwner(){
        return this.player;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public int distance;
    public int getNumber() {
        return this.number;
    }

    String name;
    protected Player player;
    private ArrayList<Position> history=new ArrayList();
    public ConcretePiece(){}
    public int getDistance(){
        this.distance=0;
        return this.calculateDistance();
    }

private int calculateDistance(){
        for (int i=0;i<history.size()-1;i++){
            int x= history.get(i).getpositionx();
            int y= history.get(i).getpositiony();
            int x1= history.get(i+1).getpositionx();
            int y1= history.get(i+1).getpositiony();
            this.distance+=Math.abs(x-x1)+Math.abs(y-y1);
        }
        return this.distance;
}
public ArrayList<Position> getHistory(){
        return this.history;
}
public void addHistory (Position a){
        this.history.add(a);
}
public Position gotlestPosition(){
        return this.history.getLast();
}
public void minosHistory(){
        this.history.removeLast();
}
public abstract String getType();
    public abstract String toString();
public String getDistanceString(){
return this.getDistance()+" squares";
}
public String getHistoryString(){
    String ans=""+this.history.get(0);
    for (int i=1;i<this.history.size();i++){
    ans=ans+","+this.history.get(i).toString()+"";
    }
    return ans;
}
}
