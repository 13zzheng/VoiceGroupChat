package stu.edu.cn.zing.voicechat.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import stu.edu.cn.zing.voicechat.presenter.AudioPresenter;
import stu.edu.cn.zing.voicechat.mvpinterface.IAudioView;
import stu.edu.cn.zing.voicechat.mywidget.LineEditText;
import stu.edu.cn.zing.voicechat.R;
import stu.edu.cn.zing.voicechat.model.User;


public class AudioActivity extends Activity implements IAudioView {

    private Button btAudio;
    private Button btJump;
    private Button btJumpBack;
    private Button btSendText;

    private TextView tvTitle;

    private LineEditText letInput;
    private AudioPresenter audioPresenter;
    private LinearLayout llText;
    private LinearLayout llVoice;
    private ListView lvChat;

    private User user;

    private final int TYPE_1 = 0;
    private final int TYPE_2 = 1;
    private final int TYPE_3 = 2;

    //放ListView的数据
    private List<String> name = new ArrayList<>();
    private List<String> text = new ArrayList<>();
    private List<String> filePath = new ArrayList<>();
    private List<Float> time = new ArrayList<>();
    private List<Integer> type = new ArrayList<>();

    private MyAdapter myAdapter;


    public static Handler handler;

    private  void setHandler() {
        handler = new Handler(){

            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1: //更新文本消息
                        Bundle bundle = msg.getData();
                        String name = bundle.getString("name");
                        String text = bundle.getString("text");
                        showText(name,text);
                        break;

                    case 2:
                        Bundle bundle1 = msg.getData();
                        String name1 = bundle1.getString("name");
                        String filePath = bundle1.getString("filePath");
                        float time = bundle1.getFloat("time");
                        showVoice(name1,time,filePath);
                        break;

                    case 3:
                        Bundle bundle2 = msg.getData();
                        String text1 = bundle2.getString("text");
                        showServerMsg(text1);
                        break;
                }
            }
        };
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_audio);

        audioPresenter = new AudioPresenter(this);
        findViews();
        setOnClickListener();
        setHandler();
        //登录
        letInput.setLineColor(getResources().getColor(R.color.green));
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        String userName = bundle.getString("userName");
        user = new User(userName);
        audioPresenter.login(user);

        tvTitle.setText(user.getUserName());



        myAdapter = new MyAdapter(this);
        lvChat.setAdapter(myAdapter);


    }

    private void findViews() {
        letInput = (LineEditText) findViewById(R.id.letInput);
        btAudio = (Button)findViewById(R.id.btAudio);
        llText = (LinearLayout) findViewById(R.id.llText);
        letInput = (LineEditText) findViewById(R.id.letInput);
        llVoice = (LinearLayout) findViewById(R.id.llVoice);
        lvChat = (ListView) findViewById(R.id.lvChat);
        btSendText = (Button) findViewById(R.id.btSendText);
        btJump = (Button) findViewById(R.id.btJump);
        btJumpBack = (Button) findViewById(R.id.btJumpBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);

    }

    private void setOnClickListener() {

        btSendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = letInput.getText().toString();
                if (text.length() > 0) {
                    audioPresenter.sendText(text);
                    letInput.setText("");
                }
            }
        });


        btJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llText.setVisibility(View.INVISIBLE);
                llVoice.setVisibility(View.VISIBLE);
            }
        });

        btJumpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llVoice.setVisibility(View.INVISIBLE);
                llText.setVisibility(View.VISIBLE);
            }
        });


        btAudio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btAudio.setText("Release to send");
                        audioPresenter.startRecoder(); //开始录音
                        break;
                    case MotionEvent.ACTION_UP:
                        btAudio.setText("Hold down to speak");
                        audioPresenter.stopRecoder(); //录音完毕
                        break;
                }

                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioPresenter.disconnect();
        System.out.println("Ativity 退出");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_audio, menu);
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

    @Override
    public void showDialog() {

    }

    @Override
    public void updateDialog(int level) {

    }

    public void showText(String name, String text) {

        this.name.add(name);
        this.text.add(text);
        this.time.add(-1f);
        this.filePath.add("");
        this.type.add(TYPE_1);
        myAdapter.notifyDataSetChanged();
    }

    public void showVoice(String name, float time, String filePath) {
        this.name.add(name);
        this.text.add("");
        this.time.add(time);
        this.filePath.add(filePath);
        this.type.add(TYPE_2);
        myAdapter.notifyDataSetChanged();
    }

    public void showServerMsg(String text) {

        System.out.println("接收服务器信息" + text);
        this.name.add("server");
        this.text.add(text);
        this.time.add(0f);
        this.filePath.add("");
        this.type.add(TYPE_3);
        myAdapter.notifyDataSetChanged();

    }


    public class MyAdapter extends BaseAdapter {
        private Context context;

        private LayoutInflater layoutInflater;


        private final int VIEW_TYPE =3 ;
        private final int TYPE_1 = 0;
        private final int TYPE_2 = 1;
        private final int TYPE_3 = 2;

        public MyAdapter(Context context) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getItemViewType(int position) {
            return type.get(position);
        }
        @Override
        public int getViewTypeCount() {
            return VIEW_TYPE;
        }

        @Override
        public int getCount() {
            return name.size();
        }

        @Override
        public Object getItem(int position) {
            return name.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            int type = getItemViewType(position);
            String userName;

            if (convertView == null) {

                switch (type) {
                    case TYPE_1:
                        convertView = layoutInflater.inflate(R.layout.list_layout_text, parent, false);
                        userName = name.get(position);
                        LinearLayout llColorBlock = (LinearLayout) convertView.findViewById(R.id.llColorBlock);
                        TextView tvUserName1 = (TextView) convertView.findViewById(R.id.tvUserName);
                        TextView tvText = (TextView) convertView.findViewById(R.id.tvText);
                        tvUserName1.setText( userName + ":");
                        tvText.setText(text.get(position));
                        if (userName.equals(user.getUserName())) {
                            llColorBlock.setBackgroundColor(getResources().getColor(R.color.green));
                        }

                        break;

                    case TYPE_2:
                        convertView = layoutInflater.inflate(R.layout.list_layout_voice,parent,false);
                        userName = name.get(position);
                        TextView tvUserName2 = (TextView) convertView.findViewById(R.id.tvUserName);
                        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
                        tvUserName2.setText(name.get(position) + ":");
                        tvTime.setText(String.valueOf(Math.round(time.get(position))) + " ''");
                        Button btVoice = (Button) convertView.findViewById(R.id.btVoice);

                        if (userName.equals(user.getUserName())) {
                           tvTime.setTextColor(getResources().getColor(R.color.green));
                        }

                        btVoice.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (audioPresenter.isPlaying()) {
                                    audioPresenter.stopPlay();
                                }else {
                                    System.out.println("file" + filePath.get(position));
                                    audioPresenter.palyAudio(filePath.get(position));
                                }
                            }
                        });

                        break;

                    case TYPE_3:
                        convertView = layoutInflater.inflate(R.layout.list_layout_server,parent,false);
                        TextView tvServer = (TextView) convertView.findViewById(R.id.tvServer);
                        tvServer.setText(text.get(position));
                        break;
                }
            }


            return convertView;
        }
    }
}
