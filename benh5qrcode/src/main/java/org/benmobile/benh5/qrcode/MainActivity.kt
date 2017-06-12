package org.benmobile.benh5.qrcode

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.zbar.lib.CaptureActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this, CaptureActivity::class.java)
        startActivityForResult(intent, CaptureActivity.REQUEST_CODE_SCAN)
        this.finish()
    }
}
