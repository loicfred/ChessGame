package chess.custom_swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static chess.Main.Prefs;

public class CButton extends JButton {
    private boolean isMouseOver = false;

    public CButton(String text) {
        setFocusable(false);
        setBackground(Prefs.getTheme2());
        setLayout(new FlowLayout());
        setBorder(BorderFactory.createLineBorder(Prefs.getTextTheme(), 1));
        add(new CLabel(text, CLabel.CENTER));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(Prefs.getTheme2().brighter());
                isMouseOver = true;
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(Prefs.getTheme2());
                setBorder(BorderFactory.createLineBorder(Prefs.getTextTheme(), 1));
                isMouseOver = false;
            }
        });
    }
    public boolean isMouseOver() {
        return isMouseOver;
    }
}