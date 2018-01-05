package ru.kalugin19.fridge.android.pub.v2.ui.common_fridge

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_common_fridge.*
import ru.kalugin19.fridge.android.pub.v2.R
import ru.kalugin19.fridge.android.pub.v2.data.entity.Member
import ru.kalugin19.fridge.android.pub.v2.ui.addAdapter
import ru.kalugin19.fridge.android.pub.v2.ui.base.view.activity.BaseActivity


class CommonFridgeActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_fridge)
        val list = ArrayList<Member>()
//        recyclerMembers.addAdapter(this, CommonFridgeMemberAdapter(this, list))
    }
}