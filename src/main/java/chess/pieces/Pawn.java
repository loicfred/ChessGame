package chess.pieces;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static chess.window.maingame.GameMenu.*;

public class Pawn extends Piece {

    public boolean canMoveTwice = true;
    public boolean enPassantVulnerable = false;

    public Pawn(Color color, Side side) {
        super(color, side);
    }

    public void setCanMoveTwice(boolean canMoveTwice) {
        this.canMoveTwice = canMoveTwice;
    }

    public List<Integer[]> getPlaceableAreas() {
        List<Integer[]> L = new ArrayList<>();
        int X = getX();
        int Y = getY();
        if (getSide().equals(Side.Top)) {
            if (Board.Grid[X][Y+1].getPiece() == null) {
                Integer[] I = {X, Y + 1};
                L.add(I);
                if (canMoveTwice && Y+2 < 8) {
                    if (Board.Grid[X][Y+2].getPiece() == null) {
                        Integer[] I2 = {X, Y + 2};
                        L.add(I2);
                    }
                }
            }
            if (X+1 < 8 && Board.Grid[X + 1][Y + 1].getPiece() != null && Board.Grid[X + 1][Y + 1].getPiece().getSide() != getSide()) {
                Integer[] I = {X + 1, Y + 1};
                L.add(I);
            }
            if (X-1 >= 0 && Board.Grid[X - 1][Y + 1].getPiece() != null && Board.Grid[X - 1][Y + 1].getPiece().getSide() != getSide()) {
                Integer[] I = {X - 1, Y + 1};
                L.add(I);
            }
            if (X+1 < 8) {
                Piece Neighbor = Board.Grid[X + 1][Y].getPiece();
                if (Neighbor instanceof Pawn P && P.enPassantVulnerable) {
                    Integer[] I = {X + 1, Y + 1};
                    L.add(I);
                }
            }
            if (X-1 >= 0) {
                Piece Neighbor = Board.Grid[X - 1][Y].getPiece();
                if (Neighbor instanceof Pawn P && P.enPassantVulnerable) {
                    Integer[] I = {X - 1, Y + 1};
                    L.add(I);
                }
            }
        } else if (getSide().equals(Side.Bottom)) {
            if (Board.Grid[X][Y-1].getPiece() == null) {
                Integer[] I = {X, Y - 1};
                L.add(I);
                if (canMoveTwice && Y-2 > 0) {
                    if (Board.Grid[X][Y-2].getPiece() == null) {
                        Integer[] I2 = {X, Y - 2};
                        L.add(I2);
                    }
                }
            }
            if (X+1 < 8 && Board.Grid[X + 1][Y - 1].getPiece() != null && Board.Grid[X + 1][Y - 1].getPiece().getSide() != getSide()) {
                Integer[] I = {X + 1, Y - 1};
                L.add(I);
            }
            if (X-1 >= 0 && Board.Grid[X - 1][Y - 1].getPiece() != null && Board.Grid[X - 1][Y - 1].getPiece().getSide() != getSide()) {
                Integer[] I = {X - 1, Y - 1};
                L.add(I);
            }
            if (X+1 < 8) {
                Piece Neighbor = Board.Grid[X + 1][Y].getPiece();
                if (Neighbor instanceof Pawn P && P.enPassantVulnerable) {
                    Integer[] I = {X + 1, Y - 1};
                    L.add(I);
                }
            }
            if (X-1 >= 0) {
                Piece Neighbor = Board.Grid[X - 1][Y].getPiece();
                if (Neighbor instanceof Pawn P && P.enPassantVulnerable) {
                    Integer[] I = {X - 1, Y - 1};
                    L.add(I);
                }
            }
        }
        return L;
    }

    @Override
    public String toString() {
        return "Pawn";
    }
}
