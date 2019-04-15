package com.greentoad.turtlebody.mediapicker.widget.inner

import android.os.Parcel
import android.os.Parcelable
import android.view.View

/**
 * Created by niraj on 30-08-2018.
 */
class CheckedSavedState : View.BaseSavedState {

    var mIsChecked: Boolean = false

    constructor(source: Parcel?) :super(source){
        mIsChecked= source?.readInt() == 1
    }

    constructor(parcelable: Parcelable) : super(parcelable){
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeByte(if (mIsChecked) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CheckedSavedState> {
        override fun createFromParcel(parcel: Parcel): CheckedSavedState {
            return CheckedSavedState(parcel)
        }

        override fun newArray(size: Int): Array<CheckedSavedState?> {
            return arrayOfNulls(size)
        }
    }

}