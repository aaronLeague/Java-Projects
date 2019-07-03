import java.io.File;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Locale;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Collections;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class TreeIndex{

    private Scanner scan;
    private BST<String,String> words = new BST<>();

    //Current directory and last files read from directory
    private File direct;
    private File[] fileList;

    public TreeIndex(){
	scan = new Scanner(System.in);
	System.out.println("****************************\n");
	System.out.println("||  WELCOME TO WORDIFIER  ||");
	this.commandProcess();
    }

    //Runs a loop which takes in user commands
    private void commandProcess(){
	boolean on = true;
	while (on){
	    System.out.println("\n****************************\n\n"
			       + "SELECT OPTION: \nn (go to new directory),"
			       + "\nr (read files),\ns (search for word),"
			       + "\ne (exit),\np (print file tree"
			       + " [not recommended])\n");
	    String cmd = scan.nextLine();
	    switch (cmd){
	    case "n": getDirect();
		break;
	    case "r": readFiles();
		break;
	    case "s": wordSearch();
		break;
	    case "p": printTree();
		break;
	    case "e": System.out.println("\n EXITING\n");
		on = false;
		break;
	    default: System.out.println("\n UNRECOGNIZED COMMAND");
	        break;
	    }
	}
    }    

    //Tries to read in a file and return a String
    private String fileToString(File f){
	try{
	    byte[] encoded = Files.readAllBytes(Paths.get(f.getAbsolutePath()));
	    return new String(encoded, "UTF-8");
	} catch (IOException e){
	    e.printStackTrace();
	    return null;
	}
    }

    //Accepts user input for what directory to pull files from
    private void getDirect(){
	System.out.println("\nEnter the directory you want to go to.");
	System.out.print("/");
	direct = new File("/" + scan.nextLine());
	System.out.println("\nNow working in directory " + direct);
        showFiles();

	//Tell if user mis-enters the directory, and recursively call getDirect()
	if (!direct.isDirectory()){
	    System.out.println("\n NOT A VALID DIRECTORY\n");
	    getDirect();
	}
    }

    private void printTree(){
	System.out.println(words);
	
	//words.printInOrder();
    }

    //Take the file names from the fileList array, and process (wordify) each one
    private void readFiles(){
	if (fileList != null)
	    for (File f : fileList)
		if (f.isFile())
		    wordify(fileToString(f), f);
	System.out.println("\n FILES HAVE BEEN READ FROM " + direct);
    }

    //Searches tree for value or wild card
    private HashSet<String> searchSingle(String s){
	if (s.endsWith("*")){
	    s = s.substring(0, s.length() - 1);
	    return words.getInRange((s + "!"),(s + "~"));
	}
	return (words.contains(s) ? words.get(s) : null);
    }

    //Lists the files in the current directory
    private void showFiles(){
	System.out.println("\nLIST OF FILES:");
	fileList = direct.listFiles();
	if (fileList != null)
	    for (File f : fileList)
		if (f.isFile())
		    System.out.println(f.getName());
    }

    //Splits file String into words, adds <word,file> pair to tree
    private void wordify(String str, File file){
	/*Delimits on the following characters:
	[]{}()~`!@#$%^&*=+-;:'"\|/<>,.
	and also new line. Good grief!*/
	String[] fileArr = str.split("[\\[\\]\\{\\}\\(\\)\\-,\\|=\\+/_"
				     +"\\\\ ;:~`!@#$%^&<>?\'\"\\*.\n]+");

	//Enters each <word,file> pair to the tree
	for (String s : fileArr){
	    words.insert(s.toUpperCase(Locale.ENGLISH), file.getName());
	}
    }

    //Takes user inputs and searches the tree for them
    private boolean wordSearch(){
	//If the tree is empty, don't waste theuser's time
	if (words.isEmpty()){
	    System.out.println("\n NO DATA HAS BEEN ENTERED YET");
	    return false;
	}

	//Takes user input and separates it into words
        System.out.println("\nEnter the words you want to search for, "
			   + "separated by a space.\n"
			   + "(Use * for wild card)\n");
	String[] str = scan.nextLine().split("\\s+");

	//Gets search result(s) and combines them into one HashSet
	//I thought a stream might be neat and clean,
	//but now I'm not so sure.
	HashSet<String> h = Stream.of(str)
	    //.peek(System.out::println)
	    .map(s -> s.toUpperCase())
	    .map(s -> this.searchSingle(s))
	    .reduce(null, (s1,s2) -> {
		    if (s1 == null) return s2;
		    if (s2 != null) s1.addAll(s2);
		    return s1;});

	//Prints ordered file list, or "none found" notification
	if (h == null) System.out.println("\n WORD(S) NOT FOUND");
	else{
	    ArrayList arr = new ArrayList(h);
	    Collections.sort(arr);
	    System.out.println("\n" + arr);
	}
	
	return true;
    }
    
    public static void main(String[] args){
	TreeIndex ind = new TreeIndex();
    }
}
