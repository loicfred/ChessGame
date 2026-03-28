package chess;

import chess.pieces.King;
import chess.pieces.Pawn;
import chess.pieces.Piece;
import chess.pieces.Side;
import chess.window.Credits;
import chess.window.maingame.GameMenu;
import chess.window.Settings;
import chess.window.TitleMenu;

import static chess.custom_swing.PromotionDialog.showPromotionDialog;
import static chess.window.maingame.GameMenu.*;
import static chess.window.maingame.GameMenu.Board;

public class Main {

    // loads preference file at the beginning
    public static Preferences Prefs = Preferences.ReadPreferences();
    // all the window declarations
    public static TitleMenu TitleScreen = new TitleMenu();
    public static Settings SettingsScreen = new Settings();
    public static Credits CreditsScreen = new Credits();
    public static GameMenu GameScreen = new GameMenu(Prefs.getP1Side(), false);

    public static void main(String[] args) {
        // sets visible the first window which must be visible at start up
        TitleScreen.setVisible(true);
    }
}

