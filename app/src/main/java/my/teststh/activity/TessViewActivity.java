package my.teststh.activity;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import my.teststh.R;

/**
 * Created by jingwenpeng on 2017/8/31.
 */
public class TessViewActivity extends AppCompatActivity {
    private ImageView iv;
    private TextView tvResult;
    private static final String TAG = "TessViewActivity";
    /**
     * TessBaseAPI初始化用到的第一个参数，是个目录。
     */
    private static final String DATAPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    /**
     * 在DATAPATH中新建这个目录，TessBaseAPI初始化要求必须有这个目录。
     */
    private static final String tessdata = DATAPATH + File.separator + "tessdata";
    /**
     * TessBaseAPI初始化测第二个参数，就是识别库的名字不要后缀名。
     */
    private static final String DEFAULT_LANGUAGE = "chi_sim";
    /**
     * assets中的文件名
     */
    private static final String DEFAULT_LANGUAGE_NAME = DEFAULT_LANGUAGE + ".traineddata";
    /**
     * 保存到SD卡中的完整文件名
     */
    private static final String LANGUAGE_PATH = tessdata + File.separator + DEFAULT_LANGUAGE_NAME;
    /**
     * 权限请求值
     */
    private static final int PERMISSION_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tessview);
        iv = (ImageView) findViewById(R.id.imageview);
        tvResult = (TextView) findViewById(R.id.tv_result);
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
//                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
//            }
//        }
//        //Android6.0之前安装时就能复制，6.0之后要先请求权限，所以6.0以上的这个方法无用。
//        copyToSD(LANGUAGE_PATH, DEFAULT_LANGUAGE_NAME);


        tessThread.start();

    }

    public void copyToSD(final String path, final String name) {
        Log.i("MainActivity", "copyToSD: " + path);
        Log.i("MainActivity", "copyToSD: " + name);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //如果存在就删掉
                File f = new File(path);
                if (f.exists()) {
                    f.delete();
                }
                if (!f.exists()) {
                    File p = new File(f.getParent());
                    if (!p.exists()) {
                        p.mkdirs();
                    }
                    try {
                        f.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                InputStream is = null;
                OutputStream os = null;
                try {
                    is = getAssets().open(name);
                    File file = new File(path);
                    os = new FileOutputStream(file);
                    byte[] bytes = new byte[2048];
                    int len = 0;
                    while ((len = is.read(bytes)) != -1) {
                        os.write(bytes, 0, len);
                    }
                    os.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                        if (os != null)
                            os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
//                tessThread.start();
            }
        }).start();
    }

    Thread tessThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Log.i(TAG, "run: kaishi " + System.currentTimeMillis());
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.timg);
                Log.i(TAG, "run: bitmap " + System.currentTimeMillis());
                TessBaseAPI tessBaseAPI = new TessBaseAPI();
                Log.e("LANGUAGE_PATH=", LANGUAGE_PATH);
                boolean b = tessBaseAPI.init(DATAPATH, DEFAULT_LANGUAGE);
                Log.e(TAG, b + "");
                tessBaseAPI.setImage(bitmap);
                final String text = tessBaseAPI.getUTF8Text();
                Log.i(TAG, "run: text " + System.currentTimeMillis() + text);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvResult.setText(text);
                    }
                });
                tessBaseAPI.end();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });

    /**
     * 请求到权限后在这里复制识别库
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionsResult: " + grantResults[0]);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "onRequestPermissionsResult: copy");
                    copyToSD(LANGUAGE_PATH, DEFAULT_LANGUAGE_NAME);
                }
                break;
            default:
                break;
        }
    }
}
