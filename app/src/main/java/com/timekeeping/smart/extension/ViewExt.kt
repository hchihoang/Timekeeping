package com.timekeeping.smart.extension

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.timekeeping.smart.R
import com.timekeeping.smart.utils.Constant
import com.timekeeping.smart.utils.DateUtils
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.Calendar
import java.util.Date

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun ViewGroup.inflate(@LayoutRes layout: Int): View {
    return LayoutInflater.from(context).inflate(layout, this, false)
}

const val CLICK_THROTTLE_DELAY = 800L

fun View.onAvoidDoubleClick(
    throttleDelay: Long = CLICK_THROTTLE_DELAY,
    onClick: (View) -> Unit
) {
    setOnClickListener {
        onClick(this)
        isClickable = false
        postDelayed({ isClickable = true }, throttleDelay)
    }
}

infix fun TextView.textChangedListener(init: TextWatcherWrapper.() -> Unit) {
    val wrapper = TextWatcherWrapper().apply { init() }
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable) {
            wrapper.after?.invoke(p0)
        }

        override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
            wrapper.before?.invoke(p0, p1, p2, p3)
        }

        override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
            wrapper.on?.invoke(p0, p1, p2, p3)
        }

    })
}

infix fun ViewPager.pageChangeListener(init: OnPageChangeListenerWrapper.() -> Unit) {
    val wrapper = OnPageChangeListenerWrapper().apply { init() }
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
            wrapper.onPageScrollStateChanged?.invoke(state)
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            wrapper.onPageScrolled?.invoke(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            wrapper.onPageSelected?.invoke(position)
        }

    })
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}

fun EditText.disablePaste() {
    val callback: ActionMode.Callback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            menu?.removeItem(android.R.id.paste)
            menu?.removeItem(android.R.id.autofill)
            return true
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode?) {}
    }
    this.customInsertionActionModeCallback = callback
    this.customSelectionActionModeCallback = callback
}

fun AppCompatEditText.passwordType() {
    inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
    typeface = Typeface.DEFAULT
}

fun AppCompatActivity.enableFullScreen() {
    val decorView = window.decorView
    decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
}

fun ImageView.loadImageUrl(url: Any?) {
    Glide.with(this.context)
        .load(url)
        .placeholder(R.drawable.img_default)
        .error(R.drawable.img_default)
        .centerCrop()
        .into(this)
}

fun ImageView.loadImageTestUrl(url: Any?) {
    Glide.with(this.context)
        .load(url)
        .placeholder(R.drawable.ic_image_default_new)
        .error(R.drawable.ic_image_default_new)
        .into(this)
}

fun Dialog.showDatePickerDialog(
    isSetMaxDate: Boolean = false,
    beforeDate: Date,
    context: Context,
    callBack: (String) -> Unit
) {
    val newCalendar: Calendar = Calendar.getInstance()
    newCalendar.time = beforeDate
    val startTime = DatePickerDialog(
        context, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
        { _, year, monthOfYear, dayOfMonth ->
            val newDate: Calendar = Calendar.getInstance()
            newDate.set(year, monthOfYear, dayOfMonth)
            callBack(DateUtils.dateToString(newDate.time, Constant.DATE_FORMAT_2))
        },
        newCalendar.get(Calendar.YEAR),
        newCalendar.get(Calendar.MONTH),
        newCalendar.get(Calendar.DAY_OF_MONTH)
    )
    if (isSetMaxDate) {
        startTime.datePicker.maxDate = Date().time
    }
    // an border xung dialog_date
    startTime.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    startTime.show()
}

fun EditText.observableFromView(): Observable<String> {
    val subject = PublishSubject.create<String>()
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(text: Editable?) {
            subject.onNext(text.toString())
        }
    })
    return subject
}