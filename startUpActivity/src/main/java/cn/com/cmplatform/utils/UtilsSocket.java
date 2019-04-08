package cn.com.cmplatform.utils;

/**
 * Created by Neng on 2016/8/12.
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import cn.com.cmplatform.gameplatform.BuildConfig;

public class UtilsSocket extends Thread {
    private String ip = "192.168.191.1";
    private int port = 6888;
    private String TAG = "socket thread";
    private int timeout = 10000;
    private String message;

    public Socket client = null;
    PrintWriter out;
    BufferedReader in;
    public boolean isRun = true;
    Handler inHandler;
    Handler outHandler;
    Context ctx;
    private String TAG1 = "===Send===";
    SharedPreferences sp;

    int i = 1;

    public UtilsSocket(Handler handlerin, Handler handlerout, Context context, String arg) {
        inHandler = handlerin;
        outHandler = handlerout;
        ctx = context;
        message = arg;
        if(BuildConfig.DEBUG)
            Log.i(TAG, "创建线程socket");
        return;
    }

    /**
     * 连接socket服务器
     */
    public void conn() {

        try {
            initdate();
            if(BuildConfig.DEBUG)
                Log.i(TAG, "连接中……");
            client = new Socket(ip, port);
            client.setSoTimeout(timeout);// 设置阻塞时间
            if(BuildConfig.DEBUG)
                Log.i(TAG, "连接成功");
            in = new BufferedReader(new InputStreamReader(
                    client.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    client.getOutputStream())), true);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "输入输出流获取成功");
        } catch (UnknownHostException e) {
            if(BuildConfig.DEBUG)
                Log.i(TAG, "连接错误UnknownHostException 重新获取");
            e.printStackTrace();
            conn();
        } catch (IOException e) {
            if(BuildConfig.DEBUG)
                Log.i(TAG, "连接服务器io错误");
            e.printStackTrace();
        } catch (Exception e) {
            if(BuildConfig.DEBUG)
                Log.i(TAG, "连接服务器错误Exception" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void initdate() {
        sp = ctx.getSharedPreferences("SP", ctx.MODE_PRIVATE);
        ip = sp.getString("ipstr", ip);
        port = Integer.parseInt(sp.getString("port", String.valueOf(port)));
        if(BuildConfig.DEBUG)
            Log.i(TAG, "获取到ip端口:" + ip + ";" + port);
    }

    /**
     * 实时接受数据
     */
    @Override
    public void run() {
        if(BuildConfig.DEBUG)
            Log.i(TAG, "线程socket开始运行");
        conn();
        if(BuildConfig.DEBUG)
            Log.i(TAG, "1.run开始");
        String line = "";
        Send(message);
        while (isRun) {
            try {

                if (client != null) {
                    if(BuildConfig.DEBUG)
                        Log.i(TAG, "2.检测数据");
                    // while ((line = in.readLine())  != null) {

                    if (i == 1) {
                        line = "111";
                        if(BuildConfig.DEBUG)
                            Log.i(TAG, "3.getdata" + line + " len=" + line.length());
                        if(BuildConfig.DEBUG)
                            Log.i(TAG, "4.start set Message");
                        Message msg = inHandler.obtainMessage();
                        msg.obj = line;
                        inHandler.sendMessage(msg);// 结果返回给UI处理
                        if(BuildConfig.DEBUG)
                            Log.i(TAG1, "5.send to handler");
                        i = 0;
                    }
                    //}
                } else {
                    if(BuildConfig.DEBUG)
                        Log.i(TAG, "没有可用连接");
                    conn();
                }
            } catch (Exception e) {
                if(BuildConfig.DEBUG)
                    Log.i(TAG, "数据接收错误" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送数据
     *
     * @param mess
     */
    public void Send(String mess) {
        try {
            if (client != null) {
                Log.i(TAG1, "发送" + mess + "至"
                        + client.getInetAddress().getHostAddress() + ":"
                        + String.valueOf(client.getPort()));
                out.println(mess);
                out.flush();
                if(BuildConfig.DEBUG)
                    Log.i(TAG1, "发送成功");
                Message msg = outHandler.obtainMessage();
                msg.obj = mess;
                msg.what = 1;
                outHandler.sendMessage(msg);// 结果返回给UI处理
            } else {
                if(BuildConfig.DEBUG)
                    Log.i(TAG, "client 不存在");
                Message msg = outHandler.obtainMessage();
                msg.obj = mess;
                msg.what = 0;
                outHandler.sendMessage(msg);// 结果返回给UI处理
                if(BuildConfig.DEBUG)
                    Log.i(TAG, "连接不存在重新连接");
                conn();
            }

        } catch (Exception e) {
            if(BuildConfig.DEBUG)
                Log.i(TAG1, "send error");
            e.printStackTrace();
        } finally {
            if(BuildConfig.DEBUG)
                Log.i(TAG1, "发送完毕");

        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        try {
            if (client != null) {
                if(BuildConfig.DEBUG)
                    Log.i(TAG, "close in");
                in.close();
                if(BuildConfig.DEBUG)
                    Log.i(TAG, "close out");
                out.close();
                if(BuildConfig.DEBUG)
                    Log.i(TAG, "close client");
                client.close();
            }
        } catch (Exception e) {
            if(BuildConfig.DEBUG)
                Log.i(TAG, "close err");
            e.printStackTrace();
        }
    }
}