package ru.kalugin19.fridge.android.pub.v1.ui

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater

/**
 * File for common methods
 */
val Context.layoutInflater: LayoutInflater get() = LayoutInflater.from(this)

/**
 * Get color code
 */
fun Context.color(res: Int): Int {
    return ContextCompat.getColor(this, res)
}

/**
 * RecyclerView Settings
 */
fun RecyclerView.addAdapter(context: Context, adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>) {
    this.layoutManager = LinearLayoutManager(context)
    this.setHasFixedSize(false)
    this.adapter = adapter
}
