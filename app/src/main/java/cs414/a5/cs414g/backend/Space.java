package cs414.a5.cs414g.backend;

/**
 * Created by James on 11/11/2016.
 */
public class Space {
    private String name;
    private SpaceType spaceType;
    private int position;
    private Property deed;

    public Space(SpaceType spaceType, String name, int position, Property deed) {
        this.spaceType = spaceType;
        this.name = name;
        this.position = position;
        this.deed = deed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SpaceType getSpaceType() {
        return spaceType;
    }

    public void setSpaceType(SpaceType spaceType) {
        this.spaceType = spaceType;
    }

    public Property getDeed() {
        return deed;
    }

    public void setDeed(Property deed) {
        this.deed = deed;
    }

    public void setPosition(int position) {

        this.position = position;
    }

    public int getPosition() {

        return position;
    }

    public String getSpaceDescription(){
        String spaceDescription = "Name: " + name
                + "\nType: " + spaceType.toString()
                + "\nPosition: " + position + "\nDeed: ";
        if(deed instanceof Property) {
            spaceDescription += deed.getDescription();
        }
        else {
            spaceDescription += "null";
        }
        spaceDescription += "\n";
        return spaceDescription;
    }

}
