package basenet.better.basenet;

import android.Manifest;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import basenet.better.basenet.utils.PermissionUtils;
import lib.basenet.okhttp.OkHttpRequest;
import lib.basenet.request.AbsRequest;
import lib.basenet.request.AbsRequestCallBack;
import lib.basenet.volley.VolleyRequest;

public class OkHttpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText url;
    EditText timeout;

    Button get;
    Button post;
    Button down;

    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Okhttp");
        setContentView(R.layout.activity_ok_http);
        url = (EditText) findViewById(R.id.url);
        timeout = (EditText) findViewById(R.id.timeout);
        get = (Button) findViewById(R.id.get);
        post = (Button) findViewById(R.id.post);
        down = (Button) findViewById(R.id.down);
        result = (TextView) findViewById(R.id.result);
        result.setMovementMethod(ScrollingMovementMethod.getInstance());

        get.setOnClickListener(this);
        post.setOnClickListener(this);
        down.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        result.setText("");
        long tTimeout = Integer.parseInt(timeout.getText().toString()) * 1000;
        String tUrl = url.getText().toString();

        final AbsRequest.Builder builder = new OkHttpRequest.Builder()
                .timeout(tTimeout)
                .url(tUrl)
                .callback(new AbsRequestCallBack() {
                    @Override
                    public void onSuccess(final Object o) {
                        super.onSuccess(o);
                        result.post(new Runnable() {
                            @Override
                            public void run() {
                                result.setText(result.getText() + "\n" + o.toString());
                            }
                        });
                    }

                    @Override
                    public void onFailure(final Throwable e) {
                        super.onFailure(e);
                        result.post(new Runnable() {
                            @Override
                            public void run() {
                                result.setText(result.getText() + "\n" + e.toString());
                            }
                        });
                    }

                    @Override
                    public void onProgressUpdate(final long contentLength, final long bytesRead, final boolean done) {
                        super.onProgressUpdate(contentLength, bytesRead, done);
                        result.post(new Runnable() {
                            @Override
                            public void run() {
                                result.setText(result.getText() + "\n" + String.format("total:%s, already:%s, isDone: %s", contentLength, bytesRead, done));
                            }
                        });
                    }
                });

        if (R.id.get == v.getId()) {
            builder.type(AbsRequest.RequestType.GET).build().request();
        } else if (R.id.post == v.getId()) {
            Map<String,String> params = new HashMap<>();
            params.put("key1", "value1");
            params.put("key2", "value2");
            builder.type(AbsRequest.RequestType.POST).body(params).build().request();
        } else if (R.id.down == v.getId()) {
            checkPermissionAndGo(builder);

        }
    }

    private void checkPermissionAndGo(final AbsRequest.Builder builder) {
        PermissionUtils.checkOnePermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, "需要访问存储权限", new Runnable() {
            @Override
            public void run() {
                String imgUrl = "https://raw.githubusercontent.com/zhaoyubetter/MarkdownPhotos/master/C/52C2504C.png";
                final String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                File file = new File(absolutePath + "/down.jpg");
                builder.url(imgUrl).downFile(file).build().request();
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestResult(requestCode, permissions, grantResults, new Runnable() {
            @Override
            public void run() {

            }
        }, null);
    }
}