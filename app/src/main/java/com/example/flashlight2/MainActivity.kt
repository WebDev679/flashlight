package com.example.flashlight2

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import android.view.GestureDetector
import android.view.GestureDetector.OnGestureListener
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GestureDetectorCompat



class MainActivity : AppCompatActivity() {

    private lateinit var toggle: ToggleButton
    private lateinit var card: CardView
    private var flashLightStatus: Boolean = false
    lateinit var gestureDetector: GestureDetectorCompat




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toggle = findViewById(com.example.flashlight2.R.id.toggleButton)
        card = findViewById(R.id.card)

        toggle.setOnCheckedChangeListener { _, isChecked -> openFlashLight(isChecked)}

        val textView = findViewById<EditText>(R.id.text)
        val layout = findViewById<ConstraintLayout>(R.id.layout)

        gestureDetector = GestureDetectorCompat(this, MyGestureListener())
        layout.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            return@setOnTouchListener true
        }



        textView.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(view: View?, keyCode: Int, keyevent: KeyEvent): Boolean {
                //If the keyevent is a key-down event on the "enter" button
                return if (keyevent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    var textValue = textView.text.toString()
                    if (textValue == "ON"){
                        openFlashLight(true)
                        toggle.setChecked(true)
                    }
                    else if (textValue == "OFF") {
                        openFlashLight(false)
                        toggle.setChecked(false)
                    }
                    true
                } else false
            }
        })


    }

    inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            // Handle the fling gesture
            if (Math.abs(velocityX) > Math.abs(velocityY)) {
                if (velocityX > 0) {
                    // Fling to the right
                } else {
                    // Fling to the left
                }
            } else {
                if (velocityY > 0) {
                    openFlashLight(false)
                    toggle.setChecked(false)
                } else {
                    openFlashLight(true)
                    toggle.setChecked(true)
                }
            }
            return true
        }
    }


    private fun openFlashLight(status: Boolean) {
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]
        if (status) {
            try {
                cameraManager.setTorchMode(cameraId, true)
                flashLightStatus = true

            } catch (e: CameraAccessException) {
            }
        } else {
            try {
                cameraManager.setTorchMode(cameraId, false)
                flashLightStatus = false
            } catch (e: CameraAccessException) {
            }
        }

    }

}