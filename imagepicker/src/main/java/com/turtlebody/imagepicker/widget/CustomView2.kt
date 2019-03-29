package com.turtlebody.imagepicker.widget

import android.animation.ObjectAnimator
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.view.View

/**
 * Created by WANGSUN on 28-Mar-19.
 */
class CustomView2 : View {
    //alwasy start variable with small letter
    //alwasy use m for global variable
    //ALL STATIC variable must be in capital later with underscore to separate 2 words

    private var mPaint: Paint? = null
    //Rectangle
    private var mRect: Rect? = null

    internal var mRectSize = 350
    internal var p: Int = 0     // p is the Flag Variable

    //Circle
    internal var mCircleX = 0
    internal var mCircleY = 0

    internal var viewAnimation: Float
        get() = 0f
        set(a) = updateAnimationView(Math.round(a))

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }


    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        init(attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }


    private fun init(attrs: AttributeSet?) {
        mPaint = Paint()
        mPaint!!.color = Color.RED
        mCircleX = 600
        mCircleY = 280

        //initialization must be done only once
        mRect = Rect()

        mRect!!.left = 200
        mRect!!.top = 200
        mRect!!.right = mRectSize
        mRect!!.bottom = mRectSize
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw the Rectangle
        canvas.drawRect(mRect!!, mPaint!!)
        canvas.drawCircle(mCircleX.toFloat(), mCircleY.toFloat(), CIRCLE_RADIUS, mPaint!!)
    }

    // Animate Function
    fun startAnimate() {

        val Animat = ObjectAnimator.ofFloat(this, "viewAnimation", RECT_MIN_SIDE, RECT_MAX_SIZE)
        Animat.duration = 1000
        Animat.repeatCount = ObjectAnimator.INFINITE
        Animat.repeatMode = ObjectAnimator.REVERSE
        Animat.start()

    }

    private fun updateAnimationView(side: Int) {
        p = side - mRectSize
        mRectSize = side
        mRect!!.left = mRect!!.left - p
        mRect!!.top = mRect!!.top - p
        mRect!!.right = mRectSize
        mRect!!.bottom = mRectSize

        mCircleX = ((side - RECT_MIN_SIDE) * (CIRCLE_MAX_X - CIRCLE_MIN_X) / (RECT_MAX_SIZE - RECT_MIN_SIDE) + CIRCLE_MIN_X).toInt()
        invalidate()
    }

    companion object {
        internal val RECT_MIN_SIDE = 350f
        internal val RECT_MAX_SIZE = 410f
        internal val CIRCLE_MIN_X = 600f
        internal val CIRCLE_MAX_X = 800f
        internal val CIRCLE_RADIUS = 140f
    }

}