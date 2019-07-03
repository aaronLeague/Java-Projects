import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

class SquareList{
    private Square head, tail;
    private int count = 0;
    
    private static class Square{
	private int halfSize;
	private Color color;
	private Square next;
	private Random rand = new Random();

	public Square(){
	    halfSize = rand.nextInt(200) + 1;
	    color = new Color(rand.nextFloat(),
			      rand.nextFloat(),
			      rand.nextFloat());
	}

	protected void paintSquare(Graphics g){
	    g.setColor(color);
	    g.fillRect(200 - halfSize, 200 - halfSize,
		       2 * halfSize, 2 * halfSize);
	    if (next != null)
		next.paintSquare(g);
	}

	@Override
	public String toString(){
	    return halfSize + ", ";
	}
    }

    //Add Square to list at the appropriate point
    public void add(){
	if(head == null) tail = head = new Square();
	else{
	    Square curr = head;
	    Square square = new Square();

	    //If the new one is biggest, put it first
	    if(head.halfSize < square.halfSize){
		square.next = head;
		head = square;
	    }else{
		//Otherwise, find where square fits in the list
		while(curr.next != null &&
		      curr.next.halfSize >= square.halfSize){
		    
		    //Don't let any two squares be the same size
		    if(curr.next.halfSize == square.halfSize){
			square.halfSize -= 5;
			if(PulsatingSquareApp.isDebug())
			    System.out.println("Changed square size to: " +
					       square.halfSize);
		    }

		    curr = curr.next;
		}
	    
	    square.next = curr.next;
	    curr.next = square;
	    
	    if(square.next == null) tail = square;
	    }
	}
	count++;

	//Print list of halfSize values in order, if in debug mode
	if(PulsatingSquareApp.isDebug()) print();
    }

    public int getCount(){return count;}

    public void paintSquares(Graphics g){
        if(head != null) head.paintSquare(g);
    }

    public void print(){
	for(Square curr = head; curr != null; curr = curr.next)
	    System.out.print(curr);
	System.out.println("\n Head = " + head + "Tail= " + tail);
    }

    public void rotate(){
	Color buffer;
	for(Square curr = head; curr != tail; curr = curr.next){
	    buffer = curr.next.color;
	    curr.next.color = head.color;
	    head.color = buffer;
	}
    }
}
