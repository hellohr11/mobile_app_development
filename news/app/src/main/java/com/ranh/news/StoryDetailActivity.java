package com.ranh.news;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class StoryDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);

        Intent intent = getIntent();
        if (intent.hasExtra(Media.class.getName())) {
            Media c = (Media) intent.getSerializableExtra(Media.class.getName());

            if (c == null) return;

            TextView country = findViewById(R.id.title);
            country.setText(c.getName());

            TextView region = findViewById(R.id.author);
            region.setText(String.format(Locale.getDefault(),
                    "%s (%s)", c.getCategory(), c.getName()));

            TextView capital = findViewById(R.id.capital);
            capital.setText("c.getCapital()");

            TextView population = findViewById(R.id.population);
            population.setText(String.format(Locale.getDefault(), "%,d", "c.getPopulation()"));

            TextView area = findViewById(R.id.area);
            area.setText(String.format(Locale.getDefault(),"%,d sq km"," c.getArea()"));

            TextView citizen = findViewById(R.id.citizens);
            citizen.setText("c.getCitizen()");

            TextView codes = findViewById(R.id.codes);
            codes.setText("c.getCallingCodes(");

            TextView borders = findViewById(R.id.description);
            borders.setText("c.getBorders()");

        }

    }
}
