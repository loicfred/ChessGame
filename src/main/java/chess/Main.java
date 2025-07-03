package chess;

import chess.window.Credits;
import chess.window.maingame.GameMenu;
import chess.window.Settings;
import chess.window.TitleMenu;

public class Main {

    // loads preference file at the beginning
    public static Preferences Prefs = Preferences.ReadPreferences();
    // all the windows declaration
    public static TitleMenu TitleScreen = new TitleMenu();
    public static Settings SettingsScreen = new Settings();
    public static Credits CreditsScreen = new Credits();
    public static GameMenu GameScreen = new GameMenu(Prefs.getP1Side(), false);

    public static void main(String[] args) {
        // sets visible the first window which must be visible at start up
        TitleScreen.setVisible(true);
    }
}

