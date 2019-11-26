package org.andrei.ppreader.service.engine;

import java.util.ArrayList;

public class Helper {
    public static  boolean containNovelUrl(String url, ArrayList<String> novelUrls){
        for(String str : novelUrls){
            if(str.compareTo(url) == 0){
                return true;
            }
        }
        return false;
    }
}
