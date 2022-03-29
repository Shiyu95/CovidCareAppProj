package edu.neu.covidcareapp.activities.test;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import edu.neu.covidcareapp.R;

public class HeartRateActivity extends AppCompatActivity {

    //曲线
    private Timer timer = new Timer();
    //Timer任务，与Timer配套使用
    private TimerTask task;
    private static int gx;
    private static int j;

    private static double flag = 1;
    private Handler handler;
    private String title = "pulse";
    private Context context;
    private int addX = -1;
    double addY;
    int[] xv = new int[300];
    int[] yv = new int[300];
    int[] hua=new int[]{9,10,11,12,13,14,13,12,11,10,9,8,7,6,7,8,9,10,11,10,10};

    private static final AtomicBoolean processing = new AtomicBoolean(false);
    //Android手机预览控件
    private static SurfaceView preview = null;
    //预览设置信息
    private static SurfaceHolder previewHolder = null;
    //Android手机相机句柄
    private static Camera camera = null;
    //private static View image = null;
    private static TextView mTV_Heart_Rate = null;
    private static TextView mTV_Avg_Pixel_Values = null;
    private static TextView mTV_pulse = null;
    private static WakeLock wakeLock = null;
    private static int averageIndex = 0;
    private static final int averageArraySize = 4;
    private static final int[] averageArray = new int[averageArraySize];
    private int heart;

    /**
     * 类型枚举
     */
    public static enum TYPE {
        GREEN, RED
    };

    //设置默认类型
    private static TYPE currentType = TYPE.GREEN;
    //获取当前类型
    public static TYPE getCurrent() {
        return currentType;
    }
    //心跳下标值
    private static int beatsIndex = 0;
    //心跳数组的大小
    private static final int beatsArraySize = 3;
    //心跳数组
    private static final int[] beatsArray = new int[beatsArraySize];
    //心跳脉冲
    private static double beats = 0;
    //开始时间
    private static long startTime = 0;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate);
        mTV_Heart_Rate = findViewById(R.id.mTV_Heart_Rate);
        initConfig();

    }

    /**
     * 初始化配置
     */
    @SuppressWarnings("deprecation")
    private void initConfig() {
        //曲线
        context = getApplicationContext();


        //这里的Handler实例将配合下面的Timer实例，完成定时更新图表的功能
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //刷新图表
                updateChart();
                super.handleMessage(msg);
            }
        };

        task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };

        timer.schedule(task, 1,20);           //曲线
        //获取SurfaceView控件
        preview = (SurfaceView) findViewById(R.id.id_preview);
        previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
    }

    //	曲线
    @Override
    public void onDestroy() {
        //当结束程序时关掉Timer
        timer.cancel();
        super.onDestroy();
    };





    /**
     * 更新图标信息
     */
    private void updateChart() {
        //设置好下一个需要增加的节点
        if(flag == 1) {
            addY = 10;
        }
        else {
            flag = 1;
            if(gx < 200){
                if(hua[20] > 1){
                    hua[20] = 0;
                }
                hua[20]++;
                return;
            }
            else {
                hua[20] = 10;
            }
            j = 0;
        }
        if(j < 20){
            addY=hua[j];
            j++;
        }


    } //曲线


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onResume() {
        super.onResume();
        wakeLock.acquire();
        camera = Camera.open();
        startTime = System.currentTimeMillis();
    }

    @Override
    public void onPause() {
        super.onPause();
        wakeLock.release();
        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
    }


    /**
     * 相机预览方法
     * 这个方法中实现动态更新界面UI的功能，
     * 通过获取手机摄像头的参数来实时动态计算平均像素值、脉冲数，从而实时动态计算心率值。
     */
    private static PreviewCallback previewCallback = new PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera cam) {
            if (data == null) {
                throw new NullPointerException();
            }
            Camera.Size size = cam.getParameters().getPreviewSize();
            if (size == null) {
                throw new NullPointerException();
            }
            if (!processing.compareAndSet(false, true)) {
                return;
            }
            int width = size.width;
            int height = size.height;

            //图像处理
            int imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(data.clone(),height,width);
            gx = imgAvg;

            if (imgAvg == 0 || imgAvg == 255) {
                processing.set(false);
                return;
            }
            //计算平均值
            int averageArrayAvg = 0;
            int averageArrayCnt = 0;
            for (int i = 0; i < averageArray.length; i++) {
                if (averageArray[i] > 0) {
                    averageArrayAvg += averageArray[i];
                    averageArrayCnt++;
                }
            }

            //计算平均值
            int rollingAverage = (averageArrayCnt > 0)?(averageArrayAvg/averageArrayCnt):0;
            TYPE newType = currentType;
            if (imgAvg < rollingAverage) {
                newType = TYPE.RED;
                if (newType != currentType) {
                    beats++;
                    flag=0;
                }
            } else if (imgAvg > rollingAverage) {
                newType = TYPE.GREEN;
            }

            if(averageIndex == averageArraySize) {
                averageIndex = 0;
            }
            averageArray[averageIndex] = imgAvg;
            averageIndex++;

            if (newType != currentType) {
                currentType = newType;
            }

            //获取系统结束时间（ms）
            long endTime = System.currentTimeMillis();
            double totalTimeInSecs = (endTime - startTime) / 1000d;
            if (totalTimeInSecs >= 2) {
                double bps = (beats / totalTimeInSecs);
                int dpm = (int) (bps * 60d);
                if (dpm < 30 || dpm > 180|| imgAvg < 200) {
                    //获取系统开始时间（ms）
                    startTime = System.currentTimeMillis();
                    //beats心跳总数
                    beats = 0;
                    processing.set(false);
                    return;
                }

                if(beatsIndex == beatsArraySize) {
                    beatsIndex = 0;
                }
                beatsArray[beatsIndex] = dpm;
                beatsIndex++;

                int beatsArrayAvg = 0;
                int beatsArrayCnt = 0;
                for (int i = 0; i < beatsArray.length; i++) {
                    if (beatsArray[i] > 0) {
                        beatsArrayAvg += beatsArray[i];
                        beatsArrayCnt++;
                    }
                }
                int beatsAvg = (beatsArrayAvg / beatsArrayCnt);
                mTV_Heart_Rate.setText("Heart:"+ beatsAvg );
                //获取系统时间（ms）
                startTime = System.currentTimeMillis();
                beats = 0;
            }
            processing.set(false);
        }
    };

    /**
     * 预览回调接口
     */
    private static SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        //创建时调用
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera.setPreviewDisplay(previewHolder);
                camera.setPreviewCallback(previewCallback);
            } catch (Throwable t) {
                Log.e("PreviewDemo-surfaceCallback","Exception in setPreviewDisplay()", t);
            }
        }

        //当预览改变的时候回调此方法
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            Camera.Size size = getSmallestPreviewSize(width, height, parameters);
            if (size != null) {
                parameters.setPreviewSize(size.width, size.height);
            }
            camera.setParameters(parameters);
            camera.startPreview();
        }

        //销毁的时候调用
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };

    /**
     * 获取相机最小的预览尺寸
     */
    private static Camera.Size getSmallestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;
        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                }
                else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;
                    if (newArea < resultArea) {
                        result = size;
                    }
                }
            }
        }
        return result;
    }
}



