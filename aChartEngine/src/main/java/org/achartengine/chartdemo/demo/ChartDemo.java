/**
 * Copyright (C) 2009 - 2013 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.achartengine.chartdemo.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.achartengine.bean.SensorBean;
import org.achartengine.chartdemo.demo.chart.IDemoChart;
import org.achartengine.chartdemo.demo.chart.TrigonometricFunctionsChart;
import org.achartengine.chartdemo.demo.chart.XYChartBuilder;
import org.achartengine.helper.DbHelper;
import org.achartengine.helper.ExcelHelper;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ChartDemo extends ListActivity {



  private IDemoChart[] mCharts = new IDemoChart[] {
      new TrigonometricFunctionsChart()};

  private String[] mMenuText;

  private String[] mMenuSummary;



  private ExecutorService executorService;


  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    int length = mCharts.length;
    mMenuText = new String[length + 3];
    mMenuSummary = new String[length + 3];
    mMenuText[0] = "使用数据库中的X.Y点绘制图形";
    mMenuSummary[0] = "A demo ";

    mMenuText[1] = "清空数据";
    mMenuSummary[1] = "A demo  ";

    for (int i = 0; i < length; i++) {
      mMenuText[i + 2] = mCharts[i].getName();
      mMenuSummary[i + 2] = mCharts[i].getDesc();
    }

    mMenuText[length + 2] = " 导出数据 ";
    mMenuSummary[length + 2] = "······";

    setListAdapter(new SimpleAdapter(this, getListValues(), android.R.layout.simple_list_item_2,
        new String[] { IDemoChart.NAME, IDemoChart.DESC }, new int[] { android.R.id.text1,
            android.R.id.text2 }));




    executorService = Executors.newCachedThreadPool();

  }

  private List<Map<String, String>> getListValues() {
    List<Map<String, String>> values = new ArrayList<Map<String, String>>();
    int length = mMenuText.length;
    for (int i = 0; i < length; i++) {
      Map<String, String> v = new HashMap<String, String>();
      v.put(IDemoChart.NAME, mMenuText[i]);
      v.put(IDemoChart.DESC, mMenuSummary[i]);
      values.add(v);
    }
    return values;
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);
    Intent intent = null;

    if (position == 0) {
      //点击这个能开始画图的类
      intent = new Intent(this, XYChartBuilder.class);
    }
//    else if (position == 1) {
//      intent = new Intent(this, PieChartBuilder.class);
//    }
    else if (position == 1) {
      //清空数据
      clearData();
      return;
    }else if (position <= mCharts.length + 1) {
      System.out.println("------------------------------");
//      find();
      System.out.println("=================================================================");

      //开始生成数据并保存，开始图形的绘制
      startCollectData();

      intent = mCharts[position - 2].execute(this);
    } else {

      exportExcel();
    return;
//      intent = new Intent(this, GeneratedChartDemo.class);
    }
    startActivity(intent);
  }

  private void clearData() {
    final ProgressDialog dialog = ProgressDialog.show(this, null, "正在清空数据……");
    new Thread() {
      @Override
      public void run() {
        DbHelper dbHelper = DbHelper.getInstance(getBaseContext());
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.execSQL("DELETE FROM sensor_test");//删除数据库中的数据
        ExcelHelper.deleteExcel();//删除Excel文件
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "已清空数据", Toast.LENGTH_SHORT).show();
          }
        });
      }
    }.start();
  }

  private void exportExcel() {
    final ProgressDialog dialog = ProgressDialog.show(this, null, "正在将数据导出到Excel……");
    new Thread() {
      @Override
      public void run() {
        DbHelper dbHelper = DbHelper.getInstance(getBaseContext());
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM sensor_test", null);
        cursor.moveToFirst();
        ArrayList<SensorBean> data = new ArrayList<SensorBean>();
        while (cursor.moveToNext()) {
          SensorBean bean = new SensorBean();

          bean.setX(cursor.getInt(cursor.getColumnIndex("x")));
          bean.setY(cursor.getFloat(cursor.getColumnIndex("y")));

          bean.setTimeline(cursor.getLong(cursor.getColumnIndex("timeline")));
          data.add(bean);
        }
        cursor.close();
        String result;
        String path;
        try {
          path = ExcelHelper.createExcel(data);
          result = "导出到Excel成功！";
        } catch (Exception e) {
          path = "";
          e.printStackTrace();
          result = "导出失败：" + e.getMessage();
        }
        final String finalResult = result;
        final String finalPath = path;
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), finalResult, Toast.LENGTH_SHORT).show();
            //发送到手机QQ
            if (!TextUtils.isEmpty(finalPath)) {
              String packageName = "com.tencent.mobileqq";
              try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setPackage(packageName);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_STREAM, finalPath);
                startActivity(intent);
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          }
        });
      }
    }.start();
  }





  private void startCollectData() {
    saveDataToDb();

  }
//  Runnable command = new Runnable() {
//    @Override
//    public void run() {
//
//    }
//  };

//开始保存数据
  private void saveDataToDb() {

    DbHelper dbHelper = DbHelper.getInstance(getBaseContext());
    final SQLiteDatabase datebase = dbHelper.getWritableDatabase();

    Thread thread = new Thread(){
      @Override
      public void run() {
    for (int i = 0; i<= 3600 ;i++){
      final int x =i ;
      final float y = (float) Math.sin(Math.toRadians(x));
      final long timeline = System.currentTimeMillis();
//          long maxTimeMillis = 300L;
//          if (RepeatHelper.isFastDoubleAction(maxTimeMillis)){
//            return;
//          }
          datebase.execSQL("INSERT INTO sensor_test (x,y,timeline) VALUES (?,?,?)",
                  new Object[]{x, y, timeline});
    }
        }
      };
      executorService.submit(thread);


  }



  //查询数据
//  public  void find (){
//    new Thread(){
//
//      @Override
//      public void run() {
//        DbHelper dbHelper = DbHelper.getInstance(getBaseContext());
//        SQLiteDatabase database = dbHelper.getReadableDatabase();
//        Cursor cursor = database.rawQuery("SELECT * FROM sensor_test ", null);
//
//        cursor.moveToFirst();
//        ArrayList<SensorBean> data = new ArrayList<SensorBean>();
//         while (cursor.moveToNext()) {
//          int x = cursor.getInt(cursor.getColumnIndex("x"));
//          float y = cursor.getFloat(cursor.getColumnIndex("y"));
//          System.out.println("x ++++++++ y " + x + "========"+ y);
//          SensorBean bean = new SensorBean();
//          bean.setX(x);
//          bean.setY(y);
//          data.add(bean);
//        }
//        cursor.close();
//      }
//    }.start();
////    executorService.submit(thread);
//
//  }


  //查询数据
  public  Cursor find (){

        DbHelper dbHelper = DbHelper.getInstance(getBaseContext());
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM sensor_test ", null);


//    executorService.submit(thread);
        return cursor;
  }
}