public class Player {

    boolean white;
    boolean doneCastling = false; //Dansk: doneRokade
    String playerType;

    public Player(boolean colorWhite, String playerType){
        this.playerType = playerType;
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

    public String getPlayerType() {
        return playerType;
    }
    public void setPlayerType(String playerType) {
        this.playerType = playerType;
    }

}
