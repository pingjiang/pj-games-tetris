// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Pit.java

package org.pj.games.tetris;

import org.newdawn.slick.geom.Point;

import java.util.ArrayList;

/**
 * 方块矩阵
 */
public class Pit {

    public Pit(int width, int height) {
        pit = null;
        this.width = width;
        this.height = height;
        pit = new ArrayList<int[]>();
        clear();
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    /**
     * 获取(x, y)的方块
     * @param x 从左向右
     * @param y 从上向下
     * @return 0表示没有方块放置，1表示有方块占用
     */
    public int getBlockAt(int x, int y) {
        return (pit.get(y))[x];
    }

    private final void setBlockAt(int x, int y) {
        (pit.get(y))[x] = 1;
    }

    public void clear() {
        pit.clear();
        for (int y = 0; y < height; y++)
            addEmptyLine();
    }

    private void addEmptyLine() {
        pit.add(new int[width]);
    }

    /**
     * 消掉
     *
     * @param index
     */
    public void destroy(int index) {
        pit.remove(index);
        addEmptyLine();
    }

    /**
     * 判断方块是否可以放下。
     *
     * @param matrix
     * @param x
     * @param y
     * @return
     */
    public boolean doesPieceFitAt(Point matrix[], int x, int y) {
        // 判断方块的每个边界，并且判断是否已经存在方块
        for (int block = 0; block < 4; block++) {
            Point blockPos = matrix[block];
            if ((float) x + blockPos.getX() < 0.0F)
                return false;
            if ((float) x + blockPos.getX() >= (float) width)
                return false;
            if ((float) y + blockPos.getY() < 0.0F)
                return false;
            if ((float) y + blockPos.getY() >= (float) height)
                return false;
            if (getBlockAt(x + (int) blockPos.getX(), y + (int) blockPos.getY()) == 1)
                return false;
        }

        return true;
    }

    public boolean insertPieceAt(Piece piece, int x, int y) {
        Point rotation[] = piece.getMatrix();
        if (!doesPieceFitAt(rotation, x, y))
            return false;
        for (int block = 0; block < 4; block++)
            setBlockAt((int) rotation[block].getX() + x, (int) rotation[block].getY() + y);

        return true;
    }

    /**
     * 判断行是否可以消掉了
     *
     * @param line 行号
     * @return <b>true</b> 可以消掉； <b>false</b> 不能消掉
     * @see #destroy
     */
    public boolean isLineFull(int line) {
        for (int i = 0; i < width; i++)
            if (getBlockAt(i, line) == 0)
                return false;

        return true;
    }

    private int height;
    private int width;
    private ArrayList<int[]> pit;
}
