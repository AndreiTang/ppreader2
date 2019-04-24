package org.andrei.ppreader.data;

import java.util.ArrayList;

public class PPReaderTextPage {
    public final static int STATUS_INVALID = -1;
    public final static int STATUS_OK = 0;
    public final static int STATUS_TEXT_NO_SLICE = 1;
    public final static int STATUS_LOADING = 2;
    public final static int STATUS_LOADED = 3;
    public final static int STATUS_FAIL = 4;
    public final static int STATUS_INIT = 5;

    public int status = STATUS_INIT;
    public String chapterId;
    public int chapterIndex;
    public int offset;
    public int gravity;
    public String text;
    public String title;
    public ArrayList<String> texts = new ArrayList<>();
}
