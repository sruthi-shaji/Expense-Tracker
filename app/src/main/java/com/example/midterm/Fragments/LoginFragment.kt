package com.example.midterm.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.midterm.ExpenseViewActivity
import com.example.midterm.LoginSignUpActivity
import com.example.midterm.Model.User
import com.example.midterm.R

/**
 * A simple [Fragment] subclass..
 */
class LoginFragment() : Fragment() {
    private lateinit var user : User;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        /*
         * setting up the singUp button
         * signUp button will replace the current LoginFragment with signUpFragment
         */
        val signupbtn = view.findViewById<Button>(R.id.gotosingup)
        signupbtn?.setOnClickListener {
            val singupFragment : Fragment = SignUpFragment()
            val parent_activity = activity as LoginSignUpActivity
            parent_activity.replaceFragment(singupFragment)
        }

        /*
         * setting up the login button
         * login button will validate the credentials
         * It will take the user to the next activity
         */
        val loginbtn = view.findViewById<Button>(R.id.login)
        loginbtn.setOnClickListener {
            if(validateLogin(view))
            {
                val intent = Intent(activity, ExpenseViewActivity::class.java)
                // username is passed to the next activity
                intent.putExtra("username",user.userName)
                startActivity(intent)
            }
        }
        return view
    }

    /*
     * validations for login
     */
    private fun validateLogin(view :View) :Boolean {
        var username = view.findViewById<EditText>(R.id.username)
        var password = view.findViewById<EditText>(R.id.password)
        //is the username / password is blank -> return false
        if(username.text.isNullOrBlank() || password.text.isNullOrBlank())
        {
            Toast.makeText(context,"Invalid Credentials!", Toast.LENGTH_SHORT).show()
            return false
        }
        //if the user does not exist -> return false
        if(!(activity as LoginSignUpActivity).isvalidUser(username.text.toString()))
        {
            Toast.makeText(context,"Invalid User!", Toast.LENGTH_SHORT).show()
            return false
        }

        this.user = (activity as LoginSignUpActivity).getUser(username.text.toString())
        //if the password does not match -> return fals
        if(!this.user.isValidPassword(password.text.toString()))
        {
            Toast.makeText(context,"Invalid Password!", Toast.LENGTH_SHORT).show()
            return false
        }
        //credentials are valid -> return true
        return true
    }
}