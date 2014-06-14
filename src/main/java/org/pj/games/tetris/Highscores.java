// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Highscores.java

package org.pj.games.tetris;

import javax.swing.*;
import java.io.*;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

public class Highscores {

    private Highscores(int size) {
        this.size = size;
        try {
            scores = (TreeMap<Integer, String>) (new ObjectInputStream(new FileInputStream(new File("scores.dat")))).readObject();
        } catch (Exception e) {
            // scores.dat 没有找到，创建一个
            scores = new TreeMap<Integer, String>(Collections.reverseOrder());
        }
    }

    public static Highscores getInstance() {
        if (instance == null)
            instance = new Highscores(10);
        return instance;
    }

    public SortedMap<Integer, String> getScores() {
        return scores;
    }

    public void addScore(Integer score) {
        String name = (String) JOptionPane.showInputDialog(null, "Congratulations! You got a high score! \nEnter your name:", "Tetris", -1, null, null, "");
        if (name.length() < 3) {
            name = name + " ";
        }
        name = name.substring(0, 3).toUpperCase();
        scores.put(score, name);
        if (scores.size() > size)
            scores.remove(scores.lastKey());
        saveScores();
    }

    private void saveScores() {
        try {
            (new ObjectOutputStream(new FileOutputStream(new File("scores.dat")))).writeObject(scores);
        } catch (IOException e) {
            System.out.println("HighScores file created.");
        }
    }

    private static Highscores instance = null;
    private static SortedMap<Integer, String> scores = null;
    private int size;

}
