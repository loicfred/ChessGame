package chess.window;

import chess.Preferences;
import chess.custom_swing.*;
import chess.window.maingame.GameMenu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import static chess.Main.*;
import static chess.window.maingame.GameMenu.P1;
import static chess.window.maingame.GameMenu.hasAI;

@SuppressWarnings("all")
public class Settings extends CFrame {

    // The main settings menu, with the initial page which can be defined.
    public Settings() {
        super("Settings - Chess Game");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500,400);

        // the main panel containing all items of the window
        CPanel MainPanel = new CPanel();
        MainPanel.setLayout(new BorderLayout());
        MainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        MainPanel.setBorder(new LineBorder(Prefs.getTheme2(), 3));


        // the title bar
        CTitleBar1 TitleBar = new CTitleBar1(getTitle());
        // the two buttons to apply changes and reset the settings to default
        CButton Apply = new CButton("Apply");
        CButton Reset = new CButton("Reset");

        // The right settings where you can modify the values of the preferences.
        // it uses a card layout which has 2 panels (2 current possible settings, which are theme and language)
        // it has 2 panels, the top (center) one with the values to switch, and the bottom one with the buttons.
        // it also has the two buttons Apply and Reset at the bottom
        CPanel contentPanel = new CPanel();
        contentPanel.setLayout(new CardLayout());
        contentPanel.add(getThemeSettings(), "Theme");
        contentPanel.add(getLanguageSetting(), "Language");
        contentPanel.add(getChessSetting(), "Chess");
        CPanel buttonPanel = new CPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(new CLabel("Warning: Saving will close current game."));
        buttonPanel.add(Apply);
        buttonPanel.add(Reset);
        CPanel ContentAndButtons = new CPanel();
        ContentAndButtons.setLayout(new BorderLayout());
        ContentAndButtons.add(contentPanel, BorderLayout.CENTER);
        ContentAndButtons.add(buttonPanel, BorderLayout.SOUTH);



