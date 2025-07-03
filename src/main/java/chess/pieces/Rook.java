package chess.pieces;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static chess.window.maingame.GameMenu.Board;

public class Rook extends Piece {

    private boolean hasMoved = false;

    public Rook(Color color, Side side) {
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
        int X = getX();
        int Y = getY();
        for (int index = X; index < 8 ; index++) {
            if (Board.Grid[index][Y].getPiece() == this) continue;
            if (Board.Grid[index][Y].getPiece() != null && Board.Grid[index][Y].getPiece().getSide() == getSide()) break;
            Integer[] l = {index, Y};
            list.add(l);
            if (Board.Grid[index][Y].getPiece() != null && Board.Grid[index][Y].getPiece().getSide() != getSide()) break;
        }
        for (int index = X; index >= 0 ; index--) {
            if (Board.Grid[index][Y].getPiece() == this) continue;
            if (Board.Grid[index][Y].getPiece() != null && Board.Grid[index][Y].getPiece().getSide() == getSide()) break;
            Integer[] l = {index, Y};
            list.add(l);
            if (Board.Grid[index][Y].getPiece() != null && Board.Grid[index][Y].getPiece().getSide() != getSide()) break;
        }
        for (int index = Y; index < 8; index++) {
            if (Board.Grid[X][index].getPiece() == this) continue;
            if (Board.Grid[X][index].getPiece() != null && Board.Grid[X][index].getPiece().getSide() == getSide()) break;
            Integer[] l = {X, index};
            list.add(l);
            if (Board.Grid[X][index].getPiece() != null && Board.Grid[X][index].getPiece().getSide() != getSide()) break;
        }
        for (int index = Y; index >= 0; index--) {
            if (Board.Grid[X][index].getPiece() == this) continue;
            if (Board.Grid[X][index].getPiece() != null && Board.Grid[X][index].getPiece().getSide() == getSide()) break;
            Integer[] l = {X, index};
            list.add(l);
            if (Board.Grid[X][index].getPiece() != null && Board.Grid[X][index].getPiece().getSide() != getSide()) break;
        }
        return list;
    }

    @Override
    public String toString() {
        return "Rook";
    }
}
