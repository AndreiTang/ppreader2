package org.andrei.ppreader.data;

import java.util.ArrayList;

public class PPReaderNovel {
    public int currIndex;

    public String id;
    public String name;
    public String img;
    public boolean isUpdated = false;
    public String engineName;
    public String url;
    public ArrayList<PPReaderChapter> chapters = new ArrayList<>();

    public int getChapterIndex(String chapterId){
        return  0;
    }

    public PPReaderChapter getChapter(int index){
        return null;
    }
}
