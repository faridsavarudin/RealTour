package id.dsc.realtour.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ParentContent(val name : String?="", val url :String?="")
    : Parcelable



