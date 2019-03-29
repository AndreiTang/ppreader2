package org.andrei.ppreader.ui.fragment.helper;

import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.IPPReaderTaskRet;

public class PPReaderAddNovelRet implements IPPReaderTaskRet {
    @Override
    public String type() {
        return PPReaderAddNovelRet.class.getName();
    }

    @Override
    public int getRetCode() {
        return 0;
    }

    public PPReaderNovel novel;
}
