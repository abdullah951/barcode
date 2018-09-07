package info.androidhive.barcode;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "Main2Activity";
    Context context;

    LinearLayout checking ;
    LinearLayout checked;
    LinearLayout fake;
    ImageView retry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        context = this;

        checking = findViewById(R.id.check);
        checked = findViewById(R.id.checked);
        fake = findViewById(R.id.fake);
        retry = findViewById(R.id.retry);
        retry.setOnClickListener(this);

        Intent i = getIntent();
        final String mac_address = i.getStringExtra("mac_address");
        final String qrcode = i.getStringExtra("qrcode");

        java.util.Date currentTime = Calendar.getInstance().getTime();
        Timestamp timestamp = new Timestamp(currentTime.getTime());


        //String uri1 = Constant.getValidityofQrcode + "?macAddress=" + mac_address + "&_product_qr_code=" + qrcode ;
        JSONObject js = new JSONObject();
        try {
            js.put("macAddress", mac_address);
            js.put("_product_qr_code", qrcode);
            js.put("date", timestamp);

        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        VolleySet.SendUserData(js, context, Constant.getValidityofQrcode, new ServerCallBack() {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onError(String message) {
                Toast.makeText(Main2Activity.this, "Database is under construction. Please try later", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                Log.e(TAG, "yahoo");
                String code = response.getString("code");
                if(code.equals("1")){
                    try {
                        MediaPlayer mPlayer = new MediaPlayer();
                        AssetFileDescriptor descriptor = getAssets().openFd("original.mp3");

                        mPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());

                        descriptor.close();

                        mPlayer.prepare();
                        mPlayer.setLooping(false);
                        mPlayer.start();
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                    showChecked();
                }else if(code.equals("2")){
                    try {
                        MediaPlayer mPlayer = new MediaPlayer();
                        AssetFileDescriptor descriptor = getAssets().openFd("fake.mp3");
                        mPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                        descriptor.close();

                        mPlayer.prepare();
                        mPlayer.setLooping(false);
                        mPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    showFake();
                }
            }
        });


    }

    private void showChecking(){
        checking.setVisibility(View.VISIBLE);
        checked.setVisibility(View.GONE);
        fake.setVisibility(View.GONE);
    }
    private void showChecked(){
        checking.setVisibility(View.GONE);
        checked.setVisibility(View.VISIBLE);
        fake.setVisibility(View.GONE);
    }
    private void showFake(){
        checking.setVisibility(View.GONE);
        checked.setVisibility(View.GONE);
        fake.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.retry) {
            finish();
        }
    }
}
