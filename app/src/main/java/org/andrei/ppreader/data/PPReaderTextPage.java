package org.andrei.ppreader.data;

import java.io.Serializable;
import java.util.ArrayList;

public class PPReaderTextPage implements Serializable {

    public final static int STATUS_INVALID = -1;
    public final static int STATUS_OK = 0;
    public final static int STATUS_TEXT_NO_SLICE = 1;
    public final static int STATUS_LOADING = 2;
    public final static int STATUS_LOADED = 3;
    public final static int STATUS_FAIL = 4;
    public final static int STATUS_INIT = 5;
    private static final long serialVersionUID = -187857478824751288L;

    public transient int status = STATUS_INIT;
    public String chapterId;
    public int chapterIndex;
    public int offset;
    public int gravity;
    public transient String text = "";
    public String title;
    public ArrayList<String> texts = new ArrayList<>();
}
