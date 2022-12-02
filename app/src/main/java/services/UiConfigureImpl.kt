package services

import android.content.res.ColorStateList
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.guillaume.mathworld.R

class UiConfigureImpl: UiConfigure {

    override fun setBelt(belt: Int, image: ImageView) {
        val context = image.context
        val beltImage: ImageView = image
        beltImage.setImageResource(R.drawable.black_belt)
        when(belt){
            1 -> ImageViewCompat.setImageTintList(image, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)))
            2 -> ImageViewCompat.setImageTintList(beltImage, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.yellow_pur)))
            3 -> ImageViewCompat.setImageTintList(beltImage, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.orange)))
            4 -> ImageViewCompat.setImageTintList(beltImage, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green_light)))
            5 -> ImageViewCompat.setImageTintList(beltImage, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.turquoise)))
            6 -> ImageViewCompat.setImageTintList(beltImage, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.purple_200)))
            7 -> ImageViewCompat.setImageTintList(beltImage, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_light)))
            8 -> ImageViewCompat.setImageTintList(beltImage, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.brown)))
            9 -> { image.setImageResource(R.drawable.white_belt)
                ImageViewCompat.setImageTintList(beltImage, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))) }
            10 -> ImageViewCompat.setImageTintList(image, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.gold)))
        }
    }

    override fun displayHeartIconLife(life: Int, image: ImageView) {
        when(life){
            0 -> image.setImageResource(R.drawable.broken_heart_3)
            1 -> image.setImageResource(R.drawable.broken_heart_2)
            2 -> image.setImageResource(R.drawable.broken_heart_1)
            3 -> image.setImageResource(R.drawable.heart)
        }
    }

    override fun displayLifeNumber(life: Int, text: TextView){
        val lifeResult = "$life/3"
        text.text = lifeResult
    }

    override fun displayExperience(currentXp: Int, xpMax: Int, textView: TextView) {
        val xpText = "$currentXp/$xpMax"
        textView.text = xpText
    }

    override fun changeGroupImageColor(number: Int, image: ImageView){
        val context = image.context
        when(number){
            1 -> ImageViewCompat.setImageTintList(image, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.dark_orange)))
            2 -> ImageViewCompat.setImageTintList(image, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.turquoise)))
            3 -> ImageViewCompat.setImageTintList(image, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.orange)))
            4 -> ImageViewCompat.setImageTintList(image, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.purple_700)))
            5 -> ImageViewCompat.setImageTintList(image, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.teal_200)))
            6 -> ImageViewCompat.setImageTintList(image, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.yellow_pur)))
            7 -> ImageViewCompat.setImageTintList(image, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_light)))
            8 -> ImageViewCompat.setImageTintList(image, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green_light)))
        }
    }

    override fun displayJobImage(job: String, image: ImageView) {
        when (job) {
            "Barde" -> image.setImageResource(R.drawable.avatar_barde)
            "Changelin" -> image.setImageResource(R.drawable.avatar_changelin)
            "Empathe" -> image.setImageResource(R.drawable.avatar_empathe)
            "Filou" -> image.setImageResource(R.drawable.avatar_filou)
            "Parieur" -> image.setImageResource(R.drawable.avatar_parieur)
            "Tisse-sort" -> image.setImageResource(R.drawable.avatar_tisse_sorts)
            "Hacker" -> image.setImageResource(R.drawable.avatar_hacker)
        }
    }

}