public class AIPlayer {

    boolean white;
    boolean doneCastling = false; //Dansk: doneRokade
    boolean isAi;

    public AIPlayer(){
        this.isAi = true;
        this.white = true;

    }

    public AIPlayer(boolean colorWhite, boolean isAi){
        this.isAi = isAi;
        this.white = colorWhite;

    }



    public boolean isWhite() {
        return white;
    }
    public void setWhite(boolean white) {
        this.white = white;
    }

    public boolean isDoneCastling() {
        return doneCastling;
    }
    public void setDoneCastling(boolean doneCastling) {
        this.doneCastling = doneCastling;
    }

//    public String getPlayerType() {
//        return playerType;
//    }
//    public void setPlayerType(String playerType) {
//        this.playerType = playerType;
//    }

}
