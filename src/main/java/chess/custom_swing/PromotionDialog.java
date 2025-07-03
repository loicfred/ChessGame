package chess.custom_swing;

import chess.pieces.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import static chess.Main.Prefs;

public class PromotionDialog extends JDialog {

    // used for moving the custom JDialog
    private Component currentlyClickedComponent;
    private int mouseX, mouseY;

    private final JComboBox<String> comboBox;

    // enabling Modal true allows the dialog to stay open as long as it isn't disposed
    // so it can also be used to return a value.
    private PromotionDialog(JFrame parent) {
        super(parent, "Pawn Promotion", true);
        setBackground(Prefs.getTheme());
        setUndecorated(true);
        setResizable(true);
        setSize(200,120);
        // the title bar
        CTitleBar1 TitleBar = new CTitleBar1("Pawn Promotion");

        // creating combo box used to store choice of promotion
        String[] promotions = {"-", "Queen", "Knight", "Bishop", "Rook"};
        comboBox = new JComboBox<>(promotions);
        CPanel P = new CPanel();
        P.setLayout(new GridLayout(2,1));
        P.add(new CLabel("The pawn will be promoted to:"));
        P.add(comboBox);
        P.setBorder(new EmptyBorder(10,10,10,10));

        // dispose the combo box once we select a valid value.
        comboBox.addItemListener(e -> {
            if (!e.getItem().toString().equals("-")) dispose();
        });


        // the main panel containing all items of the window
        CPanel MainPanel = new CPanel();
        MainPanel.setLayout(new BorderLayout());
        MainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        MainPanel.setBorder(new LineBorder(Prefs.getTheme2(), 3));
        MainPanel.add(TitleBar, BorderLayout.NORTH);
        MainPanel.add(P, BorderLayout.CENTER);

        // logic to be able to move the custom the window using the custom title bar.
        // it listens when you click the main panel, and checks if you are pressing the title bar
        MainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                currentlyClickedComponent = SwingUtilities.getDeepestComponentAt(MainPanel, e.getX(), e.getY());
                if (TitleBar.equals(currentlyClickedComponent) || TitleBar.getAppTitle().equals(currentlyClickedComponent)) {
                    mouseX = e.getX(); mouseY = e.getY();
                }
            }
        });
        MainPanel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (TitleBar.equals(currentlyClickedComponent) || TitleBar.getAppTitle().equals(currentlyClickedComponent)) {
                    setLocation(getX() + e.getX() - mouseX, getY() + e.getY() - mouseY);
                }
            }
        });

        // button to close the window and exit the program from custom title bar
        TitleBar.getCloseButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                JOptionPane.showMessageDialog(null, "Please select a promotion.");
            }
        });
        add(MainPanel);
        setVisible(true);
    }

    // used to call the modal dialog and return the chess piece based on the selected promotion
    public static Piece showPromotionDialog(JFrame parent, Pawn pawn) {
        return switch (new PromotionDialog(parent).comboBox.getSelectedItem().toString()) {
            case "Queen" -> new Queen(pawn.getColor(), pawn.getSide());
            case "Rook" -> new Rook(pawn.getColor(), pawn.getSide());
            case "Knight" -> new Knight(pawn.getColor(), pawn.getSide());
            case "Bishop" -> new Bishop(pawn.getColor(), pawn.getSide());
            default -> throw new IllegalStateException("Unexpected value");
        };
    }

}
