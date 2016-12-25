package com.touchtale.jon.prototype;

    import android.content.Intent;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.view.MotionEvent;
    import android.view.View;
    import android.view.View.OnTouchListener;
    import android.widget.ImageView;

public class CutFish6 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cut_fish6);
        ImageView Next = (ImageView) findViewById(R.id.next);
        Next.setOnTouchListener(onTouchListener_feedcat());
    }
    private OnTouchListener onTouchListener_feedcat() {
        return new OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {

                Intent intent = new Intent(CutFish6.this, FeedCat.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                return true;
            }
        };
    }
}
