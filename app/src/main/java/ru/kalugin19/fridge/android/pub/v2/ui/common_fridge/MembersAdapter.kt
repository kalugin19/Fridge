package ru.kalugin19.fridge.android.pub.v2.ui.common_fridge

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.kalugin19.fridge.android.pub.v2.R
import ru.kalugin19.fridge.android.pub.v2.data.entity.Member

class MembersAdapter(val context: Context, val members: List<Member>) : RecyclerView.Adapter<MembersAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_member, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        val member = members[position]
        holder?.itemView.apply {

        }
    }

    override fun getItemCount(): Int {
        return members.size
    }


    class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView)
}