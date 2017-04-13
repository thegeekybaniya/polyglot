package in.thegeekybaniya.polyglot;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import in.thegeekybaniya.polyglot.POJO.GetTranslate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    Retrofit retrofit;


    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn= (Button) findViewById(R.id.button);

        Intent myIntent = new Intent(MainActivity.this, ClipboardMonitor.class);
        startService(myIntent);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, FloatingWindow.class);
                startService(myIntent);





            }
        });


        final ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener( new ClipboardManager.OnPrimaryClipChangedListener() {
            public void onPrimaryClipChanged() {
                String a = clipboard.getText().toString();
                Toast.makeText(getBaseContext(),"Copy:\n"+a,Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(MainActivity.this, FloatingWindow.class);
                startService(myIntent);
                retrofit= new Retrofit.Builder()
                        .baseUrl("https://translate.yandex.net")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final ApiInterface service= retrofit.create(ApiInterface.class);

                Call<GetTranslate> result=service.getLang("trnsl.1.1.20170412T152446Z.640993651b8fd9d5.06e9cd2efa56ab30c13bf05c6e2f160b410acfb4", URLEncoder.encode(a),"hi", "plain",1);


                result.enqueue(new Callback<GetTranslate>() {
                    public static final String TAG = "TRansaction";

                    @Override
                    public void onResponse(Call<GetTranslate> call, Response<GetTranslate> response) {

                        List<String> l= response.body().getText();


                        try {
                            Log.d(TAG, "onResponse: "+ URLDecoder.decode(l.get(0), "UTF-8"));
                            Toast.makeText(MainActivity.this, "Translation:"+URLDecoder.decode(l.get(0), "UTF-8") , Toast.LENGTH_LONG).show();


                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetTranslate> call, Throwable t) {

                    }
                });





            }
        });






    }
}
