package venture.cs414.android.monopoly2.backend;

import java.util.ArrayList;
import java.util.List;

public abstract class Owner {
	private List<Property> propertiesOwned;

    public Owner(){
        propertiesOwned = new ArrayList<Property>();
    }

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
        propertiesOwned.remove(property);
    }
}
