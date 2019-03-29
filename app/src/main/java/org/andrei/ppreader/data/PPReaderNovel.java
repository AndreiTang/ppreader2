package org.andrei.ppreader.data;

public class PPReaderNovel {
    public int currIndex;

    public String id;
    public String name;
    public String img;
    public boolean isUpdated = false;

    public int getChapterIndex(String chapterId){
        return  0;
    }

    public PPReaderChapter getChapter(int index){
        return null;
    }
}
