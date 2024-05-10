package com.example.midterm.Model

data class User(var profileName : String, var userName : String, var userPassword : String) {

    fun isValidPassword(password: String): Boolean {
        if(userPassword.equals(password))
        {
            return true
        }
        return false
    }

}