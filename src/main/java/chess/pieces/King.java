package chess.pieces;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static chess.window.maingame.GameMenu.Board;

public class King extends Piece {

    private boolean hasMoved = false;

    public King(Color color, Side side) {
        super(color, side);
    }

    public boolean hasMoved() {
        return hasMoved;
    }
    public void setMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public List<Integer[]> getPlaceableAreas() {
        List<Integer[]> list = new ArrayList<>();
        int[][] PossibleAreas = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        for (int[] coordinate : PossibleAreas) {
            int newX = getX() + coordinate[0];
            int newY = getY() + coordinate[1];
            if (newX >= 0 && newX <= 7 && newY >= 0 && newY <= 7) {
                if (Board.Grid[newX][newY].getPiece() == null || Board.Grid[newX][newY].getPiece().getSide() != getSide()) {
                    Integer[] position = {newX, newY};
                    list.add(position);
                }
            }
        }
        list.addAll(getCastlingPlaceableAreas());
        return list;
    }
    // get the bonus squares the king can move when castling.
    public List<Integer[]> getCastlingPlaceableAreas() {
        List<Integer[]> list = new ArrayList<>();
        int X = getX();
        int Y = getY();
        if (!hasMoved()) {
            if (canCastleLeft()) {
                Integer[] position = {X - 2, Y};
                list.add(position);
            }
            if (canCastleRight()) {
                Integer[] position = {X + 2, Y};
                list.add(position);
                Integer[] position2 = {X + 3, Y};
                list.add(position2);
            }
        }
        return list;
    }

    // used to check if the king can move to safe squares.
    // if he has no place to go, returns true else false.
    public boolean cannotMoveDueToCheck() {
        List<Integer[]> moves = getPlaceableAreas();
        if (moves.isEmpty()) return false;
        int X = getX();
        int Y = getY();
        for (Integer[] move : moves) {
            Piece oldpiece = Board.Grid[move[0]][move[1]].getPiece();
            Board.Grid[X][Y].setPiece(null);
            Board.Grid[move[0]][move[1]].setPiece(this);
            if (!isInCheck(move[0], move[1])) {
                Board.Grid[X][Y].setPiece(this);
                Board.Grid[move[0]][move[1]].setPiece(oldpiece);
                return false;
            }
            Board.Grid[X][Y].setPiece(this);
            Board.Grid[move[0]][move[1]].setPiece(oldpiece);
        }
        return true;
    }

    // if the king cannot move and is in check, he is checkmate
    public boolean isCheckmate() {
        return cannotMoveDueToCheck() && isInCheck(this.getX(), this.getY()) && otherPiecesUnableToMove();
    }
    // if the king cannot move, but he is not in check, he is stalemate
    public boolean isStalemate() {
        return cannotMoveDueToCheck() && !isInCheck(this.getX(), this.getY()) && otherPiecesUnableToMove();
    }


    // check if other pieces of the king's team can move without risking the king's life or not
    public boolean otherPiecesUnableToMove() {
        for (Piece P : Board.getPieces(getSide())) {
            if (!(P instanceof King)) {
                for (Integer[] I : P.getPlaceableAreas()) {
                    if (!P.isPuttingKingAtRisk(I[0], I[1])) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // verifies if the king is in check at this position
    public boolean isInCheck(int X, int Y) {
        List<Piece> opposingPieces = Board.getPieces(getOpposingSide());
        // Check if any opposing piece can move to this X, Y location.
        for (Piece opp : opposingPieces) {
            for (Integer[] oppMove : opp.getPlaceableAreas()) {
                if (oppMove[0] == X && oppMove[1] == Y) {
                    return true;
                }
            }
        }
        return false;
    }

    // verify if can castle to the right
    public boolean canCastleRight() {
        int Y = getY();
        return (Board.Grid[4][Y].getPiece() == null && Board.Grid[5][Y].getPiece() == null && Board.Grid[6][Y].getPiece() == null && Board.Grid[7][Y].getPiece() instanceof Rook R && !R.hasMoved());
    }
    // verify if can castle to the left
    public boolean canCastleLeft() {
        int Y = getY();
        return (Board.Grid[2][Y].getPiece() == null && Board.Grid[1][Y].getPiece() == null && Board.Grid[0][Y].getPiece() instanceof Rook R2 && !R2.hasMoved());
    }

    @Override
    public String toString() {
        return "King";
    }
}
