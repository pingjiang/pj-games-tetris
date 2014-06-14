// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MainMenuState.java

package org.pj.games.tetris;

import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.awt.Color;
import java.io.IOException;
import java.util.Date;

/**
 * 主菜单
 */
public class MainMenuState extends BasicGameState implements InputListener {
    private int stateID;
    private Image imBackground;
    private Image imStart;
    private Image imExit;
    private UnicodeFont font;
    private static final int startX = 410;
    private static final int startY = 160;
    private static final int exitX = 410;
    private static final int exitY = 240;
    private float imStartScale;
    private float imExitScale;
    private float scaleStep;
    private boolean splashPlayed;

    public MainMenuState(int stateID) {
        this.stateID = -1;
        imBackground = null;
        imStart = null;
        imExit = null;
        font = null;
        imStartScale = 1.0F;
        imExitScale = 1.0F;
        scaleStep = 0.0001F;
        splashPlayed = false;
        this.stateID = stateID;
    }

    public int getID() {
        return stateID;
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        super.mouseMoved(oldx, oldy, newx, newy);
    }

    public void init(GameContainer container, StateBasedGame game)
            throws SlickException {
        imBackground = ResourceManager.getImage("MAIN_MENU");
        imStart = ResourceManager.getImage("START_GAME");
        imExit = ResourceManager.getImage("EXIT");
        font = new UnicodeFont("res/fonts/ITCKRIST.ttf", 20, true, false);
        font.addAsciiGlyphs();
        font.getEffects().add(new ColorEffect(Color.white));
        font.addAsciiGlyphs();
        font.loadGlyphs();
    }

    public void enter(GameContainer container, StateBasedGame game)
            throws SlickException {
        super.enter(container, game);
        imStartScale = imExitScale = 1.0F;
    }

    /**
     * 渲染当前状态
     *
     * @param container
     * @param game
     * @param g
     * @throws SlickException
     */
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {
        imBackground.draw(0.0F, 0.0F);
        imStart.draw(410F, 160F, imStartScale);
        imExit.draw(410F, 240F, imExitScale);
        org.newdawn.slick.Color c = new org.newdawn.slick.Color(160, 56, 0);
        int y = 270;
        int i = 1;
        for (Integer score : Highscores.getInstance().getScores().keySet()) {
            font.drawString(30F, y, String.valueOf(i), c);
            font.drawString(80F, y, ":", c);
            font.drawString(120F, y, Highscores.getInstance().getScores().get(score), c);
            font.drawString(200F, y, "-", c);
            font.drawString(240F, y, score.toString(), c);
            y += 30;
            i++;
        }
    }

    /**
     * 根据时间来更新屏幕
     *
     * @param gc
     * @param sb
     * @param deltaT
     * @throws SlickException
     */
    public void update(GameContainer gc, StateBasedGame sb, int deltaT)
            throws SlickException {
        if (LoadingList.get().getRemainingResources() > 0) {
            try {
                DeferredResource e = LoadingList.get().getNext();
                System.out.println(new Date() + " INFO:" + e.getDescription());
                e.load();
            } catch (IOException e) {
                throw new SlickException("Error loading resource", e);
            }
        } else {
            if (!splashPlayed) {
                ResourceManager.getSound("GAME_SPLASH").play();
                splashPlayed = true;
            }
            Input input = gc.getInput();
            // 鼠标在图片按钮上点击后就会进入下一个状态
            if (isMouseOverImage(input.getMouseX(), input.getMouseY(), imStart)) {
                if (imStartScale < 1.05F)
                    imStartScale += scaleStep * (float) deltaT;
                if (input.isMouseButtonDown(0)) {
                    ResourceManager.getSound("BUTTON_UP").play();
                    sb.enterState(1);
                }
            } else if (imStartScale > 1.0F)
                imStartScale -= scaleStep * (float) deltaT;
            if (isMouseOverImage(input.getMouseX(), input.getMouseY(), imExit)) {
                if (imExitScale < 1.05F)
                    imExitScale += scaleStep * (float) deltaT;
                if (input.isMouseButtonDown(0))
                    gc.exit();
            } else if (imExitScale > 1.0F)
                imExitScale -= scaleStep * (float) deltaT;
        }
    }

    /**
     * 根据位置判断鼠标时候在图片上
     *
     * @param mx
     * @param my
     * @param im
     * @return
     */
    private boolean isMouseOverImage(float mx, float my, Image im) {
        int x;
        int y;
        if (im == imStart) {
            x = startX;
            y = startY;
        } else {
            x = exitX;
            y = exitY;
        }
        return mx >= (float) x &&
                mx <= (float) (x + im.getWidth()) &&
                my >= (float) y &&
                my <= (float) (y + im.getHeight());
    }
}
