package venture.cs414.android.monopoly2.backend;

import java.util.List;

/**
 * Created by James on 11/11/2016.
 */
public class Street extends Property{
    private Colors color;
    private int numHouses;
    private boolean hotel;
    private int rent1House;
    private int rent2House;
    private int rent3House;
    private int rent4House;
    private int rentHotel;
    private int houseCost;
    private int hotelCost;
    private List<Property> colorGroup;

    public Street(String name, int cost, int rent, int mortgageValue) {
        super(name, cost, rent, mortgageValue);
        hotel = false;
        numHouses = 0;
    }

    public void setStreetRents(int rent1House, int rent2House, int rent3House, int rent4House, int rentHotel){
        this.rent1House = rent1House;
        this.rent2House = rent2House;
        this.rent3House = rent3House;
        this.rent4House = rent4House;
        this.rentHotel = rentHotel;
    }

    public List<Property> getColorGroup() {
        return colorGroup;
    }

    public void setColorGroup(List<Property> colorGroup) {
        this.colorGroup = colorGroup;
    }

    public int getNumHouses() {
        return numHouses;
    }

    public void setNumHouses(int numHouses) {
        this.numHouses = numHouses;
    }

    public Colors getColor() {
        return color;
    }

    public void setColor(Colors color) {
        this.color = color;
    }

    public boolean hasHotel() {
        return hotel;
    }

    public void setHotel(boolean hasHotel) {
        this.hotel = hasHotel;
    }

    public int getHouseCost() {
        return houseCost;
    }

    public void setHouseCost(int houseCost) {
        this.houseCost = houseCost;
    }

    public int getHotelCost() {
        return hotelCost;
    }

    public void setHotelCost(int hotelCost) {
        this.hotelCost = hotelCost;
    }

    public boolean placeHouse(){
        if(getNumberOfBuildings() < 4){
            numHouses++;
            return true;
        }
        return false;
    }

    public boolean placeHotel(){
        if(getNumberOfBuildings() == 4){
            numHouses = 0;
            hotel = true;
            return true;
        }
        return false;
    }

    public boolean removeHouse(){
        if(numHouses > 0){
            numHouses--;
            return true;
        }
        return false;
    }

    public boolean removeHotel(){
        if(hotel){
            hotel = false;
            numHouses = 4;
            return true;
        }
        return false;
    }

    public int getNumberOfBuildings() {
        if(hotel) return 5;
        return numHouses;
    }

    @Override
    public int calculateRent() {
        int buildings = getNumberOfBuildings();
        int rent = 0;
        switch(buildings) {
            case 0:
                rent = getRent();
                break;
            case 1:
                rent = rent1House;
                break;
            case 2:
                rent = rent2House;
                break;
            case 3:
                rent = rent3House;
                break;
            case 4:
                rent = rent4House;
                break;
            case 5:
                rent = rentHotel;
                break;
            default:
                System.out.println("Unexpected number of buildings on street: " + buildings + ".\n");

        }
        return rent;
    }

    @Override
    public void generateDescription() {
        String description = "Street: " + getName()
                + "\nPrice: " + getCost()
                + "\nRent: " + getRent()
                + "\nMortgage value: " + getMortgageValue();
        setDescription(description);

    }

    @Override
    public boolean mortgage() {
        if(getNumberOfBuildings() == 0 && !isMortgaged()){
            setIsMortgaged(true);
            return true;
        }
        return false;
    }

}
