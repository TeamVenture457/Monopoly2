package cs414.a5.cs414g.backend;

import java.util.List;

/**
 * Created by James on 11/11/2016.
 */
public class Utility extends Property {
    Dice dice;

    public Utility(String name, int cost, int rent, int mortgageValue) {
        super(name, cost, rent, mortgageValue);
        dice = new Dice();
    }

    @Override
    public int calculateRent() {
        int utilities = getUtilitiesOwned();
        int rent = 0;
        int diceRoll = dice.rollDice();
        switch(utilities){
            case 1:
                rent = 4 * diceRoll;
                break;
            case 2:
                rent = 10 * diceRoll;
                break;
            default:
                System.out.println("Unexpected number of Utilities owned: " + utilities);
        }
        return rent;
    }

    private int getUtilitiesOwned() {
        int numUtilities = 0;
        List<Property> properties = this.getOwner().getPropertiesOwned();
        for(Property owned : properties){
            if(owned instanceof Utility) numUtilities++;
        }
        //TO-DO get number of utilities owned by owner

        return numUtilities;
    }

    @Override
    public void generateDescription() {
        String description = "Utility: " + getName()
                + "\nPrice: " + getCost()
                + "\nRent: " + getRent()
                + "\nMortgage value: " + getMortgageValue();
        setDescription(description);
    }

    @Override
    public boolean mortgage() {
        if(!isMortgaged()){
            setIsMortgaged(true);
            return true;
        }
        return false;
    }
}
