package com.ey.pwbc.model

import java.io.Serializable

data class ScanStatus(val status: Int, val msg1: String, val msg2: String) : Serializable {
    companion object {
        val STATUS_SUCCESS = 0;
        val STATUS_FAILURE = 1;
    }
}