class GameCharacter{
    public CharType type;
    public String name, desc;
    
    public enum CharType{
	player, adult, child
    }

    public GameCharacter(String name, String desc, CharType T){
	this.name = name;
	this.desc = desc;
	this.type = T;
    }

    public CharType getType(){
	return type;
    }

    public String toXML(){
	String temp = "  <" + type
	    + " name=\"" + name + "\" "
	    + " description=\"" + desc + "\""
	    + "/>\n";
	
	if(LevelEdit.debug) System.out.print(temp);

	return temp;
    }

    @Override
    public String toString(){
	return name + ", a/an " + type + ": " + desc; 
    }
}
