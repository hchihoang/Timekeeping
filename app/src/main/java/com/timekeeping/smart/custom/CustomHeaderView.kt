package com.timekeeping.smart.custom

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.timekeeping.smart.R
import com.timekeeping.smart.extension.*
import com.timekeeping.smart.utils.DeviceUtil
import kotlinx.android.synthetic.main.view_custom_header.view.*

class CustomHeaderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CustomViewRelativeLayout(context, attrs, defStyleAttr) {

    var onLeftClick: () -> Unit = {}
    var onRightClick: () -> Unit = {}

    override fun getLayoutRes() = R.layout.view_custom_header

    override fun getStyableRes() = R.styleable.CustomHeaderView

    override fun initView() {
        val lp = container_header.layoutParams
        lp.height =
            resources.getDimensionPixelSize(R.dimen.height_top_bar) + DeviceUtil.getStatusBarHeight(
                context
            )
        container_header.setPadding(0, DeviceUtil.getStatusBarHeight(context), 0, 0)
    }

    override fun initListener() {

    }

    override fun initData() {

    }

    @SuppressLint("ResourceAsColor")
    override fun initDataFromStyable(attr: TypedArray?) {
        if (attr != null) {
            try {
                if (attr.hasValue(R.styleable.CustomHeaderView_leftDrawable)) {
                    btn_menu_left.visible()
                    btn_menu_left.setPadding(5.dpToPx, 3.dpToPx, 1.dpToPx, 3.dpToPx)
                    btn_menu_left.loadImageUrl(
                        attr.getDrawable(
                            R.styleable.CustomHeaderView_leftDrawable
                        )
                    )
                } else {
                    btn_menu_left.invisible()
                }

                if (attr.hasValue(R.styleable.CustomHeaderView_leftTitle)) {
                    tv_menu_left.text = attr.getString(R.styleable.CustomHeaderView_leftTitle)
                    tv_menu_left.visible()
                } else {
                    tv_menu_left.gone()
                }

                if (attr.hasValue(R.styleable.CustomHeaderView_rightDrawable)) {
                    btn_menu_right.visible()
                    btn_menu_right.loadImageUrl(
                        attr.getDrawable(
                            R.styleable.CustomHeaderView_rightDrawable
                        )
                    )
                } else {
                    btn_menu_right.invisible()
                }

                if (attr.hasValue(R.styleable.CustomHeaderView_rightTitle)) {
                    tv_menu_right.text =
                        attr.getString(R.styleable.CustomHeaderView_rightTitle)
                    tv_menu_right.visibility = VISIBLE
                } else {
                    tv_menu_right.visibility = GONE
                }

                if (attr.hasValue(R.styleable.CustomHeaderView_title)) {
                    tv_header_title.visibility = VISIBLE
                    tv_header_title.text = attr.getText(R.styleable.CustomHeaderView_title)
                } else {
                    tv_header_title.visibility = GONE
                }

                if (attr.hasValue(R.styleable.CustomHeaderView_header_background)) {
                    setHeaderBackgroundColor(
                        attr.getColor(
                            R.styleable.CustomHeaderView_header_background,
                            ContextCompat.getColor(context, R.color.colorPrimary)
                        )
                    )
                }

                if (attr.hasValue(R.styleable.CustomHeaderView_rightTitleColor)) {
                    tv_menu_right.setTextColor(
                        attr.getColor(
                            R.styleable.CustomHeaderView_rightTitleColor,
                            R.color.md_black_1000
                        )
                    )
                }
                // Click listener
                btn_menu_left.setOnClickListener { onLeftClick() }
                tv_menu_left.setOnClickListener { onLeftClick() }
                btn_menu_right.setOnClickListener { onRightClick() }
                tv_menu_right.setOnClickListener { onRightClick() }
            } finally {
                attr.recycle()
            }
        }
    }

    fun setHeaderBackgroundColor(color: Int) {
        container_header.setBackgroundColor(color)
    }

    fun setTitleBackground(drawable: Int) {
        tv_header_title.setBackgroundResource(drawable)
    }

    fun setTitle(title: String) {
        tv_header_title.text = title
    }

    fun setRightTile(title: String?) {
        tv_menu_right.text = title
    }

    fun setLeftDrawable(@DrawableRes id: Int) {
        btn_menu_left.visibility = VISIBLE
        btn_menu_left.setImageResource(id)
    }

    fun setRightDrawable(@DrawableRes id: Int) {
        btn_menu_right.visibility = VISIBLE
        btn_menu_right.setImageResource(id)
    }

    fun showRightDrawable(isShow: Boolean) {
        if (isShow) {
            btn_menu_right.visible()
        } else {
            btn_menu_right.invisible()
        }
    }

    fun showLeftDrawable(isShow: Boolean) {
        if (isShow) {
            btn_menu_left.visible()
        } else {
            btn_menu_left.invisible()
        }
    }

    fun setImageToRightTitle(@DrawableRes id: Int = 0) {
        tv_menu_right.setCompoundDrawablesWithIntrinsicBounds(id, 0, 0, 0)
    }

    fun setPaddingBtnRight(isSet: Boolean) {
        if (!isSet) {
            btn_menu_right.setPadding(0, 0, 0, 0)
        }
    }
}