        // this is the left part of the settings window.
        // it contains the list of settings type to choose from.
        String[] categories = {"Theme", "Language", "Chess"};
        JList<String> SettingsType = new JList<>(categories);
        SettingsType.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        SettingsType.setSelectedIndex(0);
        SettingsType.setBackground(Prefs.getTheme().darker());
        SettingsType.setForeground(Prefs.getTextTheme());
        SettingsType.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                CardLayout cl = (CardLayout) contentPanel.getLayout();
                cl.show(contentPanel, SettingsType.getSelectedValue());
                // changes the right settings panel based on the settings type selected.
            }
        });
        JScrollPane SettingsTypeChoice = new JScrollPane(SettingsType);
        SettingsTypeChoice.setPreferredSize(new Dimension(150, 0));


        MainPanel.add(TitleBar, BorderLayout.NORTH);
        MainPanel.add(SettingsTypeChoice, BorderLayout.WEST);
        MainPanel.add(ContentAndButtons, BorderLayout.CENTER);



        // button to apply the changes and save the new preferences in the file.
        Apply.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Prefs.setTheme(getHex(Theme.getColor()));
                Prefs.setTheme2(getHex(Theme2.getColor()));
                Prefs.setTextColor(getHex(TextTheme.getColor()));

                Prefs.setGridColor1(getHex(GridColor1.getColor()));
                Prefs.setGridColor2(getHex(GridColor2.getColor()));
                Prefs.setPlaceableAreaColor(getHex(PlaceableArea.getColor()));
                Prefs.setUnplaceableAreaColor(getHex(UnplaceableArea.getColor()));

                Prefs.setLanguage((String) langcb.getSelectedItem());
                Prefs.setP1Side((String) playerPoscb.getSelectedItem());
                Prefs.setDifficulty((String) diffcb.getSelectedItem());

                Prefs.Save();
                resetWindows();
            }
        });
        // button to reset the preferences to their default.
        Reset.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Prefs = new Preferences().Save();
                resetWindows();
            }
        });


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

        add(MainPanel);
        setVisible(false);
    }


    // these are the color selectors for the theme of the game.
    public CColorButton Theme = new CColorButton(Prefs.getTheme());
    public CColorButton Theme2 = new CColorButton(Prefs.getTheme2());
    public CColorButton TextTheme = new CColorButton(Prefs.getTextTheme());

    public CColorButton GridColor1 = new CColorButton(Prefs.getGridColor1());
    public CColorButton GridColor2 = new CColorButton(Prefs.getGridColor2());
    public CColorButton PlaceableArea = new CColorButton(Prefs.getPlaceableAreaColor());
    public CColorButton UnplaceableArea = new CColorButton(Prefs.getUnplaceableAreaColor());
    // this is the panel where all theme settings are placed, such as color selector.
    public JScrollPane getThemeSettings() {
        CPanel Settings = new CPanel();
        Settings.setLayout(new BoxLayout(Settings, BoxLayout.Y_AXIS));
        Settings.setBorder(new EmptyBorder(0, 10, 0, 10));

        CPanel P1 = new CPanel();
        P1.setLayout(new GridLayout(3, 2, 5, 5));
        P1.setBorder(BorderFactory.createTitledBorder("Theme"));
        P1.setMaximumSize(new Dimension(400,100));

        P1.add(new CLabel("Main Theme:"));
        P1.add(Theme);
        P1.add(new CLabel("Secondary Theme:"));
        P1.add(Theme2);
        P1.add(new CLabel("Text Color:"));
        P1.add(TextTheme);

        CPanel P2 = new CPanel();
        P2.setLayout(new GridLayout(4, 2, 5, 5));
        P2.setBorder(BorderFactory.createTitledBorder("Grid"));
        P2.setMaximumSize(new Dimension(400,130));

        P2.add(new CLabel("Grid Color 1"));
        P2.add(GridColor1);
        P2.add(new CLabel("Grid Color 2"));
        P2.add(GridColor2);
        P2.add(new CLabel("Placeable Area Color"));
        P2.add(PlaceableArea);
        P2.add(new CLabel("Unplaceable Area Color"));
        P2.add(UnplaceableArea);

        Settings.add(P1);
        Settings.add(P2);
        return new JScrollPane(Settings, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }



    // these are language selectors, for now only english is available.
    public String[] languages = {"English"};
    public JComboBox<String> langcb = new JComboBox<>(languages);
    // this is the panel where all language settings are
    public JScrollPane getLanguageSetting() {
        CPanel Settings = new CPanel();
        Settings.setLayout(new BoxLayout(Settings, BoxLayout.Y_AXIS));
        Settings.setBorder(new EmptyBorder(0, 10, 0, 10));

        CPanel P1 = new CPanel();
        P1.setLayout(new GridLayout(1, 2, 5, 5));
        P1.setBorder(BorderFactory.createTitledBorder("Language"));
        P1.setMaximumSize(new Dimension(400,45));

        P1.add(new CLabel("Language:"));
        P1.add(langcb);

        Settings.add(P1);
        return new JScrollPane(Settings, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }


    // these are language selectors, for now only english is available.
    public String[] difficulty = {"Easy", "Normal", "Hard"};
    public String[] playerPos = {"Top", "Bottom"};
    public JComboBox<String> diffcb = new JComboBox<>(difficulty);
    public JComboBox<String> playerPoscb = new JComboBox<>(playerPos);
    // this is the panel where all game settings are
    public JScrollPane getChessSetting() {
        CPanel Settings = new CPanel();
        Settings.setLayout(new BoxLayout(Settings, BoxLayout.Y_AXIS));
        Settings.setBorder(new EmptyBorder(0, 10, 0, 10));

        CPanel P1 = new CPanel();
        P1.setLayout(new GridLayout(2, 2, 5, 5));
        P1.setBorder(BorderFactory.createTitledBorder("Chess"));
        P1.setMaximumSize(new Dimension(400,85));


        diffcb.setSelectedItem(Prefs.getDifficulty());
        playerPoscb.setSelectedItem(Prefs.getP1Side().toString());

        P1.add(new CLabel("Difficulty:"));
        P1.add(diffcb);

        P1.add(new CLabel("Player:"));
        P1.add(playerPoscb);

        Settings.add(P1);
        return new JScrollPane(Settings, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }



    public String getHex(Color color) {
        return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
    }


    // this function is to reset all the windows
    // this is used to reload the theme changes if necessary.
    // dispose each window and re-create them with the new colors.
    public void resetWindows() {
        int x = TitleScreen.getX(), y = TitleScreen.getY();
        boolean isVisible = TitleScreen.isVisible();
        TitleScreen.dispose();
        TitleScreen = new TitleMenu();
        TitleScreen.setLocation(x, y);
        TitleScreen.setVisible(isVisible);

        x = CreditsScreen.getX();
        y = CreditsScreen.getY();
        isVisible = CreditsScreen.isVisible();
        CreditsScreen.dispose();
        CreditsScreen = new Credits();
        CreditsScreen.setLocation(x, y);
        CreditsScreen.setVisible(isVisible);

        x = GameScreen.getX();
        y = GameScreen.getY();
        isVisible = GameScreen.isVisible();
        GameScreen.dispose();
        GameScreen = new GameMenu(P1, hasAI);
        GameScreen.setLocation(x, y);
        GameScreen.setVisible(isVisible);

        dispose();
        SettingsScreen = new Settings();
        SettingsScreen.setLocation(getX(), getY());
        SettingsScreen.setVisible(true);
    }
}