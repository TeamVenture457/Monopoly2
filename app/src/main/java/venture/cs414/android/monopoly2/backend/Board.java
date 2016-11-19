package venture.cs414.android.monopoly2.backend;

import android.content.Context;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by James on 11/11/2016.
 */
public class Board {
    private Queue<Card> chanceCards;
    private Queue<Card> communityChestCards;
    private Space[] boardSpaces;
    private Context context;
    final int NUMBEROFSPACES = 41;

    public Board(Context context){
        boardSpaces = new Space[NUMBEROFSPACES];
        chanceCards = new LinkedList<>();
        communityChestCards = new LinkedList<>();
        this.context = context;
        setupBoard();
    }

    public void setupBoard() {
        fillBoardSpaces();
        fillCards(CardType.CHANCE);
        fillCards(CardType.COMMUNITYCHEST);
    }

    public Card drawChanceCard(){
        Card card = chanceCards.remove();
        if(!card.getActionDetails().get(0).equals("getOutOfJail")){
            chanceCards.add(card);
        }
        return card;
    }

    public Card drawCommunityChestCard(){
        Card card = communityChestCards.remove();
        if(!card.getActionDetails().get(0).equals("getOutOfJail")){
            communityChestCards.add(card);
        }
        return card;
    }

    public void returnCardToDeck(Card card){
        if(card.getCardType().equals(CardType.CHANCE)){
            chanceCards.add(card);
        }
        else {
            communityChestCards.add(card);
        }
    }

    public Space[] getBoardSpaces() {
        return boardSpaces;
    }

    public Space getBoardSpace(int position){
        return boardSpaces[position];
    }
    
    public List<Street> getStreetsOfColor(Colors color){
        List<Street> colorStreets = new ArrayList<Street>();
        for(Space space : boardSpaces){
            if(space.getDeed() instanceof Street){
                Street deed = (Street) space.getDeed();
                if(deed.getColor().equals(color)){
                    colorStreets.add(deed);
                }
            }
        }
        return colorStreets;
    }

