package edisonslightbulbs.accelerometer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView m_gravityMagnitude;
    TextView m_gyroscopeMagnitude;
    TextView m_accelerometerMagnitude;

    private Sensor m_gravity;
    private Sensor m_gyroscope;
    private Sensor m_accelerometer;

    private SensorManager m_sensorManager;

    String m_file;
    int m_sensorLogNumber = 0;

    // precision
    DecimalFormat df = new DecimalFormat("#.######");

    private static final String TAG = "MAIN_ACTIVITY";
    private static final String DIRECTORY = "accelerometer";
    private static final String FILE_NAME = "data.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // initialize output file
        m_file = Utils.file(this, DIRECTORY, FILE_NAME);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        df.setRoundingMode(RoundingMode.CEILING);
        m_sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // initialize accelerometer
        if (m_sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            m_accelerometer = m_sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        } else {
            Log.e(TAG, "-- accelerometer sensor not found!");
        }

        // initialize gravity sensor
        if (m_sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null){
            m_gravity = m_sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        } else{
            Log.e(TAG, "-- gravity sensor not found!");
        }

        // initialize gyroscope
        if (m_sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null){
            m_gyroscope = m_sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        } else{
            Log.e(TAG, "-- gyroscope sensor not found!");
        }

        m_gravityMagnitude = findViewById(R.id.accXTextView);
        m_gyroscopeMagnitude = findViewById(R.id.accYTextView);
        m_accelerometerMagnitude = findViewById(R.id.accZTextView);

        m_gravityMagnitude.setText("0.0");
        m_gyroscopeMagnitude.setText("0.0");
        m_accelerometerMagnitude.setText("0.0");


    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        m_sensorLogNumber++;
        String gravityMagnitude = "0";
        String gyroscopeMagnitude = "0";
        String accelerometerMagnitude = "0";

        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            gravityMagnitude = df.format(Utils.magnitude(event));
        }
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            gyroscopeMagnitude = df.format(Utils.magnitude(event));
        }
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            accelerometerMagnitude = df.format(Utils.magnitude(event));
    }
        m_gravityMagnitude.setText(gravityMagnitude);
        m_gyroscopeMagnitude.setText(gyroscopeMagnitude);
        m_accelerometerMagnitude.setText(accelerometerMagnitude);

        String data = m_sensorLogNumber + ", " + gravityMagnitude + ", " + gyroscopeMagnitude + ", " + accelerometerMagnitude + "\n";
        Utils.writeFile(m_file, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        m_sensorManager.registerListener(this, m_gravity, SensorManager.SENSOR_DELAY_NORMAL);
        m_sensorManager.registerListener(this, m_gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        m_sensorManager.registerListener(this, m_accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        m_sensorManager.unregisterListener(this);
    }
}