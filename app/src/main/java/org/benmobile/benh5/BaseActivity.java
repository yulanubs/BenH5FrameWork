package org.benmobile.benh5;

import android.app.Activity;
import android.os.Bundle;

import org.benmobile.protocol.utlis.ValueUtils;

/**
 * Created by Jekshow on 2017/3/9.
 */

public class BaseActivity extends Activity {
   protected BenH5App mApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ValueUtils.isEmpty(mApp)){
        mApp= (BenH5App) getApplication();
        }
    }
}
