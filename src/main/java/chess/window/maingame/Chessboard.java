package chess.window.maingame;

import chess.custom_swing.CPanel;
import chess.pieces.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static chess.Main.Prefs;
import static chess.Main.GameScreen;
import static chess.custom_swing.PromotionDialog.showPromotionDialog;
import static chess.window.maingame.GameMenu.*;

public class Chessboard extends CPanel {

    public Side Turn = Prefs.getP1Side(); // the current turn (always P1 who start first)
    public GridSquare selectedGrid = null; // store the reference of the currently selected grid
    public GridSquare[][] Grid = new GridSquare[8][8]; // a 2d array with all the grid squares

    public Chessboard() {
        setBorder(BorderFactory.createLineBorder(Color.BLACK,5));
        setLayout(new GridLayout(8,8));

        // create each grid of the chessboard and switch color between each.
        Color color = Prefs.getGridColor1();
        for (int Y = 0; Y < 8; Y++) {
            for (int X = 0; X < 8; X++) {
                Grid[X][Y] = new GridSquare( color, X, Y);
                add(Grid[X][Y]);
                color = (color.getRGB() == Prefs.getGridColor1().getRGB() ? Prefs.getGridColor2() : Prefs.getGridColor1());
            }
            color = (color.getRGB() == Prefs.getGridColor1().getRGB() ? Prefs.getGridColor2() : Prefs.getGridColor1());
        }
        InitializePieces();
    }

    // reset the chessboard totally.
    public void reset() {
        TopSide.reset();
        BottomSide.reset();
        InitializePieces();
        selectedGrid = null;
        GameMenu.Move = -1;
        Turn = Side.Bottom;
        SaveGameState();
        UpdateGameStatus("The game starts now! " + (P1 == Side.Bottom ? "White" : "Black") +" may start.");
    }

    // get all pieces from one team
    public List<Piece> getPieces(Side side) {
        List<Piece> L = new ArrayList<>();
        for (int Y = 0; Y < 8; Y++) {
            for (int X = 0; X < 8; X++) {
                Piece P = Grid[X][Y].getPiece();
                if (P != null && P.getSide().equals(side)) {
                    L.add(P);
                }
            }
        }
        return L;
    }

    // get the king of the request side
    public King getKing(Side side) {
        for (int Y = 0; Y < 8; Y++) {
            for (int X = 0; X < 8; X++) {
                if (Grid[X][Y].getPiece() instanceof King k && k.getSide().equals(side)) {
                    return k;
                }
            }
        }
        return null;
    }

    // empty the boards, then place the chess pieces in appropriate places.
    public void InitializePieces() {
        for (int X = 0; X < 8; X++) {
            for (int Y = 0; Y < 8; Y++) {
                Grid[X][Y].savePiece(null);
                Grid[X][Y].setPlaceable(false);
            }
        }

        Grid[0][0].savePiece(new Rook(Color.BLACK, Side.Top));
        Grid[1][0].savePiece(new Knight(Color.BLACK, Side.Top));
        Grid[2][0].savePiece(new Bishop(Color.BLACK, Side.Top));
        Grid[3][0].savePiece(new King(Color.BLACK, Side.Top));
        Grid[4][0].savePiece(new Queen(Color.BLACK, Side.Top));
        Grid[5][0].savePiece(new Bishop(Color.BLACK, Side.Top));
        Grid[6][0].savePiece(new Knight(Color.BLACK, Side.Top));
        Grid[7][0].savePiece(new Rook(Color.BLACK, Side.Top));
        for (int X = 0; X < 8; X++) {
            Grid[X][1].savePiece(new Pawn(Color.BLACK, Side.Top));
        }
        Grid[0][7].savePiece(new Rook(Color.WHITE, Side.Bottom));
        Grid[1][7].savePiece(new Knight(Color.WHITE, Side.Bottom));
        Grid[2][7].savePiece(new Bishop(Color.WHITE, Side.Bottom));
        Grid[3][7].savePiece(new King(Color.WHITE, Side.Bottom));
        Grid[4][7].savePiece(new Queen(Color.WHITE, Side.Bottom));
        Grid[5][7].savePiece(new Bishop(Color.WHITE, Side.Bottom));
        Grid[6][7].savePiece(new Knight(Color.WHITE, Side.Bottom));
        Grid[7][7].savePiece(new Rook(Color.WHITE, Side.Bottom));
        for (int X = 0; X < 8; X++) {
            Grid[X][6].savePiece(new Pawn(Color.WHITE, Side.Bottom));
        }
    }

