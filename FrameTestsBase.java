//Class for starting menu

import javax.swing.JDialog;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Color;
import java.awt.Font;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import sun.audio.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Dimension;

//Background panel
class BgsPanels extends JPanel {
    Image bg = new ImageIcon("428991.jpg").getImage();
    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 56));
        g.setColor(Color.WHITE);
        g.drawString("President", 550, 90);
    }
}

//Buttons
class LoginPanels extends JPanel {
    private JLabel label1;
    private JPanel topPanel;
    private JProgressBar progress;

    LoginPanels() {
        setOpaque(false);
        setLayout(new FlowLayout());

        //Start button
        JButton b = new JButton("Start");
        b.setPreferredSize( new Dimension(100,30));
        b.setBackground(Color.WHITE);
        b.setForeground(Color.BLACK);
        b.setFocusPainted(true);
        b.setFont(new Font("Tahoma",Font.BOLD,12));

        //When mouse hovers over the button, the colour changes
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                b.setBackground(Color.GREEN);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                b.setBackground(UIManager.getColor("Control"));
            }
        });

        //Start game
        b.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed (ActionEvent e){
                //Loading panel
                JFrame frame2 = new JFrame("Loading");
                frame2.setVisible(true);
                frame2.setSize(200,100);
                JPanel panel = new JPanel();
                frame2.add(panel);
                JLabel label = new JLabel("Loading...", JLabel.CENTER);
                label.setSize(300, 30);
                label.setLocation(50, 50);
                label.setForeground(Color.RED);
                panel.add(label);
                panel.setBackground(Color.black);
                
                //Progress bar
                progress = new JProgressBar();
                progress.setPreferredSize( new Dimension( 300, 20 ) );
                progress.setMinimum( 0 );
                progress.setMaximum( 10 );
                progress.setValue( 0 );
                progress.setBounds( 20, 35, 260, 20 );
                
                //Plays audio
                try{

                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("button-1.wav"));
                    Clip clip = AudioSystem.getClip();
                    clip.open(inputStream);
                }catch (Exception et) {}
                
                //Runs progress bar
                Runnable myRunnable = new Runnable(){
                    public void run(){

                        for(int i=0;i<10;i++)
                        {
                            progress.setValue(i);
                            try {
                                Thread.sleep(300);
                            } catch (Exception ex) {}
                            if (i == 9)
                            {
                                frame2.setVisible(false); //closes progress bar
                                Game.startClicked = true; //breaks loop and starts game
                            }
                        }
                    }
                };
                
                //Executes progress bar
                Thread thread = new Thread(myRunnable);
                thread.start();
                panel.add(progress);
            }
        });

        //Exit button
        JButton c = new JButton("Exit");
        c.setPreferredSize( new Dimension(100,30));
        c.setBackground(Color.WHITE);
        c.setForeground(Color.BLACK);
        c.setFocusPainted(true);
        c.setFont(new Font("Tahoma",Font.BOLD,12));
        
        //Closes all windows
        c.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }

        });
            
        //When mouse hovers over the button, the colour changes
        c.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                c.setBackground(Color.RED);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                c.setBackground(UIManager.getColor("Control"));
            }

        });
        
        //Add buttons to panel
        add(b);
        add(c);
    }
}

//Opening menu panel
public class FrameTestsBase extends JFrame {    
    public FrameTestsBase (){
        JPanel bgPanel = new BgsPanels();
        bgPanel.setLayout(new BorderLayout());
        bgPanel.add(new LoginPanels(), BorderLayout.CENTER);
        setContentPane(bgPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1400, 800);
        setVisible(true);
        setResizable(false);
    }

    //Plays audio
    public void execute ()
    {
        AudioInputStream inputStream = null;
        try {
            inputStream = AudioSystem.getAudioInputStream(new File("song.wav"));
        } catch (Exception e) {}
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
        } catch (Exception e) {}
        try {
            clip.open(inputStream);
        } catch (Exception e) {}
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        try {
            Thread.sleep(10000); // looping
        } catch (Exception e) {}
    }
}