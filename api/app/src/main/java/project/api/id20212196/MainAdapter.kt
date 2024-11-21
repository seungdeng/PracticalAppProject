package project.api.id20212196

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import api.Item
import com.bumptech.glide.Glide
import project.api.id20212196.databinding.ListItemBinding

class MainAdapter (
    val context: Context,
    val items: MutableList<Item>,
    val itemClickListener: OnItemClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(item: Item)
    }

    class ItemViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, item: Item, listener: OnItemClickListener) {
            if (item.itemImage == null)
            {
                binding.liImage.visibility = View.INVISIBLE
            }else {
                binding.liImage.visibility = View.VISIBLE
                Glide.with(context)
                    .load(item.itemImage)
                    .placeholder(R.drawable.img_thumbnail)
                    .error(R.drawable.img_thumbnail)
                    .into(binding.liImage)
            }

            binding.liItemSeq.text = "No.${item.itemSeq}"
            binding.liItemName.text = item.itemName
            binding.liEntpName.text = item.entpName
            binding.liEfcyQesitm.text = item.efcyQesitm
            binding.liUpdateDe.text = item.updateDe

            binding.liItem.setOnClickListener { listener.onItemClick(item) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        ItemViewHolder(binding)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ItemViewHolder
        holder.bind(context,items[position],itemClickListener)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    fun clearItems(){
        val size = this.items.size
        this.items.clear()
        notifyItemRangeRemoved(0,size)
    }
    fun addItems(items: MutableList<Item>){
        val startIdx =this.items.size
        this.items.addAll(items)
        notifyItemRangeInserted(startIdx,items.size)
    }
}

