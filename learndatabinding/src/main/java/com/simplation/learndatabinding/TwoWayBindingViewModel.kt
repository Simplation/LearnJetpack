package com.simplation.learndatabinding

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableField

class TwoWayBindingViewModel : BaseObservable() {

    var loginObservableField: ObservableField<LoginModel>

    private var loginModel: LoginModel = LoginModel()

    init {
        loginModel.userName = "Michel"
        loginObservableField = ObservableField()
        loginObservableField.set(loginModel)
    }

    @Bindable
    fun getUserName(): String? {
        return loginObservableField.get()?.userName
    }

    fun setUserName(userName: String?) {
        if (userName != null && userName == loginModel.userName) {
            loginModel.userName = userName
            notifyPropertyChanged(BR.userName)
        }


        loginObservableField.get()?.userName = userName
    }

}