package snake.logic.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URI;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONReader {
    public static JSONObject readJsonFromURL(String url) throws IOException, JSONException {
        InputStream inputStream = URI.create(url).toURL().openStream();
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")))) {
            String jsonText = "";
            int c;

            while ((c = reader.read()) != -1) {
                jsonText += (char) c;
            }
            
            return new JSONObject(jsonText);
        }
    }
}
