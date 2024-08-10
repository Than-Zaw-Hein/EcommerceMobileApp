package com.tzh.sneakerland.util

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tzh.sneakerland.data.model.SneakerModel

object Extension {


    fun String.toSneakerModel(): SneakerModel {
        val sType = object : TypeToken<SneakerModel>() {}.type
        return Gson().fromJson<SneakerModel>(this, sType)
    }

    fun Context.showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}