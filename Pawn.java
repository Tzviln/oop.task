public class Pawn extends ConcretePiece{
private int kill;
public Pawn(Player a){
    this.player=a;
    this.kill=0;
}

    public String getType(){
        if (this.player.isPlayerOne()){
            return "♙";
        }
        else return "♟";
    }
    public String toString(){
        if (this.player.isPlayerOne()){
            return "D"+this.number+": ";
        }
        else return "A"+this.number+": ";
    }
    public int getKill(){
    return this.kill;
    }
    public void addKill(){
    ++this.kill;
    }
    public void minusKill(){
        --this.kill;
    }
    public String stringkill(){
    return this.kill+" kills";
    }

}
