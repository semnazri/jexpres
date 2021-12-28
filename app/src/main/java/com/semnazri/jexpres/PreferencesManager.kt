package com.semnazri.jexpres

import com.orhanobut.hawk.Hawk
import com.semnazri.jexpres.model.MockModelResponse

object PreferencesManager {
    val TEMPDATA: String = "temptData"


    var temptdata: MockModelResponse
        set(value) {
            Hawk.put(TEMPDATA, value)
        }
        get() = Hawk.get(TEMPDATA)


}