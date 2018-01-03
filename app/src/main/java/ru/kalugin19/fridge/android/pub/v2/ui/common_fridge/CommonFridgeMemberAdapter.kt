package ru.kalugin19.fridge.android.pub.v2.ui.common_fridge

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_member.view.*
import ru.kalugin19.fridge.android.pub.v2.R
import ru.kalugin19.fridge.android.pub.v2.data.entity.Member
import ru.kalugin19.fridge.android.pub.v2.ui.layoutInflater

class CommonFridgeMemberAdapter(private val context: Context, private val members: List<Member>) : RecyclerView.Adapter<CommonFridgeMemberAdapter.Holder>() {

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        holder?.itemView?.apply {
            val member = members[position]
            email.text = member.email
            name.text = member.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        return Holder(context.layoutInflater.inflate(R.layout.item_member, parent, false))
    }

    override fun getItemCount(): Int {
        return members.size
    }

    class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView)
}