package org.andrei.ppreader.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import org.andrei.ppreader.R;
import org.andrei.ppreader.service.message.PPReaderCommonMessage;
import org.andrei.ppreader.service.message.PPReaderMessageCenter;
import org.andrei.ppreader.service.message.PPReaderMessageTypeDefine;


public class PPReaderControlPanel extends View {

    public interface IPPReaderControlPanelAction{
        public enum  Action{
            Catalog,
            Search,
            List
        }
        public void doAction(Action action);
    }

    public PPReaderControlPanel(Context context) {
        super(context);
    }

    public PPReaderControlPanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void addOnAction(IPPReaderControlPanelAction action){
        m_action = action;
    }

    public void show(int x, int y){
        m_orgX = x;
        m_orgY = y;
        setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eve = event.getActionMasked();
        if(eve == MotionEvent.ACTION_DOWN ){
            int x = (int)event.getX();
            int y = (int)event.getY();
            PPReaderCommonMessage msg = null;
            if(checkDict(x,y)==true){
                if(m_action != null){
                    m_action.doAction(IPPReaderControlPanelAction.Action.Catalog);
                }
                //msg = new PPReaderCommonMessage(PPReaderMessageTypeDefine.TYPE_SHOW_CATALOG,0);
            }
            else if(checkCache(x,y) == true){
                if(m_action != null){
                    m_action.doAction(IPPReaderControlPanelAction.Action.Catalog);
                }
               // msg = new PPReaderCommonMessage(PPReaderMessageTypeDefine.TYPE_SHOW_CATALOG,0);
            }
            else if(checkList(x,y) == true){
                if(m_action != null){
                    m_action.doAction(IPPReaderControlPanelAction.Action.List);
                }
                //msg = new PPReaderCommonMessage(PPReaderMessageTypeDefine.TYPE_TO_LIST_PAGE,0);
            }
            else if(checkSearch(x,y) == true){
                //msg = new PPReaderCommonMessage(PPReaderMessageTypeDefine.TYPE_TO_LIST_PAGE,1);
                if(m_action != null){
                    m_action.doAction(IPPReaderControlPanelAction.Action.Search);
                }

            }
//            if(msg != null){
//                PPReaderMessageCenter.instance().sendMessage(msg);
//            }
            setVisibility(View.GONE);
        }
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        drawFrame(canvas);
        drawTexts(canvas);
    }

    private void drawFrame(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(false);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#DBC49B"));
        canvas.drawCircle(m_orgX, m_orgY, m_outRadius + 10, paint);
        canvas.save();
        canvas.translate(m_orgX, m_orgY);
        canvas.rotate(45);


        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setAlpha(255);
        paint.setColor(Color.parseColor(m_frameColor));
        canvas.drawCircle(0, 0, m_outRadius + 10, paint);


        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor(m_frameColor));
        canvas.drawCircle(0, 0, m_midRadius, paint);

        canvas.drawLine(0, m_midRadius, 0, m_outRadius + 10, paint);
        canvas.drawLine(0, -m_midRadius, 0, -m_outRadius - 10, paint);
        canvas.drawLine(m_midRadius, 0, m_outRadius + 10, 0, paint);
        canvas.drawLine(-m_midRadius, 0, -m_outRadius - 10, 0, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#FFA726"));
        canvas.drawCircle(0, 0, m_inRadius, paint);

        canvas.restore();
    }

    private void drawTexts(Canvas canvas) {
        Paint paint = new Paint();
        int ts = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18, getResources().getDisplayMetrics());
//        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),"fonts/xinkai.ttf");
//        paint.setTypeface(typeface);
        paint.setStrokeWidth(3);
        paint.setTextSize(ts);
        paint.setColor(Color.parseColor("#000000"));
        Rect rc = new Rect();
        int delta = m_midRadius + (m_outRadius - m_midRadius) / 2;

        canvas.save();
        canvas.translate(m_orgX, m_orgY);

        String txt = getResources().getString(R.string.novel_panel_dict);
        paint.getTextBounds(txt, 0, txt.length(), rc);
        int begin = 0 - delta;
        begin -= rc.width() / 2;
        canvas.drawText(txt, begin, rc.height() / 2, paint);

        txt = getResources().getString(R.string.novel_panel_cache);
        paint.getTextBounds(txt, 0, txt.length(), rc);
        begin = delta;
        begin -= rc.width() / 2;
        canvas.drawText(txt, begin, rc.height() / 2, paint);

        txt = getResources().getString(R.string.novel_panel_search);
        paint.getTextBounds(txt, 0, txt.length(), rc);
        begin = delta;
        begin += rc.height() / 2;
        canvas.drawText(txt, -rc.width() / 2, begin, paint);

        txt = getResources().getString(R.string.novel_panel_list);
        paint.getTextBounds(txt, 0, txt.length(), rc);
        begin = 0 - delta;
        begin += rc.height() / 2;
        canvas.drawText(txt, -rc.width() / 2, begin, paint);

        canvas.restore();
    }

    private boolean checkDict(int x, int y) {
        int delta = m_midRadius + (m_outRadius - m_midRadius) / 2;
        int orgX = m_orgX - delta;
        int orgY = m_orgY;

        Rect rc = new Rect(orgX - m_clickRange, orgY - m_clickRange, orgX + m_clickRange, orgY + m_clickRange);
        return rc.contains(x, y);
    }

    private boolean checkCache(int x, int y) {
        int delta = m_midRadius + (m_outRadius - m_midRadius) / 2;
        int orgX = m_orgX + delta;
        int orgY = m_orgY;

        Rect rc = new Rect(orgX - m_clickRange, orgY - m_clickRange, orgX + m_clickRange, orgY + m_clickRange);
        return rc.contains(x, y);
    }

    private boolean checkList(int x, int y) {
        int delta = m_midRadius + (m_outRadius - m_midRadius) / 2;
        int orgX = m_orgX;
        int orgY = m_orgY - delta;

        Rect rc = new Rect(orgX - m_clickRange, orgY - m_clickRange, orgX + m_clickRange, orgY + m_clickRange);
        return rc.contains(x, y);
    }

    private boolean checkSearch(int x, int y) {
        int delta = m_midRadius + (m_outRadius - m_midRadius) / 2;
        int orgX = m_orgX;
        int orgY = m_orgY + delta;

        Rect rc = new Rect(orgX - m_clickRange, orgY - m_clickRange, orgX + m_clickRange, orgY + m_clickRange);
        return rc.contains(x, y);
    }

    public static final int CACHE = 1;
    public static final int LIST = 2;
    public static final int SEARCH = 3;
    public static final int DICT = 4;

    private int m_outRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
    private int m_midRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
    private int m_inRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 27, getResources().getDisplayMetrics());
    private int m_clickRange = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
    private String m_frameColor = "#9e9e9e";

    private int m_orgX = 200;
    private int m_orgY = 200;

    private IPPReaderControlPanelAction m_action;


}
