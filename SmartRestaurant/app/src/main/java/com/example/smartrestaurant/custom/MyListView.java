package com.example.smartrestaurant.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Dell 5521 on 2/9/2018.
 */

public class MyListView extends ListView {

    private android.view.ViewGroup.LayoutParams params;
    private int oldCount = 0;

    public MyListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (getCount() != oldCount)
        {
            if(getChildAt(0) != null){
                int height = getChildAt(0).getHeight() + 1 ;
                oldCount = getCount();
                params = getLayoutParams();
                params.height = getCount() * height;
                setLayoutParams(params);
            }
        }

        super.onDraw(canvas);
    }

}
