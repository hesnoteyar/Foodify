package com.example.foodifyy;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FoodNutrientsApi {

    private static final String API_URL = "https://api.edamam.com/api/nutrition-data";
    private static final String YOUR_APP_ID = "7ac272f2";
    private static final String YOUR_APP_KEY = "07b8e5c041de949f923e75e7cd88dabb";

    public static FoodInfo getFoodInfo(String searchTerm) {
        String apiUrl = buildApiUrl(searchTerm);

        try {
            // Create a URL object
            URL url = new URL(apiUrl);

            // Open a connection to the URL
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                // Read the response
                InputStream in = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder responseStringBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    responseStringBuilder.append(line);
                }

                // Parse the JSON response
                String jsonResponse = responseStringBuilder.toString();
                Log.d("FoodNutrientsApi", "Raw JSON Response: " + jsonResponse);
                return parseJsonResponse(jsonResponse);
            } finally {
                // Disconnect the URL connection
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            Log.e("FoodNutrientsApi", "Error during API request: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    private static String buildApiUrl(String searchTerm) {
        // Construct the API URL with the necessary parameters
        String apiUrl = API_URL + "?ingr=" + searchTerm + "&app_id=" + YOUR_APP_ID + "&app_key=" + YOUR_APP_KEY;
        Log.d("FoodNutrientsApi", "API URL: " + apiUrl);
        return apiUrl;
    }

    private static FoodInfo parseJsonResponse(String jsonResponse) {
        try {
            Log.d("FoodNutrientsApi", "JSON Response: " + jsonResponse);
            JSONObject jsonObject = new JSONObject(jsonResponse);

            // Extract relevant information from the JSON response
            JSONObject totalNutrients = jsonObject.getJSONObject("totalNutrients");

            double fats = totalNutrients.getJSONObject("FAT").getDouble("quantity");
            double protein = totalNutrients.getJSONObject("PROCNT").getDouble("quantity");
            double calories = jsonObject.getDouble("calories");

            return new FoodInfo(fats, protein, calories);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
