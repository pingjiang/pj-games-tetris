// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PieceFactory.java

package org.pj.games.tetris;

import org.newdawn.slick.geom.Point;

import java.util.Random;

/**
 * 方块工厂，产生随机的方块
 */
public abstract class PieceFactory {

    /**
     * 用4x4矩阵表示方块的上下左右旋转的形状。比如T型方块的四种形状分别为
     *
     * _|_, _|, _  _, |_ 用这里的点来表示端点。（实际会用方块填充）
     *       |   |    |
     */
    private static Point TMatrix[][] = {
            {
                    new Point(0.0F, 0.0F), new Point(-1F, 0.0F), new Point(1.0F, 0.0F), new Point(0.0F, -1F)
            }, {
            new Point(0.0F, 0.0F), new Point(0.0F, 1.0F), new Point(0.0F, -1F), new Point(-1F, 0.0F)
    }, {
            new Point(0.0F, 0.0F), new Point(1.0F, 0.0F), new Point(-1F, 0.0F), new Point(0.0F, 1.0F)
    }, {
            new Point(0.0F, 0.0F), new Point(0.0F, -1F), new Point(0.0F, 1.0F), new Point(1.0F, 0.0F)
    }
    };
    private static Point SMatrix[][] = {
            {
                    new Point(0.0F, 0.0F), new Point(1.0F, 0.0F), new Point(0.0F, -1F), new Point(-1F, -1F)
            }, {
            new Point(0.0F, 0.0F), new Point(0.0F, 1.0F), new Point(1.0F, 0.0F), new Point(1.0F, -1F)
    }, {
            new Point(0.0F, 0.0F), new Point(1.0F, 0.0F), new Point(0.0F, -1F), new Point(-1F, -1F)
    }, {
            new Point(0.0F, 0.0F), new Point(0.0F, 1.0F), new Point(1.0F, 0.0F), new Point(1.0F, -1F)
    }
    };
    private static Point ZMatrix[][] = {
            {
                    new Point(0.0F, 0.0F), new Point(-1F, 0.0F), new Point(0.0F, -1F), new Point(1.0F, -1F)
            }, {
            new Point(0.0F, 0.0F), new Point(0.0F, 1.0F), new Point(-1F, 0.0F), new Point(-1F, -1F)
    }, {
            new Point(0.0F, 0.0F), new Point(-1F, 0.0F), new Point(0.0F, -1F), new Point(1.0F, -1F)
    }, {
            new Point(0.0F, 0.0F), new Point(0.0F, 1.0F), new Point(-1F, 0.0F), new Point(-1F, -1F)
    }
    };
    private static Point OMatrix[][] = {
            {
                    new Point(0.0F, 0.0F), new Point(1.0F, 0.0F), new Point(0.0F, -1F), new Point(1.0F, -1F)
            }, {
            new Point(0.0F, 0.0F), new Point(1.0F, 0.0F), new Point(0.0F, -1F), new Point(1.0F, -1F)
    }, {
            new Point(0.0F, 0.0F), new Point(1.0F, 0.0F), new Point(0.0F, -1F), new Point(1.0F, -1F)
    }, {
            new Point(0.0F, 0.0F), new Point(1.0F, 0.0F), new Point(0.0F, -1F), new Point(1.0F, -1F)
    }
    };
    private static Point IMatrix[][] = {
            {
                    new Point(0.0F, 0.0F), new Point(-1F, 0.0F), new Point(1.0F, 0.0F), new Point(2.0F, 0.0F)
            }, {
            new Point(0.0F, 0.0F), new Point(0.0F, -1F), new Point(0.0F, 1.0F), new Point(0.0F, 2.0F)
    }, {
            new Point(0.0F, 0.0F), new Point(-1F, 0.0F), new Point(1.0F, 0.0F), new Point(2.0F, 0.0F)
    }, {
            new Point(0.0F, 0.0F), new Point(0.0F, -1F), new Point(0.0F, 1.0F), new Point(0.0F, 2.0F)
    }
    };
    private static Point LMatrix[][] = {
            {
                    new Point(0.0F, 0.0F), new Point(1.0F, 0.0F), new Point(-1F, 0.0F), new Point(-1F, -1F)
            }, {
            new Point(0.0F, 0.0F), new Point(0.0F, -1F), new Point(0.0F, 1.0F), new Point(-1F, 1.0F)
    }, {
            new Point(0.0F, 0.0F), new Point(-1F, 0.0F), new Point(1.0F, 0.0F), new Point(1.0F, 1.0F)
    }, {
            new Point(0.0F, 0.0F), new Point(0.0F, 1.0F), new Point(0.0F, -1F), new Point(1.0F, -1F)
    }
    };
    private static Point JMatrix[][] = {
            {
                    new Point(0.0F, 0.0F), new Point(-1F, 0.0F), new Point(1.0F, 0.0F), new Point(1.0F, -1F)
            }, {
            new Point(0.0F, 0.0F), new Point(0.0F, 1.0F), new Point(0.0F, -1F), new Point(-1F, -1F)
    }, {
            new Point(0.0F, 0.0F), new Point(1.0F, 0.0F), new Point(-1F, 0.0F), new Point(-1F, 1.0F)
    }, {
            new Point(0.0F, 0.0F), new Point(0.0F, -1F), new Point(0.0F, 1.0F), new Point(1.0F, 1.0F)
    }
    };

    private static Point[][][] _cache = new Point[][][] { IMatrix, JMatrix, LMatrix, SMatrix, TMatrix, OMatrix, ZMatrix };

    public static Piece generateRandomPiece() {
        return new Piece(_cache[new Random().nextInt(7)]);
    }
}
