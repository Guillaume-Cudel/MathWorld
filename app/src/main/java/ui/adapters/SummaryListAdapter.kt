package ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.guillaume.mathworld.R
import model.Student
import services.UiConfigure

class SummaryListAdapter(private val uiConfigure: UiConfigure):
    ListAdapter<Student, SummaryListViewHolder>(StudentViewHolder.StudentsComparator()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryListViewHolder {
        return SummaryListViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: SummaryListViewHolder, position: Int) {
        val current = getItem(position)

        //Name and lastname
        holder.firstnameText.text = current.firstName
        holder.lastnameText.text = current.lastName

        // Score
        var score = 0


        // Belt
        displayBelt(score, holder.beltImage)
    }

    private fun displayBelt(score: Int, image: ImageView) {
        when (score) {
            in 0..3 -> uiConfigure.setBelt(1, image)
            in 4..6 -> uiConfigure.setBelt(2, image)
            in 7..9 -> uiConfigure.setBelt(3, image)
            in 10..13 -> uiConfigure.setBelt(4, image)
            in 14..17 -> uiConfigure.setBelt(5, image)
            in 18..21 -> uiConfigure.setBelt(6, image)
            in 22..25 -> uiConfigure.setBelt(7, image)
            in 26..29 -> uiConfigure.setBelt(8, image)
            30 -> uiConfigure.setBelt(9, image)
        }
    }
}




class SummaryListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val firstnameText: TextView = itemView.findViewById(R.id.new_belts_summary_student_firstname)
    val lastnameText: TextView = itemView.findViewById(R.id.new_belts_summary_student_lastname)
    val scoreText: TextView = itemView.findViewById(R.id.new_belts_summary_score_result)
    val beltImage: ImageView = itemView.findViewById(R.id.new_belts_summary_belt)


    companion object {
        fun create(parent: ViewGroup): SummaryListViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_new_belts_summary, parent, false)
            return SummaryListViewHolder(view)
        }
    }
}