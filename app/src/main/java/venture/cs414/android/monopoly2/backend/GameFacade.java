package venture.cs414.android.monopoly2.backend;

import android.content.Context;
import android.os.CountDownTimer;

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

    CountDownTimer timer;
    String timerString;

    private String currentMessage;

    private boolean gameOver;

   private static GameFacade instance = new GameFacade();

    private GameFacade(){
    }

    public static GameFacade getInstance(){
        return instance;
    }

    public void setUp(int numPlayers, int numMinutes, Context context){
        players = new ArrayList<Player>();
        for (int i = 1; i < numPlayers+1; i++) {
            String playerName = ("Player " + i);
            players.add(new Player(playerName, "P" + i));
        }
        currentPlayer = players.get(0);
        currentPlayerHasMoved = false;

        board = new Board(context);

        bank = Bank.getInstance();

        dice = new Dice();
        currentMessage = "Welcome to Zelda Monopoly!";

        gameOver = false;

        int numMiliSeconds = (numMinutes * 60 * 1000);
        new CountDownTimer(numMiliSeconds, 1000) {

            public void onTick(long millisUntilFinished) {
                int minutes = (((int)millisUntilFinished / 1000) / 60);
                int seconds = (((int)millisUntilFinished / 1000) % 60);
                timerString = "Game Time:\n" + minutes + ":";
                if(seconds < 10){
                    timerString += "0";
                }
                timerString += seconds;
            }

            public void onFinish() {
                //timerText.setText("done!");
                //endGame();
                timerString = "Game Finished";
                //gameOver = true;
            }
        }.start();
    }

    public String getTimerString(){
        return timerString;
    }

    public String getCurrentMessage() {
        return currentMessage;
    }

    public boolean currentPlayerHasMoved() {
        return currentPlayerHasMoved;
    }

    public String getCurrentPlayerInfo(){
        String description = currentPlayer.getName() + "\n"
                + "Token: " + currentPlayer.getToken() + "\n"
                + currentPlayer.getMoney() + " Rupees\n"
                + "Position: " + board.getBoardSpace(currentPlayer.getLocation()).getName();

        return description;
    }

    public String getOtherPlayerInfo(){
        String description = "";
        for(Player player : players){
            if(!player.equals(currentPlayer)){
                description += player.getName() + " (" + player.getToken() + "):\n" + player.getMoney() + " Rupees\n";
            }
        }
        return description;
    }

    public List<String> getOtherPlayerNames(){
        List<String> otherPlayers = new ArrayList<>();

        for(Player player : players){
            if(!player.equals(currentPlayer)){
                otherPlayers.add(player.getName());
            }
        }

        return otherPlayers;
    }

    public List<String> getPlayerNames(){
        List<String> allPlayers = new ArrayList<>();

        for(Player player : players){
            allPlayers.add(player.getName());
        }

        return allPlayers;
    }

    public String getCurrentPropertyName(){
        Property deed = board.getBoardSpace(currentPlayer.getLocation()).getDeed();
        if(deed != null){
            return deed.getName();
        }
        return null;
    }

    public boolean currentPlayerInJail(){
        return currentPlayer.isInJail();
    }

    public String moveCurrentPlayer(){
        Property deed = null;
        Player player = currentPlayer;

        if(currentPlayerHasMoved){
            currentMessage = "You have already moved!";
            return currentMessage;
        }

        if(currentPlayer.isInJail()){
            currentMessage = "You are in  jail and can't move until you get free!";
            return currentMessage;
        }

        int diceRoll = dice.rollDice();
        boolean rolledDouble = dice.rolledDouble();
        String returnString = "You rolled a " + diceRoll;

        // player isn't in jail
        if (rolledDouble) {
            player.incrementConsecutiveTurns();
            if (player.getConsecutiveTurns() >= 3) {
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

		/*if(player.getLocation() == 30){
			player.putInJail();
			returnString = "You rolled a " + diceRoll;
			returnString += "\nand moved that many spaces!";
			returnString += "\nYou landed in 'Go to Jail' and went straight to jail";
			currentPlayerHasMoved = true;
			deed = null;
		}*/

        if(deed != null){
            if(!(deed.getOwner() instanceof Bank)){
                Player owner = (Player) deed.getOwner();
                if(!currentPlayer.equals(owner)) {
                    int rent = deed.calculateRent();
                    payPlayer(currentPlayer, (Player) deed.getOwner(), rent);
                    returnString += "\n\nYou landed on " + deed.getName() + " Owned by " + ((Player) deed.getOwner()).getName();
                    returnString += "\nYou paid " + rent + " Rupees in rent.";
                }
            }else{
                returnString += "\nYou landed on " + deed.getName();
                //returnString += "\nIf you'd like to buy it for " + deed.getCost() + " Rupees, press 'Buy Property'";
            }
        }else{
            String spaceName = board.getBoardSpaces()[currentPlayer.getLocation()].getName();
            int tax = 0;
            switch (spaceName) {
                case "Door Fee":
                    tax = 200;
                    player.removeMoney(tax);
                    returnString += "\nYou paid a " + spaceName + "of " + tax + " Rupees";
                    break;
                case "Mask Merchant":
                    tax = 100;
                    player.removeMoney(tax);
                    returnString += "\nYou paid the " + spaceName + " " + tax + " Rupees";
                    break;
                case "Go To Jail":
                    player.putInJail();
                    returnString = "You rolled a " + diceRoll;
                    returnString += "\nand moved that many spaces!";
                    returnString += "\nYou landed on 'Go to Jail' and went straight to jail";
                    currentPlayerHasMoved = true;
                    break;
                case "Empty Bottle":
                    returnString += "\nYou landed on Empty Bottle, draw a Empty Bottle card.";
                    Card chanceCard = board.drawChanceCard();
                    returnString += "\nEmpty Bottle: " + chanceCard.getDescription();
                    returnString += performCardAction(chanceCard);
                    break;
                case "Treasure Chest":
                    returnString += "\nYou landed on Treasure Chest, draw a Tresure Chest card.";
                    Card communityChestCard = board.drawCommunityChestCard();
                    returnString += "\nTreasure Chest: " + communityChestCard.getDescription();
                    returnString += performCardAction(communityChestCard);
                    break;
                default:
                    // do nothing
                    break;
            }
        }

        currentMessage = returnString;

        return returnString;
    }

    private String performCardAction(Card card) {
        List<String> actions = card.getActionDetails();
        String actionResult = "";
        int position;
        Space space;
        Property deed;

        switch(actions.get(0)){

            case "moveTo":
                position = Integer.parseInt(actions.get(1));
                currentPlayer.movePlayer(position);
                space = board.getBoardSpace(currentPlayer.getLocation());
                deed = space.getDeed();
                if(deed != null){
                    if(!(deed.getOwner() instanceof Bank)){
                        Player owner = (Player) deed.getOwner();
                        if(!currentPlayer.equals(owner)) {
                            int rent = deed.calculateRent();
                            payPlayer(currentPlayer, (Player) deed.getOwner(), rent);
                            actionResult += "\nYou moved to " + deed.getName() + " Owned by " + ((Player) deed.getOwner()).getName();
                            actionResult += "\nYou paid " + rent + "Rupees in rent.";
                        }
                    }else{
                        actionResult += "\nYou moved to " + deed.getName();
                        actionResult += "\nIf you'd like to buy it for " + deed.getCost() + " Rupees, press 'Buy Property'";
                    }
                }
                else{
                    actionResult += "\nYou moved to " + space.getName();
                }
                break;

            case "moveToNext":
                String deedType = actions.get(1);
                if(deedType.equals("utility")){
                    position = board.getNextUtilityPosition(currentPlayer.getLocation());
                }
                else{
                    position = board.getNextRailroadPosition(currentPlayer.getLocation());
                }
                currentPlayer.movePlayer(position);
                space = board.getBoardSpace(currentPlayer.getLocation());
                deed = space.getDeed();
                if(deed.getOwner() instanceof Bank){
                    actionResult += "\nYou moved to " + deed.getName();
                    //actionResult += "\nIf you'd like to buy it for " + deed.getCost() + " Rupees, press 'Buy Property'";
                }
                else{
                    Player deedOwner = (Player) deed.getOwner();
                    if(!currentPlayer.equals(deedOwner)){
                        int rent;
                        if(deed instanceof Utility){
                            int roll = dice.rollDice();
                            rent = roll * 10;
                            actionResult += "\nYou moved to the next Utility, " + deed.getName() + ", Owned by " + deedOwner.getName();
                            actionResult += "\nYou paid " + rent + "Rupees in rent. Your roll was " + roll + " (card says 10*roll).";
                        }
                        else{
                            rent = 2*deed.calculateRent();
                            actionResult += "\nYou moved to the next Railroad, " + deed.getName() + ", Owned by " + deedOwner.getName();
                            actionResult += "\nYou paid " + rent + " Rupees in rent. The rent was " + deed.calculateRent() + " (card says 2*rent).";
                        }
                        payPlayer(currentPlayer, deedOwner, rent);
                    }
                }
                break;

            case "collectBank":
                int collectAmount = Integer.parseInt(actions.get(1));
                currentPlayer.addMoney(collectAmount);
                actionResult += "\nYou collected " + collectAmount + " from the bank.";
                break;

            case "getOutOfJail":
                currentPlayer.storeGetOutOfJailCard(card);
                actionResult += "\nYou added the 'Get Out Of Jail' card to your collection for a later use!";
                break;

            case "moveBack":
                //expected distance to move back is 3
                //so distance should be 37
                //meaning that from a chance space
                //they can land on Income Tax, New York Avenue, or
                //Community Chest
                int distance = Integer.parseInt(actions.get(1));
                currentPlayer.movePlayerBack(distance);
                space = board.getBoardSpace(currentPlayer.getLocation());
                deed = space.getDeed();
                if(space.getName().equals("Door Fee")){
                    int tax = 200;
                    currentPlayer.removeMoney(tax);
                    actionResult += "\nYou moved back to Income Tax.";
                    actionResult += "\nYou paid a Income Tax of " + tax + " Rupees";
                }
                else if(space.getName().equals("Vaati's Palace")){
                    if(deed.getOwner() instanceof Bank){
                        actionResult += "\nYou moved to " + deed.getName();
                        //actionResult += "\nIf you'd like to buy it for " + deed.getCost() + " Rupees, press 'Buy Property'";
                    }
                    else{
                        if(!deed.getOwner().equals(currentPlayer)){
                            int rent = deed.calculateRent();
                            payPlayer(currentPlayer, (Player) deed.getOwner(), rent);
                            actionResult += "\nYou moved to " + deed.getName() + " Owned by " + ((Player) deed.getOwner()).getName();
                            actionResult += "\nYou paid " + rent + " Rupees in rent.";
                        }
                    }
                }
                //Community Chest
                else {
                    actionResult += "\nYou landed on Community Chest, draw a Community Chest card.";
                    Card communityChestCard = board.drawCommunityChestCard();
                    actionResult += "\nCommunity Chest:\n" + communityChestCard.getDescription();
                    actionResult += performCardAction(communityChestCard);
                }
                break;

            case "goToJail":
                currentPlayer.putInJail();
                currentPlayerHasMoved = true;
                actionResult += "\nYou have been placed in jail.";
                break;

            case "payForBuildings":
                int numHouses = 0;
                int numHotels = 0;
                int pricePerHouse = Integer.parseInt(actions.get(1));
                int pricePerHotel = Integer.parseInt(actions.get(2));
                for(Property owned : currentPlayer.getPropertiesOwned()){
                    if(owned instanceof Street){
                        numHouses += ((Street) owned).getNumHouses();
                        if(((Street) owned).hasHotel()) numHotels++;
                    }
                }
                int totalCost = numHouses * pricePerHouse + numHotels * pricePerHotel;
                currentPlayer.removeMoney(totalCost);
                actionResult += "\nYou paid a total of " + totalCost + " for your " + numHouses + " houses and " + numHotels + " hotels.";
                break;

            case "payBank":
                int payAmount = Integer.parseInt(actions.get(1));
                currentPlayer.removeMoney(payAmount);
                actionResult += "\nYou paid " + payAmount + " Rupees to the bank.";
                break;

            case "payEachPlayer":
                payAmount = Integer.parseInt(actions.get(1));
                for(Player player : players){
                    currentPlayer.removeMoney(payAmount);
                    player.addMoney(payAmount);
                }
                actionResult += "\nYou paid each player " + payAmount + "Rupees.";
                break;

            case "collectEachPlayer":
                collectAmount = Integer.parseInt(actions.get(1));
                for(Player player : players){
                    player.removeMoney(collectAmount);
                    currentPlayer.addMoney(collectAmount);
                }
                actionResult += "\nYou collected from each player " + collectAmount + "Rupees.";
                break;

            default:
                actionResult += "\nYou drew a card with an unknown action: " + actions.get(0);
                break;
        }
        return actionResult;
    }

    public void payJail() {
        if (currentPlayer.getMoney() > 50) {
            currentPlayer.removeMoney(50);
            currentPlayer.takeOutOfJail();
            currentMessage = "You paid your way out of Jail!";
        }else{
            currentMessage = "You could not afford to pay bail";
        }
        currentPlayerHasMoved = true;
    }

    public void rollForJail(){
        if(currentPlayer.getTurnsInJail() < 3) {
            int amountToMove = dice.rollDice();
            if (dice.rolledDouble()) {
                currentPlayer.setLocation(10 + amountToMove);
                currentPlayer.takeOutOfJail();
                currentMessage = "You rolled a " + amountToMove + " and got out of jail!";
            } else {
                currentPlayer.incrementTurnsInJail();
                currentPlayerHasMoved = true;
                currentMessage = "You rolled a " + amountToMove + " and did not get out of jail";
            }
        }else{
            currentMessage = "You have been in jail for 3 turns!  You must pay or use a card to get out!";
        }
    }

    public void useJailCard(){

    }

    public List<String> getMortgagableProperties(){
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
        return propertiesCanMortgage;
    }

    public List<String> getMortgagedProperties(){
        List<String> mortgagedProperties = new ArrayList<String>();
        for(Property property : currentPlayer.getPropertiesOwned()){
            if(property.isMortgaged()){
                mortgagedProperties.add(property.getName());
            }
        }
        return mortgagedProperties;
    }

    public List<String> getStreetsCanBuyHouses(){
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
        return streetsThatCanBuyAHouse;
    }

    public List<String> getStreetsCanBuyHotel(){
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
        return streetsThatCanBuyAHotel;
    }

    public List<String> getStreetsCanSellHouse(){
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
        return streetsThatCanSellAHouse;
    }

    public List<String> getStreetsCanSellHotel(){
        List<String> retList = new ArrayList<String>();
        for(Property prop: currentPlayer.getPropertiesOwned()){
            if(prop instanceof Street){
                if(((Street) prop).hasHotel()){
                    retList.add(prop.getName());
                }
            }
        }
        return retList;
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

    private Player getPlayerByName(String name){
        Player playerNamed = null;
        for(Player player : players){
            if(player.getName().equals(name)){
                playerNamed = player;
            }
        }
        return playerNamed;
    }

    //The following methods assume that the player is able to do the requested action with the specified property
    public void mortgageProperty(String propName){
        Property property = board.getPropertyByName(propName);
        Player player = (Player)property.getOwner();
        property.mortgage();
        player.addMoney(property.getMortgageValue());

        currentMessage = (player.getName() + " successfully mortgaged " + propName + " for " + property.getMortgageValue() + " Rupees");
    }

    public void unmortgageProperty(String propName){
        Property property = board.getPropertyByName(propName);
        Player player = (Player)property.getOwner();
        if(player.canAfford(property.getUnmortgageValue())){
            property.unmortgage();
            player.removeMoney(property.getUnmortgageValue());
            currentMessage = (player.getName() + " successfully unmortgaged " + propName + " for " + property.getUnmortgageValue() + " Rupees");
        }else{
            currentMessage = "You could not afford to do that";
        }
    }

    public void buyAHouse(String streetName){
        Street street = (Street) board.getPropertyByName(streetName);
        Player player = (Player)street.getOwner();
        if(player.canAfford(street.getHouseCost()) && bank.hasHouses()) {
            street.placeHouse();
            player.removeMoney(street.getHouseCost());
            bank.removeHouse();
            currentMessage = (player.getName() + "successfully bought a house on " + streetName + " for " + street.getHouseCost() + " Rupees");
        }else{
            if(!bank.hasHouses()){
                currentMessage = "The bank is out of houses!";
            }
            currentMessage = "You cannot afford to do that";
        }
    }

    public void sellAHouse(String streetName){
        Street street = (Street) board.getPropertyByName(streetName);
        Player player = (Player)street.getOwner();
        street.removeHouse();
        bank.addHouse();
        player.addMoney(street.getHouseCost() / 2);

        currentMessage = (player.getName() + "successfully sold a house from " + streetName + " for " + (street.getHouseCost()/2) + " Rupees");
    }

    public void buyAHotel(String streetName){
        Street street = (Street) board.getPropertyByName(streetName);
        Player player = (Player)street.getOwner();
        if(player.canAfford(street.getHotelCost()) && bank.hasHotels()){
            street.placeHotel();
            bank.removeHotel();
            for(int i=0; i<4;i++){
                bank.addHouse();
            }
            player.removeMoney(street.getHotelCost());
            currentMessage = (player.getName() + " successfully bought a hotel on " + streetName + " for " + street.getHotelCost() + " Rupees");
        }else{
            if(!bank.hasHotels()){
                currentMessage = "The bank is out of hotels!";
            }
            currentMessage = "You cannot afford to do that";
        }

    }

    public void sellAHotel(String streetName){
        Street street = (Street) board.getPropertyByName(streetName);
        Player player = (Player)street.getOwner();
        street.removeHotel();
        bank.addHotel();
        for(int i=0; i<4; i++){
            bank.removeHouse();
        }
        player.addMoney(street.getHotelCost() / 2);

        currentMessage = (player.getName() + " successfully sold a hotel from " + streetName + " for " + (street.getHotelCost()/2) + " Rupees");
    }

    public void sellAProperty(String propertyName, String buyerName, int cost){
        Player buyer = getPlayerByName(buyerName);
        Property property = board.getPropertyByName(propertyName);
        if(!buyer.canAfford(cost)){
            currentMessage = buyerName + " cannot afford that price!";
        }else {
            currentPlayer.removeFromPropertiesOwned(property);
            buyer.addToPropertiesOwned(property);
            property.setOwner(buyer);
            payPlayer(buyer, currentPlayer, cost);
            currentMessage = "Transaction Successful! :D";
        }
    }

    public void advanceTurn(){
        if(currentPlayerHasMoved){
            if(currentPlayer.getMoney() <= 0){
                currentMessage = (currentPlayer.getName() + " went bankrupt and left the game");
                removeCurrentPlayerFromGame();
                //int nextIndex = players.indexOf(currentPlayer) + 1;
                int nextIndex = players.indexOf(currentPlayer);
                if(nextIndex >= players.size()){
                    nextIndex = 0;
                }
                currentPlayer = players.get(nextIndex);
                currentPlayerHasMoved = false;
                currentMessage += "\nIt is now " + currentPlayer.getName() + "'s turn!";
            }else{
                currentPlayer.resetConsecutiveTurns();
                int nextIndex = players.indexOf(currentPlayer) + 1;
                if (nextIndex >= players.size()) {
                    nextIndex = 0;
                }
                currentPlayer = players.get(nextIndex);
                currentPlayerHasMoved = false;
                currentMessage = "It is now " + currentPlayer.getName() + "'s turn!";
            }

        }else{
            currentMessage = "You must move before you can end your turn!";
        }
    }

    public String removeCurrentPlayerFromGame(){
        String toRet = currentPlayer.getName() + " has left the game";
        for(Property deed : currentPlayer.getPropertiesOwned()) {
            if (deed instanceof Street) {
                while (((Street) deed).getNumHouses() > 0) {
                    ((Street) deed).removeHouse();
                }
                while (((Street) deed).hasHotel()) {
                    ((Street) deed).removeHotel();
                }
            }
            currentPlayer.removeFromPropertiesOwned(deed);
            bank.addToPropertiesOwned(deed);
            deed.setOwner(bank);
        }
        Player removed = currentPlayer;
        currentPlayerHasMoved = true;
        advanceTurn();
        players.remove(removed);

        if(players.size() == 1){
            gameOver = true;
        }

        return toRet;
    }

    public String endGame(){
        Player winner = players.get(0);
        for(Player player : players){
            if(player.getMoney() > winner.getMoney()){
                winner = player;
            }else if(player.getMoney() == winner.getMoney()){
                int playerProps = player.getPropertiesOwned().size();
                int winnerProps = winner.getPropertiesOwned().size();
                if(playerProps > winnerProps){
                    winner = player;
                }
            }
        }

        currentPlayer = winner;

        String returnString = "Game Over!\n"
                + "And the winner is:\n" + getCurrentPlayerInfo();
        return returnString;
    }

    public boolean gameIsOver(){
        return gameOver;
    }

    public int propertiesOwnedOfType(Property property){
        Owner owner = property.getOwner();
        List<Object> propertiesOwned = new ArrayList<Object>();
        for(Property deed : owner.getPropertiesOwned()){
            if(property instanceof Street){
                if(deed instanceof Street){
                    if(((Street) property).getColor().equals(((Street) deed).getColor())){
                        propertiesOwned.add(deed);
                    }
                }
            }
            else if(property instanceof Railroad){
                if(deed instanceof Railroad){
                    propertiesOwned.add(deed);
                }
            }

            else{
                if(deed instanceof Utility){
                    propertiesOwned.add(deed);
                }
            }
        }
        return propertiesOwned.size();
    }

    private void payPlayer(Player sender, Player reciever, int amount){
        sender.removeMoney(amount);
        reciever.addMoney(amount);
    }

    public String getCurrentPropertySale(){
        Property deed = board.getBoardSpace(currentPlayer.getLocation()).getDeed();
        if(deed != null){
            if(deed.getOwner() instanceof Bank){
                return "Would you like to buy " + deed.getName() + " for " + deed.getCost() + " Rupees?";
            }
        }
        return null;
    }

    public boolean currentPlayerBuyCurrentProperty(){
        Property deed = board.getBoardSpace(currentPlayer.getLocation()).getDeed();
        if(currentPlayer.canAfford(deed.getCost())){
            currentPlayer.removeMoney(deed.getCost());
            currentPlayer.addToPropertiesOwned(deed);
            bank.removeFromPropertiesOwned(deed);
            deed.setOwner(currentPlayer);
            return true;
        }else{
            return false;
        }
    }

    public void playerBuyProperty(String playerName, String propertyName, int cost){
        Player player = getPlayerByName(playerName);
        Property property = board.getPropertyByName(propertyName);

        player.removeMoney(cost);
        player.addToPropertiesOwned(property);
        bank.removeFromPropertiesOwned(property);
        property.setOwner(player);
    }

    public boolean playerCanAfford(String playerName, int amount){
        Player player = getPlayerByName(playerName);
        return player.canAfford(amount);
    }

    public String getSpaceInfo(int spaceNumber){
        String tempString = "";
        String buildings = "";
        Space tempSpace = board.getBoardSpace(spaceNumber);
        Property tempDeed = tempSpace.getDeed();
        if(tempDeed != null){
            if(tempDeed instanceof Street){
                Street street = (Street) tempDeed;
                int numBuildings = street.getNumberOfBuildings();
                if(numBuildings > 0){
                    if(numBuildings == 5){
                        buildings = "1 Hotel";
                    }
                    else{
                        buildings = numBuildings + " House(s)";
                    }
                }
            }

        }
        else{

        }
        String tempPlayersOnSpace = "";
        List<String> playerTokens = new ArrayList<>();
        for (Player player: players){
            if(player.getLocation() == spaceNumber){
                playerTokens.add(player.getToken());
            }
        }
        if(playerTokens.isEmpty()){

        }
        else{
            for(String token: playerTokens){
                tempPlayersOnSpace += token;
            }
        }
        tempString += tempPlayersOnSpace + " " + buildings;
        return tempString;
    }

    public String getClickedSpaceInfo(int spaceNumber){
        String ownerString = "";
        String buildings = "Buildings: None\n";
        Space s1 = board.getBoardSpace(spaceNumber);
        String spaceInfo = "Space: " + s1.getName() + "\n";
        Property deed = s1.getDeed();
        if (deed != null){
            Owner owner = deed.getOwner();
            if(owner instanceof Bank){
                ownerString = "Owner: Bank\n";
            }
            else{
                ownerString = "Owner: " + ((Player) deed.getOwner()).getName() + "\n";
                if(deed instanceof Street){
                    Street street = (Street) deed;
                    int numBuildings = street.getNumberOfBuildings();
                    if(numBuildings > 0){
                        if(numBuildings == 5){
                            buildings = "Buildings: 1 Hotel\n";
                        }
                        else{
                            buildings = "Buildings: " + numBuildings + " House(s)\n";
                        }
                    }
                }
            }
        }
        else{
            ownerString = "Owner: Not able to be owned.\n";
        }
        spaceInfo += ownerString;
        spaceInfo += buildings;
        String playersAtLoc = "";
        playersAtLoc = "Players on Space:\n";
        List<String> playerNames = new ArrayList<>();
        for (Player player: players){
            if(player.getLocation() == spaceNumber){
                playerNames.add(player.getName());
            }
        }
        if(playerNames.isEmpty()){
            playersAtLoc += "NONE\n";
        }
        else{
            for(String name: playerNames){
                playersAtLoc += name + "\n";
            }
        }
        spaceInfo += playersAtLoc;
        if(spaceNumber == 10){
            spaceInfo += "Players in Jail:\n";
            List<String> playerNames2 = new ArrayList<>();
            for (Player player: players){
                if(player.isInJail()){
                    playerNames2.add(player.getName());
                }
            }
            if(playerNames2.isEmpty()){
                spaceInfo += "NONE\n";
            }
            else{
                for(String name: playerNames2){
                    spaceInfo += name + "\n";
                }
            }

        }

        return spaceInfo;
    }
}
