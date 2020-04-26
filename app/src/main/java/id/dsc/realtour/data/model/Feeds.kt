package id.dsc.realtour.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Feeds(val caption:String?="",
                 val parentContent: List<ParentContent>?= mutableListOf(),
                 val companyLogo: String?="",
                 val companyName : String?="",
                 val containerType : String?="",
                 val mediaValue: String?="",
                 val price : String? ="",
                 val companyID : String?="") :Parcelable