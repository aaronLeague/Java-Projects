import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JOptionPane;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.util.stream.Collectors;
import java.util.List;


class RoomPopUp extends JFrame {
    //The LevelEdit object to be read in
    private LevelEdit editor;

    //A gaggle of Swing components
    private JPanel panel;
    private JLabel nameLab, descLab, itemNameLab, itemDescLab,
	charNameLab, charDescLab, actionsLab, charTypeLab, itemsLab, charsLab;
    private JTextField nameTxt, descTxt, itemNameTxt, itemDescTxt,
	charNameTxt, charDescTxt;
    private JButton doBut, delBut, addI, delI, addP, delP, leave;
    private JCheckBox shake, toss, possess;
    private JComboBox charTypeBox, charsBox, itemsBox;
    
    public RoomPopUp(LevelEdit editor) {
	this.setTitle("Room Editor");
	this.editor = editor;
	this.setLocationRelativeTo(null);
	this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	initComponents();
	this.pack();
	this.setVisible(true);
    }

    public void initComponents() {
	panel = new JPanel();
	
	nameLab = new JLabel("Room Name:");
	nameTxt = new JTextField("",20);

	descLab = new JLabel("Room Description:");
	descTxt = new JTextField("",20);

	doBut = new JButton("Update Room");
	doBut.addActionListener(e -> {
		if(!nameTxt.getText().equals(""))
		    editor.makeRoom(nameTxt.getText(),descTxt.getText());
		else{
		    JFrame f = new JFrame();
		    JOptionPane.showMessageDialog(f,
						  "Your room must have a name!",
						  "ANONYMOUS DOMICILE ERROR",
						  JOptionPane.ERROR_MESSAGE);
		}
	    });

	itemNameLab = new JLabel("Item Name:");
	itemNameTxt = new JTextField("",20);

	itemDescLab = new JLabel("Item Description:");
	itemDescTxt = new JTextField("",20);

	actionsLab = new JLabel("Item Actions:");
	shake = new JCheckBox("Shake");
	toss = new JCheckBox("Throw");
	possess = new JCheckBox("Possess");

	addI = new JButton("Add Item");
	addI.addActionListener(e -> addItem());
	delI = new JButton("Remove Item");
	delI.addActionListener(e -> removeItem());

	charNameLab = new JLabel("Character Name:");
	charNameTxt = new JTextField("",20);

        charDescLab = new JLabel("Character Description:");
	charDescTxt = new JTextField("",20);

	addP = new JButton("Add Character");
	addP.addActionListener(e -> addCharacter());
	delP = new JButton("Remove Character");
	delP.addActionListener(e -> removeCharacter());

	charTypeLab = new JLabel("Character Type:");
	charTypeBox = new JComboBox(GameCharacter.CharType.values());

	itemsLab = new JLabel("Current Items:");
	itemsBox = new JComboBox();
	itemsBox.setPreferredSize(new Dimension(200,30));
	
	charsLab = new JLabel("Current Characters:");
	charsBox = new JComboBox();
	charsBox.setPreferredSize(new Dimension(200,30));

	leave = new JButton("Done Editing");
	leave.addActionListener(e -> dispose());	
	
	if (editor.getCurrentRoom() != null)
	    readRoom();

	layItOut();
	
	panel.add(nameLab);
	panel.add(nameTxt);
	panel.add(descLab);
	panel.add(descTxt);
	panel.add(doBut);
	panel.add(itemNameLab);
	panel.add(itemNameTxt);
	panel.add(itemDescLab);
	panel.add(itemDescTxt);
	panel.add(actionsLab);
	panel.add(shake);
	panel.add(toss);
	panel.add(possess);
	panel.add(addI);
	panel.add(charNameLab);
	panel.add(charNameTxt);
	panel.add(charDescLab);
	panel.add(charDescTxt);
	panel.add(charTypeLab);
	panel.add(itemsLab);
	panel.add(itemsBox);
	panel.add(delI);
	panel.add(charsLab);
	panel.add(charsBox);
	panel.add(delP);
	panel.add(leave);
	this.add(panel);

	this.addWindowListener(new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent e) {
		    dispose();
		}
	    });
    }

    //Creates a character from the specified parameters
    //in the selected room in the level editor
    private void addCharacter(){
	if(editor.getCurrentRoom() != null
	   && !charNameTxt.getText().equals("")){
	    GameCharacter.CharType t = GameCharacter.CharType.child;
	    
	    if(charTypeBox.getSelectedIndex() == 1)
		t = GameCharacter.CharType.adult;
	    if(charTypeBox.getSelectedIndex() == 0){
		t = GameCharacter.CharType.player;

		//If the new character is a player, increment
		//numPlayers in the level editor
		editor.setCounts(0);
	    } else {

		//If the new character is not a player,
		//increment numNPCs in the level editor
		editor.setCounts(2);
	    }
	    
	    GameCharacter temp = new GameCharacter(charNameTxt.getText(),
						   charDescTxt.getText(),
						   t);
	    editor.getCurrentRoom().addGameCharacter(temp);

	    //Empty text from dialog boxes
	    charNameTxt.setText("");
	    charDescTxt.setText("");
	    
	    revalidate();
	}
    }

    //Creates an itme from the specified parameters
    //in the selected room in the level editor
    private void addItem(){
	if(editor.getCurrentRoom() != null
	   && !itemNameTxt.getText().equals("")){
	    boolean[] act = {shake.isSelected(),
			     toss.isSelected(),
			     possess.isSelected()};
	    Item temp = new Item(itemNameTxt.getText(),
				 itemDescTxt.getText(),
				 act);
	    editor.getCurrentRoom().addItem(temp);

	    //Empty text from dialog boxes
	    itemNameTxt.setText("");
	    itemDescTxt.setText("");
	    
	    revalidate();
	}
    }

    //Tell the level editor if the room editor is closed
    @Override
    public void dispose(){
	editor.setNoPopUp();
	super.dispose();
    }

    //Populate room editor with data from selected room
    public void readRoom() {
	//Populate comboBox list with items in the room
	List<String> itemA = editor.getCurrentRoom().getItems().stream()
            .map(i -> i.toString())
            .collect(Collectors.toList());
	itemsBox.setModel(new DefaultComboBoxModel(itemA.toArray()));

	//Populate comboBox list with characters in the room
	List<String> charA = editor.getCurrentRoom().getChars().stream()
            .map(i -> i.toString())
            .collect(Collectors.toList());
	charsBox.setModel(new DefaultComboBoxModel(charA.toArray()));

	//Populate room name and description
	nameTxt.setText(editor.getCurrentRoom().getRoomName());
	descTxt.setText(editor.getCurrentRoom().getRoomDesc());
    }

    //Remove selected character from the room, if there is one
    private void removeCharacter(){
	if(editor.getCurrentRoom() != null
	   && charsBox.getItemCount() != 0){
	    //Decrement the appropriate character type count in editor
	    GameCharacter.CharType t = editor.getCurrentRoom().getChars()
		.get(charsBox.getSelectedIndex()).getType();		
	    if (t == GameCharacter.CharType.player)
		editor.setCounts(1);
	    else editor.setCounts(3);
	    
	    editor.getCurrentRoom().removeChar(charsBox.getSelectedIndex());
	    revalidate();
	}
    }

    //Remove selected item from the room, if there is one
    private void removeItem(){
	if(editor.getCurrentRoom() != null
	   && itemsBox.getItemCount() != 0){
	    editor.getCurrentRoom().removeItem(itemsBox.getSelectedIndex());
	    revalidate();
	}
    }

    //Read in data from selected room when revalidating
    @Override
    public void revalidate() {
	if (editor.getCurrentRoom() != null)
	    readRoom();
	super.revalidate();
	this.repaint();
    }

    //To hide the ugly code where it won't scare anyone
    private void layItOut() {
	GroupLayout layout = new GroupLayout(panel);
	panel.setLayout(layout);
	layout.setAutoCreateGaps(true);
	layout.setAutoCreateContainerGaps(true);

	layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
					  .addComponent(nameLab)
					  .addComponent(nameTxt)
					  .addComponent(descLab)
					  .addComponent(descTxt)
					  .addComponent(doBut))
				.addGroup(layout.createParallelGroup()
					  .addComponent(itemNameLab)
					  .addComponent(itemNameTxt)
					  .addComponent(itemDescLab)
					  .addComponent(itemDescTxt)
					  .addComponent(addI))
				.addGroup(layout.createParallelGroup()
					  .addComponent(actionsLab)
					  .addComponent(shake)
					  .addComponent(toss)
					  .addComponent(possess))
				.addGroup(layout.createParallelGroup()
					  .addComponent(charNameLab)
					  .addComponent(charNameTxt)
					  .addComponent(charDescLab)
					  .addComponent(charDescTxt)
					  .addComponent(addP))
				.addGroup(layout.createParallelGroup()
					  .addComponent(charTypeLab)
					  .addComponent(charTypeBox))
				.addGroup(layout.createParallelGroup()
					  .addComponent(itemsLab)
					  .addComponent(charsLab))
				.addGroup(layout.createParallelGroup()
					  .addComponent(itemsBox)
					  .addComponent(charsBox))
				.addGroup(layout.createParallelGroup()
					  .addComponent(delI)
					  .addComponent(delP))
				.addComponent(leave));
	
	layout.setHorizontalGroup(layout.createParallelGroup()
				  .addGroup(layout.createSequentialGroup()
					    .addGroup(layout.createParallelGroup()
						      .addComponent(nameLab)
						      .addComponent(itemNameLab)
						      .addComponent(charNameLab))
					    .addGroup(layout.createParallelGroup()
						      .addComponent(nameTxt)
						      .addComponent(itemNameTxt)
						      .addComponent(charNameTxt))
					    .addGroup(layout.createParallelGroup()
						      .addComponent(descLab)
						      .addComponent(itemDescLab)
						      .addComponent(actionsLab)
						      .addComponent(charDescLab)
						      .addComponent(charTypeLab))
					    .addGroup(layout.createParallelGroup()
						      .addComponent(descTxt)
						      .addComponent(itemDescTxt)
						      .addGroup(layout.createSequentialGroup()
								.addComponent(shake)
								.addComponent(toss)
								.addComponent(possess))
						      .addComponent(charDescTxt)
						      .addComponent(charTypeBox))
					    .addGroup(layout.createParallelGroup()
						      .addComponent(doBut)
						      .addComponent(addI)
						      .addComponent(addP)))
				  .addGroup(layout.createSequentialGroup()
					    .addGroup(layout.createParallelGroup()
						      .addComponent(itemsLab)
						      .addComponent(itemsBox)
						      .addComponent(delI))
					    .addGroup(layout.createParallelGroup()
						      .addComponent(charsLab)
						      .addComponent(charsBox)
						      .addComponent(delP)))
				  .addGroup(layout.createSequentialGroup()
					    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						      .addComponent(leave))));
    }
}
