package com.miller.tyler.recipebuddy;


import android.app.ListFragment;
import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PopularFragment extends ListFragment {

    public static final String TAG = PopularFragment.class.getSimpleName();

    private GetRecipesTask getRecipeTask;
    private List<RecipeData> mRecipeList;
    protected  JSONObject mRecipeData;
    protected ProgressBar mProgressBar;
    private final String KEY_NAME = "recipeName";
    private final String KEY_DESCRIPTION = "sourceDisplayName";
    private final String KEY_ID = "id";


    public PopularFragment() {
        // Required empty public constructor
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

    @Override
    public void onResume() {
        super.onResume();
        requestData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_popular, container, false);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        // Inflate the layout for this fragment
        return rootView;
    }

    private class GetRecipesTask extends AsyncTask<Object, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Object[] params) {

            int responseCode;
            JSONObject jsonResponse = null;
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet("http://api.yummly.com/v1/api/recipes?_app_id=ef3e29e2&_app_key=0ad244c0fd8063f00a481e6c0cc8f4fc");
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

        if (mRecipeData == null) {

            //udateDispayForError();

        } else {
            try {
                JSONArray jsonPosts = mRecipeData.getJSONArray("matches");

                mRecipeList = new ArrayList<RecipeData>();

                for (int i = 0; i < jsonPosts.length(); i++) {
                    JSONObject post = jsonPosts.getJSONObject(i);
                    JSONObject image = post.getJSONObject("imageUrlsBySize");

                    RecipeData dataRecipe = new RecipeData();



                    String name = post.getString(KEY_NAME);
                    name = Html.fromHtml(name).toString();

                    dataRecipe.setName(name);

                    System.out.println("TITLE" + name);


                    String description = post.optString(KEY_DESCRIPTION, "N/A");
                    description = Html.fromHtml(description).toString();

                    dataRecipe.setDescription(description);

                    System.out.println("DES" + description);
                    Log.i("OutPut of recipe", name + ", " + description);

                    String Url = image.getString("90");
                    Url = Html.fromHtml(Url).toString();

                    dataRecipe.setImageUrl(Url);


                    mRecipeList.add(dataRecipe);

                }


                RecipeAdapter adapter = new RecipeAdapter(getActivity(), android.R.layout.simple_list_item_2, mRecipeList);


                setListAdapter(adapter);



            } catch (JSONException e) {
                Log.e(TAG, "exception4 caught: ", e);
                //udateDispayForError();
            }
        }
    }


}
