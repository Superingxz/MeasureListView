package com.myz.measurelistview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private Button drawBitmap;
    private ImageView image;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawBitmap = (Button) findViewById(R.id.drawBitmap);
        listView = (ListView) findViewById(R.id.listview);
        image = (ImageView) findViewById(R.id.image);
        scrollView = (ScrollView) findViewById(R.id.scroll);
        List<String> list = loadData();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
    }

    private List<String> loadData() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            data.add("item" + i);
        }
        return data;
    }



    public Bitmap shotListView(ListView listView){
        ListAdapter adapter = listView.getAdapter();
        int itemsCount = adapter.getCount();
        int allItemsHeight = 0 ;
        List<Bitmap> bmps= new ArrayList<>();
        for (int i = 0; i < itemsCount; i++) {
            View childView = adapter.getView(i, null, listView);
            childView.measure(View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
            childView.setDrawingCacheEnabled(true);
            childView.buildDrawingCache();
            bmps.add(childView.getDrawingCache());
            allItemsHeight += childView.getMeasuredHeight();
        }
        int measuredWidth = listView.getMeasuredWidth();
        Bitmap bigBitmap = Bitmap.createBitmap(measuredWidth,allItemsHeight,Bitmap.Config.ARGB_8888);
        Canvas bigCanvas = new Canvas(bigBitmap);
        Paint paint = new Paint();
        int iHeight = 0;

        for (int i = 0; i < bmps.size(); i++) {
            Bitmap bmp = bmps.get(i);
            bigCanvas.drawBitmap(bmp, 0, iHeight,paint);
            iHeight += bmp.getHeight();

            bmp.recycle();
            bmp = null;
        }
        return bigBitmap;
    }

    public void draw(View view) {
        Bitmap bm = shotListView(listView);
        if (bm != null) {
            image.setImageBitmap(bm);
            drawBitmap.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        } else {
            drawBitmap.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }
    }
}
