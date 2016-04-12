package com.elearnna.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {
    private static final String TAG = MoviesFragment.class.getSimpleName();
    private GridView mGridView;
    String[] postersArray;
    private Context mCntext;
    public MoviesFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        moviesTask.execute("popularity.desc");
        Log.v(TAG, "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCntext = getActivity().getApplicationContext();
        Log.v(TAG, "onCreate");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "onActivityCreated");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mGridView = (GridView) view.findViewById(R.id.myGrid);

        if (isAdded()) {
            ImageAdapter imgAdapter = new ImageAdapter(mCntext, postersArray);
            mGridView.setAdapter(imgAdapter);
        }
        return view;
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, String[]>{
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        private String[] getMoviesDataFromJSon(String moviesJSONString) throws JSONException{
            final String MOVIES_RESULTS = "results";
            final String MOVIE_POSTER_PATH = "poster_path";
            final String PIC_URL = "https://image.tmdb.org/t/p/w185/";
            String moviePoster;

            JSONObject moviesJson = new JSONObject(moviesJSONString);
            JSONArray moviesArray = moviesJson.getJSONArray(MOVIES_RESULTS);

            postersArray = new String[moviesArray.length()];
            for (int i = 0; i < moviesArray.length(); i++){
                JSONObject movieData = moviesArray.getJSONObject(i);
                //JSONObject moviePosterObject = movieData.getJSONObject(MOVIE_POSTER_PATH);
                moviePoster = movieData.getString(MOVIE_POSTER_PATH);
                postersArray[i] = PIC_URL + moviePoster;
            }
            for(String s: postersArray){
                Log.v(LOG_TAG, "Poster's path: " + s);
            }
            return postersArray;
        }
        @Override
        protected String[] doInBackground(String... params) {
            if(params.length == 0){
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String strMoviesJSon = null;

            String sort = "popularity.desc";
            try{
                // Create the url
                final String MOVIES_BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
                final String MOVIES_SORTING_METHOD = "sort_by";
                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(MOVIES_SORTING_METHOD, params[0])
                        .appendQueryParameter(API_KEY,BuildConfig.MOVIE_DB_API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "URL String: " + builtUri.toString());

                // Open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a string
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream == null){
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }
                if(buffer.length() == 0){
                    return null;
                }
                strMoviesJSon = buffer.toString();
                Log.v(LOG_TAG, "Movies JSON String: " + strMoviesJSon);
            } catch(IOException ioe){
                Log.e(LOG_TAG, "Error", ioe);
                return null;
            }finally{
                // disconnect connection
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(reader != null){
                    try{
                        reader.close();
                    }catch(IOException ioe){
                        Log.e(LOG_TAG, "Error in closing stream, ioe");
                    }
                }
            }
            try{
                return getMoviesDataFromJSon(strMoviesJSon);
            }catch(JSONException e){
                Log.e(LOG_TAG, e.getMessage(), e);
            }
            return null;
        }
    }
}
