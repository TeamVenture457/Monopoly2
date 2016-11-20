package cs414.a5.cs414g.backend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 11/11/2016.
 */
public class Player extends Owner {
    private String name;
    private String token;
    private int money;
    private int location;
    private boolean inJail;
    private int turnsInJail;
    private int consecutiveTurns;
    private boolean playerAI;

    List<Card> myCards;

    public Player(String name, String token, boolean isAI){
        super();
        this.name = name;
        this. token = token;
        money = 1500;
        location = 0;
        inJail = false;
        turnsInJail = 0;
        consecutiveTurns = 0;
        myCards = new ArrayList<>();
        if(isAI){
            this.playerAI = true;
        }
        else{
            this.playerAI = false;
        }
    }

    public void storeGetOutOfJailCard(Card card){
        myCards.add(card);
    }

    public Card useGetOutOfJailCard(){
        if(myCards.isEmpty()) return null;
        takeOutOfJail();
        return myCards.remove(0);
    }

    public boolean getPlayerAI(){ return playerAI; }

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
        money -= amount;
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

    public void movePlayerTo(int position){
        int currentLocation = this.location;
        if(position < currentLocation){
            addMoney(200);
        }
        location = position;
    }

    public void movePlayerBack(int distance){
        int currentLocation = location;
        distance = 40 - distance;
        location = (currentLocation + distance) % 40;
    }

    public void putInJail(){
        location = 40;
        inJail = true;
    }

    public void takeOutOfJail(){
        this.location=10;
        inJail = false;
        turnsInJail = 0;
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof Player){
            Player otherPlayer = (Player) other;
            return this.name.equals(otherPlayer.getName());
        }

        return false;
    }
}
