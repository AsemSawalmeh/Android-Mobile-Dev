package com.example.trackmaniaexchange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class popularTracks extends AppCompatActivity {
    private RequestQueue queue;
    private ArrayAdapter<String> adapter;
    ArrayList<String> tracksStringArrayList = new ArrayList<>();
    ArrayList<Track> tracksArrayList = new ArrayList<>();
    ArrayList<trackRecord> trackRecords = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_tracks);


        String currentUser = getIntent().getStringExtra("USERNAME");

        ImageView logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(popularTracks.this, loginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageView homeButton = findViewById(R.id.tmxlogo);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(popularTracks.this, landingPage.class);
                startActivity(intent);
                finish();
            }
        });

        queue = Volley.newRequestQueue(this);

        String url = "https://trackmania.exchange/mapsearch2/search?api=on&mode=4&priord=8";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    String results = response.getString("results");
                    JSONArray resultsArr = new JSONArray(results);

                    for (int i = 0; i < 10; i++) {
                        JSONObject entry = resultsArr.getJSONObject(i);

                        int trackID = entry.getInt("TrackID");
                        String gbxMapName = entry.getString("GbxMapName");
                        String newMapName = cleanMapName(gbxMapName);
                        String authorName = entry.getString("Username");
                        Track track = new Track(trackID, newMapName, authorName);

                        tracksArrayList.add(track);
                        tracksStringArrayList.add(track.toString());
                    }

                    displayTracks(tracksArrayList);


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley_error", "Error: " + error.toString());
            }
        });

        queue.add(request);

    }

    private void displayTracks(ArrayList<Track> trackArrayList) {
        for (int i = 0; i < trackArrayList.size(); i++) {
            getTrackThumbnail(trackArrayList.get(i));
        }
    }

    private void getTrackThumbnail(Track t) {
        String id = String.valueOf(t.getTrackID());
        String url = "https://trackmania.exchange/maps/thumbnail/" + id;

        View trackRecordView = getLayoutInflater().inflate(R.layout.item_track_record, null);
        ImageView thumbnailImageView = trackRecordView.findViewById(R.id.thumbnailImageView);
        TextView authorTextView = trackRecordView.findViewById(R.id.authorTextView);
        TextView titleTextView = trackRecordView.findViewById(R.id.titleTextView);

        ImageRequest imagerequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                thumbnailImageView.setImageBitmap(response);
                authorTextView.setText(t.getAuthorName());
                titleTextView.setText(t.getMapName());

                LinearLayout mainLayout = findViewById(R.id.info);
                mainLayout.addView(trackRecordView);
            }
        }, 500, 500, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley_error", "Error: " + error.toString());
            }
        });
        queue.add(imagerequest);
    }

    private String cleanMapName(String mapname) {
        String regex = "\\$((L|H)\\[.+\\]|[\\da-f]{3}|[\\w\\$\\<\\>]{1})";
        String string = mapname;
        String subst = "";

        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(string);


        String result = matcher.replaceAll(subst);
        return result;
    }

}