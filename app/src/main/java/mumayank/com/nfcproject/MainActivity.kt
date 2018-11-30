package mumayank.com.nfcproject

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        readNfcButton.setOnClickListener {
            startActivity(Intent(this, ReadNfcActivity::class.java))
        }

        beNfcCardButton.setOnClickListener {
            startActivity(Intent(this, BeNfcCardActivity::class.java))
        }

    }
}
