package venture.cs414.android.monopoly2.backend;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 11/12/2016.
 */
public class GameFacade {

    private Board board;
    private List<Player> players;
    private Bank bank;
    private Dice dice;

    private Player currentPlayer;
    private boolean currentPlayerHasMoved;

   private static GameFacade instance = new GameFacade();

    /*public GameFacade(int numPlayers, Context context){

    }*/

    private GameFacade(){
    }

    public static GameFacade getInstance(){
        return instance;
    }

    public void setUp(int numPlayers, Context context){
        players = new ArrayList<Player>();
        for (int i = 1; i < numPlayers+1; i++) {
            String playerName = ("Player " + i);
            players.add(new Player(playerName, "Token " + i));
        }
        currentPlayer = players.get(0);
        currentPlayerHasMoved = false;

        board = new Board(context);

        bank = Bank.getInstance();

        dice = new Dice();
    }

    public boolean currentPlayerHasMoved() {
        return currentPlayerHasMoved;
    }

    public String getCurrentPlayerInfo(){
        String description = currentPlayer.getName() + "\n"
                + "Token: " + currentPlayer.getToken() + "\n"
                + "$" + currentPlayer.getMoney();

        return description;
    }

    public String getOtherPlayerInfo(){
        String description = "";
        for(Player player : players){
            if(!player.equals(currentPlayer)){
                description += player.getName() + " (" + player.getToken() + "): $" + player.getMoney() + "\n";
            }
        }
        return description;
    }

    public String rollCurrentPlayer(){
        Property deed = null;
        Player player = currentPlayer;

        if(currentPlayerHasMoved){
            return "You have already moved!";
        }

        if(currentPlayer.isInJail()){
            return "You are in  jail and can't move until you get free!";
        }

        int diceRoll = dice.rollDice();
        boolean rolledDouble = dice.rolledDouble();
        String returnString = "You rolled a " + diceRoll;

        // player isn't in jail
        if (rolledDouble) {
            player.incrementConsecutiveTurns();
            if (player.getConsecutiveTurns() == 3) {
                player.putInJail();
                player.resetConsecutiveTurns();
                returnString = "\nYou got caught speeding and went to jail!";
                currentPlayerHasMoved = true;
            } else {
                currentPlayer.movePlayer(diceRoll);
                deed = board.getBoardSpace(currentPlayer.getLocation()).getDeed();
                returnString += "\nand moved that many spaces!";
                returnString += "\nAnd you rolled doubles!  Go again!";
                currentPlayerHasMoved = false;
            }
        } else {
            returnString += "\nand moved that many spaces!";
            currentPlayer.movePlayer(diceRoll);
            deed = board.getBoardSpace(currentPlayer.getLocation()).getDeed();
            player.resetConsecutiveTurns();
            currentPlayerHasMoved = true;
        }

		if(player.getLocation() == 30){
			player.putInJail();
			returnString = "You rolled a " + diceRoll;
			returnString += "\nand moved that many spaces!";
			returnString += "\nYou landed in 'Go to Jail' and went straight to jail";
			currentPlayerHasMoved = true;
			deed = null;
		}

        /*if(deed != null){
            if(!deed.getOwner().equals(bank) && !deed.getOwner().equals(currentPlayer)){
                Owner owner = deed.getOwner();
                int numOwned = 0;
                if (deed instanceof Street) {
                    if (((Street) deed).hasHotel()) {
                        numOwned = 5;
                    } else {
                        numOwned = ((Street) deed).getNumHouses();
                    }
                } else {
                    numOwned = board.propertiesOwnedOfType(deed);
                }
                int rent = deed.calculateRent();
                currentPlayer.payRent((Player)deed.getOwner(), rent);
                returnString += "\n\nYou landed on " + deed.getName() + " Owned by " + ((Player) deed.getOwner()).getName();
                returnString += "\nYou paid $" + rent + " in rent.";
            }else{
                returnString += "\nYou landed on " + deed.getName();
                returnString += "\nIf you'd like to buy it for $" + deed.getCost() + ", press 'Buy Property'";
            }
        }else{
            String spaceName = board.getBoardSpaces()[currentPlayer.getLocation()].getName();
            int tax = 0;
            switch (spaceName) {
                case "Income Tax":
                    tax = 200;
                    if(payTax(player, tax)) {
                        returnString += "\nYou paid a " + spaceName + " of $" + tax;
                    }else{
                        returnString += "\nYou could not afford the " + spaceName + " of $" + tax;
                        returnString += "\nYou're a bad American.";
                    }
                    break;
                case "Luxury Tax":
                    tax = 100;
                    if(payTax(player, tax)) {
                        returnString += "\nYou paid a " + spaceName + " of $" + tax;
                    }else{
                        returnString += "\nYou could not afford the " + spaceName + " of $" + tax;
                        returnString += "\nYou're a bad American.";
                    }
                    break;
                case "Go To Jail":
                    player.putInJail();
                    returnString = "You rolled a " + diceRoll;
                    returnString += "\nand moved that many spaces!";
                    returnString += "\nYou landed on 'Go to Jail' and went straight to jail";
                    currentPlayerHasMoved = true;
                    deed = null;
                    break;
                default:
                    // do nothing
                    break;
            }
        }*/

        return returnString;
    }

