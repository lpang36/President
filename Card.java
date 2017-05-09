import java.awt.*;
import javax.imageio.*; // allows image loading
import java.io.*; // allows file access

class Card
{
    private boolean faceup;
    private int rank;
    private String suit;
    private Image image;    
    private static Image cardback;

    public Card (int input)
    {
        //determine suit and rank based on input
        if (input/13==0) suit = "spades";
        else if (input/13==1) suit = "hearts";
        else if (input/13==2) suit = "clubs";
        else if (input/13==3) suit = "diamonds";
        rank = input%13+1;
        faceup = true;
        //image
        image = null;
        try
        {
            image = ImageIO.read (new File ("cards\\" + input + ".gif")); // load file into Image object
            cardback = ImageIO.read (new File ("cards\\b.gif")); // load file into Image object
        }
        catch (IOException e)
        {
            System.out.println ("File not found");
        }
    }

    //display card at some location
    public void show (Graphics g, int x, int y)
    {
        //image displayed depends on if card is face up or down
        if (faceup)
            g.drawImage (image, x, y, null);
        else
            g.drawImage (cardback, x, y, null);
    }

    //accessor methods
    public boolean getFaceup ()
    {
        return faceup;
    }

    //this returns a modified version of rank
    //goes from 3, 4, ... K, A, 2, as in president
    public int getRank ()
    {
        return (rank+10)%13;
    }

    public String getSuit ()
    {
        return suit;
    }
    
    //sets face up or down
    public void setFace (boolean value)
    {
        faceup = value;
    }

    //returns the relative position of a card in selection sort
    public int getSelectionRank ()
    {
        int rankValue = (rank+10)%13;
        int suitValue;
        if (suit.equals("spades"))suitValue = 0;
        else if (suit.equals("hearts")) suitValue = 1;
        else if (suit.equals("clubs")) suitValue = 2;
        else suitValue = 3;
        return rankValue*4+suitValue;
    }
}
