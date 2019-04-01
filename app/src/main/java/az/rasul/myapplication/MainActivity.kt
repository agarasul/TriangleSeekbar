package az.rasul.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekbar.setmProgressListener {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }
        button.setOnClickListener {
            val num = editText.text.toString().toFloat()
            seekbar.setProgress(num)
        }
    }
}
