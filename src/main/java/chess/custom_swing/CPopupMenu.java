package chess.custom_swing;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static chess.Main.Prefs;

public class CPopupMenu extends JPopupMenu {
    public boolean isMouseOver = false;

    public CPopupMenu() {
        setBackground(Prefs.getTheme());
        setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isMouseOver = true;
            }
            @Override
            public void mouseExited(MouseEvent e) {
                isMouseOver = false;
            }
        });
    }
}