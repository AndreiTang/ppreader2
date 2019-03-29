package org.andrei.ppreader.ui.fragment.helper;

import org.andrei.ppreader.service.IPPReaderTaskRet;

public class PPReaderTextRet implements IPPReaderTaskRet {
    public static final String TYPE_CURR = "curr";
    public static final String TYPE_FETCH_TEXT = "text";
    public static final String TYPE_DB_CLICK = "dc";
    public static final String TYPE_SHOW_CATALOG = "sc";
    public static final String TYPE_SET_CURR = "set_curr";
    public static final String TYPE_TO_LIST_PAGE = "tlp";

    public PPReaderTextRet(String type){
        m_type = type;
    }

    @Override
    public String type() {
        return m_type;
    }

    @Override
    public int getRetCode() {
        return 0;
    }

    public int index;
    private String m_type;
}
