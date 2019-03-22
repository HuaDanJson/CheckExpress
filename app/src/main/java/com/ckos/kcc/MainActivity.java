package com.ckos.kcc;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.ckos.kcc.sqlite.DBHelper;

public class MainActivity extends Activity {

    //数据库相关
    private SQLiteDatabase mDatabase;
    private DBHelper mHelper;
    private TextView tvNoInput;
    private TextView tvCopInput;
    private Button btnResetAll;
    private Button btnStartSch;
    private Button btnHisSch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //初始化
        initview();

        mHelper = new DBHelper(this);
        mDatabase = mHelper.getWritableDatabase();
        
    }

    //结果返回
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0x123) {

            if (resultCode == 2) {

                run_search(data.getStringExtra("NOSTR"),data.getStringExtra("COPSTR"));

            }else if(resultCode == 0)

                btnResetAll.callOnClick();

        }

    }


    //主初始化
    private void initview() {

        tvNoInput = this.<TextView>findViewById(R.id.tvNoInput);
        btnResetAll = this.<Button>findViewById(R.id.btnResetAll);
        btnHisSch = this.<Button>findViewById(R.id.btnHisSch);
        btnStartSch = this.<Button>findViewById(R.id.btnStartSch);
        tvCopInput = this.<TextView>findViewById(R.id.tvCopInput);

        tvCopInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.copmenu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.sf:
                                tvCopInput.setText("顺丰");
                                return true;
                            case R.id.st:
                                tvCopInput.setText("申通");
                                return true;
                            case R.id.yt:
                                tvCopInput.setText("圆通");
                                return true;
                            case R.id.tt:
                                tvCopInput.setText("天天");
                                return true;
                            case R.id.bs:
                                tvCopInput.setText("百世");
                                return true;
                            case R.id.ems:
                                tvCopInput.setText("EMS");
                                return true;
                            case R.id.yd:
                                tvCopInput.setText("韵达");
                                return true;
                            case R.id.zt:
                                tvCopInput.setText("中通");
                                return true;
                        }

                        return true;

                    }
                });
            }});

        //历史搜索按钮事件
        btnHisSch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,HisActivity.class);
                startActivityForResult(intent,0x123);
            }
        });

        //重置按钮事件
        btnResetAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvNoInput.setText("");
                tvCopInput.setText("");
            }
        });

        //搜索按钮事件
        btnStartSch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String No_str;
                String btnSelt_str;
                //检查
                if(tvNoInput.getText().toString().trim().equals("")){
                    Toast.makeText(MainActivity.this,"单号不能为空,请重新输入！",Toast.LENGTH_LONG).show();
                    return;
                }else if(tvCopInput.getText().toString().trim().equals("")){
                    Toast.makeText(MainActivity.this,"快递公司没有选择,请重新选择！",Toast.LENGTH_LONG).show();
                    return;
                }else {

                    //数据获取
                    No_str = tvNoInput.getText().toString();
                    btnSelt_str = tvCopInput.getText().toString();

                }

                //调用搜索
                run_search(No_str, btnSelt_str);
            }
        });
    }


    //数据库新增操作
    private void insertData(String no_str, String btnSelt_str) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.NOSTR, no_str);
        values.put(DBHelper.COPSTR, btnSelt_str);
        mDatabase.insert(DBHelper.TABLE_NAME, null, values);
        Toast.makeText(this, "历史记录成功", Toast.LENGTH_SHORT).show();
    }

    //搜索主程序
    private void run_search(String no_str, String btnSelt_str) {

        //记录搜索记录
        insertData(no_str , btnSelt_str);

        //调用API主查询
        //快递公司ID编码
        String cop_id = "";

        switch(btnSelt_str){

            case "顺丰":
                cop_id = "shunfeng";
                break;

            case "韵达":
                cop_id = "yunda";
                break;

            case "中通":
                cop_id = "zhongtong";
                break;

            case "圆通":
                cop_id = "yuantong";
                break;

            case "申通":
                cop_id = "shentong";
                break;

            case "天天":
                cop_id = "tiantian";
                break;

            case "百世":
                cop_id = "huitongkuaidi";
                break;

            case "EMS":
                cop_id = "ems";
                break;

        }

        //目标URL
        String tagUrl = "https://m.kuaidi100.com/index_all.html?type=" + cop_id + "&postid=" + no_str +"&callbackurl=";

        Intent i = new Intent(MainActivity.this,ResultActivity.class);
        i.putExtra("tagUrl",tagUrl);
        startActivity(i);

    }

}
