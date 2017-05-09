import java.awt.*;
import java.util.*;

//class for the pile (which is where played cards go)
class Pile {
    String type; //the current type, i.e. single, double, triple, or any
    ArrayList<Card> top; //the current cards at the top of the pile
    int highRank; //the rank of the current cards at the top of the pile
    
    //constructor
    public Pile () {
        type = "Any";
        top = new ArrayList<Card>();
        highRank = 0;
    }
    
    //displays the pile
    public void show (Graphics g)
    {
        for (int i = 0; i<top.size(); i++)
        {
            top.get(i).show(g, 400+i*20, 192);
        }
    }

    //adds cards to the top of the pile
    public void add (ArrayList<Card> played)
    {
        top = played;
        highRank = top.get(0).getRank();
    }
    
    //resets the pile
    public void reset ()
    {
        type = "Any";
        top = new ArrayList<Card>();
        highRank = 0;
    }
}