package com.pvp.codingtournament.business.connection;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class JDoodleConnection {

    @Value("${jdoodle.client.id}")
    String clientId;

    @Value("${jdoodle.client.secret}")
    String clientSecret;

    public JDoodleConnection(){

    }

    public String executeCode(String script, String language, String arguments) {
        String versionIndex = "4";
        String output = "";
        String results = "";
        try {
            URL url = new URL("https://api.jdoodle.com/v1/execute");
            HttpURLConnection connection = buildHttpURLConnection(url);
            JSONObject jsonInput = buildJsonRequestObject(script, language, arguments, versionIndex);
            String input = jsonInput.toString();

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(input.getBytes());
            outputStream.flush();
            int responseCode = connection.getResponseCode();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Please check your inputs : HTTP error code : " + connection.getResponseCode());
            }

            BufferedReader bufferedReader;
            bufferedReader = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            while ((output = bufferedReader.readLine()) != null) {
                results = output;
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    private JSONObject buildJsonRequestObject(String script, String language, String arguments, String versionIndex) {
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("clientId", clientId);
        jsonInput.put("clientSecret", clientSecret);
        jsonInput.put("script", script);
        jsonInput.put("language", language);
        jsonInput.put("versionIndex", versionIndex);
        jsonInput.put("stdin", arguments);
        return jsonInput;
    }

    private HttpURLConnection buildHttpURLConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        return connection;
    }
}
