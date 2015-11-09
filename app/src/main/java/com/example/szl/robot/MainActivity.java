package com.example.szl.robot;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends Activity implements HttpGetDataListener, OnClickListener {

    HttpData mHttpData;
    private List<ListData> lists;
    private ListView listView;
    private EditText sendText;
    private Button sendButton;
    private TextAdapter textAdapter;
    private String contentStr;
    private String welcomeStrs[];
    private double currentTime, oldTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.lv);
        sendText = (EditText) findViewById(R.id.sendText);
        sendButton = (Button) findViewById(R.id.sendButton);
        lists = new ArrayList<>();
        sendButton.setOnClickListener(this);
        lists.add(new ListData(getRandomWelcomeTips(), ListData.RECEIVE, getTime()));
        textAdapter = new TextAdapter(lists, this);
        listView.setAdapter(textAdapter);
    }

    @Override
    public void getDataUrl(String data) {
        parseText(data);
    }

    public void parseText(String str) {
        try {
            JSONObject jb = new JSONObject(str);
            ListData listData = new ListData(jb.getString("text"), ListData.RECEIVE, getTime());
            lists.add(listData);
            textAdapter.notifyDataSetChanged();
        } catch (Exception e) {

        }
    }


    public String getRandomWelcomeTips() {
        String welcomStr = null;
        welcomeStrs = this.getResources().getStringArray(R.array.welconmeTips);
        int index = (int) (Math.random() * (welcomeStrs.length - 1));
        welcomStr = welcomeStrs[index];
        return welcomStr;
    }

    @Override
    public void onClick(View v) {
        contentStr = sendText.getText().toString();
        String newstr = contentStr.replace(" ", "");
        sendText.setText("");
        ListData listData = new ListData(contentStr, ListData.SEND, getTime());
        lists.add(listData);
        textAdapter.notifyDataSetChanged();
        mHttpData = new HttpData("http://www.tuling123.com/openapi/api?key=997ed7fa8b069871c0d28e61ca03f1af&info=" + newstr, this);
        mHttpData.execute();
    }

    private String getTime() {
        currentTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年mm月dd日 HH:MM:SS");
        Date date = new Date();
        String string = formatter.format(date);
        if (currentTime - oldTime > 10000) {
            oldTime = currentTime;
            return string;
        }
        return "";
    }
}
