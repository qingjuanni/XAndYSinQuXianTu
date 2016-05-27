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
package org.achartengine.chartdemo.demo.chart;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.bean.SensorBean;
import org.achartengine.chart.PointStyle;
import org.achartengine.chartdemo.demo.R;
import org.achartengine.helper.DbHelper;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class XYChartBuilder extends Activity {



  /** The main dataset that includes all the series that go into a chart. */
  private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
  /** The main renderer that includes all the renderers customizing a chart. */
  private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
  /** The most recently added series. */
  private XYSeries mCurrentSeries;
  /** The most recently created renderer, customizing the current series. */
  private XYSeriesRenderer mCurrentRenderer;
  /** Button for creating a new series of data. */
  private Button mNewSeries;
  /** Button for adding entered data to the current series. */
  private Button mAdd;
  /** Edit text field for entering the X value of the data to be added. */
  private EditText mX;
  /** Edit text field for entering the Y value of the data to be added. */
  private EditText mY;
  /** The chart view that displays the data. */
  private GraphicalView mChartView;

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    // save the current data, for instance when changing screen orientation
    outState.putSerializable("dataset", mDataset);
    outState.putSerializable("renderer", mRenderer);
    outState.putSerializable("current_series", mCurrentSeries);
    outState.putSerializable("current_renderer", mCurrentRenderer);
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedState) {
    super.onRestoreInstanceState(savedState);
    // restore the current data, for instance when changing the screen
    // orientation
    mDataset = (XYMultipleSeriesDataset) savedState.getSerializable("dataset");
    mRenderer = (XYMultipleSeriesRenderer) savedState.getSerializable("renderer");
    mCurrentSeries = (XYSeries) savedState.getSerializable("current_series");
    mCurrentRenderer = (XYSeriesRenderer) savedState.getSerializable("current_renderer");
  }

  private Handler handler = new Handler(){
    /**
     * 延时更新画图
     */
    public void handleMessage(android.os.Message msg){
      //更新画布的
      mChartView.repaint();


    };

  };



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.xy_chart);



    // the top part of the UI components for adding new data points
    mX = (EditText) findViewById(R.id.xValue);
    mY = (EditText) findViewById(R.id.yValue);
    mAdd = (Button) findViewById(R.id.add);



    // set some properties on the main renderer
    mRenderer.setApplyBackgroundColor(true);
    mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
    mRenderer.setAxisTitleTextSize(16);
    mRenderer.setChartTitleTextSize(20);
    mRenderer.setLabelsTextSize(15);
    mRenderer.setLegendTextSize(15);
//    mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
    mRenderer.setMargins(new int[] { 30, 30, 30, 30 });//设置距离手机边框的距离
    mRenderer.setZoomButtonsVisible(true);
    mRenderer.setPointSize(5);

    // the button that handles the new series of data creation
    mNewSeries = (Button) findViewById(R.id.new_series);
    mNewSeries.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        String seriesTitle = "Series " + (mDataset.getSeriesCount() + 1);
        // create a new series of data
        XYSeries series = new XYSeries(seriesTitle);
        mDataset.addSeries(series);
        mCurrentSeries = series;
        // create a new renderer for the new series
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);
        // set some renderer properties
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setFillPoints(true);
        renderer.setDisplayChartValues(true);
        renderer.setDisplayChartValuesDistance(10);
        mCurrentRenderer = renderer;
        setSeriesWidgetsEnabled(true);
        mChartView.repaint();
      }
    });


    mAdd.setOnClickListener(new View.OnClickListener() {


      public void onClick(View v) {

        double mx =0;
        double my =0;

        new Thread() {
          @Override
          public void run() {
            DbHelper dbHelper = DbHelper.getInstance(getBaseContext());
            SQLiteDatabase database = dbHelper.getReadableDatabase();
            Cursor cursor = database.rawQuery("SELECT * FROM sensor_test", null);
            cursor.moveToFirst();
            ArrayList<SensorBean> data = new ArrayList<SensorBean>();
            while (cursor.moveToNext()) {
              int x = cursor.getInt(cursor.getColumnIndex("x"));
              float y = cursor.getFloat(cursor.getColumnIndex("y"));
              System.out.println("x ++++++++ y " + x + "========"+ y);

              // add a new data point to the current series
              mCurrentSeries.add(x, y);

//              mX.requestFocus();
              // repaint the chart such as the newly added point to be visible
              SystemClock.sleep(20);
            handler.sendEmptyMessageDelayed(1,1000);
//              SparseArray

              SensorBean bean = new SensorBean();
              bean.setX(x);
              bean.setY(y);
              data.add(bean);
            }
            cursor.close();
          }
        }.start();

      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (mChartView == null) {
      LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
      mChartView = ChartFactory.getLineChartView(this, mDataset, mRenderer);
      // enable the chart click events
      mRenderer.setClickEnabled(true);
      mRenderer.setSelectableBuffer(10);
      mChartView.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          // handle the click event on the chart
          SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
          if (seriesSelection == null) {
            Toast.makeText(XYChartBuilder.this, "No chart element", Toast.LENGTH_SHORT).show();
          } else {
            // display information of the clicked point
            Toast.makeText(
                XYChartBuilder.this,
                "Chart element in series index " + seriesSelection.getSeriesIndex()
                    + " data point index " + seriesSelection.getPointIndex() + " was clicked"
                    + " closest point value X=" + seriesSelection.getXValue() + ", Y="
                    + seriesSelection.getValue(), Toast.LENGTH_SHORT).show();
          }
        }
      });
      layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,
          LayoutParams.FILL_PARENT));
      boolean enabled = mDataset.getSeriesCount() > 0;
      setSeriesWidgetsEnabled(enabled);
    } else {
      mChartView.repaint();
    }
  }

  /**
   * Enable or disable the add data to series widgets
   * 
   * @param enabled the enabled state
   */
  private void setSeriesWidgetsEnabled(boolean enabled) {
    mX.setEnabled(enabled);
    mY.setEnabled(enabled);
    mAdd.setEnabled(enabled);
  }

  public void jieping(View view){
    screenshot();
  }

  private void screenshot() {
    // 获取屏幕
    View dView = getWindow().getDecorView();
    dView.setDrawingCacheEnabled(true);
    dView.buildDrawingCache();
    Bitmap bmp = dView.getDrawingCache();
    if (bmp != null)
    {
      try {
        // 获取内置SD卡路径
        String sdCardPath = Environment.getExternalStorageDirectory().getPath();
        // 图片文件路径
        String filePath = sdCardPath + File.separator + System.currentTimeMillis()+"screenshot.png";

        File file = new File(filePath);
        FileOutputStream os = new FileOutputStream(file);
        bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
        os.flush();
        os.close();
      } catch (Exception e) {
      }
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }
}