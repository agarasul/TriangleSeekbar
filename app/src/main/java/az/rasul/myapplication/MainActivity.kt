package az.rasul.myapplication

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        triangleSeekbar.setProgressListener {
            textView.text = "Current progress is $it"
        }



        btnMakeSeekBar.setOnClickListener {
            val rnd = Random()
            val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))

            triangleSeekbar.setSeekBarColor(color)
        }


        btnMakeProgress.setOnClickListener {
            val rnd = Random()
            val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))

            triangleSeekbar.seekbarLoadingColor = color
        }

        btnShowProgress.setOnClickListener {
            triangleSeekbar.isProgressVisible = !triangleSeekbar.isProgressVisible
            if (triangleSeekbar.isProgressVisible) {
                btnShowProgress.text = "Hide progress text on it"
            } else {
                btnShowProgress.text = "Show progress text on it"
            }
        }
    }
}
