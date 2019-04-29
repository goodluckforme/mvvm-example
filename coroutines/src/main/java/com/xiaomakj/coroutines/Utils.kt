package com.xiaomakj.coroutines

import android.content.ClipboardManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import kotlinx.coroutines.delay
import org.jetbrains.anko.toast
import thereisnospon.codeview.CodeView


val handle: Handler = Handler(Looper.getMainLooper())

fun View.canLoad(bolck: () -> Unit) {
    if (visibility == View.INVISIBLE) {
        visibility = View.VISIBLE
        bolck()
    }
}

fun TextView.addString(string: String, sb: StringBuffer) {
    handle.post { text = sb.append(string) }
}

fun View.hide() {
    handle.post { visibility = View.INVISIBLE }
}

fun resetSb(bolck: (sb: StringBuffer) -> Unit) = bolck(StringBuffer(""))

suspend fun doOne(): Int {
    delay(500L)
    return 1
}

suspend fun doTwo(): Int {
    delay(700L)
    return 5
}

fun CodeView.onClickCopy(txt: String) = setOnClickListener {
    // 从API11开始android推荐使用android.content.ClipboardManager
    // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
    // 将文本内容放到系统剪贴板里。
    (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).text = txt
    context.toast("复制成功")
}

fun String.onClickCopy(context: Context) {
    // 从API11开始android推荐使用android.content.ClipboardManager
    // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
    // 将文本内容放到系统剪贴板里。
    (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).text = this
    context.toast("复制成功")
}