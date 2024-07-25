package com.roa.libremessagesapp.utils

import android.content.Context
import android.widget.Toast

public class AndroidUtil{

    companion object {
        fun showToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}