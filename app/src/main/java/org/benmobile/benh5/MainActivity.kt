package org.benmobile.benh5

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.ohmerhe.kolley.request.Http
import org.benmobile.benh5.config.ConstData
import org.benmobile.coolhttp.http.*
import org.benmobile.core.natvie.app.PluginClient
import org.benmobile.protocol.ComponentRegister
import org.benmobile.protocol.PluginMassage
import org.benmobile.protocol.Protocol
import org.benmobile.protocol.bean.ProtocolMsgBean
import org.benmobile.protocol.bean.PushPluginAction
import org.benmobile.protocol.config.ConstMsgType
import org.benmobile.protocol.config.ConstServiceType
import org.benmobile.protocol.config.PluginConstData
import org.benmobile.protocol.controller.QuestMsgController
import org.benmobile.protocol.controller.QuestProtocolManager
import org.benmobile.protocol.event.OnAppNetQuestListener
import org.benmobile.protocol.observer.PluginProtocolObserver
import org.benmobile.protocol.service.OnProtocolObserverListener
import org.benmobile.protocol.serviceimpl.AppNetQuestService
import org.benmobile.protocol.utlis.ValueUtils
import java.nio.charset.Charset
import java.util.HashMap

@Suppress("JAVA_CLASS_ON_COMPANION")
class MainActivity : BaseActivity(), OnProtocolObserverListener {
    private val tag ="org.benmobile.benh5"
    private val pluginid = "org.benmobile.qrcode"
    private lateinit  var  btn_qrcode: Button;
    private lateinit  var  btn_http: Button;
    private lateinit  var  btn_send: Button;
    private  lateinit  var  tv_time:TextView

