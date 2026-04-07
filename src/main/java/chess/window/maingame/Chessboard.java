package chess.window.maingame;

import chess.custom_swing.CPanel;
import chess.pieces.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static chess.Main.Prefs;
import static chess.Utils.GenerateRandomNumber;
import static chess.window.maingame.GameMenu.*;

@SuppressWarnings("all")
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
}