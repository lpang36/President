import java.util.*;

public class Game
{
    static Pile p; //pile
    static Deck d; //starting deck
    int count; //number of players who have already finished the game
    int playedLast; //last player to play
    int oldPlayedLast; //first player to start a round
    int passCount; //number of players who have passed in a round
    int[] winCounts; //number of times each player has won
    static Player p1; //the user
    static AI p2,p3,p4; //the AIs
    ArrayList<Hand> players; //list of user and AIs
    static GUI window; //GUI
    static boolean startClicked; //determines if the start button has been pressed

    //constructor
    public Game()
    {
        winCounts = new int[] {0,0,0,0};
        reset();
        window = new GUI(this);
    }

    //single turn, i.e. all players go once
    public void turn ()
    {
        //starts from oldplayedlast (i.e. the player who starts a round)
        //goes through all four players
        for (int i = oldPlayedLast; i<players.size()+oldPlayedLast; i++)
        {
            //only plays if the player has cards and has not yet passed
            if (players.get(i%players.size()).cards.size()>0&&!players.get(i%players.size()).pass&&passCount<3)
            { 
                window.current = i%players.size(); //indicates current player
                players.get(i%players.size()).play(); //plays cards
                if (!players.get(i%players.size()).pass) playedLast = i%players.size(); //adjusts playedlast if the player did not pass
                //compute number of players who have passed
                passCount = 0;
                for (int j = 0; j<players.size(); j++)
                {
                    if (players.get(j).pass) passCount++;
                }
                window.repaint();
                if (players.get(i%players.size()).cards.size()==0) //player wins when they run out of cards
                {
                    players.get(i%players.size()).pass = true; //they will always pass as they have no cards
                    players.get(i%players.size()).position = count; //indicates what position they finished in
                    if (count==0)
                        winCounts[i%players.size()]++; //indicates that the player has won a game (i.e. become president)
                    count++;
                }
                //this deals with 2s, which are nukes
                if (p.top.size()>0&&p.top.get(0).getRank()==12) //2 has rank 12
                {
                    //all players pass as nothing can be played after a nuke
                    p1.pass = true;
                    p2.pass = true;
                    p3.pass = true;
                    p4.pass = true;
                    //extra delay
                    try {
                        Thread.sleep(1000);                 //1000 milliseconds is one second.
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    return; //nuke ends the turn
                }
                if (count==3) //if only one player left then the game ends
                {
                    p1.pass = true;
                    p2.pass = true;
                    p3.pass = true;
                    p4.pass = true;
                    return;
                }
            }
            else
            {
                players.get(i%players.size()).pass = true; //to avoid logic errors
            }
        }
    }

    //for each round (i.e. continuous cycle of play until only one player has cards left)
    public void round ()
    {
        reset(); //resets data fields
        window.repaint();
        try {
            exchange(); //exchange cards
        } catch (NullPointerException e) {}
        //resets positions
        p1.position = -1;
        p2.position = -1; 
        p3.position = -1;
        p4.position = -1;
        window.repaint();
        while (count<3) //at least two players remaining
        {
            while (!p1.pass||!p2.pass||!p3.pass||!p4.pass) //play turns until all players have passed
            {
                turn();
            }
            oldPlayedLast = playedLast; //player to start the next turn is the last player to have played
            //resets some variables
            passCount = 0;
            p1.pass = false;
            p2.pass = false;
            p3.pass = false;
            p4.pass = false;
            p.reset(); //resets the pile
            window.repaint();
        }
        //figures out who the bum is
        int sum = 0;
        for (int i = 0; i<4; i++)
        {
            if (players.get(i).position==-1) players.get(i).position = 3;
        }
        window.repaint();
        //delay after a round is done
        try {
            Thread.sleep(3000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    //resets data fields
    public void reset ()
    {
        p = new Pile();
        d = new Deck();
        int pos1=-1, pos2=-1, pos3=-1, pos4=-1;
        try {
            pos1 = p1.position;
            pos2 = p2.position;
            pos3 = p3.position;
            pos4 = p4.position;
        } catch (NullPointerException e) {}
        p1 = new Player(d);
        p2 = new AI(d);
        p3 = new AI(d);
        p4 = new AI(d);
        p1.position = pos1;
        p2.position = pos2;
        p3.position = pos3;
        p4.position = pos4;
        players = new ArrayList<Hand>();
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);
        count = 0;
        playedLast = 0;
        passCount = 0;
        oldPlayedLast = findFirstPlayer();
    }

    //finds the player to begin a round
    public int findFirstPlayer ()
    {
        //if there is a bum, the bum starts
        for (int i = 0; i<4; i++)
        {
            if (players.get(i).position == 3) return i;
        } 
        //if this is the first round, the player with the 3 of diamonds starts
        for (int i = 0; i<4; i++)
        {
            for (int j = 0; j<players.get(i).cards.size(); j++)
            {
                if (players.get(i).cards.get(j).getSuit().equals("diamonds")&&
                players.get(i).cards.get(j).getRank() == 0) //3 has value 0
                    return i;
            }
        }
        return 0;
    }

    //exchange cards at beginning of a round
    public void exchange()
    {
        ArrayList<Hand> others = new ArrayList<Hand>();
        for (int i = 1; i<4; i++)
        {
            if (players.get(i).position+p1.position==3) 
            {   
                players.get(i).add(p1.exchangeCards()); //ensures the user exchanges first
                p1.add(players.get(i).exchangeCards());
            }
            else others.add(players.get(i));
        }
        others.get(0).add(others.get(1).exchangeCards());
        others.get(1).add(others.get(0).exchangeCards());
    }

    public static void main (String args[])
    {
        startClicked = false;
        FrameTestsBase menu = new FrameTestsBase (); //create start menu
        menu.execute(); //execute start menu
        while (!startClicked) {} //loop until start button clicked
        menu.setVisible(false); //close menu window
        //start game
        Game g = new Game();
        window.setVisible(true);
        window.setResizable(false);
        //plays rounds
        while (true)
        {
            g.round();
        }
    }
}