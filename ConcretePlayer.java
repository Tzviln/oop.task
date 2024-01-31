public class ConcretePlayer implements Player{
    private final boolean iswhite;

    private int wins;

    public ConcretePlayer (boolean iswhite){
        this.iswhite=iswhite;
        this.wins=0;
    }
    public boolean isPlayerOne(){
        return this.iswhite;
    }
    public void addWin(){
        this.wins++;
    }

    public int getWins(){
        return this.wins;
    }

}
