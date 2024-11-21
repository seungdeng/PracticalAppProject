package project.api.id20212196

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import project.api.id20212196.api.ApiManager
import project.api.id20212196.api.ApiResponse
import project.api.id20212196.api.Item
import project.api.id20212196.databinding.ActivityMainBinding
import java.lang.Exception
//MainAdapter.OnItemClickListener
class MainActivity : AppCompatActivity(),MainAdapter.OnItemClickListener{
    private  var TAG:String? = this::class.simpleName
    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: MainAdapter

    private var PAGE_INIT = true
    private var IS_PAGE_LOADING = false
    private var CURRENT_PAGE = 1
    private var LAST_PAGE = 1

    private lateinit var SEARCH_OPTIONS: Array<String>
    private var SEARCH_OPTION_INDEX: Int = 0
    private var SEARCH_KEYWORD: String = ""

    override fun onItemClick(item: Item) {
        Toast.makeText(this,item.itemName,Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.Oncreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SEARCH_OPTIONS = resources.getStringArray((R.array.search_options))

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        adapter = MainAdapter(this, mutableListOf(), this)
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                Log.i(TAG,"Observer ItemRangeInserted ${positionStart}, ${itemCount}")
                checkEmpty()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                Log.i(TAG,"Observer ItemRangeRemoved ${positionStart}, ${itemCount}")
                checkEmpty()
            }

            fun checkEmpty() {
                binding.layoutEmpty.visibility = if (adapter.itemCount == 0) {
                    View.VISIBLE
                } else {
                    View.INVISIBLE
                }
            }
        })
        binding.listView.adapter = adapter
        binding.listView.layoutManager = LinearLayoutManager(parent, LinearLayoutManager.VERTICAL, false)

        binding.listView.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView,dx: Int, dy : Int){
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if(!PAGE_INIT && !IS_PAGE_LOADING && (lastVisibleItem == totalItemCount -1)){
                    if(LAST_PAGE> CURRENT_PAGE){
                        IS_PAGE_LOADING = true
                        Toast.makeText(this@MainActivity,"LOAD PAGE : ${CURRENT_PAGE + 1}/${LAST_PAGE}",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    super.onScrolled(recyclerView, dx, dy)
                }
            }
        })
        loadData()
    }

    private fun loadData(page: Int= 1){
        if(PAGE_INIT){
        showLoading()
        }else{
            adapter.setLoading(true)
        }
        lifecycleScope.launch {
            try {
                val data: ApiResponse = ApiManager.getService().getDrugList()
                if (data.header.resultCode =="00")
                {
                    if (data.body.items != null)
                    {
                        CURRENT_PAGE = data.body.pageNo
                        LAST_PAGE = (data.body.totalCount / data.body.numOfRows)+1
                        if (adapter.getLoading())
                        {
                            adapter.setLoading(false)
                        }
                    }
                }else{
                    Log.i(TAG,"API ERROR: ${data.header.resultCode}")
                }
            }catch (e: Exception){
                Log.e(TAG,"Error : ${e.printStackTrace()}")
            }finally {
                if(PAGE_INIT){
                    PAGE_INIT = false
                    hideLoading()
                }

                if(IS_PAGE_LOADING){
                    if (adapter.getLoading())
                    {
                        adapter.setLoading(false)
                    }
                    IS_PAGE_LOADING=false
                }
            }
        }
    }

    private fun showLoading(){
        binding.layoutLoading.visibility = View.VISIBLE
        binding.listView.visibility = View.INVISIBLE
    }

    private fun hideLoading(){
        binding.listView.visibility = View.VISIBLE
        binding.layoutLoading.visibility = View.INVISIBLE
    }



}