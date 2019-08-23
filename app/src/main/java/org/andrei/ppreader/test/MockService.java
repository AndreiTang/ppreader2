package org.andrei.ppreader.test;

import android.support.annotation.NonNull;

import org.andrei.ppreader.service.engine.IPPReaderNovelEngineManager;
import org.andrei.ppreader.service.IPPReaderService;
import org.andrei.ppreader.service.task.IPPReaderTask;

public class MockService implements IPPReaderService{
    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void waitForExit() {

    }

    @Override
    public void addTask(@NonNull IPPReaderTask task) {
//        if(task.type().compareTo(CommandNames.SEARCH_URLS) == 0){
//            PPReaderSearchUrlsRet ret = new PPReaderSearchUrlsRet();
//            PPReaderSearchUrlsTask t = (PPReaderSearchUrlsTask)task;
//            if(t.name.compareTo("1") == 0){
//                ret.engineName = "";
//                ret.urls.add("url1");
//                m_notify.onNotify(ret);
//            }
//            else if(t.name.compareTo("2") == 0 || t.name.compareTo("22") == 0){
//                ret.engineName = "";
//                ret.urls.add("url2");
//                m_isIdle = false;
//                if(t.name.compareTo("22") == 0){
//                    m_isIdle = true;
//                }
//                m_notify.onNotify(ret);
//
//            }
//            else if(t.name.compareTo("3") == 0 ){
//                ret.retCode = ServiceError.ERR_NOT_FOUND;
//                m_notify.onNotify(ret);
//            }
//            else if(t.name.compareTo("4") == 0){
//                ret.retCode = ServiceError.ERR_NOT_NETWORK;
//                m_notify.onNotify(ret);
//            }
//            else if(t.name.compareTo("5") == 0){
//                m_isIdle = true;
//                ret.engineName = "";
//                ret.urls.add("url2");
//                ret.urls.add("url3");
//                m_notify.onNotify(ret);
//
//            }
//            else if(t.name.compareTo("6") == 0){
//                m_isIdle = false;
//                ret.engineName = "";
//                ret.urls.add("url2");
//                ret.urls.add("url3");
//                m_notify.onNotify(ret);
//
//            }
//            else if(t.name.compareTo("7") == 0){
//                m_isIdle = true;
//                ret.engineName = "";
//                ret.urls.add("url2");
//                ret.urls.add("url4");
//                m_notify.onNotify(ret);
//
//            }
//
//
//
//        }
//        else if(task.type().compareTo(CommandNames.SEARCH_NOVELS) == 0){
//
//            PPReaderSearchNovelsTask t = (PPReaderSearchNovelsTask)task;
//
//
//            PPReaderSearchNovelsRet ret = new PPReaderSearchNovelsRet();
//            ret.novels = new ArrayList<PPReaderNovel>();
//            PPReaderNovel novel = new PPReaderNovel();
//
//            if(t.url.compareTo("url1") == 0 || t.url.compareTo("url2") == 0  ) {
//                novel.type = PPReaderNovel.TYPE_ING;
//                novel.author = "贼眉鼠眼";
//                novel.desc = "入赘商户的女婿没出息吗？穿越者萧凡就是个很明显的反例。";
//                novel.id = "2";
//                novel.img = "https://fm.x88dushu.com/19/19013/19013s.jpg";
//                novel.name = "大明王侯";
//                ret.novels.add(novel);
//
//                novel = new PPReaderNovel();
//                novel.type = PPReaderNovel.TYPE_ING;
//                novel.author = "卢鹏";
//                novel.desc = "读书不努力，穿越徒伤悲。这是一个算得上是学渣穿越到明末当皇帝的故事。他不懂武器制造，不精通诗词，更不会领兵作战，似乎也没有一技之长。他唯一的优势就是熟悉历史，从业经验丰富。这是一个正常的普通人当皇帝的故事，绝不是圣人当皇帝的故事！";
//                novel.id = "1";
//                novel.img = "https://fm.x88dushu.com/98/98493/98493s.jpg";
//                novel.name = "大明1624";
//                ret.novels.add(novel);
//
//                novel = new PPReaderNovel();
//                novel.type = PPReaderNovel.TYPE_ING;
//                novel.author = "炮兵";
//                novel.desc = "一个精通造假工艺大师穿越到崇祯二年，这是大明帝国最黑暗的岁月";
//                novel.id = "3";
//                novel.img = "https://fm.x88dushu.com/42/42370/42370s.jpg";
//                novel.name = "大明枭";
//                ret.novels.add(novel);
//            }
//
//            if(t.url.compareTo("url2") == 0  ){
//                novel = new PPReaderNovel();
//                novel.type = PPReaderNovel.TYPE_ING;
//                novel.author = "彩云归";
//                novel.desc = "当年明月在，曾照彩云归。";
//                novel.id = "4";
//                novel.img = "https://fm.x88dushu.com/100/100077/100077s.jpg";
//                novel.name = "大明帝国";
//                ret.novels.add(novel);
//
//                novel = new PPReaderNovel();
//                novel.type = PPReaderNovel.TYPE_ING;
//                novel.author = "不觉晓";
//                novel.desc = "猪脚周世雄是个驯兽师，穿越明朝末年。";
//                novel.id = "5";
//                novel.img = "https://fm.x88dushu.com/32/32636/32636s.jpg";
//                novel.name = "大明兴";
//                ret.novels.add(novel);
//            }
//
//            if(t.url.compareTo("url3") == 0 ){
//                novel = new PPReaderNovel();
//                novel.type = PPReaderNovel.TYPE_ING;
//                novel.author = "神灯";
//                novel.desc = "巡狩大明";
//                novel.id = "6";
//                novel.img = "https://fm.x88dushu.com/10/10861/10861s.jpg";
//                novel.name = "巡狩大明";
//                ret.novels.add(novel);
//            }
//
//            if(t.url.compareTo("url4") == 0 ){
//                return;
//            }
//
//            m_notify.onNotify(ret);
//        }
//        else if(task.type().compareTo(CommandNames.UPDATE_NOVEL)==0){
//            PPReaderUpdateNovelRet ret = new PPReaderUpdateNovelRet();
//            ret.id = ((PPReaderUpdateNovelTask)task).id;
//            ArrayList<PPReaderChapter> cs = new ArrayList<>();
//            cs.add(new PPReaderChapter());
//            ret.delta = cs;
//            m_notify.onNotify(ret);
//        }

    }

    @Override
    public void clearTasks() {

    }

    @Override
    public boolean isIdle() {
        return m_isIdle;
    }

   // IPPReaderTaskNotification m_notify;
    IPPReaderNovelEngineManager m_mgr = new MockEngineManager();
    boolean m_isIdle = false;
}
