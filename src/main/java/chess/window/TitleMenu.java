package chess.window;

import chess.Main;
import chess.custom_swing.*;
import chess.window.maingame.GameMenu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import static chess.Main.Prefs;
import static chess.Main.GameScreen;

public class TitleMenu extends CFrame {

    // declaring the title menu
    public TitleMenu() {
        super("Title Screen - Chess Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(250,300);

        // the main panel containing all items of the window
        CPanel MainPanel = new CPanel();
        MainPanel.setLayout(new BorderLayout());
        MainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        MainPanel.setBorder(new LineBorder(Prefs.getTheme2(), 3));

        // this button closes title menu and open the game window
        CButton BTN = new CButton("Singleplayer");
        BTN.addActionListener(e -> {
            setVisible(false);
            GameScreen.dispose();
            GameScreen = new GameMenu(Prefs.getP1Side(), true);
            GameScreen.setVisible(true);
        });
        // this button closes title menu and open multiplayer menu (isn't implemented)
        CButton BTN2 = new CButton("Multiplayer");
        BTN2.addActionListener(e -> {
            setVisible(false);
            GameScreen.dispose();
            GameScreen = new GameMenu(Prefs.getP1Side(), false);
            GameScreen.setVisible(true);
        });
        // this button opens the settings menu in another window
        CButton BTN3 = new CButton("Settings");
        BTN3.addActionListener(e -> {
            Main.SettingsScreen.setVisible(true);
        });
        // this button opens the credits menu in another window
        CButton BTN4 = new CButton("Credits");
        BTN4.addActionListener(e -> {
            Main.CreditsScreen.setVisible(true);
        });


        // panel with all buttons
        CPanel Buttons = new CPanel();
        Buttons.setLayout(new GridLayout(4,1, 8, 8));
        Buttons.add(BTN);
        Buttons.add(BTN2);
        Buttons.add(BTN3);
        Buttons.add(BTN4);
        Buttons.setBorder(new EmptyBorder(30, 50, 40, 50));


        // the title bar
        CTitleBar1 TitleBar = new CTitleBar1(getTitle());

        // the welcome text
        CPanel TextArea = new CPanel();
        TextArea.setLayout(new BoxLayout(TextArea, BoxLayout.Y_AXIS));
        CLabel L = new CLabel("Welcome to Chess Game.");
        L.setHorizontalAlignment(SwingConstants.CENTER);
        L.setAlignmentX(Component.CENTER_ALIGNMENT);
        L.setBorder(new EmptyBorder(15, 0, 0, 0));
        TextArea.add(L);
        CLabel L2 = new CLabel("Have fun !");
        L2.setHorizontalAlignment(SwingConstants.CENTER);
        L2.setBorder(new EmptyBorder(0, 0, 0, 0));
        L2.setAlignmentX(Component.CENTER_ALIGNMENT);
        TextArea.add(L2);


        // the top of the screen
        CPanel TopScreen = new CPanel();
        TopScreen.setLayout(new BorderLayout());
        TopScreen.add(TitleBar, BorderLayout.NORTH);
        TopScreen.add(TextArea, BorderLayout.SOUTH);

        MainPanel.add(TopScreen, BorderLayout.NORTH);
        MainPanel.add(Buttons, BorderLayout.CENTER);


        // logic to be able to move the custom the window using the custom title bar.
        // it listens when you click the main panel, and checks if you are pressing the title bar
        MainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                currentlyClickedComponent = SwingUtilities.getDeepestComponentAt(MainPanel, e.getX(), e.getY());
                if (TitleBar.equals(currentlyClickedComponent) || TitleBar.getAppTitle().equals(currentlyClickedComponent)) {
                    mouseX = e.getX(); mouseY = e.getY();
                }
            }
        });
        MainPanel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (TitleBar.equals(currentlyClickedComponent) || TitleBar.getAppTitle().equals(currentlyClickedComponent)) {
                    setLocation(getX() + e.getX() - mouseX, getY() + e.getY() - mouseY);
                }
            }
        });

        // button to close the window and exit the program from custom title bar
        TitleBar.getCloseButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                System.exit(0);
            }
        });

        add(MainPanel);
        setVisible(false);
    }
}
