import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ButtonGroup;
import javax.swing.SwingUtilities;
import javax.swing.KeyStroke;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.util.function.Predicate;
import java.util.Collections;
import java.util.stream.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.IOException;
import org.xml.sax.SAXException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;

public class LevelEdit extends JFrame {
    //Global debug boolean
    public static boolean debug = false;

    //Grid dimension, repetitions, and current grid position
    private int gs = 100;
    private static int ng = 5;
    public int gx, gy;

    //Keep track of the number of players
    private int numPlayers = 0;
    private int numNPCs = 0;

    //Room editor pane and boolean
    private RoomPopUp pop;

    //Keeps track of whether room editor is open
    private boolean noPopUp = true;

    //Swing components
    private HousePanel panel;
    private JMenuBar menuBar;
    private JMenu menu, submenu;
    private ButtonGroup group = new ButtonGroup();
    private JMenuItem loadMenu, saveMenu, quitMenu;
    private JFileChooser fileChooser;
    private JRadioButtonMenuItem[] buttons = new JRadioButtonMenuItem[7];

    //The room array
    public Room[][] rooms = new Room[7][7];
    
    public LevelEdit(){
	this.setTitle("Level Editor");
	this.setLocationRelativeTo(null);
	this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	initComponents();
	this.setSize(gs*ng,gs*ng);
	this.pack();
	this.setResizable(false);
	this.setVisible(true);
    }

    public class HousePanel extends JPanel {
	@Override
	protected void paintComponent(Graphics g){
	    super.paintComponent(g);
	    for(int i=0; i <= ng; i++){
		g.drawLine(i*gs,0,i*gs,gs*ng);
		g.drawLine(0,i*gs,gs*ng,i*gs);
	    }
	    
	    for(int i=0; i < ng; i++){
		for(int j=0; j < ng; j++){
		    if(rooms[i][j] != null){
			g.drawString(rooms[i][j].getRoomName(),
				     i*gs + 10,j*gs + 50);
		    }
		}
	    }
	}

	@Override
	public Dimension getPreferredSize(){
	    return (new Dimension(gs*ng,gs*ng));
	}
    }

