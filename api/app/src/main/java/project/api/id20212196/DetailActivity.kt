package project.api.id20212196

import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import project.api.id20212196.databinding.ActivityDetailBinding
import project.api.id20212196.databinding.ListItemLoadingBinding

class DetailActivity : AppCompatActivity() {
    private val TAG: String? = this::class.simpleName
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(Activity.OVERRIDE_TRANSITION_OPEN,R.anim.slide_in_bottom,R.anim.none)
        }else{
            overridePendingTransition(R.anim.slide_in_bottom,R.anim.none)
        }


        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val entpName = intent.getStringExtra("entpname")
        val itemName = intent.getStringExtra("itemName")
        val itemSeq = intent.getStringExtra("itemSeq")
        val efcyQesitm = intent.getStringExtra("efcyQesitm")
        val useMethodQesitm = intent.getStringExtra("useMethodQesitm")
        val atpnWarnQesitm = intent.getStringExtra("atpnWarnQesitm")
        val atpnQesitm = intent.getStringExtra("atpnQesitm")
        val intrcQesitm = intent.getStringExtra("intrcQesitm")
        val seQesitm = intent.getStringExtra("seQesitm")
        val depositMethodQesitm = intent.getStringExtra("depositMethodQesitm")
        val updateDe = intent.getStringExtra("updateDe")
        val itemImage = intent.getStringExtra("itemImage")

        binding.txtItemSeq.text = "NO.${itemSeq}"
        binding.txtItemName.text = itemName
        binding.txtEntpName.text = entpName
        binding.txtUpdateDe.text = "업데이트 : ${updateDe}"

        if(itemImage != null){
            binding.imgData.visibility = View.VISIBLE
            Glide.with(this)
                .load(itemImage)
                .placeholder(R.drawable.img_thumbnail_land)
                .error(R.drawable.img_thumbnail_land)
                .into(binding.imgData)
        }else{
            binding.imgData.visibility = View.GONE
        }

        if(efcyQesitm != null){
            binding.layoutEfcyQesitm.visibility = View.VISIBLE
            binding.txtEfcyQesitm.text = efcyQesitm
        }else{
            binding.layoutEfcyQesitm.visibility = View.GONE
        }

        if(useMethodQesitm != null){
            binding.layoutUseMethodQesitm.visibility = View.VISIBLE
            binding.txtUseMethodQesitm.text = useMethodQesitm
        }else{
            binding.layoutUseMethodQesitm.visibility = View.GONE
        }

        if(atpnWarnQesitm != null){
            binding.layoutAtpnWarnQesitm.visibility = View.VISIBLE
            binding.txtAtpnWarnQesitm.text = atpnWarnQesitm
        }else{
            binding.layoutAtpnWarnQesitm.visibility = View.GONE
        }

        if (atpnQesitm != null){
            binding.layoutAtpnQesitm.visibility = View.VISIBLE
            binding.txtAtpnQesitm.text = atpnQesitm
        }else{
            binding.layoutAtpnQesitm.visibility = View.GONE
        }

        if (intrcQesitm != null){
            binding.layoutIntrcQesitm.visibility = View.VISIBLE
            binding.txtIntrcQesitm.text = intrcQesitm
        }else{
            binding.layoutIntrcQesitm.visibility = View.GONE
        }

        if(seQesitm != null){
            binding.layoutSeQesitm.visibility = View.VISIBLE
            binding.txtSeQesitm.text = seQesitm
        }else{
            binding.layoutSeQesitm.visibility = View.GONE
        }

        if (depositMethodQesitm != null){
            binding.layoutDepositMethodQesitm.visibility = View.VISIBLE
            binding.txtDepositMethodQesitm.text = depositMethodQesitm
        }else{
            binding.layoutDepositMethodQesitm.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause(){
        super.onPause()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE){
            overrideActivityTransition(Activity.OVERRIDE_TRANSITION_CLOSE,R.anim.none,R.anim.slide_out_top)
        }else{
            overridePendingTransition(R.anim.none,R.anim.slide_out_top)
        }
    }




}