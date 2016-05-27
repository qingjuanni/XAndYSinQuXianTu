package org.achartengine.helper;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 工具类
 *
 */

public class SensorHelper implements SensorEventListener {
    private SensorManager sensorManager;
    private OnSensorChangeListener sensorChangeListener;
    private int sensorType;

    public SensorHelper(Context context, int sensorType) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.sensorType = sensorType;
    }

    public void registerListener(OnSensorChangeListener listener) {
        sensorChangeListener = listener;
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(sensorType),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterListener() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        if (null != sensorChangeListener) {
            sensorChangeListener.onSensorChanged(event.sensor, values);
        }
    }

    public interface OnSensorChangeListener {

        void onSensorChanged(Sensor sensor, float[] values);

    }

}
