package org.andrei.ppreader.service.message;

import android.view.MotionEvent;

public class PPReaderDBClicksMessage implements IPPReaderMessage {

    public PPReaderDBClicksMessage(MotionEvent event){
        m_event = event;
    }

    @Override
    public String type() {
        return PPReaderMessageTypeDefine.TYPE_DB_CLICKS;
    }

    public MotionEvent getEvent(){
        return m_event;
    }

    protected MotionEvent m_event;
}
