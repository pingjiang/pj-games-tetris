package org.pj.games.tetris;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 俄罗斯方块
 * <p/>
 * 《俄罗斯方块》是由下面这几种四格骨牌构成，全部都由四个方块组成。
 * 开始时，一个随机的方块会从区域上方开始缓慢继续落下。
 * 落下期间，玩家可以以90度为单位旋转方块，以格子为单位左右移动方块，或让方块加速落下。
 * 当方块下落到区域最下方或是着落到其他方块上无法再向下移动时，就会固定在该处，然后新的方块出现在区域上方开始落下。
 * 当区域中某一横行的格子全部由方块填满时，则该列会被消除并成为玩家的得分。同时消除的行数越多，得分指数级上升。
 * [6]玩家在游戏中的目的就是尽量得分。当固定的方块堆到区域最顶端而无法消除层数时，游戏就会退出。
 * [13]部分游戏提供单格方块，那些单格方块能穿透固定的方块到达最下层空位。其他的改版中则出现更多特别的造型。
 * 不同的方块能清除的列数不同。I方块最多能清除4列、J和L方块最多能清除3列、而剩余的则最多只能清除2列。[14]
 * 一般来说，游戏还会提示下一个将要落下的方块，熟练的玩家会计算到下一个方块，评估现在要如何进行。
 * 由于游戏能不断进行下去对商业用游戏不太理想，所以一般还会随着游戏的进行而加速提高难度。[13]
 * 计分系统在游戏中，当玩家清除越难清除的列，玩家所获得的分数就会越高。
 * 例如，当玩家清除1列能获得100分，清除4列能获得800分，而清除备份方块则能获得1200分。
 */
public class App {
    private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        //logger.log(Level.INFO, "enum ordinal = " + PlayingState.STATE.START_GAME.ordinal());
        try {
            AppGameContainer app = new AppGameContainer(new Tetris()); // new TetrisGame("Tetris Game")
            app.setMinimumLogicUpdateInterval(25);
            app.setTargetFrameRate(30);
            app.setDisplayMode(800, 600, false);
            app.start();
        } catch (SlickException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }
}

