package com.example.trackmaniaexchange;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;

public class landingPage extends AppCompatActivity {
    private RequestQueue queue;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        queue = Volley.newRequestQueue(this);


        String currentUser = getIntent().getStringExtra("USERNAME");
        TextView welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome "+currentUser);

        ImageView logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(landingPage.this, loginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button topTenTracksButton = findViewById(R.id.topTenButton);
        topTenTracksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(landingPage.this, popularTracks.class);
                intent.putExtra("USERNAME", currentUser);
                startActivity(intent);
            }
        });



        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int targetDay = calendar.getFirstDayOfWeek();

        String date = currentYear + "-" + currentMonth + "-" + targetDay;
        System.out.println(date);

        String url = "https://www.googleapis.com/youtube/v3/search?key=AIzaSyAchCZv272J543Et4i14JJLKuoqWpURBMM&q=Trackmania&type=video&part=snippet&publishedAfter=" + date + "T00:00:00.01Z&order=viewCount";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    String results = response.getString("items");
                    JSONArray resultsArr = new JSONArray(results);

                    LinearLayout mainLayout = findViewById(R.id.vidInfo);

                    for (int i = 0; i < 5; i++) {
                        JSONObject item = resultsArr.getJSONObject(i);

                        JSONObject id = item.getJSONObject("id");
                        String videoID = id.getString("videoId");

                        JSONObject snippet = item.getJSONObject("snippet");
                        String videoTitle = snippet.getString("title");
                        String channelName = snippet.getString("channelTitle");

                        JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                        JSONObject highThumbnail = thumbnails.getJSONObject("high");
                        String highThumbnailUrl = highThumbnail.getString("url");

                        video v = new video(videoTitle, channelName, videoID, highThumbnailUrl);

                        View videoRecordView = getLayoutInflater().inflate(R.layout.video_record, null);
                        ImageView thumbnailImageView = videoRecordView.findViewById(R.id.thumbnailImageView);
                        TextView channelNameTextView = videoRecordView.findViewById(R.id.channelNameTextView);
                        TextView titleTextView = videoRecordView.findViewById(R.id.titleTextView);

                        channelNameTextView.setText(v.getChannelName());
                        titleTextView.setText(v.getVideoTitle());
                        Picasso.get().load(highThumbnailUrl).into(thumbnailImageView);

                        videoRecordView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openYouTubeVideo(v, v.getTag().toString());
                            }
                        });

                        videoRecordView.setTag(videoID);

                        mainLayout.addView(videoRecordView);
                    }
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

    private void openYouTubeVideo(View v, String videoID) {
        String videoUrl = "https://www.youtube.com/watch?v=" + videoID;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        startActivity(intent);
    }

}
