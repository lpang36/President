/*Lawrence Pang
 * ICS 3U1
 * 23/11/2015
 * Card and deck program
 */

import java.awt.*;
import javax.imageio.*; // allows image loading
import java.io.*; // allows file access
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import java.util.*;

class GUI extends JFrame
{
    Game game; //the game this GUI belongs to
    int current; //player who is currently playing
    static boolean newGame; //whether newGame has been pressed
    //positions of the players
    int[] playersX; 
    int[] playersY;
    //buttons
    JButton playButton;
    JButton passButton;
    JButton exchangeButton;
    JButton newButton;

    //======================================================== constructor
    public GUI (Game h)
    {
        game = h;
        //players' x and y coordinates
        playersX = new int[4];
        playersY = new int[4];
        playersX[0] = 0;
        playersX[1] = 24;
        playersX[2] = 281;
        playersX[3] = 560;
        playersY[0] = 360;
        playersY[1] = 193;
        playersY[2] = 44;
        playersY[3] = 193;

        //initializes buttons
        BtnListener btnListener = new BtnListener (); // listener for all buttons
        playButton = new JButton ("Play"); //play button
        playButton.addActionListener (btnListener);
        playButton.setEnabled(false);
        playButton.setBounds(800,380,100,40);
        passButton = new JButton ("Pass"); //pass button
        passButton.addActionListener (btnListener);
        passButton.setEnabled(false);
        passButton.setBounds(800,430,100,40);
        exchangeButton = new JButton ("Exchange"); //exchange button
        exchangeButton.addActionListener (btnListener);
        exchangeButton.setEnabled(false);
        exchangeButton.setBounds (800, 480, 100, 40);
        newButton = new JButton ("New Game"); //new game button
        newButton.addActionListener (btnListener);
        newButton.setBounds (800, 0, 100, 30);

        //initializes drawareas 
        DrawAreaNorth north = new DrawAreaNorth (900, 360); //for the AIs
        north.setBounds(0,0,900,360);
        DrawAreaSouth south = new DrawAreaSouth (900, 160); //for the user
        south.setBounds(0,360,900,160);

        //layered pane for entire gui
        //layered pane has vertical dimension indicated by the second Integer parameter
        JLayeredPane playerArea = getLayeredPane();
        playerArea.add (north, new Integer(0));
        playerArea.add (south, new Integer(0));
        playerArea.add (playButton, new Integer(1));
        playerArea.add (passButton, new Integer(1));
        playerArea.add (exchangeButton, new Integer(1));
        playerArea.add (newButton, new Integer(1));

        pack ();
        setTitle ("President");
        setSize (900, 550);
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo (null);           // Center window.
    }

    //actionlistener for buttons
    class BtnListener implements ActionListener // Button menu
    {
        public void actionPerformed (ActionEvent e)
        {
            if (e.getActionCommand ().equals ("Play"))
            {
                game.p1.playClicked = true; //sets variable to true so as to break out of infinite loop and execute action
            }
            if (e.getActionCommand ().equals ("Pass"))
            {
                game.p1.passClicked = true; //sets variable to true so as to break out of infinite loop and execute action
            }
            if (e.getActionCommand ().equals ("Exchange"))
            {
                game.p1.exchangeClicked = true; //sets variable to true so as to break out of infinite loop and execute action
            }
            if (e.getActionCommand ().equals ("New Game"))
            {
                //resets the game
                newGame = true;
                game.p1.position = -1;
                game.p2.position = -1;
                game.p3.position = -1;
                game.p4.position = -1;
                game.winCounts = new int[] {0,0,0,0};
                game.reset();
                //delay
                try {
                    Thread.sleep(2000);                 //1000 milliseconds is one second.
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
            repaint();
        }
    }

    //drawarea for AIs
    class DrawAreaNorth extends JPanel
    {
        public DrawAreaNorth (int width, int height)
        {
            this.setPreferredSize (new Dimension (width, height)); // size
        }

        public void paintComponent (Graphics g)
        {
            //loads background
            Image background = null;
            try 
            {
                background = ImageIO.read (new File ("background.gif"));
            }
            catch (IOException e) {System.out.println ("File not found");}
            //loads scoreboard image
            Image scoreboard = null;
            try 
            {
                scoreboard = ImageIO.read (new File ("scoreboard.png"));
            }
            catch (IOException e) {System.out.println ("File not found");}
            g.drawImage (background, 0, 0, null);
            g.drawImage (scoreboard, 704, 0, null);
            g.setColor (Color.white);
            g.setFont (new Font ("Times New Roman", Font.BOLD, 15));
            //displays the scores
            g.drawString ("Scoreboard", 10+704, 25);
            g.drawString ("You: "+game.winCounts[0], 10+704, 50);
            g.drawString ("West: "+game.winCounts[1], 10+704, 75);
            g.drawString ("North: "+game.winCounts[2], 10+704, 100);
            g.drawString ("East: "+game.winCounts[3], 10+704, 125);
            g.setColor (Color.white);
            //displays a white rectangle around each AI's area
            for (int i = 1; i<4; i++)
            {
                g.drawRect (playersX[i]-5, playersY[i]-5, 323, 107);
            }
            g.drawRect (playersX[0]-5, playersY[0]-5, 900, 10);
            g.setColor (Color.red);
            //displays a red rectangle around the current player
            if (current>0) g.drawRect (playersX[current]-5, playersY[current]-5, 323, 107);
            else if (current == 0) g.drawRect (playersX[current]-5, playersY[current]-5, 900, 10);
            g.setFont (new Font ("Times New Roman", Font.PLAIN, 12));
            //displays the players' positions, i.e. pres, vice pres, etc.
            for (int i = 0; i<4; i++)
            {
                int p = game.players.get(i).position;
                String name = "";
                if (p==0) name = "President";
                if (p==1) name = "Vice President";
                if (p==2) name = "Vice Bum";
                if (p==3) name = "Bum";
                g.drawString (name,playersX[i],playersY[i]-5);
            }
            //display AI hands and pile
            game.p3.show (g, playersX[2], playersY[2]);
            game.p2.show (g, playersX[1], playersY[1]);
            game.p4.show (g, playersX[3], playersY[3]);
            game.p.show (g);
            game.p1.showMessage (g);
        }
    }

    //drawarea for user
    class DrawAreaSouth extends JPanel
    {
        public DrawAreaSouth (int width, int height)
        {
            this.setPreferredSize (new Dimension (width, height)); // size
            this.addMouseListener (new MouseAdapter(){
                    //interprets when the user clicks on a card
                    public void mouseClicked (MouseEvent e)
                    {
                        int x = e.getX();
                        int y = e.getY();
                        int posn = x/60;
                        //selects the card that the user clicks on
                        if (x<800)
                        {
                            try 
                            {
                                game.p1.selected.set(posn,(Boolean)(!game.p1.selected.get(posn)));
                            }
                            catch (IndexOutOfBoundsException f) {}
                        }
                        repaint();
                    }
                });
        }

        public void paintComponent (Graphics g)
        {
            //display background
            Image background = null;
            try 
            {
                background = ImageIO.read (new File ("background2.gif"));
            }
            catch (IOException e) {System.out.println ("File not found");}
            g.drawImage (background, 0, 0, null);
            //show user's hand
            game.p1.show (g, 0, 50);
        }
    }
}