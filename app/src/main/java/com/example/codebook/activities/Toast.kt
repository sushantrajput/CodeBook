package com.example.codebook.activities

import android.content.Context
import android.widget.Toast

class CustomToast {

    companion object {
        fun makeText(context: Context, message: String, duration: Int) {
            Toast.makeText(context, message, duration).show()
        }
    }
}
