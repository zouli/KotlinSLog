package com.riverside.skeleton.kotlin.slog.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.riverside.skeleton.kotlin.slog.printer.FilePrinter
import com.riverside.skeleton.kotlin.slog.printer.NetPrinter
import com.riverside.skeleton.kotlin.slog.SLog
import com.riverside.skeleton.kotlin.slog.printer.SLogPrinter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        //Logcat输出器
        SLog.TAG_FORMAT =
            "{{now|yyyy-MM-dd HH:mm:ss}} [{{thread_name}}({{thread_id}}):{{filename}}({{line_number}}):{{method_name}}]"
        SLog.addPrinter(SLogPrinter())

        //文件输出器
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            FilePrinter.FILENAME_FORMAT = "/SLog{{now|yyyyMMdd}}.log"
            FilePrinter.MSG_FORMAT = "{{level_long}}/{{tag}}: {{msg}}"
            SLog.addPrinter(
                FilePrinter(
                    Environment.getExternalStorageDirectory().toString() + "/SLog"
                )
            )
        }

        //网络接口输出器
        SLog.addPrinter(NetPrinter { level, tag, msg, throwable ->
            //调用网络接口
            Log.i(tag, msg)
        })

        SLog.w(null)

        btn_print.setOnClickListener {
            runBlocking {
                val channel = Channel<String>()
                launch { sendString(channel, "aaa") }
                launch { sendString(channel, "bbb") }
                repeat(100) {
                    SLog.w(channel.receive())
                }
                coroutineContext.cancelChildren()
            }
        }
    }

    suspend fun sendString(channel: SendChannel<String>, s: String) {
        var i = 0
        while (true) {
            channel.send(s + i++)
        }
    }
}