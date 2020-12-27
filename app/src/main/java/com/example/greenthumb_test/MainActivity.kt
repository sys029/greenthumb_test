package com.example.greenthumb_test

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : BaseActivity() {

    val builder = GsonBuilder()
    val gson = builder.serializeNulls().create()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setProgressBar(progressBar)
        hideProgressBar()



        buttonLogin.setOnClickListener {

            showProgressBar()
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if(email.isEmpty()){
                hideProgressBar()
                editTextEmail.error = "Email required"
                editTextEmail.requestFocus()
                return@setOnClickListener
            }


            if(password.isEmpty()){
                hideProgressBar()
                editTextPassword.error = "Password required"
                editTextPassword.requestFocus()
                return@setOnClickListener
            }
            val user_type: Int = 1
            val provider_type: Int = 1


            callLoginAPI(email,password,user_type,provider_type)

        }


    }

    fun callLoginAPI(email: String, password: String, userType: Int, providerType: Int) {


        RetrofitClient.instance.userLogin(email, password,userType,providerType)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {

                    hideProgressBar()

                    when {
                        response.code() == 400 -> {
                            val loginBase = gson.fromJson(response.errorBody()?.charStream(), Error::class.java)
                            Toast.makeText(applicationContext, loginBase.message, Toast.LENGTH_LONG).show()
                        }
                        response.code() == 200 -> {
                            val loginBase = gson.fromJson(response.body().toString(), LoginResponse::class.java)
                            Toast.makeText(applicationContext, loginBase.message, Toast.LENGTH_LONG).show()
                            if (loginBase.status == resources.getString(R.string.success)) {

                                editTextEmail.setText("")
                                editTextPassword.setText("")
                                Toast.makeText(applicationContext, loginBase.message, Toast.LENGTH_LONG).show()
                                SharedPrefManager.getInstance(applicationContext).saveUser(loginBase.data!!)
                                val intent = Intent(applicationContext, HomeActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            }
                        }
                        else -> {
                            Toast.makeText(
                                applicationContext,
                                resources.getString(R.string.something_went),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }  override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                }

            })


    }

    override fun onStart() {
        super.onStart()

        if(SharedPrefManager.getInstance(this).isLoggedIn){
            val intent = Intent(applicationContext, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }


}
