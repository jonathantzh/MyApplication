package com.touchtale.jon.prototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class IntroductionPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction_page);

        Intent intent = getIntent();
        String message = intent.getStringExtra(FirstPage.EXTRA_MESSAGE);
        TextView textView = (TextView) findViewById(R.id.greeting);
        textView.setText(message);
    }

    public void nextPage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, FourthPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}
