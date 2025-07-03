package chess.custom_swing;

import javax.swing.*;

import static chess.Main.Prefs;

public class CPanel extends JPanel {

    public CPanel() {
        setBackground(Prefs.getTheme());
    }
}
