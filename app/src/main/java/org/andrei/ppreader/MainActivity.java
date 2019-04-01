package org.andrei.ppreader;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.service.PPReaderUpdateNovelRet;
import org.andrei.ppreader.ui.fragment.PPReaderMainFragment;
import org.andrei.ppreader.ui.fragment.PPReaderStartFragment;
import org.andrei.ppreader.ui.fragment.PPReaderTextFragment;
import org.andrei.ppreader.ui.fragment.helper.PPReaderAddNovelRet;
import org.andrei.ppreader.ui.fragment.helper.PPReaderSelectNovelRet;
import org.andrei.ppreader.ui.fragment.helper.PPReaderText;
import org.andrei.ppreader.ui.fragment.helper.PPReaderTextRet;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends FragmentActivity implements IPPReaderTaskNotification {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment start = new PPReaderStartFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.ppreader_root,start).commit();

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
//                IPPReaderDataManager dataManager = null;
//                int ret = dataManager.load();
//                e.onNext(ret);
                Thread.sleep(3000);
                e.onNext(1);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                PPReaderMainFragment main = new PPReaderMainFragment();
                PPReaderTextFragment text = new PPReaderTextFragment();
                text.addListener(MainActivity.this);

                getSupportFragmentManager().beginTransaction().
                        replace(R.id.ppreader_root,main,PPReaderMainFragment.class.getName()).
                        add(R.id.ppreader_root,text,PPReaderTextFragment.class.getName()).
                        hide(text).
                        commit();
            }
        });

        changeStatusBarColor();

    }

    @Override
    public void onBackPressed(){
        PPReaderMainFragment main = (PPReaderMainFragment)getSupportFragmentManager().findFragmentByTag(PPReaderMainFragment.class.getName());
        PPReaderTextFragment text = (PPReaderTextFragment)getSupportFragmentManager().findFragmentByTag(PPReaderTextFragment.class.getName());

        if(main == null || main.isVisible()){
            super.onBackPressed();
            return;
        }
        else{
            main.switchFragment(0);
            getSupportFragmentManager().beginTransaction().show(main).hide(text).commit();
        }
    }

    @Override
    public void onNotify(IPPReaderTaskRet ret) {
        if(ret.type().compareTo(PPReaderTextRet.TYPE_TO_LIST_PAGE) == 0){
            PPReaderTextRet textRet = (PPReaderTextRet)ret;
            PPReaderMainFragment main = (PPReaderMainFragment)getSupportFragmentManager().findFragmentByTag(PPReaderMainFragment.class.getName());
            main.switchFragment(textRet.index);
            PPReaderTextFragment text = (PPReaderTextFragment)getSupportFragmentManager().findFragmentByTag(PPReaderTextFragment.class.getName());
            getSupportFragmentManager().beginTransaction().hide(text).show(main).commit();
        }
        else if(ret.type().compareTo(PPReaderSelectNovelRet.class.getName()) == 0){
            PPReaderSelectNovelRet selectNovelRet = (PPReaderSelectNovelRet)ret;
            PPReaderMainFragment main = (PPReaderMainFragment)getSupportFragmentManager().findFragmentByTag(PPReaderMainFragment.class.getName());
            PPReaderTextFragment text = (PPReaderTextFragment)getSupportFragmentManager().findFragmentByTag(PPReaderTextFragment.class.getName());
            text.setNovel(selectNovelRet.novel);
            getSupportFragmentManager().beginTransaction().hide(main).show(text).commit();
        }
        else if(ret.type().compareTo(PPReaderUpdateNovelRet .class.getName()) == 0){
            PPReaderUpdateNovelRet updateNovelRet = (PPReaderUpdateNovelRet)ret;
            PPReaderTextFragment text = (PPReaderTextFragment)getSupportFragmentManager().findFragmentByTag(PPReaderTextFragment.class.getName());
            text.onAddChapters(updateNovelRet.novel,updateNovelRet.delta);
        }
        else if(ret.type().compareTo(PPReaderAddNovelRet.class.getName()) == 0){
            PPReaderAddNovelRet addNovelRet = (PPReaderAddNovelRet)ret;
            PPReaderMainFragment main = (PPReaderMainFragment)getSupportFragmentManager().findFragmentByTag(PPReaderMainFragment.class.getName());
            main.addNovel(addNovelRet.novel);
        }
    }

    private void changeStatusBarColor(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            window.setStatusBarColor(Color.parseColor("#DBC49B"));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
