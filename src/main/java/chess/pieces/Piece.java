package chess.pieces;

import java.awt.*;
import java.util.List;

import static chess.window.maingame.GameMenu.Board;

// this is the abstract class used for all the chess peices of the game
// all the chess piece will inherit from this class and use its attributes
public abstract class Piece {
    private final Color color;
    private final Side side;
    private int X;
    private int Y;

    // the piece class access modifier is protected as it has no need to be declared outside of this package (chess.pieces.*;)
    protected Piece(Color color, Side side) {
        this.color = color;
        this.side = side;
    }

    // used to get on which side the piece is (top or bottom)
    // used to know the allowed directions of movement
    public Side getSide() {
        return side;
    }
    // used to get the color of the piece.
    public Color getColor() {
        return color;
    }
    // get the color name as string
    public String getColorString() {
        return getColor() == Color.WHITE ? "White" : "Black";
    }


    // get the side of the opponent
    public Side getOpposingSide() {
        return side == Side.Top ? Side.Bottom : Side.Top;
    }
    // get the color of the opponent
    public String getOpposingColorString() {
        return getColor() == Color.WHITE ? "Black" : "White";
    }
    // modifies the coordinate on the grid.
    public void setCoordinates(int X, int Y) {
        this.X = X;
        this.Y = Y;
    }

    // this function is abstract as all the chess pieces require it.
    // this function is used to get the coordinates of the grid squares where the piece can be moved to.
    public abstract List<Integer[]> getPlaceableAreas();

    // check if moving to this coordinate put your king at risk by simulating a move.
    public boolean isPuttingKingAtRisk(int X, int Y) {
        Piece oldPiece = Board.Grid[X][Y].getPiece();
        King k = Board.getKing(getSide());
        Board.Grid[this.X][this.Y].setPiece(null);
        Board.Grid[X][Y].setPiece(this);
        boolean isCheck = k.isInCheck(k.getX(), k.getY());
        Board.Grid[this.X][this.Y].setPiece(this);
        Board.Grid[X][Y].setPiece(oldPiece);
        return isCheck;
    }


    public int getX() {
        return X;
    }
    public int getY() {
        return Y;
    }
}