class Item{
    public String name, desc;
    public boolean[] actions = new boolean[3];

    public Item(String name, String desc, boolean[] actions){
	this.name = name;
	this.desc = desc;
	this.actions = actions;
    }

    public String toXML(){
	String temp = "  <item name=\"" + name + "\""
	    + " description=\"" + desc + "\""
	    + " actions=\"";
	temp += getActions();
	temp += "\"/>\n";
	
	if(LevelEdit.debug) System.out.print(temp);

	return temp;
    }

    @Override
    public String toString(){
	return name + ": (Actions= \""
	    + getActions() + "\") " + desc;
    }

    //Returns a comma separated String consisting of associated actions
    private String getActions(){
	String temp = "";
	if (actions[0]) temp += "shake";
	if (actions[0] && actions[1]) temp += ",";
	if (actions[1]) temp += "throw";
	if (actions[1] && actions[2]) temp += ",";
	else if(actions[0] && actions[2]) temp += ",";
	if (actions[2]) temp += "possess";

	return temp;
    }
}
