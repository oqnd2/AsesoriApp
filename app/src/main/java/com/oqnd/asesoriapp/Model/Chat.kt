package com.oqnd.asesoriapp.Model

class Chat {
    private var idMsg : String = ""
    private var transmitter : String = ""
    private var receiver : String = ""
    private var msg : String = ""
    private var url : String = ""
    private var seen = false

    constructor()

    constructor(
        idMsg: String,
        transmitter: String,
        receiver: String,
        msg: String,
        url: String,
        seen: Boolean
    ) {
        this.idMsg = idMsg
        this.transmitter = transmitter
        this.receiver = receiver
        this.msg = msg
        this.url = url
        this.seen = seen
    }

    //getters y setters
    fun getIdMsg() : String?{
        return idMsg
    }

    fun setIdMsg(idMsg : String?){
        this.idMsg = idMsg!!
    }

    fun getTransmitter() : String?{
        return transmitter
    }

    fun setTransmitter(transmitter : String?){
        this.transmitter = transmitter!!
    }

    fun getReceiver() : String?{
        return receiver
    }

    fun setReceiver(receiver : String?){
        this.receiver = receiver!!
    }

    fun getMsg() : String?{
        return msg
    }

    fun setMsg(msg : String?){
        this.msg = msg!!
    }

    fun getUrl() : String?{
        return url
    }

    fun setUrl(url : String?){
        this.url = url!!
    }

    fun isSeen() : Boolean{
        return seen
    }

    fun setIsSeen(seen : Boolean?){
        this.seen = seen!!
    }
}