    public void fillCards(CardType cardType){
        String xmlFilename;
        Queue<Card> cardQueue;
        ArrayList<Card> cards = new ArrayList<Card>();
        if(cardType.equals(CardType.CHANCE)){
            cardQueue = chanceCards;
            xmlFilename = "chanceCards.xml";
        }
        else{
            cardQueue = communityChestCards;
            xmlFilename = "communityChestCards.xml";
        }
        Document chanceCardsDoc = getXMLDoc(xmlFilename);
        chanceCardsDoc.normalize();
        NodeList nodeList = chanceCardsDoc.getElementsByTagName("card");
        for(int index = 0; index < nodeList.getLength(); index++){
            Node node = nodeList.item(index);
            CardType thisCardType = cardType;
            String cardDescription = "";
            List<String> actionList = new ArrayList<String>();

            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                cardDescription = element.getAttribute("description");
                for (int i = 0; i < element.getElementsByTagName("action").getLength(); i++) {
                    actionList.add(element.getElementsByTagName("action").item(i).getTextContent());
                }
            }
            Card thisCard = new Card(actionList, cardDescription, thisCardType);
            //cardQueue.add(thisCard);
            cards.add(thisCard);
        }
        Collections.shuffle(cards);
        cardQueue.addAll(cards);
    }

    public void fillBoardSpaces(){
        String xmlFilename = "monopolySpaces.xml";
        Document monopolySpacesDoc = getXMLDoc(xmlFilename);
        monopolySpacesDoc.normalize();
        NodeList nodeList = monopolySpacesDoc.getElementsByTagName("space");
        for(int index = 0; index < nodeList.getLength(); index++){
            Node node = nodeList.item(index);
            Space thisSpace = null;

            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String name = element.getAttribute("name");
                String type = element.getElementsByTagName("type").item(0).getTextContent();
                SpaceType spaceType = getSpaceType(name, type);
                int position = Integer.parseInt(element.getElementsByTagName("position").item(0).getTextContent());
                Property deed = null;
                if(spaceType.equals(SpaceType.PROPERTY)){
                    int cost = Integer.parseInt(element.getElementsByTagName("price").item(0).getTextContent());
                    int rent = Integer.parseInt(element.getElementsByTagName("rent").item(0).getTextContent());
                    int mortgageValue = Integer.parseInt(element.getElementsByTagName("mortgagevalue").item(0).getTextContent());
                    switch(type) {
                        case "street":
                            Street thisStreet = new Street(name, cost, rent, mortgageValue);
                            deed = finishStreet(thisStreet, element);
                            break;
                        case "railroad":
                            deed = new Railroad(name, cost, rent, mortgageValue);
                            break;
                        case "utility":
                            deed = new Utility(name, cost, rent, mortgageValue);
                            break;
                        default:
                            System.out.println("Unexpected property type" + type + " given by space " + name + ".\n");
                            break;
                    }
                }
                thisSpace = new Space(spaceType, name, position, deed);
            }
            if(thisSpace instanceof Space){
                boardSpaces[thisSpace.getPosition()] = thisSpace;
            }
        }

    }

    private Street finishStreet(Street street, Element element) {
        int rent1House = Integer.parseInt(element.getElementsByTagName("rent1house").item(0).getTextContent());
        int rent2House = Integer.parseInt(element.getElementsByTagName("rent2houses").item(0).getTextContent());
        int rent3House = Integer.parseInt(element.getElementsByTagName("rent3houses").item(0).getTextContent());
        int rent4House = Integer.parseInt(element.getElementsByTagName("rent4houses").item(0).getTextContent());
        int rentHotel = Integer.parseInt(element.getElementsByTagName("renthotel").item(0).getTextContent());
        int houseCost = Integer.parseInt(element.getElementsByTagName("housecost").item(0).getTextContent());
        int hotelCost = Integer.parseInt(element.getElementsByTagName("hotelcost").item(0).getTextContent());
        Colors thisColor = getColor(element.getElementsByTagName("color").item(0).getTextContent());
        street.setColor(thisColor);
        street.setStreetRents(rent1House, rent2House, rent3House, rent4House, rentHotel);
        street.setHouseCost(houseCost);
        street.setHotelCost(hotelCost);
        return street;
    }

    private Colors getColor(String color){
        Colors thisColor = null;
        switch(color){
            case "red":
                thisColor = Colors.RED;
                break;
            case "blue":
                thisColor = Colors.BLUE;
                break;
            case "brown":
                thisColor = Colors.BROWN;
                break;
            case "sky":
                thisColor = Colors.SKY;
                break;
            case "purple":
                thisColor = Colors.PURPLE;
                break;
            case "orange":
                thisColor = Colors.ORANGE;
                break;
            case "green":
                thisColor = Colors.GREEN;
                break;
            case "yellow":
                thisColor = Colors.YELLOW;
                break;
            default:
                System.out.println("Unexpected street color " + color + ".\n");
        }
        return thisColor;
    }

    private Document getXMLDoc(String xmlFilename) {
        InputStream xmlFileIS = null;
        try{
            xmlFileIS = context.getAssets().open(xmlFilename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            return dBuilder.parse(xmlFileIS);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    private SpaceType getSpaceType(String spaceName, String spaceType) {
        SpaceType thisSpaceType = null;
        switch (spaceType) {
            case "street":
            case "railroad":
            case "utility":
                thisSpaceType = SpaceType.PROPERTY;
                break;
            case "other":
                switch (spaceName) {
                    case "Go":
                        thisSpaceType = SpaceType.GO;
                        break;
                    case "Community Chest":
                        thisSpaceType = SpaceType.COMMUNITYCHEST;
                        break;
                    case "Income Tax":
                        thisSpaceType = SpaceType.INCOMETAX;
                        break;
                    case "Luxury Tax":
                        thisSpaceType = SpaceType.LUXURYTAX;
                        break;
                    case "Chance":
                        thisSpaceType = SpaceType.CHANCE;
                        break;
                    case "In Jail":
                        thisSpaceType = SpaceType.INJAIL;
                        break;
                    case "Just Visiting":
                        thisSpaceType = SpaceType.JUSTVISITING;
                        break;
                    case "Free Parking":
                        thisSpaceType = SpaceType.FREEPARKING;
                        break;
                    case "Go To Jail":
                        thisSpaceType = SpaceType.GOTOJAIL;
                        break;
                    default:
                        System.out.println("Unexpected name " + spaceName + " from space with type " + spaceType + ".");
                }
                break;
            default:
                System.out.println("Unexpected space type " + spaceType + " from space named " + spaceName + ".");
        }
        return thisSpaceType;
    }

    public void printBoard(){
        String boardSpacesString = "";
        for (Space currentSpace : boardSpaces){
            boardSpacesString += currentSpace.getSpaceDescription();
        }
        System.out.println(boardSpacesString);
    }

    public void printCards(){
        String chanceCardsString = "";
        for (Card currentCard : chanceCards){
            chanceCardsString += currentCard.getCardDescription();
        }
        String communityChestCardsString = "";
        for (Card currentCard : communityChestCards){
            communityChestCardsString += currentCard.getCardDescription();
        }
        System.out.println("Chance Cards:\n" + chanceCardsString);
        System.out.println("Community Chest Cards:\n" + communityChestCardsString);
    }

    public Property getPropertyByName(String propertyName) {
        Property property = null;
        for(Space space : boardSpaces){
            if(space.getName().equals(propertyName)){
                property = space.getDeed();
            }
        }
        return property;
    }

    public int getNextUtilityPosition(int location) {
        int position = location;
        boolean found = false;
        while(!found){
            position = (position + 1) % (NUMBEROFSPACES-1);
            Space space = boardSpaces[position];
            Property deed = space.getDeed();
            if(deed instanceof Utility){
                found = true;
            }
        }
        return position;
    }

    public int getNextRailroadPosition(int location) {
        int position = location;
        boolean found = false;
        while(!found){
            position = (position + 1) % (NUMBEROFSPACES-1);
            Space space = boardSpaces[position];
            Property deed = space.getDeed();
            if(deed instanceof Railroad){
                found = true;
            }
        }
        return position;
    }
}
