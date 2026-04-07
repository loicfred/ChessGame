package chess.window.maingame;

import chess.custom_swing.*;
import chess.pieces.Pawn;
import chess.pieces.Piece;
import chess.pieces.Side;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

import static chess.Main.Prefs;

@SuppressWarnings("all")
public class GameMenu extends CFrame {

    // an array of the 101 states of the game and the value representing the current turn of the game.
    // the turn is also used as index to select a states and there is a maximum of 101 states possible.
    // which means a maximum of 100 turns (excluding game start) before the game turn into a draw.
    public static GameState[] states = new GameState[500];
    public static int Move = 0;

    public static Side P1 = Side.Bottom;
    public static boolean hasAI = false;

    // the active board and defeated pieces side board.
    public static Chessboard Board;
    public static DefeatSideBoard TopSide;
    public static DefeatSideBoard BottomSide;


    private static CLabel moveCounterLabel;
    private static CLabel Description;

    public GameMenu(Side p1, boolean ai) {
        super(ai ? "Singleplayer - Chess Game" : "Multiplayer - Chess Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 775);
        P1 = p1;
        hasAI = ai;

        CPanel MainPanel = new CPanel();
        MainPanel.setLayout(new BorderLayout());
        MainPanel.setBorder(new LineBorder(Prefs.getTheme2(), 3));

        Move = 0;
        Board = new Chessboard();
        Board.setBounds(5, 5, 75 * 8, 75 * 8);
        TopSide = new DefeatSideBoard();
        TopSide.setBounds(625, 5, 75 * 2, 75 * 4);
        BottomSide = new DefeatSideBoard();
        BottomSide.setBounds(625, 305, 75 * 2, 75 * 4);
        states[Move] = new GameState(Board, TopSide, BottomSide);

        CPanel Top = new CPanel();
        Top.setLayout(null);
        Top.add(Board);
        Top.add(TopSide);
        Top.add(BottomSide);

        // Bottom Panel
        CPanel Bottom = new CPanel();
        Bottom.setLayout(new GridLayout(3, 1)); // 3 rows (Description, Move Counter, Buttons)
        Bottom.setBorder(new EmptyBorder(0,10,10,10));
        // Game Status Text
        Description = new CLabel("The game starts now! " + (P1 == Side.Bottom ? "White" : "Black") +" may start.");
        Description.setFont(new Font("Arial", Font.BOLD, 24));
        Description.setHorizontalAlignment(SwingConstants.CENTER);
        Bottom.add(Description);

        // Move Counter
        moveCounterLabel = new CLabel("Moves: 0");
        moveCounterLabel.setFont(new Font("Arial", Font.BOLD, 18));
        moveCounterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        Bottom.add(moveCounterLabel);

        // Buttons Panel
        CButton Restart = new CButton("Restart");
        Restart.setPreferredSize(new Dimension(100, 30));
        CButton Rollback = new CButton("Rollback");
        Rollback.setPreferredSize(new Dimension(100, 30));
        CPanel Buttons = new CPanel();
        Buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5)); // Centered with spacing
        Buttons.add(Restart);
        Buttons.add(Rollback);
        Bottom.add(Buttons);

        // reset the chessboard like the beginning
        Restart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Board.reset();
            }
        });

        // returns back to the previous game state, which means the previous turn.
        Rollback.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (Move > 0) {
                    LoadGameState(states[--Move]);
                    if (hasAI) LoadGameState(states[--Move]); // if AI is enabled, rollback twice.
                    moveCounterLabel.setText("Moves: " + Move);
                }
            }
        });

        // Custom Title Bar and Mouse Drag Support
        CTitleBar2 TitleBar = new CTitleBar2(this, getTitle());

        // the logic to be able to move the custom the window using the custom title bar.
        // it listens when you click the main panel, and checks if you are pressing the title bar
        MainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                currentlyClickedComponent = SwingUtilities.getDeepestComponentAt(MainPanel, e.getX(), e.getY());
                if (TitleBar.equals(currentlyClickedComponent) || TitleBar.getAppTitle().equals(currentlyClickedComponent)) {
                    mouseX = e.getX(); mouseY = e.getY();
                }
            }
        });
        MainPanel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (TitleBar.equals(currentlyClickedComponent) || TitleBar.getAppTitle().equals(currentlyClickedComponent)) {
                    setLocation(getX() + e.getX() - mouseX, getY() + e.getY() - mouseY);
                }
            }
        });

        // button to close the window and exit the application from custom title bar
        TitleBar.getCloseButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                System.exit(0);
            }
        });

        MainPanel.add(TitleBar, BorderLayout.NORTH);
        MainPanel.add(Top, BorderLayout.CENTER);
        MainPanel.add(Bottom, BorderLayout.SOUTH);
        add(MainPanel);
        setVisible(false);
    }

    // save game state, such as turn and piece positions
    // which can be used for rollback later.
    public static void SaveGameState() {
        if (Move < 500) {
            states[++Move] = new GameState(Board, TopSide, BottomSide);
            moveCounterLabel.setText("Moves: " + Move);
        }
    }

    // load this game state, which means it will place all the pieces
    // from this game state into the current state.
    public void LoadGameState(GameState GS) {
        Board.Turn = GS.Turn;
        Board.selectedGrid = null;
        // assigning pieces
        for (int X = 0; X < 8; X++) {
            for (int Y = 0; Y < 8; Y++) {
                Board.Grid[X][Y].setPlaceable(false);
                Board.Grid[X][Y].savePiece(GS.Pieces[X][Y]);
                Piece Pc = Board.Grid[X][Y].getPiece();
                if (Pc instanceof Pawn P) {
                    if (P.getSide().equals(Side.Top) && P.getY() == 1) {
                        P.setCanMoveTwice(true);
                    } else if (P.getSide().equals(Side.Bottom) && P.getY() == 6) {
                        P.setCanMoveTwice(true);
                    }
                }
            }
        }
        // assigning defeated pieces
        for (int X = 0; X < 2; X++) {
            for (int Y = 0; Y < 8; Y++) {
                TopSide.Grid[X][Y].setPiece(GS.DefeatedPieces1[X][Y]);
                BottomSide.Grid[X][Y].setPiece(GS.DefeatedPieces2[X][Y]);
            }
        }
    }

    public static void UpdateGameStatus(String text) {
        Description.setText(text);
    }
}