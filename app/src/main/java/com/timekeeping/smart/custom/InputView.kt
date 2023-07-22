package com.timekeeping.smart.custom

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import android.text.Html
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.core.content.ContextCompat
import com.timekeeping.smart.R
import com.timekeeping.smart.extension.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.view_input.view.*


class InputView(context: Context, attrs: AttributeSet?) :
    CustomViewConstraintLayout(context, attrs) {

    private var isShow = true

    var onClickBirthday: () -> Unit = {}

    companion object {
        const val TYPE_PASSWORD = 1
        const val TYPE_NORMAL = 2
        const val TYPE_REGISTER_PASSWORD = 3
        const val TYPE_REGISTER_CONFIRM_PASSWORD = 4
        const val TYPE_BIRTHDAY = 5
        const val TYPE_LOGIN = 6
        const val TYPE_REGISTER = 7
        const val TYPE_PHONE = 8
        const val TYPE_SEARCH = 9
    }

    override fun getLayoutRes(): Int {
        return R.layout.view_input
    }

    override fun getStyableRes(): IntArray? {
        return R.styleable.InputView
    }

    override fun initView() {

    }

    override fun initListener() {

    }

    override fun initData() {

    }

    @SuppressLint("ResourceAsColor")
    override fun initDataFromStyable(attr: TypedArray?) {
        if (attr != null) {
            try {
                if (attr.hasValue(R.styleable.InputView_background_input_view)) {
                    layout_input.background = attr.getDrawable(R.styleable.InputView_background_input_view)
                }
                if (attr.hasValue(R.styleable.InputView_color_hint)) {
                    edt_input.setHintTextColor(attr.getColor(R.styleable.InputView_color_hint, R.color.color_text_hint))
                }
                if (attr.hasValue(R.styleable.InputView_margin_start)) {
                    bg_icon.gone()
                    val marginStart= attr.getInt(R.styleable.InputView_margin_start, 0)
                    edt_input.setPaddingRelative(marginStart.dpToPx,0,0,0)
                }
                if (attr.hasValue(R.styleable.InputView_padding_end)) {
                    val paddingEnd = attr.getInt(R.styleable.InputView_padding_end, 0)
                    edt_input.setPaddingRelative(edt_input.paddingStart,edt_input.paddingTop,paddingEnd.dpToPx, edt_input.paddingBottom)
                }
                if (attr.hasValue(R.styleable.InputView_input_hint)) {
                    edt_input.hint = attr.getString(R.styleable.InputView_input_hint)
                }
                if (attr.hasValue(R.styleable.InputView_disable_paste)) {
                    if (attr.getBoolean(R.styleable.InputView_disable_paste, false)) {
                        edt_input.disablePaste()
                    }
                }
                if (attr.hasValue(R.styleable.InputView_left_drawable)) {
                    ic_left.setImageResource(
                        attr.getResourceId(
                            R.styleable.InputView_left_drawable,
                            0
                        )
                    )
                }
                if (attr.hasValue(R.styleable.InputView_input_type)) {
                    when (attr.getInt(R.styleable.InputView_input_type, 0)) {
                        TYPE_NORMAL -> {
                            edt_input.inputType = InputType.TYPE_CLASS_TEXT
                        }
                        TYPE_PASSWORD -> {
                            ic_show_hide_password.visible()
                            ic_show_hide_password.setOnClickListener {
                                showPassword(isShow)
                                isShow = !isShow
                            }
                            edt_input.passwordType()
                            edt_input.filters = arrayOf(InputFilter.LengthFilter(30))
                        }
                        TYPE_REGISTER_PASSWORD -> {
                            ic_show_hide_password.visible()
                            ic_show_hide_password.setOnClickListener {
                                showPassword(isShow)
                                isShow = !isShow
                            }
                            edt_input.hint =
                                Html.fromHtml(resources.getString(R.string.register_password_hint))
                            edt_input.passwordType()
                        }
                        TYPE_REGISTER_CONFIRM_PASSWORD -> {
                            ic_show_hide_password.visible()
                            ic_show_hide_password.setOnClickListener {
                                showPassword(isShow)
                                isShow = !isShow
                            }
                            edt_input.passwordType()
                        }
                        TYPE_BIRTHDAY -> {
                            edt_input.inputType = InputType.TYPE_CLASS_TEXT
                            edt_input.isFocusable = false
                            edt_input.onAvoidDoubleClick {
                                onClickBirthday()
                            }
                        }
                        TYPE_PHONE -> {
                            edt_input.filters = arrayOf(InputFilter.LengthFilter(10))
                            edt_input.inputType = InputType.TYPE_CLASS_PHONE
                        }
                        TYPE_SEARCH -> {
                            edt_input.inputType = InputType.TYPE_CLASS_TEXT
//                            layout_input.background =layout_input
//                                ContextCompat.getDrawable(context, R.drawable.bg_input_search)
                            bg_icon.setPadding(
                                4.dpToPx, 8.dpToPx, 8.dpToPx, 4.dpToPx
                            )
                            edt_input.setPadding(0, 0, 10.dpToPx, 0)
                            edt_input.imeOptions = EditorInfo.IME_ACTION_SEARCH
                        }
                    }
                }
                if (attr.hasValue(R.styleable.InputView_type)) {
                    when (attr.getInt(R.styleable.InputView_type, 0)) {
                        TYPE_LOGIN -> {
                            edt_input.filters = arrayOf(InputFilter.LengthFilter(20))
//                            bg_icon.background =
//                                ContextCompat.getDrawable(context, R.drawable.bg_icon_edt_login)
                        }
                        TYPE_REGISTER -> {
                            bg_icon.setBackgroundColor(Color.TRANSPARENT)
                        }
                    }
                }
                if (attr.hasValue(R.styleable.InputView_padding_vertical)) {
                    val paddingVertical = attr.getInt(R.styleable.InputView_padding_vertical, 0)
                    if (paddingVertical > 0) {
                        bg_icon.setPadding(
                            18.dpToPx,
                            paddingVertical.dpToPx,
                            13.dpToPx,
                            paddingVertical.dpToPx
                        )
                    }
                }
            } finally {
                attr.recycle()
            }

            edt_input.setOnEditorActionListener { textView, actionId, keyEvent ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edt_input.hideKeyboard()
                }
                true
            }
        }
    }

    fun setText(text: String?) {
        edt_input.setText(text)
    }

    fun getText() = edt_input.text?.trim().toString()

    fun getTextNotTrim() = edt_input.text.toString()

    private fun showPassword(isShow: Boolean) {
        if (isShow) {
            ic_show_hide_password.setImageResource(R.drawable.ic_hide_password)
            edt_input.inputType = InputType.TYPE_CLASS_TEXT
        } else {
            ic_show_hide_password.setImageResource(R.drawable.ic_show_password)
            edt_input.inputType =
                InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            edt_input.typeface = Typeface.DEFAULT
        }
    }

    fun textChangedListener(action: (text: String) -> Unit) {
        edt_input.textChangedListener {
            after {
                action(it.toString().trim())
            }
        }
    }

    fun setOnEditorActionListener(action: (text: String) -> Unit) {
        edt_input.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                edt_input.hideKeyboard()
                action(edt_input.text.toString())
                return@OnEditorActionListener true
            }
            false
        })
    }

    fun setOnFocusChange(action: (hasFocus: Boolean) -> Unit){
        edt_input.setOnFocusChangeListener { _, hasFocus ->
            action(hasFocus)
        }
    }

    fun getFocus(): Boolean{
       return edt_input.isFocused
    }

    fun setObservableFromView(action: (text: String) -> Unit) {
        edt_input.observableFromView()
            .debounce(500, java.util.concurrent.TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                action(edt_input.text.toString().trim())
            }
    }

    fun setPaddingHorizontal(padding: Float) {
        edt_input.setPadding(padding.toInt(), 0, padding.toInt(), 0)
    }

    fun setPaddingContent(
        left: Float = 0F,
        top: Float = 0F,
        right: Float = 0F,
        bottom: Float = 0F
    ) {
        edt_input.setPadding(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
    }

    fun hideKeyboard() {
        edt_input.hideKeyboard()
    }

    fun setMaxLength(maxLength: Int = 100) {
        edt_input.filters = arrayOf(InputFilter.LengthFilter(maxLength))
    }

    fun showButtonRight(status: Boolean) {
        if (status)
            ic_show_hide_password.visible()
        else
            ic_show_hide_password.gone()
    }

    fun setBackgroundButtonRight(id: Int) {
        ic_show_hide_password.setImageResource(id)
    }

    fun rightIconClick(onClick: (View) -> Unit) {
        ic_show_hide_password.onAvoidDoubleClick {
            onClick(it)
        }
    }

    fun setBackgroundButtonLeft(id: Int) {
        bg_icon.background = ContextCompat.getDrawable(context, id)
    }

    fun setBackgroundLayoutInput(id: Int) {
        layout_input.background = ContextCompat.getDrawable(context, id)
    }

}