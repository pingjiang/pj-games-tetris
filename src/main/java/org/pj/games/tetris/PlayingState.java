// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PlayingState.java

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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 游戏页面
 */
public class PlayingState extends BasicGameState {
    private static final Logger logger = Logger.getLogger(PlayingState.class.getName());

    private static final int PIT_X = 52;
    private static final int PIT_Y = 18;
    private static final int BLOCK_SIZE = 28;
    private static final int INPUT_TIMETOUT = 100;
    private int stateID;
    private Image hud;
    private UnicodeFont font;
    private Pit pit;
    private int score;
    private int scoreGained;
    private int multiplier;
    private boolean comboStreak;
    private int level;
    private int milestones[] = {
            2000, 5000, 10000, 30000, 50000, 100000, 150000, 350000, 600000, 1000000
    };
    private int inputDelta;
    private int dropTimer;
    private int dropTimeouts[] = {
            1000, 900, 850, 800, 750, 700, 650, 600, 550, 500
    };
    private Piece nextPiece;
    private Piece currentPiece;
    private int pieceX;
    private int pieceY;
    private int shadowPieceY;
    private boolean enableShadow;
    private STATE currentState;

    public enum STATE {
        START_GAME("START_GAME", 0),
        NEW_PIECE("NEW_PIECE", 1),
        MOVING_PIECE("MOVING_PIECE", 2),
        LINE_DESTRUCTION("LINE_DESTRUCTION", 3),
        PAUSE_GAME("PAUSE_GAME", 4),
        GAME_OVER("GAME_OVER", 5);

        private STATE(String s, int i) {
            this.name = s;
            this.index = i;
        }

        private final String name;
        private final int index;
    }


    public PlayingState(int stateID) {
        hud = null;
        font = null;
        pit = new Pit(10, 20);
        scoreGained = 0;
        multiplier = 1;
        comboStreak = false;
        level = 0;
        inputDelta = 0;
        dropTimer = 0;
        nextPiece = null;
        currentPiece = null;
        enableShadow = true;
        currentState = null;
        this.stateID = stateID;
    }

    public int getID() {
        return stateID;
    }

    public void enter(GameContainer gc, StateBasedGame sb)
            throws SlickException {
        super.enter(gc, sb);
        currentState = STATE.START_GAME;
    }

    public void init(GameContainer container, StateBasedGame game)
            throws SlickException {
        hud = ResourceManager.getImage("HUD");
    }

    /**
     * 用一个简单的状态机来驱动游戏的运行。
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
                logger.log(Level.INFO, new Date() + e.getDescription());
                e.load();
            } catch (IOException e) {
                throw new SlickException("Error loading resource", e);
            }
        } else {
            if (font == null) {
                font = new UnicodeFont("res/fonts/ITCKRIST.ttf", 20, true, false);
                font.addAsciiGlyphs();
                font.getEffects().add(new ColorEffect(Color.white));
                font.addAsciiGlyphs();
                font.loadGlyphs();
            }
            switch (currentState) {
                case START_GAME: // '\001'
                    ResourceManager.getSound("GAME_START").play();
                    pit.clear();
                    score = 0;
                    level = 0;
                    scoreGained = 0;
                    multiplier = level + 1;
                    dropTimer = dropTimeouts[level];
                    currentState = STATE.NEW_PIECE;
                    break;

                case NEW_PIECE: // '\002'
                    generateNewPiece();
                    calculateShadowPiece();
                    break;

                case MOVING_PIECE: // '\003'
                    movePiece(gc, sb, deltaT);
                    calculateShadowPiece();
                    break;

                case LINE_DESTRUCTION: // '\004'
                    checkForFullLines();
                    currentState = STATE.NEW_PIECE;
                    break;

                case PAUSE_GAME: // '\005'
                    if (gc.getInput().isKeyDown(1))
                        currentState = STATE.MOVING_PIECE;
                    break;

                case GAME_OVER: // '\006'
                    ResourceManager.getSound("GAME_OVER").play();
                    Highscores.getInstance().addScore(Integer.valueOf(score));
                    sb.enterState(0);
                    break;
                default:
                    break;
            }
        }
    }

    public void drawPieceAt(Piece p, int x, int y, boolean shadow) {
        if (p == null)
            return;
        Point rotation[] = p.getMatrix();
        for (int i = 0; i < 4; i++)
            ResourceManager.getImage(shadow ? "TRANSPARENT_BLOCK" : "BLOCK").draw(52F + (rotation[i].getX() + (float) x) * 28F, 18F + ((float) (pit.getHeight() - 1) - (rotation[i].getY() + (float) y)) * 28F);

    }

    private void calculateShadowPiece() {
        for (shadowPieceY = pieceY; pit.doesPieceFitAt(currentPiece.getMatrix(), pieceX, shadowPieceY); shadowPieceY--)
            ;
        shadowPieceY++;
    }

    /**
     * 渲染游戏界面
     *
     * @param container
     * @param game
     * @param g
     * @throws SlickException
     */
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {
        hud.draw(0.0F, 0.0F);
        font.drawString(600F, 25F, String.valueOf(score), org.newdawn.slick.Color.orange);
        font.drawString(430F, 190F, String.valueOf(level + 1), org.newdawn.slick.Color.orange);
        font.drawString(530F, 223F, level >= milestones.length ? "-" : String.valueOf(milestones[level]), org.newdawn.slick.Color.orange);
        font.drawString(480F, 255F, String.valueOf(multiplier), org.newdawn.slick.Color.orange);
        font.drawString(560F, 290F, String.valueOf(scoreGained), org.newdawn.slick.Color.orange);

        // 重绘所有的方块，因为所有信息都是保存在成员变量里面的，只需要重绘即可。
        for (int line = 0; line < pit.getHeight(); line++) {
            for (int col = 0; col < pit.getWidth(); col++) {
                int block = pit.getBlockAt(col, line);
                if (block != 0) {
                    ResourceManager.getImage("BLOCK").draw(PIT_X + col * BLOCK_SIZE, PIT_Y + BLOCK_SIZE * (pit.getHeight() - line - 1));
                }
            }
        }

        if (enableShadow && pieceY - shadowPieceY > 6) {
            drawPieceAt(currentPiece, pieceX, shadowPieceY, true);
        }
        drawPieceAt(currentPiece, pieceX, pieceY, false);
        drawPieceAt(nextPiece, pit.getWidth() + 2, pit.getHeight() - 2, false);
    }

