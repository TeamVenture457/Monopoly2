package venture.cs414.android.monopoly2.backend;

import java.util.List;

public abstract class Owner {
	private List<Property> propertiesOwned;

    public List<Property> getPropertiesOwned() {
        return propertiesOwned;
    }

    public void setPropertiesOwned(List<Property> propertiesOwned) {
        this.propertiesOwned = propertiesOwned;
    }

    public void addToPropertiesOwned(Property property){
        propertiesOwned.add(property);
    }

    public void removeFromPropertiesOwned(Property property){

    }
}