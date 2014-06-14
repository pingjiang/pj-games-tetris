// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ResourceManager.java

package org.pj.games.tetris;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.loading.LoadingList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public abstract class ResourceManager {

    public ResourceManager() {
    }

    public static void loadResources(String path)
            throws SlickException {
        try {
            LoadingList.setDeferredLoading(true);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(new FileInputStream(new File(path)));
            document.getDocumentElement().normalize();
            NodeList resources = document.getElementsByTagName("resource");
            for (int i = 0; i < resources.getLength(); i++) {
                Node n = resources.item(i);
                if (n.getNodeType() == 1) {
                    Element resource = (Element) n;
                    String type = resource.getAttribute("type");
                    if (type.equals("image"))
                        addImage(resource);
                    else if (type.equals("sound"))
                        addSound(resource);
                }
            }

        } catch (Exception e) {
            throw new SlickException("Resources XML file could not be loaded");
        }
    }

    public static final Image getImage(String id) {
        return images.get(id);
    }

    private static final void addImage(Element resource)
            throws SlickException {
        String id = resource.getAttribute("id");
        String fileName = resource.getTextContent();
        if (fileName == null || fileName.length() == 0)
            throw new SlickException("Image resource [" + id + "] has an invalid path");
        try {
            images.put(id, new Image("res/images/" + fileName));
        } catch (SlickException e) {
            throw new SlickException("Image resource [" + id + "] could not be loaded", e);
        }
    }

    public static final Sound getSound(String id) {
        return sounds.get(id);
    }

    private static final void addSound(Element resource)
            throws SlickException {
        String id = resource.getAttribute("id");
        String fileName = resource.getTextContent();
        if (fileName == null || fileName.length() == 0)
            throw new SlickException("Sound resource [" + id + "] has an invalid path");
        try {
            sounds.put(id, new Sound("res/sounds/" + fileName));
        } catch (SlickException e) {
            throw new SlickException("Sound resource [" + id + "] could not be loaded", e);
        }
    }

    private static Map<String, Image> images = new HashMap<String, Image>();
    private static Map<String, Sound> sounds = new HashMap<String, Sound>();

}
