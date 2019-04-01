package org.andrei.ppreader.test;

import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.data.PPReaderDataManager;
import org.andrei.ppreader.data.PPReaderNovel;

import java.util.ArrayList;

public class MockDataManager extends PPReaderDataManager {

    public MockDataManager(){
        PPReaderNovel novel = new PPReaderNovel();
        novel.img = "https://www.ptwxz.com/files/article/image/8/8875/8875s.jpg";
        novel.name = "大明傲骨";
        novel.id = "1";
        m_novels.add(novel);

        novel = new PPReaderNovel();
        novel.img = "https://www.xbiquge6.com/cover/79/79769/79769s.jpg";
        novel.name = "卡牌大明星";
        novel.id = "2";
        m_novels.add(novel);


        novel = new PPReaderNovel();
        novel.img = "https://www.xbiquge6.com/cover/83/83802/83802s.jpg";
        novel.name = "穿越大明之汉骨永存";
        novel.id = "3";
        m_novels.add(novel);

        novel = new PPReaderNovel();
        novel.img = "https://www.xbiquge6.com/cover/81/81356/81356s.jpg";
        novel.name = "大明领主";
        novel.id = "4";
        m_novels.add(novel);
    }


}
