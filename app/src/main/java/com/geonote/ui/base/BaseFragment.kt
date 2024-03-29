package com.geonote.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.geonote.ViewModelFactory

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel, A : BaseActivity<*, *>> : Fragment() {

    abstract val mViewModelClass: Class<V>
    abstract val mLayoutId: Int
    abstract val mBindingVariable: Int

    protected lateinit var mActivity: A
    protected lateinit var mViewDataBinding: T
    protected lateinit var mViewModel: V

    protected open fun setupViewModel(viewModel: V) {
    }

    open fun onBackPressed() = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as A
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance()).get(mViewModelClass)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mViewDataBinding = DataBindingUtil.inflate(inflater, mLayoutId, container, false)
        return  mViewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.setVariable(mBindingVariable, mViewModel)
        mViewDataBinding.lifecycleOwner = this
        mViewDataBinding.executePendingBindings()
        setupViewModel(mViewModel)
    }
}

inline fun <T : Fragment> T.withArgs(builder: Bundle.() -> Unit): T =
    apply {
        arguments = Bundle().apply(builder)
    }