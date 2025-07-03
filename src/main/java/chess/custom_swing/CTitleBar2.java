package chess.custom_swing;

import chess.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.InputStream;

import static chess.Main.*;

public class CTitleBar2 extends CPanel {

    private final CLabel Title;
    private final CMenu Close;
    private CMenu Settings = null;
    private CPopupMenu SettingsPopup = null;

    private boolean hasSelectedPopupMenu = false;
    public CTitleBar2(JFrame parentFrame, String title) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(getWidth(), 28));

        // use InputStream to read the icon from the resources folder
        // used a try-with-resources to automatically close the inputstream once it exits
        // the try-catch block.
        JLabel Icon = new JLabel();
        try (InputStream S = Main.class.getResourceAsStream("/img/icon.png")) {
            if (S != null) {
                Icon.setIcon(new ImageIcon(ImageIO.read(S).getScaledInstance(16, 16, 0)));
                Icon.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6)); // Padding
                Icon.setBackground(Color.white);
            }
        } catch (Exception ignored) {}


        // the popup menu which opens with the 2 buttons of the settings menu
        // the Exit button dispose the window and returns the title screen
        // the preferences is a shortcut to the settings menu.
        SettingsPopup = new CPopupMenu();
        CMenuItem menuItem1 = new CMenuItem("Exit");
        CMenuItem menuItem2 = new CMenuItem("Preferences...");
        menuItem1.addActionListener(e -> {
            TitleScreen.setVisible(true);
            parentFrame.dispose();
        });
        menuItem2.addActionListener(e -> {
            SettingsScreen.setVisible(true);
        });
        SettingsPopup.add(menuItem1);
        SettingsPopup.add(menuItem2);

        // the settings menu which creates a popup which clicked on.
        // the popup also appears when hovering over it.
        Settings = new CMenu("Settings");
        Settings.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                hasSelectedPopupMenu = true;
                getSettingsPopup().show(e.getComponent(), 0, Settings.getHeight()); // Show popup at mouse location
            }
        });
        Settings.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (hasSelectedPopupMenu) {
                    getSettingsPopup().show(e.getComponent(), 0, Settings.getHeight()); // Show popup at mouse location
                }
            }
        });

        // this is the title text of the title bar
        Title = new CLabel(title);
        Title.setForeground(Prefs.getTextTheme().brighter());
        Title.setBorder(BorderFactory.createEmptyBorder(2, 10, 0, 0));
        Title.setFont(new Font("Arial", Font.BOLD, 12));

        // this is the exit button of the title bar used to close the window
        Close = new CMenu("X");
        Close.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        Close.setFocusable(false);
        Close.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (getCloseButton().isMouseOver()) {
                    System.exit(0);
                }
            }
        });

        // a panel to position the icon and window title as well as the menu button at the left
        CPanel TopLeft = new CPanel();
        TopLeft.setLayout(new BoxLayout(TopLeft,BoxLayout.X_AXIS));
        TopLeft.setOpaque(false);
        TopLeft.add(Icon);
        TopLeft.add(getSettingsMenu());
        TopLeft.add(getAppTitle());

        // a panel to position the exit button to the right
        JPanel TopRight = new JPanel();
        TopRight.setLayout(new GridLayout(1,3));
        TopRight.setOpaque(true);
        TopRight.add(getCloseButton());

        add(TopLeft, BorderLayout.WEST);
        add(TopRight, BorderLayout.EAST);
    }

    public CMenu getCloseButton() {
        return Close;
    }
    public JLabel getAppTitle() {
        return Title;
    }
    public CMenu getSettingsMenu() {
        return Settings;
    }

    private CPopupMenu getSettingsPopup() {
        return SettingsPopup;
    }

}
