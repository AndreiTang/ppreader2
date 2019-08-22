package org.andrei.ppreader.service.message;

import org.andrei.ppreader.data.PPReaderNovel;

public class PPReaderAddNovelMessage implements IPPReaderMessage {

    public PPReaderAddNovelMessage(PPReaderNovel novel){
        m_novel = novel;
    }

    @Override
    public String type() {
        return PPReaderMessageTypeDefine.TYPE_ADD_NOVEL;
    }

    public PPReaderNovel getNovel() {
        return m_novel;
    }

    private PPReaderNovel m_novel;
}
