import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class PulsatingSquareApp {
    //Global debug boolean
    private static final boolean debug = true;
    public static boolean isDebug(){ return debug;}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
		@Override
		public void run() {
		    createAndShowGUI(); 
		}
	    });
    }

    private static void createAndShowGUI() {
	if(debug)
	    System.out.println("Created GUI on EDT? "+
			       SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame("Pulsating Squares!");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	f.add(new SquarePanel());
	f.pack();
        f.setVisible(true);
    }
}
