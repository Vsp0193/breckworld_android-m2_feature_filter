package com.breckworld.architecture

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.breckworld.BR
import com.breckworld.R
import com.breckworld.extensions.toast


abstract class BaseMVVMActivity<V : BaseMVVMViewModel<T>, B : ViewDataBinding, T : Enum<T>> : AppCompatActivity() {

    protected lateinit var viewModel: V
    protected lateinit var binding: B
    var navController: NavController? = null

    protected abstract fun viewModelClass(): Class<V>

    abstract fun subscribeToEvents()


    @LayoutRes
    protected abstract fun layoutResId(): Int

    @IdRes
    protected abstract fun navContainerId(): Int?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //router = createRouter()
        viewModel = ViewModelProviders.of(this).get(viewModelClass())
        binding = DataBindingUtil.setContentView(this, layoutResId())
        binding.setVariable(BR.viewModel, viewModel)
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        if (navContainerId() != null) navController = Navigation.findNavController(this, R.id.container)
        subcribeNoInternet()
        subscribeToEvents()
        subscribeLogout()
        setStatusBarFlags()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController?.navigateUp() ?: super.onSupportNavigateUp()
    }

    open fun shouldDisplayHomeUp() {

    }

    fun setLoading(visible: Boolean) {
        viewModel.progressVisibility.postValue(visible)
    }

    fun showNoInternet(visible: Boolean) {
        viewModel.noInternet.postValue(visible)
    }

    fun subscribeLogout() {
        observe(viewModel.logout) {
            if (it) logout()
        }
    }

    fun subcribeNoInternet() {
       observe(viewModel.noInternet){
           showNoInternetConnection()
       }
    }

    open fun getStatusBarHeight() = 0

    open fun logout() {

    }

    open fun showNoInternetConnection() {

    }

    open fun showBottomNavigation(show: Boolean) {

    }

    fun showToast(message: String) {
        toast(message, Toast.LENGTH_SHORT)
    }

    fun <T, LD : LiveData<T>> observeNullable(liveData: LD, onChanged: (T?) -> Unit) {
        liveData.observe(this, Observer { value ->
            onChanged(value)
        })
    }

    fun <T, LD : LiveData<T>> observe(liveData: LD, onChanged: (T) -> Unit) {
        liveData.observe(this, Observer { value ->
            value?.let(onChanged)
        })
    }

    private fun setStatusBarFlags() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        window.statusBarColor = Color.TRANSPARENT
    }

    //clear focus all views. You need add attributes for xml to root view
    //android:focusableInTouchMode="true"
    //android:descendantFocusability="beforeDescendants"
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    enum class Events {
        SHOW_LOADING,
        HIDE_LOADING
    }
}