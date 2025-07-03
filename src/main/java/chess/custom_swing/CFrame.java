package chess.custom_swing;

import chess.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

import static chess.Main.Prefs;

public class CFrame extends JFrame {
    // used for moving the custom JPanel
    public Component currentlyClickedComponent;
    public int mouseX, mouseY;

    public CFrame(String title) {
        super(title);
        setBackground(Prefs.getTheme());
        setUndecorated(true);
        setResizable(false);
        try (InputStream S = Main.class.getResourceAsStream("/img/icon.png")) {
            if (S != null) {
                setIconImage(ImageIO.read(S));
            }
        } catch (Exception ignored) {}
    }
}
