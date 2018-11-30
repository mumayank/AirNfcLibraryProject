package mumayank.com.airnfc

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.nfc.NfcManager
import android.nfc.tech.IsoDep
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import mumayank.com.airdialog.AirDialog

abstract class AirNfcActivity: AppCompatActivity() {

    private var nfcAdapter: NfcAdapter? = null

    abstract fun onResult(string: String)

    override fun onResume() {
        super.onResume()

        if (nfcAdapter == null) {
            nfcAdapter = (getSystemService(Context.NFC_SERVICE) as NfcManager).defaultAdapter
        }
        checkIfNfcIsSupportedOnDevice()
    }

    private fun checkIfNfcIsSupportedOnDevice() {
        if (
            (nfcAdapter == null)
            || (packageManager.hasSystemFeature(PackageManager.FEATURE_NFC) == false)
            || (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
        ) {
            AirDialog.show(
                this,
                "This mobile device is not supported",
                "The feature you are trying to access requires NFC on Android 4.4+, which is not present in this mobile device. Please use a different supported mobile device to proceed.",
                R.drawable.ic_do_not_disturb_on_black_24dp,
                false,
                AirDialog.Button("EXIT", fun() {
                    finish()
                })
            )
        } else {
            checkIfNfcIsEnabledOnDevice()
        }
    }

    private fun checkIfNfcIsEnabledOnDevice() {
        if (nfcAdapter?.isEnabled == false) {
            AirDialog.show(
                this,
                "Enable NFC from Settings",
                "The feature you are trying to access requires NFC. Please go to settings and turn it on.",
                R.drawable.ic_settings_black_24dp,
                false,
                AirDialog.Button("GO TO SETTINGS", fun() {
                    startActivity(Intent(android.provider.Settings.ACTION_SETTINGS))
                }),
                AirDialog.Button("EXIT", fun() {
                    finish()
                })
            )
        } else {
            enableNfcReadMode()
        }
    }

    @SuppressLint("NewApi")
    private fun enableNfcReadMode() {
        nfcAdapter?.enableReaderMode(this, {
            val isoDep = IsoDep.get(it)
            isoDep.connect()
            val response = isoDep.transceive(Utils.hexStringToByteArray("00A4040007A0000002471001"))
            runOnUiThread {
                onResult(Utils.toHex(response))
            }
            isoDep.close()

        }, NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null)
    }

    public override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            nfcAdapter?.disableReaderMode(this)
        }
    }

}