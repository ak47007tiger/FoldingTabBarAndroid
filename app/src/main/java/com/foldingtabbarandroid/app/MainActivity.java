package com.foldingtabbarandroid.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.foldingtabbarandroid.app.widget.foldingtabbar.FoldingTabBar;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startRotateAm = (Button) findViewById(R.id.startRotateAm);
        startRotateAm.setOnClickListener(new View.OnClickListener() {
            boolean show = true;
            @Override
            public void onClick(View v) {
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.rotate_container);
                long duration = 400;
                int angle = show ? 90:0;
                show = !show;
                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    View view = linearLayout.getChildAt(i);
                    view.setPivotX(0);
                    view.setPivotY(-view.getHeight() / 2);
                    view.animate().setDuration(duration).rotationY(angle).setStartDelay(i *
                            (duration - 300)).setInterpolator(new
                            AccelerateDecelerateInterpolator());
                }
            }
        });
        FoldingTabBar foldingTabBar = (FoldingTabBar) findViewById(R.id.foldingTabBar);

        TextView leftTabItem1 = new TextView(this);
        leftTabItem1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        leftTabItem1.setText("left1");
        foldingTabBar.addTabLeft(leftTabItem1);

        TextView leftTabItem2 = new TextView(this);
        leftTabItem2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        leftTabItem2.setText("left2");
        foldingTabBar.addTabLeft(leftTabItem2);

        TextView rightTabItem1 = new TextView(this);
        rightTabItem1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rightTabItem1.setText("right1");
        foldingTabBar.addTabRight(rightTabItem1);

        TextView rightTabItem2 = new TextView(this);
        rightTabItem2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rightTabItem2.setText("right2");
        foldingTabBar.addTabRight(rightTabItem2);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
