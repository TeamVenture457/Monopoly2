package venture.cs414.android.monopoly2.backend;

/**
 * Created by Ben on 11/11/2016.
 */
public class Player extends Owner{
    private String name;
    private String token;
    private int money;
    private int location;
    private boolean inJail;
    private int turnsInJail;
    private int consecutiveTurns;

    public Player(String name, String token){
        super();
        this.name = name;
        this. token = token;
        money = 1500;
        location = 0;
        inJail = false;
        turnsInJail = 0;
        consecutiveTurns = 0;
    }


    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public int getMoney() {
        return money;
    }

    public void addMoney(int amount){
        money += amount;
    }

    public void removeMoney(int amount){
        money += amount;
    }

    public boolean canAfford(int amount){
        return money > amount;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public boolean isInJail() {
        return inJail;
    }

    public int getTurnsInJail() {
        return turnsInJail;
    }

    public void incrementTurnsInJail(){
        turnsInJail++;
    }

    public void resetTurnsInJail(){
        turnsInJail = 0;
    }

    public int getConsecutiveTurns() {
        return consecutiveTurns;
    }

    public void incrementConsecutiveTurns(){
        consecutiveTurns++;
    }

    public void resetConsecutiveTurns(){
        consecutiveTurns = 0;
    }

    public void movePlayer(int distance){
        int currentLocation = location;
        int nextLocation = (location + distance) % 40;
        if(nextLocation < currentLocation){
            addMoney(200);
        }
        location = nextLocation;
    }
}