    private void movePiece(GameContainer gc, StateBasedGame sb, int deltaT) {
        dropTimer -= deltaT;
        inputDelta -= deltaT;
        if (dropTimer < 0) {
            if (pit.doesPieceFitAt(currentPiece.getMatrix(), pieceX, pieceY - 1)) {
                pieceY--;
                ResourceManager.getSound("PIECE_FALL").play();
            } else {
                pit.insertPieceAt(currentPiece, pieceX, pieceY);
                currentState = STATE.LINE_DESTRUCTION;
            }
            dropTimer = dropTimeouts[level];
        }
        if (inputDelta < 0) {
            Input input = gc.getInput();
            if (input.isKeyDown(1))
                currentState = STATE.PAUSE_GAME;
            if (input.isKeyDown(31)) {
                enableShadow = !enableShadow;
                inputDelta = INPUT_TIMETOUT;
            }
            if (input.isKeyDown(203) && pit.doesPieceFitAt(currentPiece.getMatrix(), pieceX - 1, pieceY)) {
                pieceX--;
                inputDelta = INPUT_TIMETOUT;
                ResourceManager.getSound("PIECE_MOVE").play();
            }
            if (input.isKeyDown(205) && pit.doesPieceFitAt(currentPiece.getMatrix(), pieceX + 1, pieceY)) {
                pieceX++;
                inputDelta = INPUT_TIMETOUT;
                ResourceManager.getSound("PIECE_MOVE").play();
            }
            if (input.isKeyDown(208))
                if (pit.doesPieceFitAt(currentPiece.getMatrix(), pieceX, pieceY - 1)) {
                    pieceY--;
                    inputDelta = 50;
                } else {
                    pit.insertPieceAt(currentPiece, pieceX, pieceY);
                    currentState = STATE.LINE_DESTRUCTION;
                    ResourceManager.getSound("PIECE_TOUCH").play();
                }
            if (input.isKeyDown(200) && pit.doesPieceFitAt(currentPiece.getMatrix(currentPiece.getRotation() + 1), pieceX, pieceY)) {
                inputDelta = 150;
                currentPiece.rotate();
                ResourceManager.getSound("PIECE_ROTATE").play();
            }
            if (input.isKeyDown(57)) {
                inputDelta = INPUT_TIMETOUT;
                pieceY = shadowPieceY;
                pit.insertPieceAt(currentPiece, pieceX, pieceY);
                currentState = STATE.LINE_DESTRUCTION;
                ResourceManager.getSound("PIECE_TOUCH").play();
            }
        }
    }

    private void generateNewPiece() {
        if (currentPiece == null) {
            nextPiece = PieceFactory.generateRandomPiece();
        }
        currentPiece = nextPiece;
        pieceX = 5;
        pieceY = 19;

        // 如果方块可以放下，就可以移动方块了，否则结束游戏
        if (pit.doesPieceFitAt(currentPiece.getMatrix(), pieceX, pieceY)) {
            nextPiece = PieceFactory.generateRandomPiece();
            currentState = STATE.MOVING_PIECE;
        } else {
            currentState = STATE.GAME_OVER;
        }
    }

    private void checkForFullLines() {
        int linesDestroyed = 0;

        // 检查所有方块，如果可以消掉，就全部消掉
        for (int line = 0; line < pit.getHeight(); ) {
            if (pit.isLineFull(line)) {
                pit.destroy(line);
                linesDestroyed++;
            } else {
                line++;
            }
        }

        // 消掉行后可以计分
        switch (linesDestroyed) {
            case 0: // '\0'
                score += 10;
                multiplier = level + 1;
                comboStreak = false;
                break;

            case 1: // '\001'
                scoreGained = 100;
                ResourceManager.getSound("SINGLE").play();
                break;

            case 2: // '\002'
                scoreGained = 300;
                ResourceManager.getSound("DOUBLE").play();
                break;

            case 3: // '\003'
                scoreGained = 600;
                ResourceManager.getSound("TRIPLE").play();
                break;

            case 4: // '\004'
                scoreGained = 1000;
                ResourceManager.getSound("TETRIS").play();
                break;
        }

        if (linesDestroyed > 0) {
            if (comboStreak)
                multiplier++;
            scoreGained *= multiplier;
            score += scoreGained;
            if (level < milestones.length && score > milestones[level]) {
                ResourceManager.getSound("LEVEL_UP").play();
                level++;
            }
            comboStreak = true;
        }
    }
}