    public void initComponents(){
	panel = new HousePanel();
	menuBar = new JMenuBar();
	menu = new JMenu("File");
	loadMenu = new JMenuItem("Load");
	saveMenu = new JMenuItem("Save");
	quitMenu = new JMenuItem("Quit");
	submenu = new JMenu("Resize");
	
	fileChooser = new JFileChooser();
	fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
	fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filt =
	    new FileNameExtensionFilter("XML Files (.xml)","xml");
	fileChooser.addChoosableFileFilter(filt);

        loadMenu.addActionListener(e -> loadFile());
	saveMenu.addActionListener(e -> saveFile());
	quitMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,ActionEvent.CTRL_MASK));
	quitMenu.addActionListener(e -> confirmClose());
	
	panel.addMouseListener(new MouseAdapter() {
	    @Override
            public void mouseClicked(MouseEvent e) {
		if (noPopUp){
		    findGrid(e.getX(),e.getY());
		    popUp();
		}
            }
        });

	for (int i=2; i < 8; i++){
	    buttons[i-2] = new JRadioButtonMenuItem(i + "x" + i);
	    makeButton(buttons[i-2],i);
	}

	menu.add(loadMenu);
	menu.add(saveMenu);
	menu.add(quitMenu);
	menu.add(submenu);
	menuBar.add(menu);
	panel.add(menuBar);
	this.add(panel);
    }

    //Remind users to save before closing
    private void confirmClose(){
	JFrame f = new JFrame();
	int n = JOptionPane.showConfirmDialog(
					      f,
					      "Are you sure you want to quit?\n"
					      + "Any unsaved changes"
					      + " will be lost.",
					      "Confirm Quit",
					      JOptionPane.YES_NO_OPTION);
	if (n == JOptionPane.YES_OPTION)
	    System.exit(0);
	if (n == JOptionPane.NO_OPTION)
	    f.dispose();
    }

    //Sets which grid square the editor is working in
    private void findGrid(int x, int y) {
	gx = (int) Math.floor(x / gs);
	gy = (int) Math.floor(y / gs);

	if (debug){
	System.out.print("Actual: (" + x + "," + y + ")");
	System.out.println("  Grid square: (" + gx + "," + gy + ")");
	}
    }

    //Returns the Room the editor is working in
    public Room getCurrentRoom(){
	return rooms[gx][gy];
    }

    //Read in XML data from a file
    private void loadFile(){
	int returnVal = fileChooser.showOpenDialog(this);

	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    File file = fileChooser.getSelectedFile();
	    SAXParserFactory spf = SAXParserFactory.newInstance();
	    try {
		InputStream xmlInput  = new FileInputStream(file);
		SAXParser saxParser = spf.newSAXParser();
		RoomXMLParser rxp = new RoomXMLParser();
		saxParser.parse(xmlInput, rxp);

		rooms = rxp.getRooms();
		numPlayers = rxp.getPlayers();
		numNPCs = rxp.getNPCs();
		revalidate();
	    }
	    catch(SAXException|ParserConfigurationException|IOException e){
		e.printStackTrace();
	    }
	}
    }

    //Used to construct buttons for the resize submenu
    private void makeButton(JRadioButtonMenuItem r, int i){
	r.setAccelerator(KeyStroke.getKeyStroke(
			i+48, ActionEvent.ALT_MASK));
	r.addActionListener(e -> {
		ng = i;
		this.revalidate();
	    });

	group.add(r);
	submenu.add(r);
    }

    //Make a new room in the current grid square
    public void makeRoom(String name, String desc){
	Room buffer = new Room(name,desc,gx,gy);

	//Add adjacencies
	if(gy > 0 && rooms[gx][gy - 1] != null){
	    buffer.addCompass(0, rooms[gx][gy - 1].getRoomName());
	    rooms[gx][gy - 1].addCompass(1, name);
	}
	if(gy < 6 && rooms[gx][gy + 1] != null){
	    buffer.addCompass(1, rooms[gx][gy + 1].getRoomName());
	    rooms[gx][gy + 1].addCompass(0, name);
	}
	if(gx < 6 && rooms[gx + 1][gy] != null){
	    buffer.addCompass(2, rooms[gx + 1][gy].getRoomName());
	    rooms[gx + 1][gy].addCompass(3, name);
	}
	if(gx > 0 && rooms[gx - 1][gy] != null){
	    buffer.addCompass(3, rooms[gx - 1][gy].getRoomName());
	    rooms[gx - 1][gy].addCompass(2, name);
	}
	
	rooms[gx][gy] = buffer;	
	revalidate();
    }

    //Instantiate a new Room editor window
    private void popUp() {
	pop = new RoomPopUp(this);
	noPopUp = false;
    }

    @Override
    public void revalidate() {
	super.revalidate();
	this.repaint();
	this.pack();
    }

    //Try to save a file, if it will be a valid game
    private void saveFile(){
	if (numPlayers == 1 && 0 < numNPCs && numNPCs < 6){
	    int returnVal = fileChooser.showSaveDialog(this);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
		try {
		    FileWriter writer =
			new FileWriter(fileChooser.getSelectedFile());
		    writer.write(this.toXML());
		    writer.close();
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	    }
	} else {
	    //If it wouldn't make a valid game, warn the user
	    JFrame f = new JFrame();
	    JOptionPane.showMessageDialog(f,
					  "You must have exactly one playable\n"
					  + "character and 1-5 NPCs.\n" +
					  "Current PCs= " + numPlayers +
					  ", NPCs = " + numNPCs,
					  "Improper Game Configuration",
					  JOptionPane.WARNING_MESSAGE);	    
	}
    }

    //Used to increment and decrement the number of players
    protected void setCounts(int doWhat){
	switch(doWhat){
	case 0: numPlayers++;
	    break;
	case 1: numPlayers--;
	    break;
	case 2: numNPCs++;
	    break;
	case 3: numNPCs--;
	    break;
	default: break;
	}
	
	if (debug) System.out.println("# of Players= " + numPlayers
				      + "# of NPCs = " + numNPCs);
    }
    
    public void setNoPopUp(){
	noPopUp = true;
    }

    //Return the current room array as a string of XML data
    private String toXML(){
	String buffer = "<xml version=\"1.0\" encoding=\"UTF-8\">\n";
	for(Room[] r : rooms)
	    for(Room s : r)
		if(s != null)
		    buffer += s.toXML();
	buffer += "</xml>";
	return buffer;
    }
	
    public static void main(String[] args){
	SwingUtilities.invokeLater(new Runnable(){
		@Override
		public void run(){
		    new LevelEdit();
		}
	    });
    }  
}
