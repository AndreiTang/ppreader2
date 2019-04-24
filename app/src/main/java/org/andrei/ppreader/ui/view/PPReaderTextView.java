package org.andrei.ppreader.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import org.andrei.ppreader.data.PPReaderTextPage;

public class PPReaderTextView extends TextView {

    public PPReaderTextView(Context context) {
        super(context);
    }

    public PPReaderTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PPReaderTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PPReaderTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setText(PPReaderTextPage page){
        String str="";
        if(page.offset == 0){
            String title = page.title + "\r\n";
            str += title;
            str +=  "\r\n";
        }

        for(String txt :page.texts){
            str += txt;
        }
        setText(str);
    }

}
