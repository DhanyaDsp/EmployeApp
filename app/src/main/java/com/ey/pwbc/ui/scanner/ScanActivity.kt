package com.ey.pwbc.ui.scanner

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ey.pwbc.R
import com.ey.pwbc.model.ScanData
import com.google.android.gms.vision.barcode.Barcode
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import info.androidhive.barcode.BarcodeReader
import kotlinx.android.synthetic.main.activity_scan.*


class ScanActivity : AppCompatActivity(), PermissionListener, BarcodeReader.BarcodeReaderListener {

    private var isScanned: Boolean = false
    private var barcodeReader: BarcodeReader? = null;

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
    }

    override fun onPermissionRationaleShouldBeShown(
        permission: PermissionRequest?,
        token: PermissionToken?
    ) {

    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        supportActionBar?.title = "Acquita Voucher"
        barcodeReader =
            supportFragmentManager.findFragmentById(R.id.barcode_scanner) as BarcodeReader?
        initPerms()

    }

    private fun initPerms() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(this)
            .check();
    }

    override fun onBitmapScanned(sparseArray: SparseArray<Barcode>?) {
    }

    override fun onScannedMultiple(barcodes: MutableList<Barcode>?) {
    }

    override fun onCameraPermissionDenied() {
    }

    override fun onScanned(barcode: Barcode?) {
        val rawValue = barcode?.rawValue;
        flContainer.visibility = View.VISIBLE
        scanner_overlay.visibility = View.GONE
        val array = rawValue?.split(";")
        if (array?.size == 4) {
            barcodeReader?.playBeep();
            val scanData = ScanData(array[0], array[1], array[2], array[3])
            val intent = Intent()
            intent.putExtra("scanData", scanData);
            setResult(200, intent)
            finish()
            isScanned = true;
        } else {
            setResult(-1, null)
            finish()
        }
    }

    override fun onScanError(errorMessage: String?) {
    }
}
