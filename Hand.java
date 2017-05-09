import java.awt.*;
import java.util.*;

public abstract class Hand extends Deck
{
    ArrayList<Card> cards = new ArrayList<Card> (); //list of all cards in the hand
    boolean pass; //whether or not the player has passed
    int position; //the place in which the player had finished in the previous round

    public Hand (Deck d) {
        cards = d.destroy(); //creates hand using first 13 cards from deck (which is shuffled)
        selectionSort(cards); //sorts the hand
        pass = false;
    }

    //abstract methods implemented separately by Player and AI classes
    public abstract void show (Graphics g, int x, int y);

    public abstract void play ();

    public abstract ArrayList<Card> exchangeCards ();

    //adds an arraylist of cards to the hand and then sorts
    public void add (ArrayList<Card> input)
    {
        cards.addAll(input);
        selectionSort(cards);
    }

    //determines if a list of cards is a double, triple, or two
    public boolean isDouble (ArrayList<Card> input)
    {
        return input.size()==2&&input.get(0).getRank()==input.get(1).getRank();
    }

    public boolean isTriple (ArrayList<Card> input)
    {
        return input.size()==3&&input.get(0).getRank()==input.get(1).getRank()&&input.get(2).getRank()==input.get(1).getRank();
    }
    
    public boolean isTwo (ArrayList<Card> input)
    {
        return input.size()==1&&input.get(0).getRank()==12;
    }

    //selection sorts the hand
    public void selectionSort (ArrayList<Card> cards)
    {       
        for (int i = 0; i<cards.size()-1; i++)
        {
            int min = 52;
            int minAt = i+1;
            for (int j = i; j<cards.size(); j++)
            {
                //find the lowest card yet to be sorted
                if (cards.get(j).getSelectionRank()<min)
                {
                    min = cards.get(j).getSelectionRank();
                    minAt = j;
                }
            }
            //send that card to the front of the cards yet to be sorted
            Card temp = cards.get(i);
            cards.set(i,cards.get(minAt));
            cards.set(minAt,temp);
        }
    }
}
