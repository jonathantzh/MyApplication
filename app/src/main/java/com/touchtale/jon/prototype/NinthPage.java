package com.touchtale.jon.prototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.Display;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import co.tanvas.haptics.service.app.*;
import co.tanvas.haptics.service.adapter.*;
import co.tanvas.haptics.service.model.*;

public class NinthPage extends AppCompatActivity {

    private HapticView mHapticView;
    private HapticTexture mHapticTexture;
    private HapticMaterial mHapticMaterial;
    private HapticSprite mHapticSprite;
    private int scaleStripes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ninth_page);
        ImageView scale1 = (ImageView) findViewById(R.id.scale1);
        ImageView scale2 = (ImageView) findViewById(R.id.scale2);
        ImageView scale3 = (ImageView) findViewById(R.id.scale3);
        scale1.setOnTouchListener(onTouchListener_Scale());
        scale2.setOnTouchListener(onTouchListener_Scale());
        scale3.setOnTouchListener(onTouchListener_Scale());
        ImageView Next = (ImageView) findViewById(R.id.next);
        Next.setOnTouchListener(onTouchListener_cutfish());
        Next.setVisibility(View.GONE);

        // Init haptics
        initHaptics();
        scaleStripes = 3;
    }

    private OnTouchListener onTouchListener_Scale() {
        return new OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {

                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        view.setVisibility(View.GONE);
                        scaleStripes -= 1;
                        break;
                }
                if (scaleStripes == 0) {
                    try {
                        mHapticView.removeAllSprites();
                    } catch (Exception e) {
                        Log.e(null, e.toString());
                    }
                    ImageView Next = (ImageView) findViewById(R.id.next);
                    Next.setVisibility(View.VISIBLE);
                }
                return true;
            }
        };
    }

    private OnTouchListener onTouchListener_cutfish() {
        return new OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {

                Intent intent = new Intent(NinthPage.this, TenthPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                return true;
            }
        };
    }

    public void initHaptics(){
        try {
            // Get the service adapter
            HapticServiceAdapter serviceAdapter = HapticApplication.getHapticServiceAdapter();

            // Create a haptic view and activate it
            mHapticView = HapticView.create(serviceAdapter);
            mHapticView.activate();

            // Set the orientation of the haptic view
            Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int rotation = display.getRotation();
            HapticView.Orientation orientation = HapticView.getOrientationFromAndroidDisplayRotation(rotation);mHapticView.setOrientation(orientation);

            // Retrieve texture data from the bitmap
            Bitmap hapticBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.scales);
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
    };
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // The activity is gaining focus
        if (hasFocus) {
            try {
                // Set the size and position of the haptic sprite to correspond to the view we created
                View view = findViewById(R.id.fishscales);
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                mHapticSprite.setSize(view.getWidth(), view.getHeight());
                mHapticSprite.setPosition(location[0], location[1]);
            } catch (Exception e) {
                Log.e(null, e.toString());
            }
        }
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

};