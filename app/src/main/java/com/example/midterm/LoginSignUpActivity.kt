package com.example.midterm

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.midterm.Fragments.LoginFragment
import com.example.midterm.Model.User
import com.google.gson.Gson

class LoginSignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_sign_up)

        //initialize the frame to loginFragment initially
        val loginFragment : Fragment = LoginFragment()
        val frtrans : FragmentTransaction = supportFragmentManager.beginTransaction()
        frtrans.replace(R.id.loginframe,loginFragment).commit()

    }

    //the fragment is replaced with new fragment in the framelayout
    fun replaceFragment(fragment: Fragment) {
        val frtrans : FragmentTransaction = supportFragmentManager.beginTransaction()
        frtrans.replace(R.id.loginframe,fragment).commit()
    }

    // get the user from SharedPreferences using userName
    fun getUser(userName: String): User {

        val  sharedPreferences : SharedPreferences = getSharedPreferences("Users", MODE_PRIVATE)
        val json : String? = sharedPreferences.getString(
            userName,null)
        val gson = Gson()
        return gson.fromJson(json, User::class.java)
    }

    // checks if the user exists -> return true if exits
    fun isvalidUser(userName: String): Boolean {
        val  sharedPreferences : SharedPreferences = getSharedPreferences("Users", MODE_PRIVATE)
        val json : String? = sharedPreferences.getString(
            userName,null)
        if(json != null)
        {
            return true
        }
        return false
    }

    // saves the user inside SharedPreferences
    fun saveUser(user: User) {
        val  sharedPreferences : SharedPreferences = getSharedPreferences("Users", MODE_PRIVATE)
        val gson = Gson()
        val json = gson.toJson(user)
        sharedPreferences.edit().putString(user.userName, json).apply()
    }

}