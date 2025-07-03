package chess.window;

import chess.custom_swing.CFrame;
import chess.custom_swing.CLabel;
import chess.custom_swing.CPanel;
import chess.custom_swing.CTitleBar1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import static chess.Main.Prefs;

public class Credits extends CFrame {

    // used for moving the custom JPanel
    private Component currentlyClickedComponent;
    private int mouseX, mouseY;

    public Credits() {
        super("Credits - Chess Game");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(280,180);

        // the main panel containing all items of the window
        CPanel MainPanel = new CPanel();
        MainPanel.setLayout(new BorderLayout());
        MainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        MainPanel.setBorder(new LineBorder(Prefs.getTheme2(), 3));

        // the title bar
        CTitleBar1 TitleBar = new CTitleBar1(getTitle());

        // the description and names
        CPanel Text = new CPanel();
        Text.setLayout(new GridLayout(4,1));
        Text.setBorder(new EmptyBorder(30,5,50,5));
        Text.add(new CLabel("This project has been presented to you by:"));
        Text.add(new CLabel("• Loic Fred Cheerkoot (2403_27696)"));
        Text.add(new CLabel("• Harry Bhuckory (2403_27691)"));
        Text.add(new CLabel("• Ajay Dooshan Gangadurdoss (2403_27704)"));

        // the logic to be able to move the custom the window using the custom title bar.
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

        // button to close the window from custom title bar
        TitleBar.getCloseButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                setVisible(false);
            }
        });

        MainPanel.add(TitleBar, BorderLayout.NORTH);
        MainPanel.add(Text, BorderLayout.CENTER);
        add(MainPanel);
        setVisible(false);
    }
}