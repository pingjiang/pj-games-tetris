package org.pj.games.tetris;

import org.newdawn.slick.*;

import java.util.Random;

/**
 * Created by pingjiang on 14-6-14.
 */
public class TetrisGame extends BasicGame {
    public TetrisGame(String gamename) {
        super(gamename);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        g.drawString("Howdy!", 50, 50);

        Random rand = new Random();
        final int size = 40;
        final int gap = 2;
        int rows = (gc.getHeight() - 100) / (size + gap);
        int cols = gc.getWidth() / (size + gap);
        int padWidth = 7;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                g.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
                g.fillRect(padWidth + j * (size + gap), 100 + i * (size + gap), size, size);
            }
        }
    }
}