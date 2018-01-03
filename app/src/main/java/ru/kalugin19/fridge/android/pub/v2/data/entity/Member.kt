package ru.kalugin19.fridge.android.pub.v2.data.entity


class Member(){
    var name = ""
    var email = ""
    var type = ""


    constructor(name: String, email: String, type: String) : this() {
        this.name = name
        this.email = email
        this.type = type
    }
}