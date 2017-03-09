package com.example.omoarukhe.javadeveloperslagos;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GitHubListActivity extends ListActivity {

    public static final String TAG = GitHubListActivity.class.getSimpleName();
    protected JSONObject githubData;
    protected ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_git_hub_list);

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);

        if (isNetworkAvailable()) {
            progressBar.setVisibility(View.VISIBLE);
            GetGithubUsers getGithubUsers = new GetGithubUsers();
            getGithubUsers.execute();
        }else {
            Toast.makeText(this, "Network is unavailable", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        try {
            JSONArray itemProfiles = githubData.getJSONArray("items");
            JSONObject jsonItem = itemProfiles.getJSONObject(position);
            String profile_url = jsonItem.getString("html_url");
            String image = jsonItem.getString("avatar_url");
            String userName = jsonItem.getString("login");
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("NAME",userName);
            intent.putExtra("IMAGE",image);
            intent.putExtra("URL",profile_url);
            startActivity(intent);
        } catch (JSONException e) {
            Log.e(TAG, "Exception Caught: ", e);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }

    private void updateList() {

        progressBar.setVisibility(View.INVISIBLE);

        if(githubData == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error!");
            builder.setMessage("No Data to display!");
            builder.setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            try {
                JSONArray itemProfiles = githubData.getJSONArray("items");
                Profile[] profiles = new Profile[itemProfiles.length()];

                for (int i = 0; i < itemProfiles.length(); i++){
                    JSONObject jsonItem = itemProfiles.getJSONObject(i);
                    Profile profile = new Profile();

                    profile.setUserName(jsonItem.getString("login"));
                    profile.setProfileUrl(jsonItem.getString("url"));
                    profile.setUserImage(jsonItem.getString("avatar_url"));

                    profiles[i] = profile;
                }

                ProfileAdapter profileAdapter = new ProfileAdapter(getApplicationContext(), profiles);
                setListAdapter(profileAdapter);

            } catch (JSONException e) {
                Log.e(TAG, "Exception Caught: ", e);
            }
        }

    }

    private class GetGithubUsers extends AsyncTask<Object, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Object[] params) {

            int responseCode =-1;
            String responseData = "";
            JSONObject jsonResponse = null;


            try {

                URL githubFeedUrl = new URL("https://api.github.com/search/users?q=location:nigeria+language:java");
                HttpURLConnection connection = (HttpURLConnection) githubFeedUrl.openConnection();
                connection.connect();

                responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    InputStream inputStream = connection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(inputStream);

                    int data = reader.read();

                    while(data != -1){

                        char current = (char) data;

                        responseData += current;

                        data = reader.read();
                    }

                    jsonResponse = new JSONObject(responseData);

                }else {

                    Log.i(TAG, "Unsuccessful HTTP Response Code: " + responseCode);

                }

            } catch (MalformedURLException e) {
                Log.e(TAG, "Exception caught: ", e);
            } catch (IOException e) {
                Log.e(TAG, "Exception caught: ", e);
            }catch (NegativeArraySizeException e){
                Log.e(TAG, "Exception caught: ", e);
            }catch (Exception e){
                Log.e(TAG, "Exception caught: ", e);
            }

            return jsonResponse;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            githubData = result;
            updateList();
        }
    }
}
