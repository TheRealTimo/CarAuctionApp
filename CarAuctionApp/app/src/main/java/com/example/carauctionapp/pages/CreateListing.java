package com.example.carauctionapp.pages;

import static java.lang.Integer.parseInt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carauctionapp.R;
import com.example.carauctionapp.classes.SessionManagement;
import com.example.carauctionapp.utilities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateListing extends Activity {
    private String listingTitle, listingDescription, listingColor;
    private Integer listingOpeningBid, listingDuration, listingMileage;

    private String[] makesNames, modelNames, trimNames;

    private ImageView navMenu;

    private EditText listingTitleInput, listingDescriptionInput, listingOpeningBidInput, listingDurationInput, listingMileageInput, listingColorInput;

    private Spinner listingMakeInput, listingModelInput, listingTrimInput, listingEngineInput, listingConditionInput;

    private TextView listingCreateButton;

    private Integer createdItemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_listing);

        //Nav menu button
        navMenu = findViewById(R.id.createListingNavMenu);

        //Listing Info Inputs
        listingTitleInput = findViewById(R.id.createListingTitle);
        listingDescriptionInput = findViewById(R.id.createListingDescription);
        listingOpeningBidInput = findViewById(R.id.createListingOpeningBid);
        listingDurationInput = findViewById(R.id.createListingDuration);

        //Listing Car Info Inputs
        //Spinners
        listingMakeInput = findViewById(R.id.createListingMake);
        listingModelInput = findViewById(R.id.createListingModel);
        listingTrimInput = findViewById(R.id.createListingTrim);
        listingEngineInput = findViewById(R.id.createListingEngine);
        listingConditionInput = findViewById(R.id.createListingCondition);

        //EditText
        listingMileageInput = findViewById(R.id.createListingMileage);
        listingColorInput = findViewById(R.id.createListingColor);

        //Create button
        listingCreateButton = findViewById(R.id.createListingButton);
    }

    @Override
    protected void onStart() {
        super.onStart();

        fetchCarMakeData();

        listingMakeInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fetchCarModelData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listingModelInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fetchCarTrimData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listingCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saveInputValues();
                    createItem();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        navMenu.setOnClickListener(openNavPage -> redirectToNavPage());
    }

    private void redirectToNavPage() {
        Intent openAddPaymentPage = new Intent(this, Navbar.class);
        startActivity(openAddPaymentPage);
    }

    private void redirectToListingsPage() {
        Intent openAddPaymentPage = new Intent(this, ListingActivity.class);
        startActivity(openAddPaymentPage);
    }

    private void saveInputValues() {
        listingTitle = listingTitleInput.getText().toString();
        listingDescription = listingDescriptionInput.getText().toString();
        listingColor = listingColorInput.getText().toString();
        listingOpeningBid = parseInt(listingOpeningBidInput.getText().toString());
        listingDuration = parseInt(listingDurationInput.getText().toString());
        listingMileage = parseInt(listingMileageInput.getText().toString());
    }

    private void setMakesSpinnerValues() {
        ArrayAdapter<String> makesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, makesNames);
        makesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listingMakeInput.setAdapter(makesAdapter);
    }

    private void setModelSpinnerValues() {
        ArrayAdapter<String> modelsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, modelNames);
        modelsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listingModelInput.setAdapter(modelsAdapter);
    }

    private void setTrimSpinnerValues() {
        ArrayAdapter<String> trimAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, trimNames);
        trimAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listingTrimInput.setAdapter(trimAdapter);
    }

    private void fetchCarMakeData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest fetchCarMakeDataRequest = new JsonObjectRequest(Request.Method.GET, Constants.CAR_API_MAKES_URL, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //Define local variable for data json array of response
                        JSONArray data = response.getJSONArray("data");

                        //Instantiate makesNames array
                        makesNames = new String[data.length()];

                        //Append data of response to string array
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            makesNames[i] = item.getString("name");
                        }

                        //Set received data to spinner widget
                        setMakesSpinnerValues();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Context currentContext = getApplicationContext();
                    Toast errorToast = Toast.makeText(currentContext, "There was an error, please try again!", Toast.LENGTH_LONG);
                    errorToast.show();
                }
            }
        );

        requestQueue.add(fetchCarMakeDataRequest);
    }

    private void fetchCarModelData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Preparing url
        String make = listingMakeInput.getSelectedItem().toString();
        make.replace(" ", "%20");

        String requestUrl = Constants.CAR_API_MODELS_URL + make;

        JsonObjectRequest fetchCarModelDataRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //Define local variable for data json array of response
                        JSONArray data = response.getJSONArray("data");

                        //Instantiate makesNames array
                        modelNames = new String[data.length()];

                        //Append data of response to string array
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            modelNames[i] = item.getString("name");
                        }

                        //Set received data to spinner widget
                        setModelSpinnerValues();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Context currentContext = getApplicationContext();
                    Toast errorToast = Toast.makeText(currentContext, "There was an error, please try again!", Toast.LENGTH_LONG);
                    errorToast.show();
                }
            }
        );

        requestQueue.add(fetchCarModelDataRequest);
    }

    private void fetchCarTrimData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Preparing url
        String make = listingMakeInput.getSelectedItem().toString();
        make.replace(" ", "%20");

        String model = listingModelInput.getSelectedItem().toString();
        model = model.replace(" ", "%20");

        String requestUrl = Constants.CAR_API_MODELS_URL + make + "&model=" + model;

        JsonObjectRequest fetchCarTrimDataRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //Define local variable for data json array of response
                        JSONArray data = response.getJSONArray("data");

                        //Instantiate makesNames array
                        trimNames = new String[data.length()];

                        //Append data of response to string array
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            trimNames[i] = item.getString("name");
                        }

                        //Set received data to spinner widget
                        setTrimSpinnerValues();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Context currentContext = getApplicationContext();
                    Toast errorToast = Toast.makeText(currentContext, "There was an error, please try again!", Toast.LENGTH_LONG);
                    errorToast.show();
                }
            }
        );

        requestQueue.add(fetchCarTrimDataRequest);
    }

    private void createItem() throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        SessionManagement sessionManagement = new SessionManagement(this);
        String userEmail = sessionManagement.getCurrentUserEmail();

        if (userEmail.isEmpty()) {
            return;
        }

        JSONObject jsonCarObj = new JSONObject();
        jsonCarObj.put("make", listingMakeInput.getSelectedItem().toString());
        jsonCarObj.put("model", listingModelInput.getSelectedItem().toString());
        jsonCarObj.put("year", 2020);
        jsonCarObj.put("trim", listingTrimInput.getSelectedItem().toString());
        jsonCarObj.put("mileage", listingMileage);
        jsonCarObj.put("color", listingColor);
        jsonCarObj.put("condition", listingConditionInput.getSelectedItem().toString());
        jsonCarObj.put("engine", listingEngineInput.getSelectedItem().toString());
        jsonCarObj.put("description", listingDescription);
        jsonCarObj.put("images", "Test");

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("car", jsonCarObj);

        JsonObjectRequest createItemRequest = new JsonObjectRequest(Request.Method.POST, Constants.CREATE_ITEM_API_URL, jsonBody,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        createdItemID = (Integer) response.getInt("itemID");
                        createListing();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Context currentContext = getApplicationContext();
                    Toast errorToast = Toast.makeText(currentContext, "There was an error, please try again!", Toast.LENGTH_LONG);
                    errorToast.show();
                }
            }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(Constants.HEADER_API_KEY, sessionManagement.getCurrentUserApiKey());
                return headers;
            }
        };

        requestQueue.add(createItemRequest);
    }

    private void createListing() throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        SessionManagement sessionManagement = new SessionManagement(this);
        String userEmail = sessionManagement.getCurrentUserEmail();

        if (userEmail.isEmpty()) {
            return;
        }

        JSONObject jsonAuctionObject = new JSONObject();
        jsonAuctionObject.put("title", listingTitle);
        jsonAuctionObject.put("description", listingDescription);
        jsonAuctionObject.put("openingBid", listingOpeningBid);
        jsonAuctionObject.put("duration", listingDuration);

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("item", createdItemID);
        jsonBody.put("auction", jsonAuctionObject);

        Log.d("jsonAuctionObj", jsonAuctionObject.toString());
        Log.d("jsonBody", jsonBody.toString());

        JsonObjectRequest createListingRequest = new JsonObjectRequest(Request.Method.POST, Constants.AUCTION_API_URL, jsonBody,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Context currentContext = getApplicationContext();
                    Toast successToast = Toast.makeText(currentContext, "You have successfully created a new listing!", Toast.LENGTH_LONG);
                    successToast.show();

                    redirectToListingsPage();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Context currentContext = getApplicationContext();
                    Toast errorToast = Toast.makeText(currentContext, "There was an error, please try again!", Toast.LENGTH_LONG);
                    errorToast.show();
                }
            }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(Constants.HEADER_API_KEY, sessionManagement.getCurrentUserApiKey());
                return headers;
            }
        };

        requestQueue.add(createListingRequest);
    }
}
