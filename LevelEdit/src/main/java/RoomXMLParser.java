import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

class RoomXMLParser extends DefaultHandler{
    private Room[][] rooms = new Room[7][7];
    private Room room;
    private int numPlayers;
    private int numNPCs;
    
    @Override
    public void startDocument() throws SAXException{
	if (LevelEdit.debug) System.out.println("BEGIN READING DOCUMENT");
	
	numPlayers = 0;
	numNPCs = 0;
    }

    @Override
    public void startElement(String namespaceURI,
                             String localName,
                             String qName, 
                             Attributes atts) throws SAXException {
	switch(qName){
	case "room": int x = Integer.parseInt(atts.getValue("xcoord"));
	    int y = Integer.parseInt(atts.getValue("ycoord"));
	    room = new Room(atts.getValue("name"),
			    atts.getValue("description"),
			    x, y);
	    rooms[x][y] = room;
	    break;
	    
	case "item": boolean[] actions = {false,false,false};
	    List<String> act =
		Arrays.asList(atts.getValue("actions").split(","));
	    if (act.contains("shake"))
		actions[0] = true;		       
	    if (act.contains("throw"))
		actions[1] = true;
	    if (act.contains("possess"))
		actions[2] = true;
	    room.addItem(new Item(atts.getValue("name"),
				  atts.getValue("description"),
				  actions));
	    break;
	    
	case "player":
	    room.addGameCharacter(new GameCharacter(atts.getValue("name"),
						    atts.getValue("description"),
						    GameCharacter.CharType.player));
	    numPlayers++;
	    break;
	    
	case "adult":
	    room.addGameCharacter(new GameCharacter(atts.getValue("name"),
						    atts.getValue("description"),
						    GameCharacter.CharType.adult));
	    numNPCs++;
	    break;
	    
	case "child":
	    room.addGameCharacter(new GameCharacter(atts.getValue("name"),
						    atts.getValue("description"),
						    GameCharacter.CharType.child));
	    numNPCs++;
	    break;

	default: break;
	}	 
    }

    @Override
    public void endElement(String namespaceURI,
                           String localName,
                           String qName) throws SAXException {
	if(qName.equals("room"))
	    room = null;
    }

    @Override
    public void endDocument() throws SAXException{
	if (LevelEdit.debug) System.out.println("END OF DOCUMENT");
    }

    public Room[][] getRooms(){
	return rooms;
    }

    public int getPlayers(){
	return numPlayers;
    }

    public int getNPCs(){
	return numNPCs;
    }
}
