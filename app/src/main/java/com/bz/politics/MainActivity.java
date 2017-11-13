package com.bz.politics;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.framework.domain.param.BaseParam;
import com.framework.net.NetworkParam;
import com.framework.net.Request;
import com.framework.net.ServiceMap;
import com.framework.view.tab.TabItem;
import com.framework.view.tab.TabLayout;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chenxi.cui on 2017/9/30.
 */

public class MainActivity extends MainTabActivity {
    TabLayout tlTab;
    private WeatherLayout weatherView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        tlTab = (TabLayout) findViewById(R.id.tl_tab);
        weatherView = (WeatherLayout) findViewById(R.id.weather_view);
        tabLayout = tlTab;
        addTab("制度建设", WebFragment.class, getBundle("制度建设","http://dj.qfant.com/index.php/App/Index/baseinfo/id/24"), R.drawable.icon_zhidu);
        addTab("组织网格", WebFragment.class, getBundle("组织网格","http://dj.qfant.com/index.php/App/Index/news/catid/74"), R.drawable.icon_group);
        addTab("工作开展", WebFragment.class, getBundle("工作开展","http://dj.qfant.com/index.php/App/Index/news/catid/73"), R.drawable.icon_gongzuo);
        addTab("治安防范", WebFragment.class, getBundle("治安防范","http://dj.qfant.com/index.php/App/Index/grouplife/catid/72"), R.drawable.icon_zhian);
        onPostCreate();
        initView();

    }

    private void initView() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Request.startRequest(new BaseParam(), ServiceMap.weather, mHandler);
            }
        };
        timer.schedule(timerTask, 0, 1000 * 60 * 30);

    }

    @Override
    public void onTabClick(TabItem tabItem) {
        if ("微信党支部".equals(tabItem.text)) {
            try {
                showToast("打开微信");
                Intent intent = new Intent();
                ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(cmp);
                startActivity(intent);
            } catch (Exception e) {
                showToast("未安装微信");
            }

        } else {
            super.onTabClick(tabItem);
        }
    }

    @Override
    public boolean onMsgSearchComplete(NetworkParam param) {
        if (param.key == ServiceMap.weather) {
            WeatherResult result = (WeatherResult) param.result;
            if (result.data != null && result.data.size() > 0) {
                weatherView.setData(result.data.get(0));
            }
        }
        return super.onMsgSearchComplete(param);
    }

    private Bundle getBundle(String title, String url) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("title", title);
        return bundle;
    }
}
