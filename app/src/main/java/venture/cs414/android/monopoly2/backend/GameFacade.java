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

    public GameFacade(int numPlayers, Context context){
        players = new ArrayList<Player>();
        for (int i = 1; i < numPlayers+1; i++) {
            String playerName = ("Player " + i);
            players.add(new Player(playerName, "Token " + i));
        }
        currentPlayer = players.get(0);
        currentPlayerHasMoved = false;

        board = new Board();

        bank = Bank.getInstance();

        dice = new Dice();
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
                playerTakesAnotherTurn = true;
                deed = board.movePlayer(player, diceRoll);
                returnString += "\nand moved that many spaces!";
                returnString += "\nAnd you rolled doubles!  Go again!";
                currentPlayerHasMoved = false;
            }
        } else {
            returnString += "\nand moved that many spaces!";
            deed = board.movePlayer(player, diceRoll);
            player.consecutiveTurns = 0;
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
        }

        return returnString;
    }

}
