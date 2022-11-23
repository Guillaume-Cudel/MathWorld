package services

import android.widget.ImageView
import android.widget.TextView

interface UiConfigure {

    fun setBelt(belt: Int, image: ImageView)

    fun displayHeartIconLife(life: Int, image: ImageView)

    fun displayLifeNumber(life: Int, text: TextView)

    fun displayExperience(currentXp : Int, xpMax: Int, textView: TextView)

    fun changeGroupImageColor(number: Int, image: ImageView)

    fun displayJobImage(job: String, image: ImageView)

}