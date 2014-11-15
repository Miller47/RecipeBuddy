package com.miller.tyler.recipebuddy;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private String mRecipeId = "";
    public static final String TAG = PopularFragment.class.getSimpleName();

    private GetRecipesTask getRecipeTask;
    protected JSONObject mRecipeData;
    protected ProgressBar mProgressBar;
    protected ImageView mImageView;
    protected TextView mTime;
    protected Button mCook;
    protected ListView mIngred;
    private String mImageUrl;
    private String mSourceUrl;
    private ArrayList<String> mIngredientLines;




    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        //Set up ui
        mImageView = (ImageView) rootView.findViewById(R.id.large_image);
        mTime = (TextView) rootView.findViewById(R.id.time);
        mCook = (Button) rootView.findViewById(R.id.cook_it);
        mIngred = (ListView) rootView.findViewById(R.id.recipe_in);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        Intent intent = getActivity().getIntent();
        Bundle data = intent.getExtras();

        if (data != null) {
            mRecipeId = data.getString("recipeId").toString();
        }
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestData();
    }

    protected boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    private void requestData() {

        if (isOnline()) {

            mProgressBar.setVisibility(View.VISIBLE);

            getRecipeTask = new GetRecipesTask();
            getRecipeTask.execute();

        } else {
            Toast.makeText(getActivity(), "Network is unavailable!", Toast.LENGTH_LONG).show();
            mProgressBar.setVisibility(View.INVISIBLE);

        }

    }

    private class GetRecipesTask extends AsyncTask<Object, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Object[] params) {

            int responseCode;
            JSONObject jsonResponse = null;
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();

            HttpGet httpGet = new
                    HttpGet("http://api.yummly.com/v1/api/recipe/" + mRecipeId + "?_app_id=ef3e29e2&_app_key=0ad244c0fd8063f00a481e6c0cc8f4fc");
            try {
                HttpResponse response = client.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                responseCode = statusLine.getStatusCode();




                if (responseCode == HttpURLConnection.HTTP_OK) {

                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }

                    jsonResponse = new JSONObject(builder.toString());

                    System.out.println("Response data: " + jsonResponse);


                } else {
                    Log.i(TAG, "Unsuccessful HTTP Response Code: " + responseCode);
                }

                Log.i(TAG, "Code: " + responseCode);
                System.out.println("Code: " + responseCode);
            } catch (MalformedURLException e) {
                Log.e(TAG, "Exception1 caught: ", e);
            } catch (IOException e) {
                Log.e(TAG, "Exception2 caught: ", e);
            } catch (Exception e) {
                Log.e(TAG, "Exception3 caught: ", e);
            }

            return jsonResponse;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            mRecipeData = result;
            handleAPIResponse();

        }
    }

    private void handleAPIResponse() {
       mProgressBar.setVisibility(View.INVISIBLE);
       mTime.setVisibility(View.VISIBLE);
       mCook.setVisibility(View.VISIBLE);

        if (mRecipeData == null) {

            //udateDispayForError();

        } else {
            try {
                JSONObject jsonParent = new JSONObject(String.valueOf(mRecipeData));
                JSONArray jsonImage = jsonParent.getJSONArray("images");
                JSONObject source = jsonParent.getJSONObject("source");

                //Set actionbar title
                ActionBar ab = getActivity().getActionBar();
                ab.setTitle(jsonParent.getString("name"));

                //Set time to cook label
                mTime.setText(jsonParent.getString("totalTime"));

                //Set up button
                mSourceUrl = source.getString("sourceRecipeUrl");

                //Set arraylist to ingredientLines array
                mIngredientLines = new ArrayList<String>();
                JSONArray temp = jsonParent.getJSONArray("ingredientLines");
                if (temp != null) {
                    for (int i = 0; i < temp.length(); i++) {
                        mIngredientLines.add(temp.get(i).toString());
                    }
                    mIngred.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.ingredient_list, R.id.list_content, mIngredientLines));

                }



                //Image data set up
                for (int i = 0; i < jsonImage.length(); i++) {
                    JSONObject recipeImages = jsonImage.getJSONObject(i);


                    JSONObject images = recipeImages.getJSONObject("imageUrlsBySize");

                    mImageUrl = images.getString("360");


                }

                //Set imageview
                Picasso.with(getActivity().getApplicationContext())
                        .load(mImageUrl)
                        .fit()
                        .centerCrop()
                        .into(mImageView);












            } catch (JSONException e) {
                Log.e(TAG, "exception4 caught: ", e);
                //udateDispayForError();
            }
        }
    }


}