    val requestcode = 1001
    /**
     * 观察者
     */
    private var mPluginProtocolObserver: PluginProtocolObserver? = null
    /**
     * 被观察者
     */
    private var mComponentRegister: ComponentRegister? = null
    /**
     * 观察者监听
     */
    private var mPluginMsgListener: OnProtocolObserverListener? = null
    private  var mpush: PushPluginAction? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Http.init(this)
        initObeerver()
        tv_time=findViewById(R.id.tv_time)as TextView;
        btn_qrcode= findViewById(R.id.btn_qrcode) as Button;
        btn_http= findViewById(R.id.btn_http) as Button;
        btn_send= findViewById(R.id.btn_send) as Button;
        btn_qrcode.setOnClickListener(View.OnClickListener {
            PluginClient.getInstance().launch(pluginid, this@MainActivity, false)

        })
        btn_send.setOnClickListener(View.OnClickListener {
            testApi()
        })
        btn_http.setOnClickListener(View.OnClickListener {
//            request()
            testNet()
        })
        Http.get {

            url = "http://182.254.241.141/BenH5Analysis/api/mobile/getSysTime.do"

            tag = this@MainActivity

            params {
                "q" - "shanghai"
                "appid" - "d7a98cf22463b1c0c3df4adfe5abbc77"
            }

            onStart { log("on start") }

            onSuccess { bytes ->
                tv_time.text=bytes.toString(Charset.defaultCharset())
                log("on success ${bytes.toString(Charset.defaultCharset())}")
            }

            onFail { error ->
                log("on fail ${error.toString()}")
            }

            onFinish { log( "on finish") }

        }
    }
    fun log(text: String) {
        Log.d("MainActivity", text)
    }


    /**
     * 中间件被改变的数据
     */
    override fun setPluginMsg(key: String?) {

        val pt = ComponentRegister.getInstance().getComponent(key)
        System.out.println("==========结果：" + pt.call("from plugin"))
        if (key == PluginConstData.JUMP_ACTION_TYPE) {
            val call = pt.call("from plugin")
            if (call is PluginMassage) {
                val pmsg = call as PluginMassage
                val toPackageName = pmsg.toPackageName
                if (ValueUtils.isStrNotEmpty(toPackageName)) {
                    PluginClient.getInstance().launch(toPackageName, this@MainActivity, false)

                }
            }
        } else if (key == requestcode.toString()) {
            val call = pt.call("")
            if (call is ProtocolMsgBean) {
                val msgBean = call as ProtocolMsgBean
                if (msgBean.getRequestCode() === requestcode) {
                    Toast.makeText(this@MainActivity, "消息：" + msgBean.getData(), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@MainActivity, "消息：系统异常！", Toast.LENGTH_LONG).show()
                }


            }
        } else if (key == 1002.toString() + "") {
            val call = pt.call("")
            if (call is ProtocolMsgBean) {
                val msgBean = call as ProtocolMsgBean
                if (msgBean.getRequestCode() === 1002) {
                    Toast.makeText(this@MainActivity, "二维码扫描结果：" + msgBean.getData(), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@MainActivity, "消息：系统异常！", Toast.LENGTH_LONG).show()
                }


            }
        }
        println("======requestCode：" + key)
        //                    Toast.makeText(MainActivity.this,"消息：响应码"+s,Toast.LENGTH_LONG).show();



    }
    /**
     * 初始化观察者
     */
    public fun initObeerver() {
        if (null == mComponentRegister) {
            if (null == mPluginProtocolObserver) {
                mPluginProtocolObserver =  PluginProtocolObserver(tag);

            }
            mComponentRegister = ComponentRegister.getInstance();
            var temp:PluginProtocolObserver?= null
            temp= mComponentRegister!!.addObserver(mPluginProtocolObserver) as PluginProtocolObserver?;
//            PluginProtocolObserver temp = (PluginProtocolObserver) mComponentRegister.addObserver(mPluginProtocolObserver);
            if (null != temp) {
                mPluginProtocolObserver = temp;
                mPluginMsgListener =  temp.getmOnProtocolObserverListener();
            } else {
                if (null == mPluginMsgListener) {
                    mPluginMsgListener = this;
                    mPluginProtocolObserver!!.setmOnProtocolObserverListener(mPluginMsgListener);
                }
            }
        }


        System.out.println("====initObeerver");
    }

    fun testNet() {
        val mRequestCode = 1100
        val eventAction = "0x10001"
        val data = HashMap<String, String>()
        data.put("name", "15773273445")
        data.put("password", "123456")
        //        mpush.setMsgType(ConstServiceType.INFRASTRUCTURE_SERVICE);
        //        mpush.setServiceType(ConstServiceType.Infrastructure.BENH5_APP_NET_FRAME);
        //        mpush.setEventAction(eventAction);
        //        mpush.setParam(data);
        //        mpush.setPluginId(tag);
        //        new MagController().pushPluginAction(mpush, mRequestCode, new GetMsgInfoListener() {
        //            @Override
        //            public void MsgInfo(String pluignId, int requestCode, int resultCode, Protocol msgBean) {
        //                if (mRequestCode==requestCode&&ValueUtils.isNotEmpty(msgBean)) {
        //                    ProtocolMsgBean msgBean1= (ProtocolMsgBean) msgBean.call("");
        //                    Toast.makeText(MainActivity.this, msgBean1.toString(), Toast.LENGTH_LONG).show();
        //                }
        //            }
        //        });

        val service = AppNetQuestService(mApp.configs.SERVER_BENH5_VPS, ConstData.LOGIN, eventAction, data, tag, mRequestCode, object : OnAppNetQuestListener {
            override fun onMsgSucceed(pluignId: String, requestCode: Int, resultCode: Int, msgBean: Protocol) {
                run {
                    if (mRequestCode == requestCode && org.benmobile.core.natvie.utils.ValueUtils.isNotEmpty(msgBean)) {
                        val msgBean1 = msgBean.call("") as ProtocolMsgBean
                        Toast.makeText(this@MainActivity, msgBean1.getData(), Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onMsgFailed(pluignId: String, requestCode: Int, resultCode: Int, msgBean: Protocol) {

            }
        })


    }
    private fun request() {
        // 测试接口见：http://api.nohttp.net

        val url = "http://api.nohttp.net/upload"
        val request = StringRequest(url, Method.POST)
        request.addParams("name", "yanzhenjie")
        request.addParams("pwd", 123)

        CoolHttp.asyncRequest(request, object : HttpListener<String> {
            override fun onSucceed(response: Response<String>) {
                val result = response.result
                Log.e("CoolHttp", result)
                Toast.makeText(this@MainActivity, result.toString(), Toast.LENGTH_LONG).show()
            }

            override fun onFailed(response: Response<String>) {
                Log.e("CoolHttp", "", response.exception)
                Toast.makeText(this@MainActivity, "请求失败", Toast.LENGTH_LONG).show()
            }
        })
    }
    fun testApi() {
        //向中间件推送一条文本消息
        QuestProtocolManager.getInstance().QusetProtocolPushMsgData(tag,"发送消息测试",requestcode);
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
