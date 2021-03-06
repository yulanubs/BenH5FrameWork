package org.benmobile.benh5;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import org.benmobile.benh5.config.Configs;
import org.benmobile.benh5.config.ConstData;
import org.benmobile.benh5.log.BenH5Log;
import org.benmobile.coolhttp.http.CoolHttp;
import org.benmobile.coolhttp.http.execute.URLConnectionExecutor;
import org.benmobile.core.natvie.app.PluginClient;
import org.benmobile.core.natvie.config.PluginConfig;
import org.benmobile.core.natvie.log.Logcat;
import org.benmobile.core.natvie.log.SLogger;
import org.benmobile.protocol.ComponentRegister;

/**
 * Created by Jekshow on 2017/6/5.
 * BenH5App框架入口
 */

public class BenH5App extends Application {
    /**AppId*/
    public final String   appid="10002";
    /**日志开关*/
    private boolean debug = true;
    /**APP配置文件*/
    public Configs configs;
    /**测试环境开关,false为Beta环境，true为生产环境*/
    public boolean isBetaorPrd = true;
    @Override
    public void onCreate() {
        super.onCreate();
        initApp();
    }
    /**
     * 初始化应用组件
     */
    private void initApp() {
        //初始化Log
        initLog();
        //初始化App配置
        initConfigs();
        //初始化Plugin配置
        initPlugin();
        //初始化中间件
        initProtocol();
    }
    /**
     * initProtocol
     * 初始化中间中间件
     */
    private void initProtocol() {
        ComponentRegister.getInstance().initialize(this, new CoolHttp.HttpConfig()
                .setConnectionTimeout(10 * 1000)
                .setReadTimeout(20 * 1000)
                .setHttpExecutor(URLConnectionExecutor.getInstance()));
    }

    /**
     * 初始化Log
     */
    private void initLog() {
        boolean mIsPrintLog[]={debug, debug, debug, debug, debug, debug, debug, debug, debug, debug};
        //设置TAG以及是否打印
        BenH5Log.setEnable("POST_STORE", mIsPrintLog[0], mIsPrintLog[1], mIsPrintLog[2], mIsPrintLog[3], mIsPrintLog[4], mIsPrintLog[5], mIsPrintLog[6], mIsPrintLog[7], mIsPrintLog[8], mIsPrintLog[9]);
    }
    /**
     * 初始化App配置
     */
    private void initConfigs() {
        {
            configs = new Configs();
            configs.marketId = this.getResources().getString(R.string.marketId);
            configs.SERVER_BENH5_PUSH = this.getResources()
                    .getString(R.string.SERVER_BENH5_PUSH).trim();
            if (isBetaorPrd) {
                //生产环境
                configs.SERVER_BENH5_VPS = this.getResources().getString(
                        R.string.SERVER_PRD_BENH5_VPS).trim();
                configs.SERVER_BENH5_ALTERNATE_VPS = this.getResources()
                        .getString(R.string.SERVER_PRD_BENH5_ALTERNATE_VPS).trim();

            } else {
                //Beta环境
                configs.SERVER_BENH5_VPS = this.getResources().getString(
                        R.string.SERVER_BETA_BENH5_VPS);
                configs.SERVER_BENH5_ALTERNATE_VPS = this.getResources()
                        .getString(R.string.SERVER_BETA_BENH5_ALTERNATE_VPS).trim();

            }

            configs.UPDATE_KEY = this.getResources().getString(R.string.UPDATE_KEY);


        }

    }
    /**
     * 初始化Plugin配置
     */
    private void initPlugin() {
        //1.初始化配置
        initPluginConfigs();
        //2.初始化PluginClient
        PluginClient.init(this);

    }
    /**初始化PluginConfig*/
    private void initPluginConfigs() {
        if (BuildConfig.DEBUG) {
            PluginConfig    config = new PluginConfig();
            config.hostUrl = configs.SERVER_BENH5_VPS;
            //是否调用预置的插件
            config.copyAsset = true;
            config.isDebug = true;
            //0.全部，根据主项目的AppId查询所有插件,1.根据某个插件的id,查询插件信息,2.根据某个插件的appProject，查询插件信息
            config.flageType="2";
            //插件更新接口
            config.CHECK_UPDATEAPPPLUGIN= ConstData.check_updateappplugin;
            config.APP_ID=appid;
            //设置配置
            PluginClient.setConfig(config);
        }
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

}
