package venture.cs414.android.monopoly2.backend;

import java.util.List;

/**
 * Created by James on 11/12/2016.
 */
public class Card {
    private String description;
    private List<String> actionDetails;
    private CardType cardType;

    public Card(List<String> actionDetails, String description, CardType cardType) {
        this.actionDetails = actionDetails;
        this.description = description;
        this.cardType = cardType;
    }

    public List<String> getActionDetails() {
        return actionDetails;
    }

    public CardType getCardType() {
        return cardType;
    }

    public String getCardDescription(){
        String cardDescription = "Type: " + cardType.toString()
                + "\n\tDescription: " + description
                + "\n\tAction Details: ";
        for(String action : actionDetails){
            cardDescription += action + " ";
        }
        cardDescription += "\n";
        return cardDescription;
    }
}
