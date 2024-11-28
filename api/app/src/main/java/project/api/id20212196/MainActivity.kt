package project.api.id20212196

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.ApiManager
import api.ApiResponse
import api.Item
import kotlinx.coroutines.launch
import project.api.id20212196.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MainActivity : AppCompatActivity(), MainAdapter.OnItemClickListener {

    private var TAG: String? = this::class.simpleName
    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: MainAdapter

    private var PAGE_INIT = true
    private var IS_PAGE_LOADING = false
    private var CURRENT_PAGE = 1
    private var LAST_PAGE = 1

    private lateinit var SEARCH_OPTIONS: Array<String>
    private var SEARCH_OPTION_INDEX: Int = 0
    private var SEARCH_KEYWORD: String = ""

    override fun onItemClick(item: Item){
        val intent = Intent(this,DetailActivity::class.java)
        intent.putExtra("entpName",item.entpName)
        intent.putExtra("itemName",item.itemName)
        intent.putExtra("itemSeq", item.itemSeq)
        intent.putExtra("efcyQesitm", item.efcyQesitm)
        intent.putExtra("useMethodQesitm",item.useMethodQesitm)
        intent.putExtra("atpnWarnQesitm",item.atpnWarnQesitm)
        intent.putExtra("atpnQesitm",item.atpnQesitm)
        intent.putExtra("intrcQesitm",item.intrcQesitm)
        intent.putExtra("seQesitm",item.seQesitm)
        intent.putExtra("depositMethodQesitm",item.depositMethodQesitm)
        intent.putExtra("updateDe",item.updateDe)
        intent.putExtra("itemImage", item.itemImage)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SEARCH_OPTIONS = resources.getStringArray(R.array.search_options)

        binding.inKeyword.setOnEditorActionListener{v, actionId, event->
            if(actionId==EditorInfo.IME_ACTION_SEARCH){
                searchSubmit()
                true
            }else{
                false
            }
        }
        binding.btnSubmit.setOnClickListener{searchSubmit()}
        binding.inOptionsSelected.setOnClickListener{showDialog()}

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        adapter = MainAdapter(this, mutableListOf(),this)
        adapter.registerAdapterDataObserver(object :RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                Log.i(TAG,"Observer ItemRangeInserted ${positionStart}, ${itemCount}")
                checkEmpty()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                Log.i(TAG,"Observer ItemRangeRemoved ${positionStart},${itemCount}")
                checkEmpty()
            }

            fun checkEmpty(){
                binding.layoutEmpty.visibility = if ( adapter.itemCount ==0){
                    View.VISIBLE
                }else{
                    View.INVISIBLE
                }
            }
        })

        binding.listView.adapter = adapter
        binding.listView.layoutManager = LinearLayoutManager(parent,LinearLayoutManager.VERTICAL,false)

        binding.listView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if(!PAGE_INIT && !IS_PAGE_LOADING && (lastVisibleItem == totalItemCount -1)){
                    if(LAST_PAGE> CURRENT_PAGE){
                        IS_PAGE_LOADING = true
                        Toast.makeText(this@MainActivity,"LOAD PAGE : ${CURRENT_PAGE +1}/${LAST_PAGE}",Toast.LENGTH_SHORT).show()
                        loadData(CURRENT_PAGE+1)
                    }
                }else{
                    super.onScrolled(recyclerView, dx, dy)
                }
            }
        })
        loadData()
    }

    private fun loadData(page: Int=1){
        if (PAGE_INIT){
            showLoading()
        }else{
            adapter.setLoading(true)
        }

        lifecycleScope.launch {
            try{
                val data:ApiResponse
                if(SEARCH_KEYWORD =="")
                {
                    data = ApiManager.getService().getDrugList(pageNo = page)
                }else{
                    when(SEARCH_OPTION_INDEX){
                        0 -> data = ApiManager.getService().getDrugList(pageNo = page, itemName = SEARCH_KEYWORD)
                        1 -> data = ApiManager.getService().getDrugList(pageNo = page, entpName = SEARCH_KEYWORD)
                        2 -> data = ApiManager.getService().getDrugList(pageNo = page, efcyQesitm = SEARCH_KEYWORD)
                        else -> data = ApiManager.getService().getDrugList(pageNo = page)


                    }
                }

                if (data.header.resultCode =="00")
                {
                    if(data.body.items != null)
                    {
                        CURRENT_PAGE = data.body.pageNo
                        LAST_PAGE = (data.body.totalCount / data.body.numOfRows)+1
                        if(adapter.getLoading()){
                            adapter.setLoading(false)
                        }
                        adapter.addItems(data.body.items.toMutableList())
                    }
                }else{
                    Log.i(TAG,"API ERROR : ${data.header.resultCode}")
                }
            }catch (e: Exception){
                Log.e(TAG,"ERROR : ${e.printStackTrace()}")
            }finally {
                if(PAGE_INIT){
                    PAGE_INIT = false
                    hideLoading()
                }

                if(IS_PAGE_LOADING){
                    if(adapter.getLoading()){
                        adapter.setLoading(false)
                    }
                    IS_PAGE_LOADING = false
                }
            }
        }
    }

    private fun showLoading(){
        binding.layoutLoading.visibility = View.VISIBLE
        binding.listView.visibility = View.INVISIBLE

        binding.layoutShimmer.startShimmer()
        binding.layoutShimmer.visibility = View.VISIBLE
    }

    private fun hideLoading(){
        binding.listView.visibility = View.VISIBLE
        binding.layoutLoading.visibility = View.INVISIBLE

        binding.layoutShimmer.visibility = View.INVISIBLE
        binding.layoutShimmer.stopShimmer()
    }

    private fun searchSubmit(){
        if (!PAGE_INIT && !IS_PAGE_LOADING){
            hideKeyboard()
            SEARCH_KEYWORD = binding.inKeyword.text.toString()
            PAGE_INIT = true
            CURRENT_PAGE = 1
            LAST_PAGE = 1
            loadData(CURRENT_PAGE)
            adapter.clearItems()
        }
    }

    private fun hideKeyboard(){
        binding.listView.requestFocus()
        var inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(this.currentFocus!!.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
    }

    private fun showDialog(){
        hideKeyboard()
        var builder = AlertDialog.Builder(this)
        builder.setTitle("검색 옵션을 선택하세요.")
            .setItems(SEARCH_OPTIONS){dialog, which ->
                SEARCH_OPTION_INDEX = which
                binding.inOption.text = SEARCH_OPTIONS[which]
            }

        val alterDialog = builder.create()
        alterDialog.show()
    }

}