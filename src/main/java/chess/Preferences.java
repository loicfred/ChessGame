package chess;

import chess.pieces.Side;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Preferences {

    // attributes of preferences
    private String GridColor1 = "#FFFFFF";
    private String GridColor2 = "#606060";
    private String PlaceableAreaColor = "#00FF00";
    private String UnplaceableAreaColor = "#FF0000";
    private String Language = "English";
    private String Theme = "#EEEEEE";
    private String Theme2 = "#CCCCCC";
    private String TextColor = "#000000";

    private String P1Side = Side.Bottom.toString();
    private String Difficulty = "Normal";


    // this static function reads a preference file and creates a new preferences object.
    // it uses a scanner to read the file line by line and a try-with-resources to automatically close the scanner
    // once it exits the try-catch block.
    public static Preferences ReadPreferences() {
        try (Scanner scan = new Scanner(new File("Preferences.txt"), StandardCharsets.UTF_8)) {
            Preferences P = new Preferences();
            P.GridColor1 = scan.nextLine();
            P.GridColor2 = scan.nextLine();
            P.PlaceableAreaColor = scan.nextLine();
            P.UnplaceableAreaColor = scan.nextLine();
            P.Language = scan.nextLine();
            P.Theme = scan.nextLine();
            P.Theme2 = scan.nextLine();
            P.TextColor = scan.nextLine();
            P.P1Side = scan.nextLine();
            P.Difficulty = scan.nextLine();
            return P;
        } catch (Exception e) {
            // if no preferences file is found, create a new one.
            return new Preferences().Save();
        }
    }

    // this function saves the current preferences object into a file.
    // it uses a file writer and print writer to write the file line by line and a try-with-resources to automatically close them
    // once it exits the try-catch block.
    public Preferences Save() {
        try (FileWriter FW = new FileWriter("Preferences.txt", StandardCharsets.UTF_8);
             PrintWriter PW = new PrintWriter(FW)) {
            PW.println(GridColor1);
            PW.println(GridColor2);
            PW.println(PlaceableAreaColor);
            PW.println(UnplaceableAreaColor);
            PW.println(Language);
            PW.println(Theme);
            PW.println(Theme2);
            PW.println(TextColor);
            PW.println(P1Side);
            PW.println(Difficulty);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to save the preferences file.");
        }
        return this;
    }

    public Preferences() {}

    public Color getGridColor1() {
        return Color.decode(GridColor1);
    }

    public Color getGridColor2() {
        return Color.decode(GridColor2);
    }

    public Color getPlaceableAreaColor() {
        return Color.decode(PlaceableAreaColor);
    }

    public Color getUnplaceableAreaColor() {
        return Color.decode(UnplaceableAreaColor);
    }

    public String getLanguage() {
        return Language;
    }

    public Color getTheme() {
        return Color.decode(Theme);
    }
    public Color getTheme2() {
        return Color.decode(Theme2);
    }
    public Color getTextTheme() {
        return Color.decode(TextColor);
    }

    public Side getP1Side() {
        return Side.valueOf(P1Side);
    }
    public Integer getDifficultyLevel() {
        if (Difficulty.equals("Easy")) {
            return 3;
        } else if (Difficulty.equals("Normal")) {
            return 2;
        } else { // Hard
            return 1;
        }
    }
    public String getDifficulty() {
        return Difficulty;
    }

    public void setGridColor1(String color) {
        GridColor1 = color;
        Save();
    }

    public void setGridColor2(String color) {
        GridColor2 = color;
        Save();
    }
    public void setPlaceableAreaColor(String color) {
        PlaceableAreaColor = color;
        Save();
    }
    public void setUnplaceableAreaColor(String color) {
        UnplaceableAreaColor = color;
        Save();
    }

    public void setLanguage(String language) {
        Language = language;
        Save();
    }

    public void setTheme(String theme) {
        Theme = theme;
        Save();
    }

    public void setTheme2(String theme2) {
        Theme2 = theme2;
        Save();
    }

    public void setTextColor(String textColor) {
        TextColor = textColor;
        Save();
    }

    public void setP1Side(String p1Side) {
        P1Side = p1Side;
    }

    public void setDifficulty(String difficulty) {
        Difficulty = difficulty;
    }
}
