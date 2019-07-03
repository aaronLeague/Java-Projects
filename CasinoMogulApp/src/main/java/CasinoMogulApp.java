import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class CasinoMogulApp extends JFrame{
    private HorseRace race;
    private HorsePanel panel;
    private JButton buy, start, reset;

    private Timer timer;
    private static final int SLEEP = 50;

    private boolean hasWinner = false;
    private int moneyLeft = 100;
    private static final int POT = 26;

    private class HorsePanel extends JPanel{
	@Override
	protected void paintComponent(Graphics g){
	    super.paintComponent(g);
	    g.drawString(("Money Left: $" + moneyLeft
			  +"    Pot: $" + POT
			  +"    WIN CHANCE= " + winChance() + "%"),100,230);
	    g.drawString("FINISH",440,35);
	    g.drawRect(65,40,395,170);
	    race.paintTrack(g);
	}

	@Override
	public Dimension getPreferredSize(){
	    return (new Dimension(500, 250));
	}
    }

    public CasinoMogulApp(){
	this.setTitle("<<< CASINO MOGUL >>>");
	this.setLocationRelativeTo(null);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	initComponents();
	this.pack();
	this.setResizable(false);
	this.setVisible(true);
    }

    public void initComponents(){
	panel = new HorsePanel();
        race = new HorseRace();

	buy = new JButton("Buy Line: $2");
	buy.addActionListener(e -> buyPlayer());
	buy.setLocation(0,180);

	start = new JButton("START! ($2)");
	start.addActionListener(e -> {
		if(hasNoMoney()) showLoser();
		else{
		    moneyLeft -= 2;
		    timer.start();
		}
	    });

	timer = new Timer(SLEEP, new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e){
		    if(race.run() != null){
			timer.stop();
			showWinner(race.run());
		    }
		    repaint();
		}
	    });

	reset = new JButton("Reset");
	reset.addActionListener(e -> resetTrack());
	
	panel.add(buy);
	panel.add(start);
	panel.add(reset);
	this.add(panel);
    }

    private void buyPlayer(){
	if(hasNoMoney()) showLoser();
	else if(race.addPlayer()){
	    moneyLeft -= 2;
	    repaint();
	}
    }

    private boolean hasNoMoney(){return (moneyLeft < 2);}

    private void resetTrack(){
	timer.stop();
	race.refresh();
	repaint();
    }

    private void showLoser(){
	JFrame f = new JFrame();
	JOptionPane.showMessageDialog(f, "You do not have the\n"
				      +	"money to continue.");
    }

    private void showWinner(Horse winner){

	if(winner.getClass().toString().equals("class Player"))
	    moneyLeft += POT;
	
	JFrame f = new JFrame();
	int n = JOptionPane.showConfirmDialog(f, "THE WINNER IS "
					      + winner.getName() + "!!!\n"
					      + "Pot: $" + POT,
					      "WE HAVE A WINNER!",
					      JOptionPane.DEFAULT_OPTION,
					      JOptionPane.INFORMATION_MESSAGE);
	if(n == JOptionPane.OK_OPTION || n == JOptionPane.CLOSED_OPTION)
	    resetTrack();
    }
    
    private int winChance(){
	return (int) (((double) race.getNumPlayers() / 7) * 52);
    }
    
    public static void main(String[] args){
	SwingUtilities.invokeLater(new Runnable(){
		@Override
		public void run(){
		    new CasinoMogulApp();
		}
	    });
    }
}
