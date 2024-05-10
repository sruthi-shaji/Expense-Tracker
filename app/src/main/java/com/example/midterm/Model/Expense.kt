package com.example.midterm.Model

data class Expense(var ID: Int, var username : String, var type: String, var name: String, var amount: Float, var description: String ) {
    fun isValid(): Boolean {
        if(username.isNullOrBlank() || type.isNullOrBlank() ||  name.isNullOrBlank())
        {
            return false
        }
        return true
    }
}