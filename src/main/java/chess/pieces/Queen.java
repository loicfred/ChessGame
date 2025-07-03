package chess.pieces;

import chess.window.maingame.Chessboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static chess.window.maingame.GameMenu.Board;

public class Queen extends Piece {

    public Queen(Color color, Side side) {
        super(color, side);
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
        for (int x = X,y = Y; x >= 0 && y >= 0; x--,y--) {
            if (Board.Grid[x][y].getPiece() == this) continue;
            if (Board.Grid[x][y].getPiece() != null && Board.Grid[x][y].getPiece().getSide() == getSide()) break;
            Integer[] l = {x, y};
            list.add(l);
            if (Board.Grid[x][y].getPiece() != null && Board.Grid[x][y].getPiece().getSide() != getSide()) break;
        }
        for (int x = X,y = Y; x <= 7 && y <= 7; x++,y++) {
            if (Board.Grid[x][y].getPiece() == this) continue;
            if (Board.Grid[x][y].getPiece() != null && Board.Grid[x][y].getPiece().getSide() == getSide()) break;
            Integer[] l = {x, y};
            list.add(l);
            if (Board.Grid[x][y].getPiece() != null && Board.Grid[x][y].getPiece().getSide() != getSide()) break;
        }
        for (int x = X,y = Y; x >= 0 && y <= 7; x--,y++) {
            if (Board.Grid[x][y].getPiece() == this) continue;
            if (Board.Grid[x][y].getPiece() != null && Board.Grid[x][y].getPiece().getSide() == getSide()) break;
            Integer[] l = {x, y};
            list.add(l);
            if (Board.Grid[x][y].getPiece() != null && Board.Grid[x][y].getPiece().getSide() != getSide()) break;
        }
        for (int x = X,y = Y; x <= 7 && y >= 0; x++,y--) {
            if (Board.Grid[x][y].getPiece() == this) continue;
            if (Board.Grid[x][y].getPiece() != null && Board.Grid[x][y].getPiece().getSide() == getSide()) break;
            Integer[] l = {x, y};
            list.add(l);
            if (Board.Grid[x][y].getPiece() != null && Board.Grid[x][y].getPiece().getSide() != getSide()) break;
        }
        return list;
    }

    @Override
    public String toString() {
        return "Queen";
    }
}
