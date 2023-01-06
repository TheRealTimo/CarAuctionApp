package com.example.carauctionapp.pages;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.carauctionapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreateListing extends Activity {
    public static String[] getMake() throws IOException, JSONException {
        URL url = new URL("https://carapi.app/api/makes?year=2020");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("accept", "application/json");

        int responseCode = con.getResponseCode();
        //System.out.println("Response code: " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject json = new JSONObject(response.toString());
        JSONArray data = json.getJSONArray("data");

        String[] names = new String[data.length()];
        for (int i = 0; i < data.length(); i++) {
            JSONObject item = data.getJSONObject(i);
            names[i] = item.getString("name");
        }
        return names;
    }

    public static String[] getModel(String make) throws IOException, JSONException {
        // Replace spaces with %20 to encode the make parameter in the URL
        make = make.replace(" ", "%20");
        URL url = new URL("https://carapi.app/api/models?year=2020&make=" + make);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("accept", "application/json");

        int responseCode = con.getResponseCode();
        //System.out.println("Response code: " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject json = new JSONObject(response.toString());
        JSONArray data = json.getJSONArray("data");

        String[] names = new String[data.length()];
        for (int i = 0; i < data.length(); i++) {
            JSONObject item = data.getJSONObject(i);
            names[i] = item.getString("name");
        }
        return names;
    }

    public static String[] getTrim(String make, String model) throws IOException, JSONException {
        // Replace spaces with %20 to encode the year, make, and model parameters in the URL
        make = make.replace(" ", "%20");
        model = model.replace(" ", "%20");
        URL url = new URL("https://carapi.app/api/trims?year=2020&make=" + make + "&model=" + model);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("accept", "application/json");

        int responseCode = con.getResponseCode();
        //System.out.println("Response code: " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject json = new JSONObject(response.toString());
        JSONArray data = json.getJSONArray("data");

        String[] names = new String[data.length()];
        for (int i = 0; i < data.length(); i++) {
            JSONObject item = data.getJSONObject(i);
            names[i] = item.getString("name");
        }
        return names;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_listing);

        EditText create_listing_Title = findViewById(R.id.create_listing_Title);
        EditText create_listing_description = findViewById(R.id.create_listing_description);
        EditText create_listing_opening_bid = findViewById(R.id.create_listing_opening_bid);
        EditText create_listing_duration = findViewById(R.id.create_listing_duration);

        Spinner create_listing_s_make = findViewById(R.id.create_listing_s_make);
        Spinner create_listing_s_model = findViewById(R.id.create_listing_s_model);
        Spinner create_listing_s_trim = findViewById(R.id.create_listing_s_trim);
        Spinner create_listing_s_engine = findViewById(R.id.create_listing_s_engine);
        Spinner create_listing_s_condition = findViewById(R.id.create_listing_s_condition);
        EditText create_listing_mileage = findViewById(R.id.create_listing_mileage);
        EditText create_listing_color = findViewById(R.id.create_listing_color);


        try {
            String makes[] = getMake();
            ArrayAdapter<String> makesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, makes);
            makesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            create_listing_s_make.setAdapter(makesAdapter);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        create_listing_s_make.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String[] models = getModel(create_listing_s_make.getSelectedItem().toString());
                    ArrayAdapter<String> modelsAdapter = new ArrayAdapter<>(CreateListing.this, android.R.layout.simple_spinner_item, models);
                    modelsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    create_listing_s_model.setAdapter(modelsAdapter);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        create_listing_s_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] trim = new String[0];
                try {
                    trim = getTrim(create_listing_s_make.getSelectedItem().toString(), create_listing_s_model.getSelectedItem().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayAdapter<String> trimAdapter = new ArrayAdapter<>(CreateListing.this, android.R.layout.simple_spinner_item, trim);
                trimAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                create_listing_s_trim.setAdapter(trimAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // This method is called when no item is selected
            }
        });

    }
}
