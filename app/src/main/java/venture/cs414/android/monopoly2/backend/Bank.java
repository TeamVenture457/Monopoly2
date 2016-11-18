package venture.cs414.android.monopoly2.backend;

/**
 * Created by Ben on 11/11/2016.
 */
public class Bank extends Owner{
    private int houseInventory;
    private int hotelInventory;

    private static Bank instance = new Bank();

    private Bank(){
        houseInventory = 32;
        hotelInventory = 12;
    }

    public static Bank getInstance(){
        return instance;
    }

    public int getHouseInventory() {
        return houseInventory;
    }

    public boolean hasHouses(){
        return houseInventory > 0;
    }

    public void addHouse(){
        houseInventory++;
    }

    public void removeHouse(){
        houseInventory--;
    }

    public int getHotelInventory() {
        return hotelInventory;
    }

    public boolean hasHotels(){
        return hotelInventory > 0;
    }

    public void addHotel(){
        hotelInventory++;
    }

    public void removeHotel(){
        hotelInventory--;
    }

    @Override
    public boolean equals(Object other){
        return (other instanceof Bank);
    }

}
