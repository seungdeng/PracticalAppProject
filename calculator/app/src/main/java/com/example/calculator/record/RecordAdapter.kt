package com.example.calculator.record

import com.example.calculator.databinding.ListItemBinding
import  android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView


class RecordAdapter(var items: MutableList<RecordData>,val itemClickListener: OnItemClickListener):RecyclerView.Adapter<RecordAdapter.RecordViewHolder>() {
    interface OnItemClickListener{
        fun onItemClick(item: RecordData)
    }

    data class RecordData(val val1: String, val val2:String, val symbol: String, val result: String)

    class RecordViewHolder(val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item:RecordData, listener: OnItemClickListener){
            binding.liTxtResult.text=item.result
            binding.liTxtStep.text= "${item.val1} ${item.symbol} ${item.val2}"

            binding.root.setOnClickListener{listener.onItemClick(item)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater,parent,false)
        return  RecordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        holder.bind(items[position], itemClickListener)
    }

    override fun getItemCount(): Int {
        return  items.count()
    }

    fun setAllItems (items:MutableList<RecordData>){
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}