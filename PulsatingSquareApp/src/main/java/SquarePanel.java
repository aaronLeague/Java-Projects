import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

class SquarePanel extends JPanel{
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem add, rotate, pulse;
    
    private Timer timer;
    private static final int SLEEP = 500;
    
    private SquareList list;
    
    public SquarePanel() {
	list = new SquareList();
        
        this.setPreferredSize(new Dimension(400,400));

	menuBar = new JMenuBar();
	menu = new JMenu("Actions");
	
	add = new JMenuItem("Add Square");
	add.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,ActionEvent.CTRL_MASK));
	add.addActionListener(e -> addSquare());

	rotate = new JMenuItem("Rotate Colors");
	rotate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,ActionEvent.CTRL_MASK));
	rotate.addActionListener(e -> rotateColors());

	pulse = new JMenuItem("Pulse Colors");
	pulse.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,ActionEvent.CTRL_MASK));
	
	pulse.addActionListener(e -> timer.start());

	timer = new Timer(SLEEP, new ActionListener(){
		private int index = 0;
		
		@Override
		public void actionPerformed(ActionEvent e){
		    if (index < list.getCount()){
			rotateColors();
			index++;
		    } else{
			timer.stop();
			index = 0;
		    }
		}
	    });

	menu.add(add);
	menu.add(rotate);
	menu.add(pulse);
	menuBar.add(menu);
	
	this.add(menuBar);
    }

    @Override
    protected void paintComponent(Graphics g){
	super.paintComponent(g);
	list.paintSquares(g);
    }

    private void addSquare(){
	list.add();
	this.repaint();
    }

    private void rotateColors(){
	list.rotate();
	this.repaint();
    }
}
