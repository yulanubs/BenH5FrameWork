package org.benmobile.protocol.controller;

import org.benmobile.protocol.bean.PushPluginAction;
import org.benmobile.protocol.config.ConstServiceType;
import org.benmobile.protocol.utlis.ValueUtils;

/**
 * Created by Jekshow on 2017/6/9.
 * 中间件请求管理器
 */

public class QuestProtocolManager {
    private static   QuestProtocolManager mQuestProtocolManager;

    private QuestProtocolManager() {

    }

    /**
     * 获取单例对象
     * @return
     */
    public static QuestProtocolManager getInstance(){
        if (null==mQuestProtocolManager){
            synchronized (QuestProtocolManager.class){
                if(null==mQuestProtocolManager){
                    mQuestProtocolManager=new QuestProtocolManager();
                }
            }
        }
        return  mQuestProtocolManager;
    }

    /**
     * 向中间件推送一条消息
     * @param PluginId
     * @param data
     * @param requestcode
     */
    public void  QusetProtocolPushMsgData(String PluginId,String data,int requestcode){
        if (ValueUtils.isStrNotEmpty(PluginId)&&ValueUtils.isStrNotEmpty(data)){
            new  QuestMsgController().pushPluginAction(new  PushPluginAction(ConstServiceType.MSG_SERVICE,"0x1000",PluginId,data), requestcode);
        }

    }
}
