// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Piece.java

package org.pj.games.tetris;

import org.newdawn.slick.geom.Point;

/**
 * 可以旋转的方块，用一个4x4矩阵表示方块的形状，不同形状的方块可以通过工厂方法来生成。
 */
public class Piece {

    public Piece(Point rotations[][]) {
        currentRotation = 0;
        this.rotations = rotations;
    }

    /**
     * 返回某种旋转体的形状
     *
     * @return
     */
    public Point[] getMatrix() {
        return rotations[currentRotation];
    }

    public int getRotation() {
        return currentRotation;
    }

    public Point[] getMatrix(int rotation) {
        return rotations[rotation % rotations.length];
    }

    public void rotate() {
        currentRotation = ++currentRotation % rotations.length;
    }

    Point rotations[][];
    int currentRotation;
}