    public String[] getMortgagableProperties(){
        List<String> propertiesCanMortgage = new ArrayList<String>();
        for (Property property : currentPlayer.getPropertiesOwned()) {
            if (!property.isMortgaged()) {
                if(property instanceof Street){
                    if(((Street)property).getNumberOfBuildings() == 0){
                        propertiesCanMortgage.add(property.getName());
                    }
                }else {
                    propertiesCanMortgage.add(property.getName());
                }
            }
        }
        return (String[])propertiesCanMortgage.toArray();
    }

    public String[] getMortgagedProperties(){
        List<String> mortgagedProperties = new ArrayList<String>();
        for(Property property : currentPlayer.getPropertiesOwned()){
            if(property.isMortgaged()){
                mortgagedProperties.add(property.getName());
            }
        }
        return (String[])mortgagedProperties.toArray();
    }

    public String[] getStreetsCanBuyHouses(){
        List<Street> streetsWithAllColors = getStreetsWithAllColors(currentPlayer);
        List<String> streetsThatCanBuyAHouse = new ArrayList<String>();
        while(!streetsWithAllColors.isEmpty()){
            Colors thisColor = null;
            List<Street> streetsOfThisColor = new ArrayList<Street>();
            for(Street street : streetsWithAllColors){
                if(streetsWithAllColors.indexOf(street) == 0){
                    thisColor = street.getColor();
                }
                if(street.getColor().equals(thisColor)){
                    streetsOfThisColor.add(street);
                }
            }
            int minHouses = 4;
            for(Street street : streetsOfThisColor){
                streetsWithAllColors.remove(street);
                if(street.getNumHouses() < minHouses && !street.hasHotel()){
                    minHouses = street.getNumHouses();
                }
            }
            if(minHouses < 4){
                for(Street street : streetsOfThisColor){
                    if(street.getNumHouses() == minHouses){
                        streetsThatCanBuyAHouse.add(street.getName());
                    }
                }
            }
        }
        return (String[])streetsThatCanBuyAHouse.toArray();
    }

    public String[] getStreetsCanBuyHotel(){
        List<Street> streetsWithAllColors = getStreetsWithAllColors(currentPlayer);
        List<String> streetsThatCanBuyAHotel = new ArrayList<String>();
        while(!streetsWithAllColors.isEmpty()){
            Colors thisColor = null;
            List<Street> streetsOfThisColor = new ArrayList<Street>();
            for(Street street : streetsWithAllColors){
                if(streetsWithAllColors.indexOf(street) == 0){
                    thisColor = street.getColor();
                }
                if(street.getColor().equals(thisColor)){
                    streetsOfThisColor.add(street);
                }
            }
            boolean allStreetsHaveMinBuildings = true;
            for(Street street : streetsOfThisColor){
                streetsWithAllColors.remove(street);
                if(street.getNumberOfBuildings() < 4){
                    allStreetsHaveMinBuildings = false;
                }
            }
            if(allStreetsHaveMinBuildings){
                for(Street street : streetsOfThisColor){
                    if(street.getNumHouses() == 4){
                        streetsThatCanBuyAHotel.add(street.getName());
                    }
                }
            }
        }
        return (String[])streetsThatCanBuyAHotel.toArray();
    }

