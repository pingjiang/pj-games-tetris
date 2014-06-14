// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Tetris.java

package org.pj.games.tetris;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

// Referenced classes of package org.pj.games.tetris:
//            MainMenuState, PlayingState, ResourceManager

public class Tetris extends StateBasedGame {
    public static final int MAINMENU_STATE = 0;
    public static final int GAMEPLAY_STATE = 1;

    public Tetris() throws SlickException {
        super("Tetris");
        addState(new MainMenuState(MAINMENU_STATE));
        addState(new PlayingState(GAMEPLAY_STATE));
        ResourceManager.loadResources("res/resources.xml");
        enterState(MAINMENU_STATE);
    }

    public void initStatesList(GameContainer gc)
            throws SlickException {
        getState(MAINMENU_STATE).init(gc, this);
        getState(GAMEPLAY_STATE).init(gc, this);
    }
}
