package chess.custom_swing;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static chess.Main.Prefs;

public class CMenu extends JPanel {
    private boolean isMouseOver = false;
    public CLabel Text;

    public CMenu(String text) {
        setFocusable(false);
        setBackground(Prefs.getTheme());
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(Prefs.getTheme().brighter());
                isMouseOver = true;
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(Prefs.getTheme());
                isMouseOver = false;
            }
        });
        Text = new CLabel(text);
        Text.setHorizontalAlignment(JLabel.CENTER);
        Text.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        add(Text);
    }
    public boolean isMouseOver() {
        return isMouseOver;
    }
}