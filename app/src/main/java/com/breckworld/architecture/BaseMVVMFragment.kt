package com.breckworld.architecture

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.breckworld.App
import com.breckworld.BR
import com.breckworld.extensions.toast

abstract class BaseMVVMFragment<V : BaseMVVMViewModel<T>, B : ViewDataBinding, T : Enum<T>> : Fragment() {

    protected lateinit var viewModel: V
    protected lateinit var binding: B

    protected abstract fun viewModelClass(): Class<V>

    @LayoutRes
    protected abstract fun layoutResId(): Int

    abstract fun subscribeToEvents()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //router = createRouter()
        viewModel = ViewModelProviders.of(this).get(viewModelClass())
        subscribeToEvents()
        subscribeProgressEvents()
        subscribeNoInternetEvent()
        subscribeLogout()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId(), container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()
    }

    override fun onStart() {
        super.onStart()
        resolveStatusBarAndIconsColor()
    }

    override fun onStop() {
        super.onStop()
        setLoading(false)
    }

    open fun getStatusBarIconColor(): Boolean {
        return true //If true icons will be white otherwise status bar icons will be black
    }

    open fun setupStatusbar(transparent: Boolean, lightStatusBar: Boolean) {
        activity?.window?.let { win ->
            if (transparent) {
                win.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                win.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                win.statusBarColor = Color.TRANSPARENT
            } else {
                win.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }

            val winParams = win.attributes

            val oldFlags = win.decorView.systemUiVisibility

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                var flags = oldFlags
                flags = if (lightStatusBar) {
                    flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                }
                win.decorView.systemUiVisibility = flags
            }
        }
    }

    open fun setToolbarColor(@ColorRes colorRes: Int) {
        (activity as BaseMVVMActivity<*, *, *>).getSupportActionBar()
            ?.setBackgroundDrawable(ColorDrawable(App.getResColor(colorRes)))
    }

    fun showToast(message: String) {
        toast(message, Toast.LENGTH_SHORT)
    }

    fun <T, LD : LiveData<T>> observeNullable(liveData: LD, onChanged: (T?) -> Unit) {
        liveData.observe(this, Observer {
            onChanged(it)
        })
    }

    fun <T, LD : LiveData<T>> observe(liveData: LD, onChanged: (T) -> Unit) {
        liveData.observe(this, Observer {
            it?.let(onChanged)
        })
    }

    private fun resolveStatusBarAndIconsColor() {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getStatusBarIconColor()) {
                //StatusBarUtil.setDarkMode(activity)
            } else {
                //StatusBarUtil.setLightMode(activity)
            }
        } else {
            if (getStatusBarIconColor()) {
                activity?.window?.statusBarColor = Color.TRANSPARENT
            } else {
                activity?.window?.statusBarColor = ContextCompat.getColor(activity!!, R.color.colorBlack)
            }
        }*/
    }

    private fun setLoading(visible: Boolean) {
        (activity as? BaseMVVMActivity<*, *, *>)?.setLoading(visible)
    }

    private fun subscribeProgressEvents() {
        observe(viewModel.progressVisibility) {
            setLoading(it)
        }
    }

    fun subscribeLogout() {
        observe(viewModel.logout) {
            if (it) {
                logout()
            }
        }
    }

    private fun showNoInternet(visible: Boolean) {
        (activity as? BaseMVVMActivity<*, *, *>)?.showNoInternet(visible)
    }

    fun showBottomNavigation(show: Boolean) {
        (activity as? BaseMVVMActivity<*, *, *>)?.showBottomNavigation(show)
    }

    fun getStatusBarHeight() =
        (activity as? BaseMVVMActivity<*, *, *>)?.getStatusBarHeight() ?: 0

    private fun subscribeNoInternetEvent() {
        observe(viewModel.noInternet) {
            showNoInternet(it)
        }
    }

    private fun logout() {
        (activity as BaseMVVMActivity<*, *, *>).logout()
    }

    open fun onBackPressed(): Boolean {
        return true
    }

}