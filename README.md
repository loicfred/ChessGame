# ChessGame
This project is a chess game made for my Object Oriented Programming assignment at University.
It is a complete chess game with most of the rules and very customisable colors.

**Applied Skills:**
- Java Swing
- Object-Oriented Programming
- Chess Gameplay Knowledge

## Title Screen
This is the screen that greets us when we start the application.

<img width="250" alt="image" src="https://github.com/user-attachments/assets/ce5e218c-8cf7-4e9f-9df4-92443330e6f8" />

## Singleplayer Mode
Allows the user to play chess against the program (the opponent plays by itself).  
The logic behind it is based on probability, each turn after the user makes a move, the program will iterate between each opposing chess piece and decide with a small percentage if he moves that piece or not, the positions possible to move the piece is also chosen at random, but is a lot more likely to target an opposing chess piece if possible, which increases based on difficulty set.

<div style="display: flex">
  <img width="400" alt="image" src="https://github.com/user-attachments/assets/dcfff576-7806-4015-84ba-f5d98544af52" />
  <img width="400" alt="image" src="https://github.com/user-attachments/assets/c53ff2d8-241b-400f-adae-3c001e88b85d" />
</div>

## Multiplayer Mode
Allows 2 users to play chess against each other on the same device.
It is similar to single player mode except you can also control the opponent.

<img width="400" alt="image" src="https://github.com/user-attachments/assets/c749d824-c8af-4b63-8a07-5765dcfb8efb" />

## Settings
You can also modify various settings such as the UI color, the chessboard color, the difficulty and so on.

<img width="600" alt="image" src="https://github.com/user-attachments/assets/7c031c2f-bb07-478d-9974-9794f51f817c" />
<img width="600" alt="image" src="https://github.com/user-attachments/assets/c906e873-0868-465f-a765-fcd08088bee1" />
<img width="600" alt="image" src="https://github.com/user-attachments/assets/e5d0ac8a-1e13-483a-94d9-36f9504fbbd8" />

### Theme Demonstration

<img width="400" alt="image" src="https://github.com/user-attachments/assets/774b19fe-6ce0-4efa-af3d-18fa981e89ed" />
<img width="400" alt="image" src="https://github.com/user-attachments/assets/f8d48863-6427-4e36-a84f-400535f74d4d" />

## Object Oriented Principles
In this application, each chess piece was an object inheriting from abstract class `Piece.java`, which contains an abstract method `getPlaceableAreas()` determing all locations a piece can move. It also includes basic attributes such as color, coordinates and side.

The coordinates of each piece are determined using mathematical checks, getting the current coordinates of a piece, verifying if his current coordinates in addition to their possible moveable areas are freely, then returning the coordinates as 2-item array of integer and do the rest of the calculations and display on the board.  
This is an example for the `Knight.java` piece.

```java
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
```

### Chessboard Logic
The chessboard includes an 2D-array (8x8) of GridSquare object which can contain a chess piece each.  
The board content is drawn using `Graphics2D` with the chess pieces being in resources folder.  
Clicking a grid calls several complex code lines which handles the grid switch and overall chess rules check for stalemate or checkmate.
Below is an example of all the complex gameplay checks to be done for a Pawn.
```java
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
```

For the King, we need to check if he isn't checkmate in a grid first before allowing placement.
```java
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
```

Chess gameplay is very complex.

## Credits
This screen shows the contributors of the application.

<img width="250" alt="image" src="https://github.com/user-attachments/assets/38d48290-1c7c-4797-9d8e-40f8548ba544" />
