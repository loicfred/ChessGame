package chess.custom_swing;

import javax.swing.*;

import static chess.Main.Prefs;

public class CMenuItem extends JMenuItem {
    public CMenuItem(String text) {
        super(text);
        setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        setBackground(Prefs.getTheme());
        setForeground(Prefs.getTextTheme());
    }
}