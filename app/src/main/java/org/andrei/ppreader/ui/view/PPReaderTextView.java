package org.andrei.ppreader.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import org.andrei.ppreader.data.PPReaderTextPage;
import org.andrei.ppreader.ui.view.helper.PPReaderLineSpan;
import org.andrei.ppreader.ui.view.helper.PPReaderTitleCenterBoldSpan;

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

    public void setText(PPReaderTextPage page,String textTitle){

        SpannableStringBuilder sb = new SpannableStringBuilder();

        float fontSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18, getResources().getDisplayMetrics());
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        if(page.offset == 0){
            sb.append('\n');
            SpannableString title = new SpannableString(textTitle);
            title.setSpan(new PPReaderTitleCenterBoldSpan(fontSize, padding), 0, textTitle.length(), 0);
            sb.append(title);
            sb.append('\n');
            sb.append('\n');
        }

        fontSize = getTextSize();
        float left = getPaddingLeft();
        float right = getPaddingRight();

        for(String str: page.texts){
            if(str.indexOf('\n') == -1){
                SpannableStringBuilder item = new SpannableStringBuilder(str);
                item.setSpan(new PPReaderLineSpan(fontSize,left,right),0,str.length(),0);
                sb.append(item);
                sb.append('\n');
            }
            else{
                sb.append(str);
            }
        }

        //the last line can't be \n  at the end. otherwise, it will add a new empty line.
        if(sb.charAt(sb.length() - 1) == '\n'){
            sb.delete(sb.length() - 1,sb.length());
        }

        setGravity(page.gravity);
        setText(sb);
    }

}
