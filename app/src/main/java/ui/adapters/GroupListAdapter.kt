package ui.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.guillaume.mathworld.R
import services.UiConfigure
import services.XpByGroupUpdater
import ui.fragments.NotebookFragment

class GroupListAdapter(private val xpUpdater: XpByGroupUpdater, private val uiConfigure: UiConfigure, ):
    ListAdapter<Int, GroupViewHolder>(GroupViewHolder.GroupsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        return GroupViewHolder.create(parent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val currentGroup = getItem(position)

        // Display group data
        holder.groupNumberText.text = currentGroup.toString()
        uiConfigure.changeGroupImageColor(currentGroup, holder.groupImage)

        holder.groupPlusFive.setOnClickListener {
            xpUpdater.addXpByGroup(currentGroup, 5)
        }
        holder.groupPlusTen.setOnClickListener {
            xpUpdater.addXpByGroup(currentGroup, 10)
        }
    }

}

class GroupViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    val groupImage: ImageView = itemView.findViewById(R.id.group_ilot_image)
    val groupNumberText: TextView = itemView.findViewById(R.id.group_ilot_number)
    val groupPlusFive: MaterialButton = itemView.findViewById(R.id.group_xp_plus_five)
    val groupPlusTen: MaterialButton = itemView.findViewById(R.id.group_xp_plus_ten)

    companion object{
        fun create(parent: ViewGroup): GroupViewHolder{
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_groups, parent, false)
            return GroupViewHolder(view)
        }
    }

    class GroupsComparator : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }
}