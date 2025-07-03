package chess.custom_swing;

import javax.swing.*;

import static chess.Main.Prefs;

public class CLabel extends JLabel {

    public CLabel(String text, int alignment) {
        super(text);
        setHorizontalAlignment(alignment);
        setForeground(Prefs.getTextTheme());
    }
    public CLabel(String text) {
        super(text);
        setForeground(Prefs.getTextTheme());
    }
}
