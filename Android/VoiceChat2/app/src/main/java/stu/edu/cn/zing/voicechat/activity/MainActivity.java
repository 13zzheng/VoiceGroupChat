package stu.edu.cn.zing.voicechat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import stu.edu.cn.zing.voicechat.mywidget.LineEditText;
import stu.edu.cn.zing.voicechat.R;


public class MainActivity extends Activity {

    private LineEditText letUserName;
    private Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//»•µÙ±ÍÃ‚¿∏
        setContentView(R.layout.activity_main);

        btLogin = (Button)findViewById(R.id.btLogin);
        letUserName = (LineEditText)findViewById(R.id.letUserName);

        letUserName.setLineColor(getResources().getColor(R.color.blue));

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = letUserName.getText().toString();
                if (name.length() != 0) {
                    System.out.println(name.length());
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, AudioActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("userName",name);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
