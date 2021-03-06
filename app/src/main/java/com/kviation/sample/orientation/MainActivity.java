package com.kviation.sample.orientation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.*;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import java.io.*;
import android.widget.Toast;
import com.kviation.sample.orientation.R;

public class MainActivity extends Activity implements SensorEventListener {
  private SensorManager sensorManager;
  private boolean color = false;
  private View view;
  private long lastUpdate;
  public int paused = 0;
  String Acc;
  String Gyro;
  File AccF;
  OutputStream AccFO;
  File GyroF;
  OutputStream GyroFO;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

    lastUpdate = System.currentTimeMillis();

    Acc = "accel.csv";
    Gyro = "gyro.csv";
    try{
      AccF = new File(this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),Acc);
      AccF.createNewFile();
      try {
        AccFO = new FileOutputStream(AccF);
      }
      catch(Exception e){
        e.printStackTrace();
      }
    }
    catch(Exception e){
      e.printStackTrace();
    }

    try{
      GyroF = new File(this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),Gyro);
      GyroF.createNewFile();
      try {
        GyroFO = new FileOutputStream(GyroF);
      }
      catch(Exception e){
        e.printStackTrace();
      }
    }
    catch(Exception e){
      e.printStackTrace();
    }

    Button btn2 = (Button) findViewById(R.id.stop);
    System.out.println("Here-1");

    btn2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        System.out.println("Here0");
        Intent i = new Intent(MainActivity.this, Main2Activity.class);
        System.out.println("Here1");

        startActivity(i);

      }
    });

  }

  public float map(float x, float in_min, float in_max, float out_min, float out_max)
  {
    return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
  }

  @Override
  public void onSensorChanged(SensorEvent event) {
    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
      getAccelerometer(event);
    }
    else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
      getGyroscope(event);
    }

  }

  private void getAccelerometer(SensorEvent event) {
    float[] values = event.values;
    // Movement
    float x = values[0];
    float y = values[1];
    float z = values[2];
    SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    float max = proximitySensor.getMaximumRange();
    float min = -1*max;
    float xToRed = map(x, min, max, 0, 255);  // you declare this
    //System.out.println(xToRed);
    float yToGreen = map(x, min, max, 0, 255);
    float zToBlue = map(x, min, max, 0, 255);



    try {
      writeToCsv(Float.toString(x),Float.toString(y),Float.toString(z));

    }
    catch (IOException e) {
      e.printStackTrace();
    }
    // globally
    TextView myAwesomeTextView = (TextView)findViewById(R.id.textView1);

//in your OnCreate() method
    myAwesomeTextView.setText(Float.toString(x));
    myAwesomeTextView.setBackgroundColor(Color.argb(128,Math.round(xToRed),0,0));
    System.out.println(Math.round(xToRed));

    TextView myAwesomeTextView1 = (TextView)findViewById(R.id.textView2);

//in your OnCreate() method
    myAwesomeTextView1.setText(Float.toString(y));
    myAwesomeTextView1.setBackgroundColor(Color.argb(128,0,Math.round(yToGreen),0));


    TextView myAwesomeTextView2 = (TextView)findViewById(R.id.textView13);

//in your OnCreate() method
    myAwesomeTextView2.setText(Float.toString(z));
    myAwesomeTextView2.setBackgroundColor(Color.argb(128,0,0,Math.round(zToBlue)));

  }

  private void getGyroscope(SensorEvent event) {
    float[] values = event.values;
    // Movement
    float x = values[0];
    float y = values[1];
    float z = values[2];
    SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    float max = proximitySensor.getMaximumRange();
    float min = max * -1;
    float xToRed = map(x, min, max, 0, 255);  // you declare this
    //System.out.println(xToRed);
    float yToGreen = map(x, min, max, 0, 255);
    float zToBlue = map(x, min, max, 0, 255);
    try {
      writeToCsvGy(Float.toString(x),Float.toString(y),Float.toString(z));

    }
    catch (IOException e) {
      e.printStackTrace();
    }
    // globally
    TextView myAwesomeTextView = (TextView)findViewById(R.id.textView3);

//in your OnCreate() method
    myAwesomeTextView.setText(Float.toString(x));
    myAwesomeTextView.setBackgroundColor(Color.argb(128,Math.round(xToRed),0,0));

    TextView myAwesomeTextView1 = (TextView)findViewById(R.id.textView16);
    myAwesomeTextView1.setBackgroundColor(Color.argb(128,0,Math.round(yToGreen),0));


//in your OnCreate() method
    myAwesomeTextView1.setText(Float.toString(y));

    TextView myAwesomeTextView2 = (TextView)findViewById(R.id.textView15);
    myAwesomeTextView2.setBackgroundColor(Color.argb(128,0,0,Math.round(zToBlue)));


//in your OnCreate() method
    myAwesomeTextView2.setText(Float.toString(z));
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {

  }

  public void writeToCsv(String x,String y,String z) throws IOException{
//
//    File folder = new File(Environment.getDataDirectory() + "/project");
//    boolean success = true;
//    if (!folder.exists()) {
//      success = folder.mkdir();
//    }
//    if (success) {
//      // Do something on success
//      String csv = "AccelerometerValue.csv";
//      //File flie1 = new File(Environment.)
//      FileWriter file_writer = new FileWriter(csv,true);;
    try {
      String s = x + "," + y + "," + z + "\n";
      AccFO.write(s.getBytes());
    }  catch (NullPointerException e) {      e.printStackTrace();}
    catch (Exception e) {      e.printStackTrace();
    }
     //file_writer.close();
    //}
  }

  public void writeToCsvGy(String x,String y,String z) throws IOException {


//    File folder = new File(Environment.getDataDirectory() + "/TollCulator");
//    boolean success = true;
//    if (!folder.exists()) {
//      success = folder.mkdir();
//    }
//    if (success) {
//      // Do something on success
//      String csv = "/storage/sdcard0/project/GyroscopeValue.csv";
//      FileWriter file_writer = new FileWriter(csv,true);
    try {
      String s = x + "," + y + "," + z + "\n";
      GyroFO.write(s.getBytes());
//      file_writer.close();
//    }
    } catch (NullPointerException e) {      e.printStackTrace();}
    catch (Exception e) {      e.printStackTrace();
    }


  }
  @Override
  protected void onResume() {
    super.onResume();
    // register this class as a listener for the orientation and
    // accelerometer sensors
    sensorManager.registerListener(this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL);
    sensorManager.registerListener(this,
            sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
            SensorManager.SENSOR_DELAY_NORMAL);
  }

  @Override
  protected void onPause() {
    // unregister listener
    super.onPause();
    sensorManager.unregisterListener(this);
    try {
      GyroFO.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      AccFO.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
