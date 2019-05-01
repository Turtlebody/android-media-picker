package com.greentoad.turtlebody.mediapicker.widget

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import android.widget.ImageView
import com.greentoad.turtlebody.mediapicker.widget.inner.CheckedSavedState

/**
 * Created by niraj on 31-08-2018.
 */
class ImageViewCheckable : ImageView, Checkable {

    private val CHECKED_STATE_SET = intArrayOf(android.R.attr.state_activated, android.R.attr.state_checked)
    internal var mIsChecked = false

    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes)


    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 2)

        if (mIsChecked) {
            View.mergeDrawableStates(drawableState, CHECKED_STATE_SET)
        }

        return drawableState
    }

    override fun setChecked(checked: Boolean) {
        if (mIsChecked != checked) {
            mIsChecked = checked
            invalidate()
            refreshDrawableState()
        }
    }

    override fun isChecked(): Boolean {

        return mIsChecked
    }

    override fun toggle() {
        setChecked(!mIsChecked)
    }


    override fun performClick(): Boolean {
        toggle()
        return super.performClick()
    }

    override fun onSaveInstanceState(): Parcelable? {

        val savedStateChecked = CheckedSavedState(super.onSaveInstanceState()!!)
        savedStateChecked.mIsChecked = mIsChecked
        return savedStateChecked
    }

    override fun onRestoreInstanceState(state: Parcelable) {

        if (state !is CheckedSavedState) {
            super.onRestoreInstanceState(state)
            return

        }

        super.onRestoreInstanceState(state.superState)
        isChecked = state.mIsChecked


    }
}
