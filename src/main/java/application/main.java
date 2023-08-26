package application;

import util.SongPlayer;

public class main {
    public static void main(String[] args) {
        Gui window = new Gui();
        window.createWindow();

        SongPlayer.close();
    }
}