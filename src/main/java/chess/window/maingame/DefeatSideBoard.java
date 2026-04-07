package chess.window.maingame;

import chess.custom_swing.CPanel;
import chess.pieces.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static chess.Main.Prefs;

@SuppressWarnings("all")
public class DefeatSideBoard extends CPanel {
    protected final DefeatGrid[][] Grid = new DefeatGrid[2][8];

    // declaring the empty grids.
    public DefeatSideBoard() {
        setBorder(BorderFactory.createLineBorder(Prefs.getTheme2(),5));
        setLayout(new GridLayout(8,2));
        for (int X = 0; X < 2; X++) {
            for (int Y = 0; Y < 8; Y++) {
                Grid[X][Y] = new DefeatGrid(X, Y);
                add(Grid[X][Y]);
            }
        }
    }

    // add a new piece to the grids while ensuring they are placed in order.
    public boolean AddNewDefeatedPiece(Piece piece) {
        for (int X = 0; X < 2; X++) {
            for (int Y = 0; Y < 8; Y++) {
                if (Grid[X][Y].getPiece() == null) {
                    Grid[X][Y].setPiece(piece);
                    return true;
                }
            }
        }
        return false;
    }

    public void reset() {
        for (int X = 0; X < 2; X++) {
            for (int Y = 0; Y < 8; Y++) {
                Grid[X][Y].setPiece(null);
            }
        }
    }


    public static class DefeatGrid extends JPanel {
        private Piece piece;
        private int X;
        private int Y;

        // declaring the grid with its coordinates
        public DefeatGrid(int X, int Y) {
            this.X = X;
            this.Y = Y;
            setBackground(Prefs.getTheme().darker());
        }

        public Piece getPiece() {
            return piece;
        }
        public void setPiece(Piece piece) {
            this.piece = piece;
            if (piece != null) {
                this.piece.setCoordinates(X, Y);
            }
            super.repaint();
        }
        // similar to the chessboard, with fewer details as it has no interaction.
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            try {
                if (piece != null) {
                    String color = piece.getColorString();
                    String piecename = piece.getClass().getSimpleName();
                    Image I = ImageIO.read(Chessboard.class.getClassLoader().getResourceAsStream("img/" + color + "/" + piecename + ".png"));
                    g.drawImage(I,0,0,75,75, null);
                }
            } catch (IOException ignored) {}
            g.dispose();
        }
    }
}
