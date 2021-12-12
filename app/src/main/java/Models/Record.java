package Models;

public class Record {

    private String playerName= "";
    private int score = 0;
    private double lat = 0.0;
    private double lon = 0.0;

    public Record() { }

    public Record(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public Record setScore(int score) {
        this.score = score;
        return this;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Record setPlayerName(String name) {
        this.playerName = playerName;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public Record setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLon() {
        return lon;
    }

    public Record setLon(double lon) {
        this.lon = lon;
        return this;
    }
}
