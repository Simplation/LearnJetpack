package com.simplation.mvvm.app.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.view.View
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.blankj.utilcode.util.Utils
import com.kingja.loadsir.core.LoadService
import com.simplation.mvvm.R
import com.simplation.mvvm.app.weight.loadCallback.LoadingCallback
import com.tencent.mmkv.MMKV
import java.lang.reflect.InvocationTargetException
import kotlin.math.roundToInt

/**
 * Setting util
 *
 * @constructor Create empty Setting util
 */
object SettingUtil {

    /**
     * 获取当前主题颜色
     */
    fun getColor(context: Context): Int {
        val setting = PreferenceManager.getDefaultSharedPreferences(context)
        val defaultColor = ContextCompat.getColor(context, R.color.purple_500)
        val color = setting.getInt("color", defaultColor)
        return if (color != 0 && Color.alpha(color) != 255) {
            defaultColor
        } else {
            color
        }

    }

    /**
     * 设置主题颜色
     */
    fun setColor(context: Context, color: Int) {
        val setting = PreferenceManager.getDefaultSharedPreferences(context)
        setting.edit().putInt("color", color).apply()
    }

    /**
     * 获取列表动画模式
     */
    fun getListMode(): Int {
        val kv = MMKV.mmkvWithID("app")
        // 0.关闭动画 1.渐显 2.缩放 3.从下到上 4.从左到右 5.从右到左
        return kv.decodeInt("mode", 2)
    }

    /**
     * 设置列表动画模式
     */
    fun setListMode(mode: Int) {
        val kv = MMKV.mmkvWithID("app")
        kv.encode("mode", mode)
    }

    /**
     * 获取是否请求置顶文章
     */
    fun getRequestTop(context: Context): Boolean {
        val setting = PreferenceManager.getDefaultSharedPreferences(context)
        return setting.getBoolean("top", true)
    }

    fun getColorStateList(context: Context): ColorStateList {
        val colors = intArrayOf(getColor(context), ContextCompat.getColor(context, R.color.purple_500))
        val states = arrayOfNulls<IntArray>(2)
        states[0] = intArrayOf(android.R.attr.state_checked, android.R.attr.state_checked)
        states[1] = intArrayOf()
        return ColorStateList(states, colors)
    }

    fun getColorStateList(color: Int): ColorStateList {
        val colors = intArrayOf(color, ContextCompat.getColor(Utils.getApp(), R.color.teal_700))
        val states = arrayOfNulls<IntArray>(2)
        states[0] = intArrayOf(android.R.attr.state_checked, android.R.attr.state_checked)
        states[1] = intArrayOf()
        return ColorStateList(states, colors)
    }

    fun getOneColorStateList(context: Context): ColorStateList {
        val colors = intArrayOf(getColor(context))
        val states = arrayOfNulls<IntArray>(1)
        states[0] = intArrayOf()
        return ColorStateList(states, colors)
    }

    fun getOneColorStateList(color: Int): ColorStateList {
        val colors = intArrayOf(color)
        val states = arrayOfNulls<IntArray>(1)
        states[0] = intArrayOf()
        return ColorStateList(states, colors)
    }

    /**
     * 设置 shape 文件的颜色
     *
     * @param view
     * @param color
     */
    fun setShapeColor(view: View, color: Int) {
        val drawable = view.background as GradientDrawable
        drawable.setColor(color)
    }

    /**
     * 设置 shape 的渐变颜色
     */
    fun setShapeColor(view: View, color: IntArray, orientation: GradientDrawable.Orientation) {
        val drawable = view.background as GradientDrawable
        // 渐变方向
        drawable.orientation = orientation
        // 渐变颜色数组
        drawable.colors = color
    }

    /**
     * 设置 selector 文件的颜色
     *
     * @param view
     * @param yesColor
     * @param noColor
     */
    fun setSelectorColor(view: View, yesColor: Int, noColor: Int) {
        val mySelectorGrad = view.background as StateListDrawable
        try {
            val slDraClass = StateListDrawable::class.java
            val getStateCountMethod = slDraClass.getDeclaredMethod("getStateCount", *arrayOfNulls(0))
            val getStateSetMethod = slDraClass.getDeclaredMethod("getStateSet", Int::class.javaPrimitiveType)
            val getDrawableMethod = slDraClass.getDeclaredMethod("getStateDrawable", Int::class.javaPrimitiveType)
            // 对应 item 标签
            val count = getStateCountMethod.invoke(mySelectorGrad) as Int
            for (i in 0 until count) {
                // 对应 item 标签中的 android:state_xxxx
                val stateSet = getStateSetMethod.invoke(mySelectorGrad, i) as IntArray
                if (stateSet.isEmpty()) {
                    // 这就是你要获得的 Enabled 为 false 时候的 drawable
                    val drawable = getDrawableMethod.invoke(mySelectorGrad, i) as GradientDrawable
                    drawable.setColor(yesColor)
                } else {
                    for (j in stateSet.indices) {
                        // 这就是你要获得的 Enabled 为 false 时候的 drawable
                        val drawable = getDrawableMethod.invoke(mySelectorGrad, i) as GradientDrawable
                        drawable.setColor(noColor)
                    }
                }
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

    }

    /**
     * 设置颜色透明一半
     * @param color
     * @return
     */
    fun translucentColor(color: Int): Int {
        val factor = 0.5f
        val alpha = (Color.alpha(color) * factor).roundToInt()
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }

    /**
     * 设置 loading 的颜色 加载布局
     */
    @SuppressLint("CutPasteId")
    fun setLoadingColor(color:Int, loadsir: LoadService<Any>) {
        loadsir.setCallBack(LoadingCallback::class.java) { _, view ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.findViewById<ProgressBar>(R.id.loading_progress).indeterminateTintMode = PorterDuff.Mode.SRC_ATOP
                view.findViewById<ProgressBar>(R.id.loading_progress).indeterminateTintList = getOneColorStateList(color)
            }
        }
    }

}
