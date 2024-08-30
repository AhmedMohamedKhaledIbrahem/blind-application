package com.example.smartglass.ui.design.customizecompose

import android.annotation.SuppressLint
import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

@SuppressLint("ViewConstructor")
class CustomDoubleTapView(context: Context, private val tts:TextToSpeech?):View(context) {
    private var gestureDetector: GestureDetector
    init {
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                tts?.stop()
                Log.e("double tap??","yes")
                return true
            }
        })
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean
    {
        Log.e("double tap??","yes")
        return gestureDetector.onTouchEvent(event)
    }

}