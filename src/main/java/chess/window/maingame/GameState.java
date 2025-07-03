package chess.window.maingame;

import chess.pieces.Piece;
import chess.pieces.Side;

public class GameState {

    public Side Turn;
    public Piece[][] Pieces = new Piece[8][8];
    public Piece[][] DefeatedPieces1 = new Piece[2][8];
    public Piece[][] DefeatedPieces2 = new Piece[2][8];


    // takes the chessboard and defeated pieces side board pieces position
    public GameState(Chessboard board, DefeatSideBoard topSide, DefeatSideBoard bottomSide) {
        Turn = board.Turn;
        for (int X = 0; X < 8; X++) {
            for (int Y = 0; Y < 8; Y++) {
                Pieces[X][Y] = board.Grid[X][Y].getPiece();
            }
        }
        for (int X = 0; X < 2; X++) {
            for (int Y = 0; Y < 8; Y++) {
                DefeatedPieces1[X][Y] = topSide.Grid[X][Y].getPiece();
                DefeatedPieces2[X][Y] = bottomSide.Grid[X][Y].getPiece();
            }
        }
    }
}
