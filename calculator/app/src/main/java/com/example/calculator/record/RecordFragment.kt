package com.example.calculator.record

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.core.view.MenuProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator.MainActivity
import com.example.calculator.R
import com.example.calculator.databinding.ActivityMainBinding
import com.example.calculator.databinding.FragmentRecordBinding
import com.example.calculator.page.calculator.CalculatorViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class RecordFragment : Fragment(),RecordAdapter.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private val tag: String?  = this::class.simpleName
    private var binding: FragmentRecordBinding? = null

    private  lateinit var parent: MainActivity
    private lateinit var adapter: RecordAdapter
    private lateinit var sharedData: CalculatorViewModel

    override fun onItemClick(item: RecordAdapter.RecordData){
        val current = findNavController().currentDestination
        if(current?.id == R.id.nav_record)
        {
            sharedData.val1 = item.result.replace(",", "")
            findNavController().navigate(R.id.action_nav_record_to_nav_calculator)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecordBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        parent = requireActivity() as MainActivity
        parent.supportActionBar?.setTitle(R.string.nav_record_label)

        sharedData = ViewModelProvider(parent).get(CalculatorViewModel::class.java)
        sharedData.val1=""

        val menuProvider = object: MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.record_menu,menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return  when (menuItem.itemId){
                    R.id.record_menu_delete->{
                        parent.RECORD_LIST.clear()
                        adapter.notifyDataSetChanged()
                        true
                    }
                    else->{
                        false
                    }
                }
            }
        }
        parent.addMenuProvider(menuProvider,viewLifecycleOwner,Lifecycle.State.RESUMED)



//        val exampleDatas = mutableListOf<RecordAdapter.RecordData>()
//        exampleDatas.add(RecordAdapter.RecordData("32","42","+","74"))
//        exampleDatas.add(RecordAdapter.RecordData("532","21","-","511"))
//        exampleDatas.add(RecordAdapter.RecordData("0.1","0.2","+*","0.02"))

        adapter = RecordAdapter(mutableListOf(),this)

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                checkEmpty()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                checkEmpty()
            }

            override fun onChanged() {
                super.onChanged()
                checkEmpty()
            }

            fun checkEmpty(){
                binding?.layoutEmpty?.visibility = if(adapter.itemCount == 0){
                    View.VISIBLE
                }
                else{
                    View.INVISIBLE
                }
            }
        })
        binding?.listView?.adapter = adapter
        binding?.listView?.layoutManager = LinearLayoutManager(parent,LinearLayoutManager.VERTICAL,false)
        binding?.listView?.addItemDecoration(DividerItemDecoration(parent,LinearLayout.VERTICAL))

        loadData()
    }

    fun loadData(){
        showLoading()
        lifecycleScope.launch {
            val datas: MutableList<RecordAdapter.RecordData> = getData()
            adapter.setAllItems(datas)
            hideLoading()
        }
    }

    suspend fun getData() : MutableList<RecordAdapter.RecordData>{
        delay(1000)
        return parent.RECORD_LIST.reversed().toMutableList()
    }

    fun showLoading(){
        binding?.layoutLoading?.visibility = View.VISIBLE
    }

    fun hideLoading(){
        binding?.layoutLoading?.visibility = View.INVISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding=null
    }
}