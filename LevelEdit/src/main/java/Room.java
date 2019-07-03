import java.util.ArrayList;
import java.util.List;

class Room {
    //Room name and description
    private String name = "";
    private String desc = "";

    //String values for the names of surrounding rooms
    private String[] compass = new String[4];

    //Grid coordinates
    private int x, y;

    //Lists of characters and items associated with the room
    private List<Item> items = new ArrayList<>();
    private List<GameCharacter> characters = new ArrayList<>();
    
    public Room(String name, String desc, int x, int y){
	this.name = name;
	this.desc = desc;
	this.x = x;
	this.y = y;
    }

    //Add the name of the room to the
    //north, south, east, or west, respectively
    public void addCompass(int i, String s){
	compass[i] = s;
    }

    public void addGameCharacter(GameCharacter g){
	characters.add(g);
    }

    public void addItem(Item i){
	items.add(i);
    }

    public List<GameCharacter> getChars(){
	return characters;
    }

    public List<Item> getItems(){
	return items;
    }
    
    public String getRoomDesc(){
	return desc;
    }

    public String getRoomName(){
	return name;
    }

    public void removeChar(int i){
	characters.remove(i);
    }

    public void removeItem(int i){
	items.remove(i);
    }

    public String toXML(){
	String temp = "<room name=\"" + name + "\""
	    + " description=\"" + desc + "\""
	    + " xcoord=\"" + x + "\""
	    + " ycoord=\"" + y + "\"";
	if(compass[0] != null) temp += " north=\"" + compass[0] + "\"";
	if(compass[1] != null) temp += " south=\"" + compass[1] + "\"";
	if(compass[2] != null) temp += " east=\"" + compass[2] + "\"";
	if(compass[3] != null) temp += " west=\"" + compass[3] + "\"";
	temp += ">\n";

	for(Item i : items) temp += i.toXML();
	for(GameCharacter g : characters) temp += g.toXML();

	temp += "</room>\n";
	
	if(LevelEdit.debug) System.out.print(temp);

	return temp;
    }
}
