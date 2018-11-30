package mumayank.com.nfcproject

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_read_nfc.*
import mumayank.com.airnfc.AirNfcActivity

class ReadNfcActivity: AirNfcActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_nfc)
    }

    override fun onResult(string: String) {
        textView.text = "string\n\n${textView.text}"
    }

}
