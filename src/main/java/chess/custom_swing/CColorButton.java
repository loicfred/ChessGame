package chess.custom_swing;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CColorButton extends JPanel {
    private Color color;

    public CColorButton(Color col) {
        this.color = col;
        setBackground(color);
        setBorder(new LineBorder(Color.BLACK, 1));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Color newColor = JColorChooser.showDialog(null, "Choose a color: ", color);
                if (newColor != null) color = newColor;
                setBackground(color);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(color.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(color);
            }
        });
    }
    public Color getColor() {
        return color;
    }
}
