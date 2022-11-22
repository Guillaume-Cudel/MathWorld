package ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.guillaume.mathworld.R
import model.StudentsClass

class ClassesListAdapter: ListAdapter<StudentsClass, ClassViewHolder>(ClassViewHolder.ClassesComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder{
        return ClassViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int){
        val current = getItem(position)

        holder.nameText.text = current.name
        holder.lvlText.text = current.level

        //todo continue to set changeButtons

    }
}
class ClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nameText: TextView = itemView.findViewById(R.id.item_class_name)
    val lvlText: TextView = itemView.findViewById(R.id.item_class_lvl)
    val color: View = itemView.findViewById(R.id.item_class_color)
    val changeColorButton: FloatingActionButton = itemView.findViewById(R.id.item_class_modify_color)
    val changeNameButton: FloatingActionButton = itemView.findViewById(R.id.item_class_modify_name)
    
    companion object {
        fun create(parent: ViewGroup): ClassViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_class, parent, false)
            return ClassViewHolder(view)
        }
    }

    class ClassesComparator : DiffUtil.ItemCallback<StudentsClass>() {
        override fun areItemsTheSame(oldItem: StudentsClass, newItem: StudentsClass): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: StudentsClass, newItem: StudentsClass): Boolean {
            return oldItem.name == newItem.name
        }
    }
}

