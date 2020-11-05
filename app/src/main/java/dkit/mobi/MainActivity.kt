package dkit.mobi

// Sensor Basic Kotlin
// This App does not use the UI (it uses Log only for output)
//
// Demonstrates accessing the accelerometer sensor
// - SensorManager
// - SensorEventListener & onAccuracyChanged() callback
// Use the emulator or a device to test the App

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var mSensorManager: SensorManager  // non-nullable
    private var mAccelerometer: Sensor? = null          // is nullable

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        Log.d("SensorApp","... in onCreate()")
        setContentView(R.layout.activity_main)

        // get reference to the Sensor Manager from the system service
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // get reference to the accelerometer sensor (could return null)
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        //TODO Exercise
        // Add code to gather and display sensor data from
        // 1. the Gyroscope sensor and from
        // 2. the Magnetometer sensor.
        // Display the sensor values in the Log.
        // Move device or use emulator to move device virtually
        // and observe changes in values.
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    /**
     * This activity implements the SensorEventListener interface and thus must
     * override the onSensorChanged() call-back function.
     * This method is called when the sensor generates an event and the associated
     * event object is passed as an argument.
     */
    override fun onSensorChanged(event: SensorEvent?) {

        //Log.d("SensorApp", "onSensorChanged() called:")

        // 'switch' on the event type.
        // safe call operator "?" for nullable references allows the call to block to execute
        // only if 'event' and 'sensor' references are non-null.
        when (event?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                val x = event.values[0];    // x,y,z components of acceleration
                val y = event.values[1];
                val z = event.values[2];
                Log.d("SensorApp", "ACCELEROMETER: x=" + x + ", y=" + y + ", z=" + z)
            }
            else -> Log.d("SensorApp", "some other event raised - ignored.")
        }
    }

    /**
     * Register this Activity as a listener on the accelerometer sensor.
     * This is done here - as the activity starts - because
     * the activity should only listen for events when it has been started,
     * and is thus able to process events.
     * (Registering Listeners in onStart() and unregistering in onStop(),
     * instead of in onResume() and onPause(), was a change introduced in API24)
     */
    override fun onStart() {
        super.onStart()
        Log.d("SensorApp", "in onStart() - registering accelerometer listener")

        mSensorManager.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL)
    }

    /**
     * When an activity enters the stopped state, we should unregister the listener
     * as, when stopped, it cannot process events from the sensor.
     * Failing to unregister the listener will cause unnecessary
     * callbacks that slow the system down & drain the battery.
     */
    override fun onStop() {
        super.onStop()
        Log.d("SensorApp", "in onStop() - unregistering all listeners")

        mSensorManager.unregisterListener(this)
    }
}