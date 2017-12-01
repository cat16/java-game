package main.java.util;

import main.java.rendering.Texture;
import org.json.JSONObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

public class TextureLoader {

    private static HashMap<String, Texture> textures = new HashMap<>();

    public static void loadTextures(String loadFile) throws ResourceNotFoundException {
        JSONObject data = new JSONObject(Utils.loadResource(loadFile));
        String base = data.getString("location");
        List<Object> textureArray = data.getJSONArray("textures").toList();
        for(Object textureObj : textureArray){
            HashMap texture = (HashMap)textureObj;
            String location = base + texture.get("location");
            String name = (String)texture.get("name");
            if(name == null) name = location.split("/")[location.split("/").length-1].split("\\.")[0];
            String newName = new String(name);
            int count = 2;
            while (textures.containsKey(name)) {
                newName = name + count;
                count++;
            }
            textures.put(newName, loadTexture(location));
        }
    }

    private static Texture loadTexture(String location) {
        BufferedImage image;
        try {
            Texture texture = Texture.loadTexture(location);
            return texture;
        } catch (Exception ex) {
            System.out.println("[IMAGE_ERROR] Couldn't find file at " + location);
            System.out.println(ex);
            return null;
        }
    }

    public static Texture getTexture(String name) {
        if (textures.containsKey(name)) {
            return textures.get(name);
        } else {
            System.out.println("[IMAGE_ERROR] Invalid image requested: " + name);
            return null;
        }
    }

    private static int[][][] getPixels(BufferedImage image) {
        int[][][] result = new int[image.getHeight()][image.getWidth()][4];
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color c = new Color(image.getRGB(x, y), true);
                result[y][x][0] = c.getRed();
                result[y][x][1] = c.getGreen();
                result[y][x][2] = c.getBlue();
                result[y][x][3] = c.getAlpha();
            }
        }
        return result;
    }
}