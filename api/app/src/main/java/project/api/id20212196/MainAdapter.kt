package project.api.id20212196

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import project.api.id20212196.api.Item
import project.api.id20212196.databinding.ListItemBinding
import project.api.id20212196.databinding.ListItemLoadingBinding

class MainAdapter (

    val context: Context,
    val items: MutableList<Item>,
    var itemClickListener: OnItemClickListener)  : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val ITEM_TYPE_NORMAL = 0
    private val ITEM_TYPE_LOADING = 1
    private var isLoading = false

    interface  OnItemClickListener{
        fun onItemClick(item: Item)
    }

    class LoadingViewHolder(val binding:ListItemLoadingBinding):RecyclerView.ViewHolder(binding.root)

    class ItemViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(context: Context, item: Item, listener: OnItemClickListener){
            if (item.itemImage==null)
            {
                binding.liImage.visibility = View.INVISIBLE
            }else{
                binding.liImage.visibility = View.INVISIBLE
                Glide.with(context)
                    .load(item.itemImage)
                    .placeholder(R.drawable.img_thumbnail)
                    .error(R.drawable.img_thumbnail)
                    .into(binding.liImage)
            }

            binding.liItemSeq.text = "No.${item.itemSeq}"
            binding.liItemName.text = item.itemName
            binding.liEntpName.text = item.enptName
            binding.liEfcyQesitm.text = item.efcyQesitm
            binding.liUpdateDe.text = item.updateDe

            binding.liItem.setOnClickListenter{ listener.onItemClick(item)}
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(isLoading && position == itemCount -1){
            ITEM_TYPE_LOADING
        }else{
            ITEM_TYPE_NORMAL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            ITEM_TYPE_LOADING ->{
                val binding = ListItemLoadingBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                LoadingViewHolder(binding)
            }
            else ->{
                val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                ItemViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder){
            holder.bind(context,items[position],itemClickListener)
        }
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
        val startIdx = this.items.size
        this.items.addAll(items)
        notifyItemRangeInserted(startIdx,items.size)
    }



}