package chess;

import java.util.Random;

public class Utils {
    public static int GenerateRandomNumber(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }
}
