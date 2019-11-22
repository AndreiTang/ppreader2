package org.andrei.ppreader.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PPReaderDataManager implements IPPReaderDataManager {

    @Override
    public int load(final String folder) {
        loadNovels(folder);
        loadEngines(folder);
        return 0;
    }

    @Override
    public void save(final String folder) {
        for (PPReaderNovel novel : m_novels) {
            if(!novel.needValidate){
                continue;
            }
            saveNovel(folder,novel);
            novel.needValidate = false;
        }
        saveEngines(folder);
    }

    @Override
    public void addNovel(PPReaderNovel novel) {
        if (getNovel(novel.id) != null) {
            return;
        }
        m_novels.add(novel);
    }

    @Override
    public PPReaderNovel getNovel(int index) {
        return m_novels.get(index);
    }

    @Override
    public PPReaderNovel getNovel(String id) {
        for (PPReaderNovel novel : m_novels) {
            if (novel.id.compareTo(id) == 0) {
                return novel;
            }
        }
        return null;
    }

    @Override
    public int getNovelCount() {
        return m_novels.size();
    }

    @Override
    public void removeNovel(final String folder,final String id) {
        for (PPReaderNovel novel : m_novels) {
            if (id.compareTo(novel.id) == 0 ) {
                m_novels.remove(novel);
                String path = folder + "/" + novel.name + "-" + novel.id + ".json";
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
                break;
            }
        }
    }

    @Override
    public PPReaderEngineInfo getEngineInfo(int index) {
        return m_infos.get(index);
    }

    @Override
    public PPReaderEngineInfo getEngineInfo(String engineName) {
        for(PPReaderEngineInfo info : m_infos){
            if(info.name.compareTo(engineName) == 0){
                return info;
            }
        }
        return null;
    }

    @Override
    public int getEngineInfoCount() {
        if (m_infos == null) {
            return  0;
        }
        return m_infos.size();
    }

    @Override
    public void setEngineInfos(ArrayList<PPReaderEngineInfo> infos) {
        m_infos = infos;
    }

    private void saveEngines(final String folder) {
        Gson gson = new Gson();
        String infos = gson.toJson(m_infos);
        String path = folder + "/infos.json";

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }

        try {
            FileOutputStream fot = new FileOutputStream(file);
            fot.write(infos.getBytes());
            fot.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void saveNovel(final String folder, final PPReaderNovel novel) {
        String path = folder + "/" + novel.id + ".json";
        Gson gson = new Gson();
        String txt = gson.toJson(novel);

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }

        try {
            FileOutputStream fot = new FileOutputStream(file);
            fot.write(txt.getBytes());
            fot.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadEngines(final String folder){
        String path = folder + "/infos.json";
        File file = new File(path);

        String txt = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = reader.readLine()) != null) {
                txt += line + "\n";
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        ArrayList<PPReaderEngineInfo> infos = gson.fromJson(txt,new TypeToken<ArrayList<PPReaderEngineInfo>>(){}.getType());
        setEngineInfos(infos);
    }

    private void loadNovels(final String path) {
        File folder = new File(path);
        File fs[] = folder.listFiles();
        if (fs == null || fs.length == 0) {
            return;
        }
        for (File item : fs) {
            if (item.isDirectory() || item.getName().indexOf("infos.json") != -1) {
                continue;
            }

            String novelTxt = "";
            try {
                BufferedReader reader = new BufferedReader(new FileReader(item));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    novelTxt += line + "\n";
                }
                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(!novelTxt.isEmpty()){
                Gson gson = new Gson();
                PPReaderNovel novel = gson.fromJson(novelTxt,PPReaderNovel.class);
                m_novels.add(novel);
            }
        }
    }

    protected ArrayList<PPReaderNovel> m_novels = new ArrayList<>();
    protected ArrayList<PPReaderEngineInfo> m_infos;
}
