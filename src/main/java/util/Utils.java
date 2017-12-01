package main.java.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Utils {

    public static String loadResource(String fileName) throws ResourceNotFoundException {
        String result;
        try (
                InputStream in = Utils.class.getResourceAsStream(fileName);
                Scanner scanner = new Scanner(in, "UTF-8")
        ) {
            result = scanner.useDelimiter("\\A").next();
        } catch (IOException | NullPointerException ex) {
            throw new ResourceNotFoundException("Could not load resource " + fileName);
        }
        return result;
    }

}
