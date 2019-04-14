package com.example.stackoverflow;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class HttpHandler {
    ArrayList<String> tags = new ArrayList<String>();
    ArrayList<String> searchTags;
    ArrayList<String> posts;

    ArrayList<String> getTags() throws Exception {
        URL url = new URL("https://api.stackexchange.com/2.2/tags?order=desc&sort=popular&site=stackoverflow");
        URLConnection conn = (HttpURLConnection) url.openConnection();
        conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        ((HttpURLConnection) conn).setRequestMethod("GET");

        InputStream is = conn.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        String inputLine;
        String data;
        StringBuilder build = new StringBuilder();
        while ((inputLine = br.readLine()) != null) {
            build.append(inputLine);
        }
        data = build.toString();
        //filter tags from data
        JSONObject jsonObject = new JSONObject(data);
        JSONArray jsonArray = jsonObject.getJSONArray("items");
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            tags.add(jsonObject.getString("name"));
        }
       // Log.i("Main", data);
        br.close();
        return tags;
    }

    /***
     *
     * @param searchTags List of tags to search
     * @return
     * @throws Exception
     */
    ArrayList<String> getPosts(ArrayList<String> searchTags) throws Exception {
        String search="";
        if (searchTags == null)
            return null;
        for (int i=0; i<searchTags.size();i++){
            search=search + searchTags.get(i)+";";
        }
            URL url = new URL("https://api.stackexchange.com/2.2/questions?order=desc&sort=activity&tagged=" + search+ "&site=stackoverflow");
        URLConnection conn = (HttpURLConnection) url.openConnection();
        conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        ((HttpURLConnection) conn).setRequestMethod("GET");

        InputStream is = conn.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        String inputLine;
        String data;
        StringBuilder build = new StringBuilder();
        while ((inputLine = br.readLine()) != null) {
            build.append(inputLine);
        }
        data = build.toString();
        //filter posts from data
        JSONObject jsonObject = new JSONObject(data);
        JSONArray jsonArray = jsonObject.getJSONArray("items");
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            posts.add(jsonObject.getString("title"));
        }
        //Log.i("Main", data);
        br.close();
        return posts;
    }
}
