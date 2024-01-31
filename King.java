public class King extends ConcretePiece{
    public King(Player a){
        this.player=a;
    }
    public String getType(){
        return "♔";
    }
    public String toString(){
        return "k"+this.number+": ";
    }
}
