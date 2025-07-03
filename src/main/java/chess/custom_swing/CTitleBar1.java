package chess.custom_swing;

import chess.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

import static chess.Main.Prefs;

public class CTitleBar1 extends CPanel {

    private final CLabel Title;
    private final CMenu Close;

    public CTitleBar1(String title) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(getWidth(), 28));


        // use InputStream to read the icon from the resources folder
        // used a try-with-resources to automatically close the inputstream once it exits
        // the try-catch block.
        JLabel Icon = new JLabel();
        try (InputStream S = Main.class.getResourceAsStream("/img/icon.png")) {
            if (S != null) {
                Icon.setIcon(new ImageIcon(ImageIO.read(S).getScaledInstance(16, 16, 0)));
                Icon.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6)); // Padding
                Icon.setBackground(Color.white);
            }
        } catch (Exception ignored) {}

        // this is the title text of the title bar
        Title = new CLabel(title);
        Title.setForeground(Prefs.getTextTheme().brighter());
        Title.setBorder(BorderFactory.createEmptyBorder(2, 10, 0, 0));
        Title.setFont(new Font("Arial", Font.BOLD, 12));

        // this is the exit button of the title bar used to close the window
        Close = new CMenu("X");
        Close.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        Close.setFocusable(false);

        // a panel to position the icon and window title at the left
        CPanel TopLeft = new CPanel();
        TopLeft.setLayout(new BoxLayout(TopLeft,BoxLayout.X_AXIS));
        TopLeft.setOpaque(false);
        TopLeft.add(Icon);
        TopLeft.add(getAppTitle());

        // a panel to position the exit button to the right
        CPanel TopRight = new CPanel();
        TopRight.setLayout(new GridLayout(1,3));
        TopRight.setOpaque(true);
        TopRight.add(getCloseButton());

        add(TopLeft, BorderLayout.WEST);
        add(TopRight, BorderLayout.EAST);
    }

    public CMenu getCloseButton() {
        return Close;
    }
    public JLabel getAppTitle() {
        return Title;
    }
}
