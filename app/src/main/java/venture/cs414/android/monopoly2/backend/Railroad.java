package venture.cs414.android.monopoly2.backend;

import java.util.List;

/**
 * Created by James on 11/11/2016.
 */
public class Railroad extends Property {
    public Railroad(String name, int cost, int rent, int mortgageValue) {
        super(name, cost, rent, mortgageValue);
    }

    @Override
    public int calculateRent() {
        int railroads = getRailroadsOwned();
        return 2^railroads * getRent();
    }

    private int getRailroadsOwned() {
        int numRailroads = 0;
        List<Property> properties = this.getOwner().getPropertiesOwned();
        for(Property owned : properties){
            if(owned instanceof Railroad) numRailroads++;
        }
        //TO-DO get number of railroads owned by this railroad's owner
        return numRailroads;
    }

    @Override
    public void generateDescription() {
        String description = "Railroad: " + getName()
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
