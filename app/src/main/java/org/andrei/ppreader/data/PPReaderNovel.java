package org.andrei.ppreader.data;

import java.io.Serializable;
import java.util.ArrayList;

public class PPReaderNovel implements Serializable {

    public static final int TYPE_ING = 0;
    public static final int TYPE_OVER = 1;
    private static final long serialVersionUID = -1307596945261426047L;

    public int currIndex;
    public String id;
    public String name;
    public String img;
    public boolean isUpdated = false;
    public String engineName;
    public String url;
    public String author;
    public String desc;
    public int type;
    public ArrayList<PPReaderChapter> chapters = new ArrayList<>();

    public int getChapterIndex(String chapterId){
        return  0;
    }

    public PPReaderChapter getChapter(int index){
        return null;
    }
}
