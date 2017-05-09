import java.awt.*;
import java.util.*;
import javax.imageio.*; // allows image loading
import java.io.*; // allows file access

//class for AI players
public class AI extends Hand
{
    public AI (Deck d)
    {
        super(d); //calls Hand constructor
    }

    //displays AI hand
    public void show (Graphics g, int x, int y)
    {
        Image cardback = null;
        try
        {
            cardback = ImageIO.read (new File ("cards\\b.gif")); // load file into Image object
        }
        catch (IOException e)
        {
            System.out.println ("File not found");
        }
        for (int c = 0 ; c < cards.size() ; c++)
        {
            g.drawImage (cardback, c % 13 * 20+x, c / 13 * 50+y, null);
        }
        if (pass) g.drawString ("PASS", x, y+112);
    }

    //exchange cards at end of the round
    //pres gives lowest two cards
    //vice pres gives lowest card
    //bum gives highest two cards
    //vice bum gives highest card
    public ArrayList<Card> exchangeCards ()
    {
        ArrayList<Card> output = null;
        if (position==3) 
        {
            output = new ArrayList<Card> (cards.subList(cards.size()-2,cards.size()));
            cards.remove (cards.size()-1);
            cards.remove (cards.size()-2);
        }
        else if (position==0)
        {
            output = new ArrayList<Card> (cards.subList(0,2));
            cards.remove (1);
            cards.remove (0);
        }
        else if (position==2)
        {
            output = new ArrayList<Card> (cards.subList(cards.size()-1,cards.size()));
            cards.remove (cards.size()-1);
        }
        else if (position==1) 
        {
            output = new ArrayList<Card> (cards.subList(0,1));
            cards.remove (0);
        }
        return output;
    }

    //plays the lowest appropriate card
    public void play ()
    {
        //delay
        try {
            Thread.sleep(1000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        ArrayList<Integer> positions = new ArrayList<Integer> (); //positions of cards to be played
        ArrayList<Card> playedCards = new ArrayList<Card> (); //cards to be played
        //finds triple, double, then single in that order to lead into a round
        if (Game.p.type.equals("Any"))
        {
            if (findTriple().size()!=0)
            {
                Game.p.type = "Triple";
                positions = findTriple();
            }
            else if (findDouble().size()!=0)
            {
                Game.p.type = "Double";
                positions = findDouble();
            }
            else if (findSingle().size()!=0)
            {
                Game.p.type = "Single";
                positions = findSingle();
            }
        }
        //plays according to what is already on pile (if not leading into a round)
        if (Game.p.type.equals("Triple"))
            positions = findTriple();
        if (Game.p.type.equals("Double"))
            positions = findDouble();
        if (Game.p.type.equals("Single"))
            positions = findSingle();
        if (positions.size()==0)
            positions = findTwo();
        //if there are cards to play, removes them from deck and adds them to top of pile
        if (positions.size()>0)
        {
            Collections.sort(positions, Collections.reverseOrder());
            for (int i = 0; i<positions.size(); i++)
            {
                playedCards.add(cards.get(positions.get(i)));
                cards.remove(positions.get(i).intValue());
            }
            Game.p.add(playedCards);
        }
        else { //otherwise, the AI has passed
            pass = true;
        }
    }

    //finds lowest appropriate single
    public ArrayList<Integer> findSingle ()
    {
        ArrayList<Integer> output = new ArrayList<Integer> ();
        for (int i = 0; i<cards.size(); i++)
        {
            if (cards.get(i).getRank()>=Game.p.highRank)
            {
                output.add(i);
                return output;
            }
        }
        return output;
    }

    //finds lowest appropriate double
    public ArrayList<Integer> findDouble ()
    {
        ArrayList<Integer> output = new ArrayList<Integer> ();
        for (int i = 0; i<cards.size()-1; i++)
        {
            if (cards.get(i).getRank()>=Game.p.highRank&&
            cards.get(i+1).getRank()==cards.get(i).getRank()&&
            cards.get(i).getRank()!=12) //2 has value 12
            {
                output.add(i);
                output.add(i+1);
                return output;
            }
        }
        return output;
    }

    //finds lowest appropriate triple
    public ArrayList<Integer> findTriple ()
    {
        ArrayList<Integer> output = new ArrayList<Integer> ();
        for (int i = 0; i<cards.size()-2; i++)
        {
            if (cards.get(i).getRank()>=Game.p.highRank&&
            cards.get(i+1).getRank()==cards.get(i).getRank()&&
            cards.get(i+2).getRank()==cards.get(i).getRank()&&
            cards.get(i).getRank()!=12) //2 has value 12
            {
                output.add(i);
                output.add(i+1);
                output.add(i+2);
                return output;
            }
        }
        return output;
    }

    //finds a two (nuke)
    public ArrayList<Integer> findTwo ()
    {
        ArrayList<Integer> output = new ArrayList<Integer> ();
        try {
            if (output.size()<1&&cards.get(cards.size()-1).getRank()==12) //2 has value 12
            {
                output.add(cards.size()-1);
            }
        } catch (ArrayIndexOutOfBoundsException e) {}
        return output;
    }
}
