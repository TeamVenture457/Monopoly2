package venture.cs414.android.monopoly2.backend;

/**
 * Created by James on 11/11/2016.
 */
public abstract class Property {
    private String name;
    private int cost;
    private int rent;
    private int mortgageValue;
    private boolean isMortgaged;
    private String description;
    private Owner owner;

    public Property(String name, int cost, int rent, int mortgageValue) {
        this.name = name;
        this.cost = cost;
        this.rent = rent;
        this.mortgageValue = mortgageValue;
        isMortgaged = false;
        owner = Bank.getInstance();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

    public int getMortgageValue() {
        return mortgageValue;
    }

    public void setMortgageValue(int mortgageValue) {
        this.mortgageValue = mortgageValue;
    }

    public boolean isMortgaged() {
        return isMortgaged;
    }

    public void setIsMortgaged(boolean isMortgaged) {
        this.isMortgaged = isMortgaged;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public void unmortgage(){
        isMortgaged = false;
    }

    public int getUnmortgageValue(){
        return (int)(mortgageValue * 1.10);
    }

    public abstract int calculateRent();
    public abstract void generateDescription();
    public abstract boolean mortgage();
}
