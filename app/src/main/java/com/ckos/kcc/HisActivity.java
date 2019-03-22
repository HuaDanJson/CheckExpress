package com.ckos.kcc;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ckos.kcc.sqlite.DBHelper;

public class HisActivity extends Activity {

    //数据库相关
    private SQLiteDatabase mDatabase;

    private ListView lvSearchData;
    private ArrayAdapter<ListCellData> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_his);

        //初始化
        initview();

        //读取历史信息
        queryData();
    }

    private void initview() {

        mDatabase = SQLiteDatabase.openOrCreateDatabase("data/data/com.ckos.kcc/databases/"+DBHelper.DB_NAME,null);

        myAdapter = new ArrayAdapter<ListCellData>(this,android.R.layout.simple_list_item_1);
        lvSearchData = this.<ListView>findViewById(R.id.lvSearchData);
        lvSearchData.setAdapter(myAdapter);
        lvSearchData.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                ListCellData data = myAdapter.getItem(position);
                deleteDataone(data.getNOSTR(),data.getCOPSTR());
                //Toast.makeText(HisActivity.this,data.getNOSTR()+data.getCOPSTR(), Toast.LENGTH_LONG).show();
                //读取历史信息
                queryData();
                return true;
            }
        });
        lvSearchData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ListCellData data = myAdapter.getItem(position);

                if(data.getCOPSTR().equals("")==true){
                    //清空历史记录
                    deleteData();
                    Intent i = new Intent();
                    setResult(0, i);
                    finish();
                }else {
                    //返回点击历史信息
                    Intent i = new Intent();
                    i.putExtra("NOSTR", data.getNOSTR());
                    i.putExtra("COPSTR", data.getCOPSTR());
                    setResult(2, i);
                    finish();
                }
            }
        });

    }

    // 数据库删除
    private void deleteDataone(String nostr,String copstr) {
        int count = mDatabase.delete(DBHelper.TABLE_NAME,DBHelper.NOSTR+"=? and "+DBHelper.COPSTR+"=?",new String[]{nostr,copstr});
        Toast.makeText(this, "该条记录已经清除！", Toast.LENGTH_SHORT).show();
    }

    // 数据库删除
    private void deleteData() {
        int count = mDatabase.delete(DBHelper.TABLE_NAME,null,null);
        Toast.makeText(this, "历史记录已经清除： "+count+" 条！", Toast.LENGTH_SHORT).show();
    }

    //数据库查询操作
    private void queryData() {
        myAdapter.clear();
        Cursor cursor = mDatabase.query(DBHelper.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                DBHelper.ID + " desc",
                "10");

        if (cursor != null) {
            int NOSTRindex = cursor.getColumnIndex(DBHelper.NOSTR);
            int COPSTRindex = cursor.getColumnIndex(DBHelper.COPSTR);
            while (cursor.moveToNext()) {
                String NOSTR = cursor.getString(NOSTRindex);
                String COPSTR = cursor.getString(COPSTRindex);

                myAdapter.add(new ListCellData(NOSTR, COPSTR));

            }
            myAdapter.add(new ListCellData("清空所有搜索记录",""));
        }
    }
}
