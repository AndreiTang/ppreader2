package org.andrei.ppreader.data;

import java.util.ArrayList;

public class PPReaderNovel {

    public static final int TYPE_ING = 0;
    public static final int TYPE_OVER = 1;

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
