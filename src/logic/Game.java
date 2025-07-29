package logic;

import file.Sound;

public class Game {
    private static boolean FINISHED, PAUSED, RESTARTING;
    private static int LEVEL, SCORE;

    public static void START() {
        PAUSED = false;
        FINISHED = false;
        Sound.rewindBackgroundSound();
        Sound.playBackgroundSound();
    }

    public static void END() {
        Sound.stopBackgroundSound();
        PAUSED = true;
        FINISHED = true;
    }

    public static void setPAUSED(boolean PAUSED) {
        Game.PAUSED = PAUSED;
    }

    public static boolean isFINISHED() {
        return FINISHED;
    }

    public static boolean isPAUSED() {
        return PAUSED;
    }

    public static boolean isRESTARTING() {
        return RESTARTING;
    }

    public static void setRESTARTING(boolean RESTARTING) {
        Game.RESTARTING = RESTARTING;
    }

    public static int getLEVEL() {
        return LEVEL;
    }

    public static void setLEVEL(int LEVEL) {
        Game.LEVEL = LEVEL;
    }

    public static int getSCORE() {
        return SCORE;
    }

    public static void setSCORE(int SCORE) {
        Game.SCORE = SCORE;
    }
}
