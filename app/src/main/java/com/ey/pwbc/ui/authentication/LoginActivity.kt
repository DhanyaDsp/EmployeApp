package com.ey.pwbc.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ey.pwbc.R
import com.ey.pwbc.UtilsDialog
import com.ey.pwbc.databinding.ActivityLoginBinding
import com.ey.pwbc.model.User
import com.ey.pwbc.webservice.APICallback
import com.ey.pwbc.webservice.ApiClient
import com.ey.pwbc.webservice.ApiInterface
import com.ey.pwbc.webservice.response.ContractAddressResponse
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity(), APICallback {


    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private var contractAddress: String? = null
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = getString(R.string.app_name)
        initDataBinding();
        subscribe();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun subscribe() {
        loginViewModel.getUser().observe(this, Observer<User> { user ->
            run {
                if (user != null) {
                    moveToKeyGenerationScreen(user.getUserType())
                }
            }
        })
    }

    private fun initDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        binding.viewModel = loginViewModel
        getContractAddress(this)
    }

    fun onLoginClicked(view: View) {

        user =
            User(edt_login_username.text.toString(), txt_input_text_password.text.toString(), 0)
        binding.user = user

        if (user!!.isDataValid) {
            loginViewModel.onLoginClicked(user!!)
        } else {
            UtilsDialog.showAlert(
                "Please ensure username and password length is greater than 6",
                "Confermi",
                this,
                View.OnClickListener { },
                View.OnClickListener { },
                View.OnClickListener { },
                UtilsDialog.TYPE_ALERT
            )
        }
    }

    private fun getContractAddress(callBack: APICallback) {
        val apiService: ApiInterface =
            ApiClient.getContractAddress().create(ApiInterface::class.java)
        val call: Call<ContractAddressResponse> =
            apiService.getContractAddress()
        Log.d("sos", "---URL--- contract address: " + call.request().url())
        call.enqueue(object : Callback<ContractAddressResponse> {
            override fun onFailure(call: Call<ContractAddressResponse>, t: Throwable) {
                Log.d("sos", "contract address Failure:" + t.message)
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<ContractAddressResponse>,
                response: Response<ContractAddressResponse>
            ) {

                try {
                    var contractAddressResponse: ContractAddressResponse = response.body()!!
                    Log.d("sos", "contract address Success " + contractAddressResponse.isSuccess())

                    if (contractAddressResponse != null) {
                        if (contractAddressResponse.isSuccess()!!) {
                            contractAddress = contractAddressResponse.getContractAddress()
                            Log.d(
                                "sos",
                                "contract address: get Contract Address:" + contractAddress
                            )
                            callBack.onSuccess(103, contractAddressResponse, response.code())


                        } else {
                            contractAddressResponse.setMessage("Failed to get the Contract Address")
                            callBack.onFailure(
                                103, contractAddressResponse.getMessage(), response.code()
                            )
                        }

                    } else {
                        contractAddressResponse = ContractAddressResponse();
                        contractAddressResponse.setMessage("Failed to get the Contract Address")
                        callBack.onFailure(
                            103,
                            contractAddressResponse.getMessage(),
                            response.code()
                        )
                    }
                } catch (e: java.lang.Exception) {
                    runOnUiThread {
                        Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
                    }


                }


            }

        })
    }

    private fun moveToKeyGenerationScreen(userType: Int) {
        if (userType == User.TYPE_EMPLOYEE || userType == User.TYPE_MERCHANT) {
            //we have to pass the user type to the SDK
            val intent = Intent(this, KeyGenerationActivity::class.java)
            intent.putExtra("user_name", user?.getUserName())
            intent.putExtra("password", user?.getPassword())
            intent.putExtra("contractAddress", contractAddress)
            startActivity(intent)
        }
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


    override fun onSuccess(requestCode: Int, obj: Any, code: Int) {
        Log.d("sos", "request code $requestCode")
    }

    override fun onFailure(requestCode: Int, obj: Any, code: Int) {
        Log.d("sos", "onFailure request code $requestCode")
    }

    override fun onProgress(requestCode: Int, isLoading: Boolean) {
        Log.d("sos", "onProgress  $isLoading")
    }
}