    public static class GridSquare extends JPanel {
        // data about the grid such as its coordinates, floor color and piece it contains.
        private boolean isHovered = false;
        private final Color floor;
        private final int X;
        private final int Y;
        private boolean isPlaceable = false;
        private Piece piece;

        // declaring the grid with its coordinates
        public GridSquare(Color floor, int X, int Y) {
            this.floor = floor;
            this.X = X;
            this.Y = Y;
            setBackground(floor);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.getComponent() instanceof GridSquare gs) {
                        // if we selected a grid with a piece before, and then select another grid which is a placeable area.
                        // it will either move the piece, or eliminate an opposing piece.
                        if (Board.selectedGrid != null && Board.selectedGrid.getPiece() != null && gs.isPlaceable) {
                            // removes the highlight of placeable grids
                            for (Integer[] I : Board.selectedGrid.getPiece().getPlaceableAreas()) {
                                Board.Grid[I[0]][I[1]].setPlaceable(false);
                            }
                            // set the currently selected grid's piece onto the new selected grid
                            doCastleIfPossible();
                            gs.movePiece(Board.selectedGrid.getPiece());
                            Board.selectedGrid.movePiece(null);
                            // if it's a pawn, he can't move twice anymore.
                            if (gs.getPiece() instanceof Pawn P) {
                                P.setCanMoveTwice(false);
                            } else if (gs.getPiece() instanceof King K) {
                                K.setMoved(true);
                            } else if (gs.getPiece() instanceof Rook R) {
                                R.setMoved(true);
                            }
                            // change the turn
                            Board.Turn = Board.Turn == Side.Top ? Side.Bottom : Side.Top;
                            if (!isCheckmateStalemate()) {
                                // save current state of the game for rollback
                                SaveGameState();
                                // if the number of game states/turns reaches 60, the game ends in a draw.
                                if (GameMenu.Move >= 500) {
                                    JOptionPane.showMessageDialog(null, "Reached turn limit: Draw");
                                    Board.reset();
                                } else {
                                    UpdateGameStatus("It is now " + gs.getPiece().getOpposingColorString() + "'s turn!");
                                    if (hasAI) {
                                        AI_Turn();
                                        isCheckmateStalemate();
                                    }
                                }
                            }
                        }

                        // if the player didn't select a grid and click a grids which contains a piece.
                        // it will select the piece's grid and highlight the placeable areas of the piece within it.
                        else if (Board.selectedGrid == null || Board.selectedGrid != gs) {
                            if (gs.getPiece() != null) {
                                if (gs.getPiece().getSide().equals(Board.Turn)) {
                                    Board.selectedGrid = gs;
                                    // clear highlighted areas of the grid in case another chess piece was selected before.
                                    for (int Y = 0; Y < 8; Y++) {
                                        for (int X = 0; X < 8; X++) {
                                            Board.Grid[X][Y].setPlaceable(false);
                                        }
                                    }
                                    // highlight the placeable areas of the piece within the grid we select.
                                    for (Integer[] I : Board.selectedGrid.getPiece().getPlaceableAreas()) {
                                        // if is a king, placeable areas are only set to places where he isn't checked.
                                        if (Board.selectedGrid.getPiece() instanceof King k) {
                                            Board.Grid[X][Y].piece = null;
                                            Piece oldPiece = Board.Grid[I[0]][I[1]].piece;
                                            Board.Grid[I[0]][I[1]].piece = k;
                                            if (!k.isInCheck(I[0], I[1])) {
                                                Board.Grid[X][Y].piece = k;
                                                Board.Grid[I[0]][I[1]].piece = oldPiece;
                                                Board.Grid[I[0]][I[1]].setPlaceable(true);
                                            } else {
                                                Board.Grid[X][Y].piece = k;
                                                Board.Grid[I[0]][I[1]].piece = oldPiece;
                                            }
                                        } else {
                                            // if this area doesn't put your king at risk, is placeable
                                            Board.Grid[I[0]][I[1]].setPlaceable(!Board.selectedGrid.getPiece().isPuttingKingAtRisk(I[0], I[1]));
                                        }
                                    }
                                } else {
                                    // if the piece you selected isn't the color of your turn.
                                    JOptionPane.showMessageDialog(null, "This is not your turn!");
                                }
                            } else {
                                // if you select an empty grid.
                                JOptionPane.showMessageDialog(null, "Please, select a chess piece.");
                            }
                        }
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(floor.darker());
                    isHovered = true;
                    repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(floor);
                    isHovered = false;
                    repaint();
                }
            });
        }

        // verify if this position is a castle for King
        // if it is, then moves the rook as necessary.
        public void doCastleIfPossible() {
            if (Board.selectedGrid.getPiece() instanceof King K) {
                if (K.getCastlingPlaceableAreas().stream().anyMatch(i -> i[0] == X || i[1] == Y)) {
                    if (X == 6) {
                        Board.Grid[X - 1][Y].movePiece(Board.Grid[X + 1][Y].getPiece());
                        Board.Grid[X + 1][Y].savePiece(null);
                    } else if (X == 1) {
                        Board.Grid[X + 1][Y].movePiece(Board.Grid[X - 1][Y].getPiece());
                        Board.Grid[X - 1][Y].savePiece(null);
                    }
                }
            }
        }
        public boolean isCheckmateStalemate() {
            King K = Board.getKing(Board.Turn);
            // ends the game in a draw if stalemate
            if (K.isStalemate()) {
                System.out.println(Board.Turn + " " + K.getColorString());
                JOptionPane.showMessageDialog(null, "Stalemate: Draw");
                Board.reset();
                return true;
            }
            // ends the game in a win if checkmate
            else if (K.isCheckmate()) {
                JOptionPane.showMessageDialog(null, "Checkmate: " + K.getOpposingColorString() + " won!");
                Board.reset();
                return true;
            }
            return false;
        }

        public Piece getPiece() {
            return piece;
        }


        public void setPiece(Piece piece) {
            this.piece = piece;
        }
        public void savePiece(Piece piece) {
            setPiece(piece);
            if (piece != null) {
                this.piece.setCoordinates(X, Y);
            }
            super.repaint();
        }
        public void movePiece(Piece piece) {
            // if you move a piece on another piece, it defeats a piece and adds him to the defeat side board.
            if (this.piece != null && piece != null) {
                if (this.piece.getSide().equals(Side.Top)) {
                    TopSide.AddNewDefeatedPiece(this.piece);
                } else  if (this.piece.getSide().equals(Side.Bottom)) {
                    BottomSide.AddNewDefeatedPiece(this.piece);
                }
                if (this.piece instanceof King) {
                    UpdateGameStatus("Congrats! " + piece.getColorString() + " won !");
                }
            }

            // rule verification for pawn
            if (piece instanceof Pawn P) {
                // set the current pawn vulnerable to En passant rule if condition met
                if (X+1 < 8 && Board.Grid[X + 1][Y].getPiece() instanceof Pawn oppPawn && !oppPawn.getSide().equals(P.getSide())
                        || X-1 >= 0 && Board.Grid[X - 1][Y].getPiece() instanceof Pawn oppPawn2 && !oppPawn2.getSide().equals(P.getSide())) {
                    if ((P.getSide().equals(Side.Bottom) && P.getY() == 6 && Y == 4) || (P.getSide().equals(Side.Top) && P.getY() == 1 && Y == 3)) {
                        P.enPassantVulnerable = true;
                    }
                }
                // pawn promotion if they reached the end
                else if (P.getSide().equals(Side.Bottom) && Y == 0 || P.getSide().equals(Side.Top) && Y == 7) {
                    piece = showPromotionDialog(GameScreen, P);
                }
                // eliminate an opponent by En Passant if you moved behind the opponent pawn.
                else {
                    if (P.getSide().equals(Side.Bottom) && Board.Grid[X][Y+1].getPiece() instanceof Pawn oppPawn && !oppPawn.getSide().equals(P.getSide())) {
                        TopSide.AddNewDefeatedPiece(Board.Grid[X][Y+1].getPiece());
                        Board.Grid[X][Y+1].savePiece(null);
                    }
                    else if (P.getSide().equals(Side.Top) && Board.Grid[X][Y-1].getPiece() instanceof Pawn oppPawn && !oppPawn.getSide().equals(P.getSide())) {
                        BottomSide.AddNewDefeatedPiece(Board.Grid[X][Y-1].getPiece());
                        Board.Grid[X][Y-1].savePiece(null);
                    }
                }
            }
            savePiece(piece);
        }
        public void setPlaceable(boolean placeable) {
            this.isPlaceable = placeable && (piece == null || piece.getSide() != Board.selectedGrid.getPiece().getSide());
            super.repaint();
        }


        // paints the chess piece image and background color of the grid square
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            try {
                // determine the color if hovered or not
                g.setColor(isHovered && Board.selectedGrid != null && (getPiece() != null && Board.selectedGrid.getPiece() != null && getPiece().getSide() != Board.selectedGrid.getPiece().getSide()) ? Prefs.getUnplaceableAreaColor() : isHovered ? floor.brighter() : floor);
                g.fillRect(0,0,75,75);
                // determine the color if the grid square is a placeable area for the selected piece.
                if (isPlaceable) {
                    Color col = Prefs.getPlaceableAreaColor();
                    g.setColor(isHovered ? Prefs.getPlaceableAreaColor() : new Color(col.getRed(), col.getGreen(), col.getBlue(), 128));
                    g.fillRect(0,0,75,75);
                }
                // if the grid square has a piece in it, draws it.
                if (piece != null) {
                    String color = piece.getColorString();
                    String piecename = piece.getClass().getSimpleName();
                    // uses inputstream to input the chess piece image from the resources folder.
                    try (InputStream input = Chessboard.class.getClassLoader().getResourceAsStream("img/" + color + "/" + piecename + ".png")) {
                        if (input == null) throw new Exception();
                        Image I = ImageIO.read(input);
                        g.drawImage(I,0,0,75,75, null);
                    } catch (Exception ignored) {
                        g.drawString(color.charAt(0) + piece.toString(),0,0);
                    }
                }
            } catch (Exception ignored) {}
            g.dispose();
        }

    }



    public static boolean AI_Turn() {
        Side opp = P1 == Side.Top ? Side.Bottom : Side.Top;
        List<Piece> oppPieces = Board.getPieces(opp);
        while (true) {
            boolean hasPlaceableArea = false;
            // iterate to see if there are pieces he can take down
            for (Piece P : oppPieces) {
                for (Integer[] oppMove : P.getPlaceableAreas()) {
                    // verify all valid moves
                    if (P instanceof King k) {
                        Board.Grid[P.getX()][P.getY()].piece = null;
                        Piece oldPiece = Board.Grid[oppMove[0]][oppMove[1]].piece;
                        Board.Grid[oppMove[0]][oppMove[1]].piece = k;
                        if (!k.isInCheck(oppMove[0], oppMove[1])) {
                            Board.Grid[P.getX()][P.getY()].piece = k;
                            Board.Grid[oppMove[0]][oppMove[1]].piece = oldPiece;

                            hasPlaceableArea = true;
                            if (Board.Grid[oppMove[0]][oppMove[1]].getPiece() != null && Board.Grid[oppMove[0]][oppMove[1]].getPiece().getSide() != opp) {
                                if (GenerateRandomNumber(1, Prefs.getDifficultyLevel()) == 1) {
                                    Board.Grid[P.getX()][P.getY()].movePiece(null);
                                    Board.Grid[oppMove[0]][oppMove[1]].movePiece(P);
                                    Board.Turn = Board.Turn == Side.Top ? Side.Bottom : Side.Top;
                                    SaveGameState();
                                    UpdateGameStatus("It is now " + P.getOpposingColorString() + "'s turn!");
                                    return true;
                                }
                            }
                        } else {
                            Board.Grid[P.getX()][P.getY()].piece = k;
                            Board.Grid[oppMove[0]][oppMove[1]].piece = oldPiece;
                        }
                    } else if (!P.isPuttingKingAtRisk(oppMove[0], oppMove[1])) {
                        hasPlaceableArea = true;
                        if (Board.Grid[oppMove[0]][oppMove[1]].getPiece() != null && Board.Grid[oppMove[0]][oppMove[1]].getPiece().getSide() != opp) {
                            if (GenerateRandomNumber(1, Prefs.getDifficultyLevel()) == 1) {
                                Board.Grid[P.getX()][P.getY()].movePiece(null);
                                Board.Grid[oppMove[0]][oppMove[1]].movePiece(P);
                                Board.Turn = Board.Turn == Side.Top ? Side.Bottom : Side.Top;
                                SaveGameState();
                                UpdateGameStatus("It is now " + P.getOpposingColorString() + "'s turn!");
                                return true;
                            }
                        }
                    }
                }
            }
            // moves to grid square randomly
            for (Piece P : oppPieces) {
                for (Integer[] oppMove : P.getPlaceableAreas()) {
                    // verify all valid moves
                    if (P instanceof King k) {
                        Board.Grid[P.getX()][P.getY()].piece = null;
                        Piece oldPiece = Board.Grid[oppMove[0]][oppMove[1]].piece;
                        Board.Grid[oppMove[0]][oppMove[1]].piece = k;
                        if (!k.isInCheck(oppMove[0], oppMove[1])) {
                            Board.Grid[P.getX()][P.getY()].piece = k;
                            Board.Grid[oppMove[0]][oppMove[1]].piece = oldPiece;
                            if (GenerateRandomNumber(1, 10) == 1) {
                                Board.Grid[P.getX()][P.getY()].movePiece(null);
                                Board.Grid[oppMove[0]][oppMove[1]].movePiece(P);
                                Board.Turn = Board.Turn == Side.Top ? Side.Bottom : Side.Top;
                                SaveGameState();
                                UpdateGameStatus("It is now " + P.getOpposingColorString() + "'s turn!");
                                return true;
                            }
                        } else {
                            Board.Grid[P.getX()][P.getY()].piece = k;
                            Board.Grid[oppMove[0]][oppMove[1]].piece = oldPiece;
                        }
                    } else if (!P.isPuttingKingAtRisk(oppMove[0], oppMove[1])) {
                        if (GenerateRandomNumber(1, 10 - (P instanceof Pawn ? 9 : 0)) == 1) {
                            Board.Grid[P.getX()][P.getY()].movePiece(null);
                            Board.Grid[oppMove[0]][oppMove[1]].movePiece(P);
                            Board.Turn = Board.Turn == Side.Top ? Side.Bottom : Side.Top;
                            SaveGameState();
                            UpdateGameStatus("It is now " + P.getOpposingColorString() + "'s turn!");
                            return true;
                        }
                    }
                }
            }
            if (!hasPlaceableArea) break;
        }
        return false;
    }
    public static int GenerateRandomNumber(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }
}