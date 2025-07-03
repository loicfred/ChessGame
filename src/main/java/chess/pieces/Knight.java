package chess.pieces;

import chess.window.maingame.Chessboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static chess.window.maingame.GameMenu.Board;

public class Knight extends Piece {

    public Knight(Color color, Side side) {
        super(color, side);
    }

    public List<Integer[]> getPlaceableAreas() {
        List<Integer[]> list = new ArrayList<>();
        int[][] PossibleAreas = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};
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
        return list;
    }

    @Override
    public String toString() {
        return "Knight";
    }
}
