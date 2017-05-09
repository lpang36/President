import java.awt.*;
import java.util.*;

class Deck
{
    private ArrayList<Card> deck;

    public Deck ()
    {
        //creates new deck with 52 cards in order
        deck = new ArrayList<Card>();
        for (int i = 0; i<52; i++)
        {
            deck.add(new Card(i));
        }
        shuffle();
    }

    //returns first 13 cards and removes them from the deck
    public ArrayList<Card> destroy ()
    {
        ArrayList<Card> c = new ArrayList<Card> (deck.subList(0,13));
        for (int i = 0; i<13; i++) 
        {
            deck.remove(0);
        }
        return c;
    }

    //displays deck
    public void show (Graphics g)
    {
        for (int c = 0 ; c < deck.size() ; c++)
        {
            deck.get(c).show (g, c % 13 * 20 + 150, c / 13 * 50 + 20);
        }
    }

    //shuffles the deck
    //each card in the deck switches places with another random card
    public void shuffle ()
    {
        for (int i = 0; i<deck.size(); i++)
        {
            int j = (int)(Math.random()*(deck.size()-1));
            Card temp = deck.get(i);
            deck.set(i,deck.get(j));
            deck.set(j,temp);
        }
    }
}

