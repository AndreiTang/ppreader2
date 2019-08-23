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
    public transient boolean isUpdated = false;
    public String engineName;
    public String url;
    public String author;
    public String desc;
    public int type;
    public ArrayList<PPReaderChapter> chapters = new ArrayList<>();
    public long duration = 0;
    public String chapterUrl;

    public int getChapterIndex(String chapterId){
        for(int i = 0 ; i <chapters.size(); i++){
            PPReaderChapter chapter = chapters.get(i);
            if(chapter.id.compareTo(chapterId) == 0){
                return i;
            }
        }
        return  -1;
    }

    public PPReaderChapter getChapter(int index){
        return chapters.get(index);
    }
}
