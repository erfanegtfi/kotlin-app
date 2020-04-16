package com.e.kotlinapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.e.kotlinapp.BR
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.e.kotlinapp.model.Category.Companion.TABLE_NAME
import com.google.gson.annotations.SerializedName

@Entity(tableName = TABLE_NAME)
data class Category(

//    @PrimaryKey(autoGenerate = true)
    var id: Int? = 0,
    @PrimaryKey
    @SerializedName("cat_slug")
    var catSlug: String,
    @SerializedName("name")
    var name: String,

    @SerializedName("icon")
    var icon: String,

    @SerializedName("logo")
    var logo: String
) : BaseObservable() {


    @get:Bindable
    var selected: Boolean?=null
        set(value) {
            field = value
            notifyPropertyChanged(BR.selected)
        }

    companion object {
        const val TABLE_NAME = "ikopon_categories"
    }

}