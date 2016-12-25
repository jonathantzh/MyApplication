package com.touchtale.jon.prototype;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.content.Context;
        import android.content.Intent;
        import android.view.View;
        import android.view.WindowManager;
        import android.view.Display;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.util.Log;
        import android.view.animation.AlphaAnimation;
        import android.widget.Button;
        import android.widget.ProgressBar;
        import android.widget.ImageView;

        import co.tanvas.haptics.service.app.*;
        import co.tanvas.haptics.service.adapter.*;
        import co.tanvas.haptics.service.model.*;

public class TimeSleep extends AppCompatActivity {

    private HapticView mHapticView;
    private HapticTexture mHapticTexture;
    private HapticMaterial mHapticMaterial;
    private HapticSprite mHapticSprite;
    private Button catButton;
    private ImageView iw;
    private Button paw;
    private ProgressBar mProgress;
    private int mProgressStatus = 0;

    AlphaAnimation alpha = new AlphaAnimation(0, 1); // change values as you want
    AlphaAnimation alpha2 = new AlphaAnimation(1, 0); // change values as you want

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_sleep);

        alpha.setDuration(0); // Make animation instant
        alpha.setFillAfter(true); // Tell it to persist after the animation ends
        alpha2.setDuration(0); // Make animation instant
        alpha2.setFillAfter(true); // Tell it to persist after the animation ends

        iw = (ImageView) findViewById(R.id.catstroke);
        paw = (Button) findViewById(R.id.paw);

        mProgress = (ProgressBar) findViewById(R.id.progressBar);

        catButton = (Button) findViewById(R.id.catButton);

        catButton.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {
                mProgressStatus += 34;
                mProgress.setProgress(mProgressStatus);

                if(mProgressStatus>30 && mProgressStatus<60)
                    iw.setImageResource(R.drawable.sleepprestroke);
                if(mProgressStatus>70)
                    iw.setImageResource(R.drawable.sleepprestroke);
                if(mProgressStatus>100) {
                    iw.setImageResource(R.drawable.sleeppoststroke);
                    paw.setAlpha(1);
                }

                return true;    // <- set to true
            }
        });

        // Init haptics
        initHaptics();
    }
    public void initHaptics() {
        try {
            // Get the service adapter
            HapticServiceAdapter serviceAdapter =
                    HapticApplication.getHapticServiceAdapter();
            // Create a haptic view and activate it
            mHapticView = HapticView.create(serviceAdapter);
            mHapticView.activate();
            // Set the orientation of the haptic view
            Display display = ((WindowManager)
                    getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int rotation = display.getRotation();
            HapticView.Orientation orientation =
                    HapticView.getOrientationFromAndroidDisplayRotation(rotation);

            mHapticView.setOrientation(orientation);
            // Retrieve texture data from the bitmap

            Bitmap hapticBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scales);
            byte[] textureData = HapticTexture.createTextureDataFromBitmap(hapticBitmap);

            // Create a haptic texture with the retrieved texture data
            mHapticTexture = HapticTexture.create(serviceAdapter);
            int textureDataWidth = hapticBitmap.getRowBytes() / 4; // 4 channels, i.e., ARGB
            int textureDataHeight = hapticBitmap.getHeight();
            mHapticTexture.setSize(textureDataWidth, textureDataHeight);
            mHapticTexture.setData(textureData);
            // Create a haptic material with the created haptic texture
            mHapticMaterial = HapticMaterial.create(serviceAdapter);
            mHapticMaterial.setTexture(0, mHapticTexture);
            // Create a haptic sprite with the haptic material
            mHapticSprite = HapticSprite.create(serviceAdapter);
            mHapticSprite.setMaterial(mHapticMaterial);
            // Add the haptic sprite to the haptic view
            mHapticView.addSprite(mHapticSprite);
        } catch (Exception e) {
            Log.e(null, e.toString());
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // The activity is gaining focus
        if (hasFocus) {
            try {
                // Set the size and position of the haptic sprite to correspond to the view we created
                View view = findViewById(R.id.view);
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                mHapticSprite.setSize(view.getWidth(), view.getHeight());
                mHapticSprite.setPosition(location[0], location[1]);
            } catch (Exception e) {
                Log.e(null, e.toString());
            } }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mHapticView.deactivate();
        } catch (Exception e) {
            Log.e(null, e.toString());
        }
    }

    /** Called when the user clicks the Send button */
    public void nextPage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, TheEnd.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

}