    public String[] getStreetsCanSellHouse(){
        List<Street> streetsWithHouses = getStreetsWithHouses(currentPlayer);
        List<String> streetsThatCanSellAHouse = new ArrayList<String>();

        while (!streetsWithHouses.isEmpty()) {
            List<Street> streetsOfCurrentColor = new ArrayList<Street>();
            Colors currentColor = null;
            for (Street currentStreet : streetsWithHouses) {
                if (streetsWithHouses.indexOf(currentStreet) == 0) {
                    currentColor = currentStreet.getColor();
                }
                if (currentStreet.getColor().equals(currentColor)) {
                    streetsOfCurrentColor.add(currentStreet);
                }
            }
            int mostHouses = 0;
            for (Street currentColorStreet : streetsOfCurrentColor) {
                streetsWithHouses.remove(currentColorStreet);
                if (currentColorStreet.getNumHouses() > mostHouses) {
                    mostHouses = currentColorStreet.getNumberOfBuildings();
                }
            }
            for (Street currentColorStreet : streetsOfCurrentColor) {
                if (currentColorStreet.getNumberOfBuildings() == mostHouses) {
                    streetsThatCanSellAHouse.add(currentColorStreet.getName());
                }
            }
        }
        return (String[])streetsThatCanSellAHouse.toArray();
    }

    public String[] getStreetsCanSellHotel(){
        List<String> retList = new ArrayList<String>();
        for(Property prop: currentPlayer.getPropertiesOwned()){
            if(prop instanceof Street){
                if(((Street) prop).hasHotel()){
                    retList.add(prop.getName());
                }
            }
        }
        return (String[])retList.toArray();
    }

    private List<Street> getStreetsWithAllColors(Player player) {
        List<Street> streetsPlayerOwns = new ArrayList<Street>();
        for(Property property : player.getPropertiesOwned()){
            if(property instanceof Street){
                streetsPlayerOwns.add((Street) property);
            }
        }
        List<Street> streetsWithAllColors = new ArrayList<Street>();
        while (!streetsPlayerOwns.isEmpty()) {
            Colors thisColor = null;
            List<Street> streetsOfThisColor = new ArrayList<Street>();
            for (Street streetOwned : streetsPlayerOwns) {
                if (streetsPlayerOwns.indexOf(streetOwned) == 0) {
                    thisColor = streetOwned.getColor();
                }
                if(thisColor.equals(streetOwned.getColor())){
                    streetsOfThisColor.add(streetOwned);
                }
            }
            streetsPlayerOwns.removeAll(streetsOfThisColor);
            List<Street> streetsExpectedOfThisColor = board.getStreetsOfColor(thisColor);
            if(streetsExpectedOfThisColor.size() == streetsOfThisColor.size()) {
                streetsWithAllColors.addAll(streetsOfThisColor);
            }
        }
        return streetsWithAllColors;
    }

    private List<Street> getStreetsWithHouses(Player player) {
        List<Street> streetsWithHouses = new ArrayList<Street>();
        List<Street> streetsOwned = getStreetProperties(player.getPropertiesOwned());
        for (Street streetOwned : streetsOwned) {
            if (streetOwned.getNumHouses() > 0) {
                streetsWithHouses.add(streetOwned);
            }
        }
        return streetsWithHouses;
    }

    private List<Street> getStreetProperties(List<Property> propertiesOwned) {
        List<Street> streets = new ArrayList<Street>();
        for (Property property : propertiesOwned) {
            if (property instanceof Street) {
                streets.add((Street) property);
            }
        }
        return streets;
    }

    public String advanceTurn(){
        if(currentPlayerHasMoved){
            currentPlayer.resetConsecutiveTurns();
            int nextIndex = players.indexOf(currentPlayer) + 1;
            currentPlayer = players.get(nextIndex);
            currentPlayerHasMoved = false;
            return "It is now " + currentPlayer.getName() + "'s turn!";
        }else{
            return "You must move before you can end your turn!";
        }
    }
}
