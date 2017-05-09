import java.awt.*;
import java.util.*;
import javax.imageio.*; // allows image loading
import java.io.*; // allows file access
import javax.swing.*;

//the user's hand
public class Player extends Hand
{
    ArrayList<Boolean> selected; //positions of cards selected by the user's mouse
    boolean passClicked; //if the pass button has been clicked, this is true
    boolean playClicked; //if the play button has been clicked, this is true
    boolean exchangeClicked; //if the exchange button has been clicked, this is true
    String message; //message displayed

    public Player (Deck d)
    {
        super (d); //calls Hand constructor
        selected = new ArrayList<Boolean> (); //selected initialized with all false
        for (int i = 0; i<cards.size(); i++)
        {
            selected.add(false);
        }
        passClicked = false;
        playClicked = false;
        exchangeClicked = false;
        message = "";
    }

    //displays the user's hand
    public void show (Graphics g, int x, int y)
    {
        for (int i = 0 ; i < cards.size () ; i++)
        {
            int up = 0;
            if (selected.get(i)) up = 20;
            cards.get (i).show (g, i % 13 * 60 + x, i / 13 *50 + y - up);
        }
    }

    //exchange of cards between pres and bum or vice pres and vice bum between rounds
    public ArrayList<Card> exchangeCards ()
    {
        Game.window.exchangeButton.setEnabled(true); //enables exchange button
        ArrayList<Card> output = new ArrayList<Card> (); //list of cards to be outputted
        //displays help message
        if (position==3) message = "Please select your highest two cards.";
        else if (position==2) message = "Please select your highest card.";
        else if (position==1) message = "Please select a card to give to the vice bum.";
        else if (position==0) message = "Please select two cards to give to the bum.";
        while (!exchangeClicked) //infinite loop until button is clicked
        {
            try {
                Thread.sleep(10);
            } catch (InterruptedException f) {
            }
            if (GUI.newGame) //exit method if new game button pressed
            {   
                GUI.newGame = false;
                exchangeClicked = false;
                Game.window.exchangeButton.setEnabled(false);
                return null;
            }
        }
        //adds selected cards to be exchanged
        for (int i = cards.size()-1; i>=0; i--)
        {
            if (selected.get(i)) 
            {
                output.add (cards.get(i));
                cards.remove(i);
            }
        }
        //resets button status
        exchangeClicked = false;
        Game.window.exchangeButton.setEnabled(false);
        //if the exchange is valid
        if (isValidExchange(output))
        {
            selected = new ArrayList<Boolean>(); //reset selected cards
            for (int i = 0; i<cards.size()+2; i++) //+2 accounts for cards added
            {
                selected.add(false);
            }
            message = "";
            return output;
        }
        //otherwise, add cards back into the hand and sort again
        cards.addAll(output);
        selectionSort(cards);
        //reset selected cards
        selected = new ArrayList<Boolean>();
        for (int i = 0; i<cards.size()+2; i++) //+2 accounts for cards added
        {
            selected.add(false);
        }
        //display error message
        JOptionPane.showMessageDialog(Game.window, "You cannot exchange that; please choose different cards.");
        return exchangeCards(); //recursive call until user selection is valid
    }

    //determines if exchanged cards are valid
    public boolean isValidExchange (ArrayList<Card> input)
    {
        if (position==0)
            return input.size()==2;
        else if (position==1)
            return input.size()==1;
        else if (position==2)
        {
            if (input.size()==1)
            {
                boolean output = true;
                for (int i = 0; i<cards.size(); i++)
                {
                    if (cards.get(i).getRank()>input.get(0).getRank()) output = false;
                }
                return output;
            }
        }
        else if (position==3)
        {
            if (input.size()==2)
            {
                boolean output = true;
                for (int i = 0; i<cards.size(); i++)
                {
                    if (cards.get(i).getRank()>input.get(1).getRank()) output = false;
                }
                return output;
            }
        }
        return false;
    }

    //plays cards
    public void play ()
    {
        //enables buttons
        Game.window.playButton.setEnabled(true);
        Game.window.passButton.setEnabled(true);
        //displays help message
        if (Game.p.type.equals("Any")) message = "Please play a single, double, or triple.";
        else message = "Please play a "+Game.p.type+" or a nuke. If you cannot play, click pass.";
        while (!playClicked&&!passClicked) //infinite loop until buttons are pressed
        {
            try {
                Thread.sleep(10);
            } catch (InterruptedException f) {
            }
            if (GUI.newGame) //exit method if new game button pressed
            {   
                GUI.newGame = false;
                Game.window.playButton.setEnabled(false);
                Game.window.passButton.setEnabled(false);
                playClicked = false;
                passClicked = false;
                return;
            }
        }
        ArrayList<Card> output = new ArrayList<Card> (); //cards to be played
        if (playClicked) //play button clicked
        {
            //resets buttons
            playClicked = false;
            passClicked = false;
            Game.window.playButton.setEnabled(false);
            Game.window.passButton.setEnabled(false);
            for (int i = cards.size()-1; i>=0; i--) //adds selected cards to output and removes them from deck
            {
                if (selected.get(i)) 
                {
                    output.add (cards.get(i));
                    cards.remove(i);
                }
            }
            //add cards to top of pile if they are valid
            if (isValid(output))
            {
                Game.p.add(output);
            }
            else {
                cards.addAll(output); //adds them back to the hand if invalid
                selectionSort(cards); //re sorts the hand
                selected = new ArrayList<Boolean>(); //resets selected cards
                for (int i = 0; i<cards.size(); i++)
                {
                    selected.add(false);
                }
                //display error message
                JOptionPane.showMessageDialog(Game.window, "You cannot play that; please choose different cards.");
                play(); //recursive call until user plays properly
            }
        }
        else if (passClicked) //pass button clicked
        {
            //resets buttons
            playClicked = false;
            passClicked = false;
            pass = true;
            Game.window.playButton.setEnabled(false);
            Game.window.passButton.setEnabled(false);
        }
        selected = new ArrayList<Boolean>(); //resets selected cards
        for (int i = 0; i<cards.size(); i++)
        {
            selected.add(false);
        }
        message = "";
    }

    //determines if cards played are valid, based on cards at top of the pile
    public boolean isValid (ArrayList<Card> output)
    {
        if (Game.p.type.equals("Any"))
        {
            if (output.size()==1) 
            {
                Game.p.type = "Single";
                return true;
            }
            else if (isDouble(output)) 
            {
                Game.p.type = "Double";
                return true;
            }
            else if (isTriple(output)) 
            {
                Game.p.type = "Triple";
                return true;
            }
            return false;
        }
        else if (Game.p.type.equals("Triple")&&(isTriple(output)||isTwo(output)))
        {
            return output.get(0).getRank()>=Game.p.highRank;
        }
        else if (Game.p.type.equals("Double")&&(isDouble(output)||isTwo(output)))
        {
            return output.get(0).getRank()>=Game.p.highRank;
        }
        else if (Game.p.type.equals("Single")&&output.size()==1)
        {
            return output.get(0).getRank()>=Game.p.highRank;
        }
        return false;
    }

    //displays help message
    public void showMessage (Graphics g)
    {
        g.setColor (Color.white);
        g.setFont (new Font ("Times New Roman", Font.BOLD, 20));
        g.drawString (message, 10, 30);
        if (pass) 
        {
            g.setColor (Color.red);
            g.setFont (new Font ("Times New Roman", Font.PLAIN, 12));
            g.drawString ("PASS", 850, 355); 
        }
    }
}