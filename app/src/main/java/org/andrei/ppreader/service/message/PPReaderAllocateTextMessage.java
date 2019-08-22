package org.andrei.ppreader.service.message;

import android.widget.TextView;

import org.andrei.ppreader.data.PPReaderTextPage;

public class PPReaderAllocateTextMessage implements IPPReaderMessage {

    public PPReaderAllocateTextMessage(TextView tv, PPReaderTextPage page){
        m_tv = tv;
        m_page = page;
    }

    @Override
    public String type() {
        return PPReaderMessageTypeDefine.TYPE_ALLOCATE_TEXT;
    }


    public TextView getTv(){
        return m_tv;
    }

    public PPReaderTextPage getPage(){
        return m_page;
    }

    private TextView m_tv;
    private PPReaderTextPage m_page;
}
