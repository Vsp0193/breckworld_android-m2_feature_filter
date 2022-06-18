package com.breckworld.architecture

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.breckworld.BR
import com.breckworld.R
import com.breckworld.extensions.toast

abstract class BaseMVVMDialogFragment<V : BaseMVVMViewModel<T>, B : ViewDataBinding, T : Enum<T>>
    : DialogFragment() {

    protected lateinit var viewModel: V
    protected lateinit var binding: B

    protected abstract fun viewModelClass(): Class<V>

    @LayoutRes
    protected abstract fun layoutResId(): Int

    abstract fun subscribeToEvents()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(viewModelClass())
        subscribeToEvents()
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

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    open fun getStatusBarIconColor(): Boolean {
        return true//If true icons will be white otherwise status bar icons will be black
    }

    fun resolveStatusBarAndIconsColor(){
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

    override fun onStart() {
        super.onStart()
        resolveStatusBarAndIconsColor()
    }

    fun setLoading(visible: Boolean) {

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

    open fun onBackPressed(): Boolean {
        return true
    }
}