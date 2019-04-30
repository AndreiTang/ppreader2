package org.andrei.ppreader.data;

import java.io.Serializable;

public class PPReaderChapter implements Serializable{
    private static final long serialVersionUID = 1045669074495564162L;
    public String id;
    public String text;
    public String title;
    public String url;
    public int offset;
}
