import java.awt.Graphics;

public class HorseRace{
    private Horse[] startGate = new Horse[8];
    private int numPlayers;

    public HorseRace(){
	refresh();
    }

    public void refresh(){
	startGate[0] = new House();
	startGate[1] = new Player(1);
	for(int i = 2; i < 8; i++)
	    startGate[i] = new NPC(i - 1);
	numPlayers = 1;
    }

    public boolean addPlayer(){
	if(numPlayers < 7){
	    startGate[++numPlayers] = new Player(numPlayers);
	    return true;
	}
	return false;
    }

    public int getNumPlayers(){return numPlayers;}

    //Paints the horses and their names
    public void paintTrack(Graphics g){
	for(int i = 0; i < 8; i++){
	    g.drawString(startGate[i].getName(),5,(60 + 20*i));
	    startGate[i].paintHorse(g, (50 + 20*i));
	}
    }

    //Advances the horses and returns the winning horse, if there is one
    public Horse run(){
	for (Horse h : startGate)
	    if(h.step())
		return h;
	return null;
    }
}
