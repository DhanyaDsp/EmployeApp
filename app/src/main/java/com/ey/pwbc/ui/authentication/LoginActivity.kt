package com.ey.pwbc.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ey.pwbc.Interface.LoginResultCallBacks
import com.ey.pwbc.R
import com.ey.pwbc.Utils
import com.ey.pwbc.databinding.ActivityLoginBinding
import com.ey.pwbc.model.User


class LoginActivity : AppCompatActivity() {


    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    val user = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = getString(R.string.app_name)
        initDataBinding();
        subscribe();

        //loginViewModel.onLoginClicked()

/*        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        binding = DataBindingUtil.setContentView(this@LoginActivity, R.layout.activity_login)

        binding.lifecycleOwner = this

        binding.viewModel = loginViewModel*/


/*        val activityMainBinding =
            DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        activityMainBinding.viewModel = ViewModelProviders.of(
            this,
            LoginViewModelFactory(this)
        )
            .get(LoginViewModel::class.java)*/

        //loginViewModel.onLoginClicked(loginButton)

        //createPrivateKey()
        /*loginButton.setOnClickListener {
            if (validateComponents()) {
                intent = Intent(applicationContext, KeyGenerationActivity::class.java)
                startActivity(intent)
                finish()
            }
        }*/
    }

    private fun subscribe() {
        loginViewModel.getUser().observe(this, Observer<User> { user ->
            run {
                if (user != null) {
                    moveToKeyGenerationScreen()
                }
            }
        })
    }

    private fun moveToKeyGenerationScreen() {
        val intent = Intent(this, KeyGenerationActivity::class.java)
        startActivity(intent)
    }

    private fun initDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        binding.viewModel = loginViewModel
        binding.user = user;
    }


/*    private fun validateComponents(): Boolean {
        if (userName?.text.toString().isEmpty()) {
            Toast.makeText(this, "Username can not be blank", Toast.LENGTH_SHORT).show()
            return false
        } else if (userPassword?.text.toString().isEmpty()) {
            Toast.makeText(this, "Password can not be blank", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }*/

    fun onLoginClicked(view: View) {
        if (user.isDataValid) {
            loginViewModel.onLoginClicked(user)
        } else {
            Utils.showAlert(
                "Please ensure username and password length is greater than 6",
                "Confermi",
                this,
                View.OnClickListener { },
                View.OnClickListener { },
                View.OnClickListener { },
                Utils.TYPE_ALERT
            )
        }
    }
}
