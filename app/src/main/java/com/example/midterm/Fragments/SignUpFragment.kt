package com.example.midterm.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.midterm.LoginSignUpActivity
import com.example.midterm.Model.User
import com.example.midterm.R

/**
 * A simple [Fragment] subclass.
 */
class SignUpFragment : Fragment() {

    private lateinit var  user : User;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        /*
         * setting up the login button
         * login button will replace the current signUpFragment with LoginFragment
         */
        val loginbtn = view.findViewById<Button>(R.id.gotologin)
        loginbtn?.setOnClickListener {
            val singupFragment : Fragment = LoginFragment()
            val parent_activity = activity as LoginSignUpActivity
            parent_activity.replaceFragment(singupFragment)
        }

        /*
         * setting up the signup button
         * signUp button will validate the user details and store them.
         * the user will be taken back to the loginFragment to login again.
         */
        val signupbtn = view.findViewById<Button>(R.id.signup)
        signupbtn.setOnClickListener {
            if(validateSignup(view))
            {
                (activity as LoginSignUpActivity).saveUser(user)
                Toast.makeText(context,"New user created!", Toast.LENGTH_SHORT).show()

                val singupFragment : Fragment = LoginFragment()
                val parent_activity = activity as LoginSignUpActivity
                parent_activity.replaceFragment(singupFragment)
            }
        }

        return view
    }

    fun validateSignup(view: View) : Boolean
    {
        var profileName = view.findViewById<EditText>(R.id.profileName)
        var userName = view.findViewById<EditText>(R.id.userName)
        var passWord = view.findViewById<EditText>(R.id.passWord)
        //is the username / profile name /password is blank -> return false
        if(profileName.text.isNullOrBlank() || userName.text.isNullOrBlank() || passWord.text.isNullOrBlank())
        {
            Toast.makeText(context,"Invalid Details", Toast.LENGTH_SHORT).show()
            return false
        }
        //if the user already exist -> return false
        if((activity as LoginSignUpActivity).isvalidUser(userName.text.toString()))
        {
            Toast.makeText(context,"UserName already exists", Toast.LENGTH_SHORT).show()
            return false
        }
        //details are valid -> return true
        user = User(profileName.text.toString(), userName.text.toString(),passWord.text.toString())
        return true
    }
}