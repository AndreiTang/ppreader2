package org.andrei.ppreader.ui.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jakewharton.rxbinding2.view.RxView;

import org.andrei.ppreader.PPReader;
import org.andrei.ppreader.R;
import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.message.PPReaderSelectNovelMessage;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.functions.Consumer;

public class PPReaderListAdapter extends BaseAdapter {

    public interface  IPPReaderListAdapterAction {
        void openNovel(PPReaderNovel novel);
    }

    public PPReaderListAdapter(@NonNull Fragment fragment,IPPReaderListAdapterAction action){
        m_fragment = fragment;
        m_action = action;
        m_dataManager = PPReader.getDataManager();
    }

    @Override
    public int getCount() {
        return m_dataManager.getNovelCount();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = createView();
        }
        updateView(convertView,position);
        return convertView;
    }

    public boolean isEditMode(){
        return m_isEditMode;
    }

    public void setEditMode(boolean isEditMode){
        m_isEditMode = isEditMode;
        notifyDataSetChanged();
    }

    private View createView(){
        final View v = m_fragment.getLayoutInflater().inflate(R.layout.view_ppreader_list,null);
        RxView.clicks(v).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception{
                int pos = (Integer) v.getTag();
                PPReaderNovel novel = m_dataManager.getNovel(pos);
                if(m_isEditMode){
                    removeNovel(novel);
                }
                else{
                    novel.isUpdated = false;
                    notifyDataSetChanged();
                    if(m_action!=null){
                        m_action.openNovel(novel);
                    }
                }
            }
        });
        return v;
    }

    private void updateView(View view, int position){
        view.setTag(new Integer(position));
        PPReaderNovel novel = m_dataManager.getNovel(position);
        ImageView img = view.findViewById(R.id.novel_cover);
        Glide.with(view).clear(img);
        String imgRootUrl = m_dataManager.getEngineInfo(novel.engineName).imageUrl;
        Glide.with(view).load(imgRootUrl + novel.img).apply(RequestOptions.fitCenterTransform().error(R.drawable.nocover)).into(img);
        TextView tv = view.findViewById(R.id.novel_title);
        tv.setText(novel.name);

        if(m_isEditMode){
            view.findViewById(R.id.novel_remove).setVisibility(View.VISIBLE);
        }
        else{
            view.findViewById(R.id.novel_remove).setVisibility(View.GONE);
        }

        if(novel.isUpdated){
            view.findViewById(R.id.novel_update).setVisibility(View.VISIBLE);
        }
        else{
            view.findViewById(R.id.novel_update).setVisibility(View.GONE);
        }

    }

    private void removeNovel(final PPReaderNovel novel){
        AlertDialog.Builder dlg = new AlertDialog.Builder(m_fragment.getActivity());
        String msg = m_fragment.getString(R.string.novel_list_remove_msg);
        msg = String.format(msg,novel.name);

        dlg.setMessage(msg);
        dlg.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String path = m_fragment.getContext().getExternalFilesDir(null).getPath();
                m_dataManager.removeNovel(path,novel.id);
                notifyDataSetChanged();
            }
        });
        dlg.setNegativeButton(R.string.btn_cancel,null);
        dlg.show();
    }

//    private void openNovel(final PPReaderNovel novel){
//        PPReaderSelectNovelMessage msg = new PPReaderSelectNovelMessage(novel);
//        sendMessage(msg);
//    }

    //private IPPReaderTaskNotification m_notification;
    private Fragment m_fragment;
    private boolean m_isEditMode = false;
    private IPPReaderListAdapterAction m_action;
    private IPPReaderDataManager m_dataManager;

}
