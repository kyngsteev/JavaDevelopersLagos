package com.example.omoarukhe.javadeveloperslagos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView profileName = (TextView) findViewById(R.id.userTextView);
        TextView urlName = (TextView) findViewById(R.id.urlTextView);
        ImageView profileImage = (ImageView) findViewById(R.id.proImage);
        Button shareButton = (Button) findViewById(R.id.shareButton);

        final Intent intent = getIntent();
        final String userName = intent.getStringExtra("NAME");
        final String profile_url = intent.getStringExtra("URL");
        String image = intent.getStringExtra("IMAGE");

        profileName.setText(userName);
        urlName.setText(profile_url);
        Picasso.with(this).load(image).into(profileImage);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, "Check out this awesome developer @" + userName +  "<" + profile_url + ">.");
                startActivity(Intent.createChooser(share,  "Check out this awesome developer @" + userName));

            }
        });
    }
}
