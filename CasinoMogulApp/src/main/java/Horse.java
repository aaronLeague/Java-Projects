import java.awt.Graphics;
import java.awt.Color;
import java.util.Random;

//Horse parent class is abstract so it can't be instantiated
abstract class Horse{
    private double speed;
    private double loc;
    private String name;
    private Color color;
    private Random rand = new Random();

    public Horse(String name, Color color){
	this.name = name;
	this.color = color;
	this.speed = getNewSpeed();
    }

    public double getLocation(){return loc;}

    public String getName(){return name;}

    public abstract double getNewSpeed();

    public double getRand(){return rand.nextGaussian();}

    public double getSpeed(){return speed;}

    private boolean hasWon(){
	return loc > 400;
    }

    public void paintHorse(Graphics g, int i){
	g.setColor(color);
	g.fillOval((int) (loc + 60), i, 10, 10);
	g.setColor(Color.BLACK);
    }

    //Advances the horse, and tells whether it won
    public boolean step(){
	loc += getSpeed();
	return hasWon();
    }
}

class House extends Horse{
    
    public House(){
        super("House", Color.RED);
	
    }

    @Override
    public double getNewSpeed(){
	return (double) 0.999999;
    }
}

class Player extends Horse{

    public Player(int i){
	super("Player" + i, Color.GREEN);
	
    }

    @Override
    public double getNewSpeed(){
	return ((8.72 + this.getRand()) / 10);
    }
}

class NPC extends Horse{

    public NPC(int i){
	super("NPC" + i, Color.GRAY);
    }

    @Override
    public double getNewSpeed(){
	return ((8.72 + this.getRand()) / 10);
    }
}
