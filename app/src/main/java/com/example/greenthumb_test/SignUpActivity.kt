package com.example.greenthumb_test

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignUpActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    val builder = GsonBuilder()
    val gson = builder.serializeNulls().create()

    private lateinit var countryList : Array<CountryData>
    private lateinit var stateList : Array<StateData>
    private lateinit var cityList : Array<CityData>
    private lateinit var countryId :String
    private lateinit var stateId:String
    private lateinit var cityId:String


    var user_type: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        toolbarSignUp.title=""
        setSupportActionBar(toolbarSignUp)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        countrySpinner.onItemSelectedListener = this
        stateSpinner.onItemSelectedListener = this
        citySpinner.onItemSelectedListener = this

        countryApi()


        radioSignUp.setOnCheckedChangeListener { group, checkedId ->

            if (checkedId == R.id.rUser)
                user_type = 1
            if (checkedId == R.id.rProvider)
                user_type = 2
            if (checkedId == R.id.rConsultant)
                user_type = 3
        }

        signUpButton.setOnClickListener {

            val firstN = firstName.text.toString().trim()
            val lastN = lastName.text.toString().trim()
            val phone = phoneNumber.text.toString().trim()
            val emailId = emailID.text.toString().trim()
            val password = passwordText.text.toString().trim()
            val cpassword = confirmPass.text.toString().trim()

            if(firstN.isEmpty()){
                firstName.error = "First Name required"
                firstName.requestFocus()
                return@setOnClickListener
            }

            if(lastN.isEmpty()){
                lastName.error = "Last Name required"
                lastName.requestFocus()
                return@setOnClickListener
            }

            if(phone.isEmpty()){
                phoneNumber.error = "Phone required"
                phoneNumber.requestFocus()
                return@setOnClickListener
            }

            if(emailId.isEmpty()){
                emailID.error = "Email required"
                emailID.requestFocus()
                return@setOnClickListener
            }

            if(password.isEmpty()){
                passwordText.error = "Password required"
                passwordText.requestFocus()
                return@setOnClickListener
            }

            if(cpassword.isEmpty()){
                confirmPass.error = "Confirm Password required"
                confirmPass.requestFocus()
                return@setOnClickListener
            }

            if(cpassword != password){
                confirmPass.error = "Dont match"
                confirmPass.requestFocus()
                return@setOnClickListener
            }



            Toast.makeText(this,cityId,Toast.LENGTH_SHORT).show()
            callSignUpAPI(firstN,lastN,phone,password,user_type,emailId,countryId,cityId,stateId)
        }



    }

    private fun countryApi() {

        RetrofitClient.instance.countryList()
            .enqueue(object : Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                    when{
                        response.code() == 200 -> {
                            CountrySpinnerLoad(response)
                        }

                        response.code() == 400 -> {
                            val res = gson.fromJson(response.errorBody()?.charStream(), Error::class.java)
                        }

                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(applicationContext, "Some error",Toast.LENGTH_SHORT).show()
                }

            })





    }



    private fun callSignUpAPI(
        firstName: String,
        lastName: String,
        phone: String,
        password: String,
        userType: Int,
        emailId: String,
        country: String,
        city: String,
        state: String
    ) {

        RetrofitClient.instance.userSignup(firstName, lastName,emailId,password,phone,country,state,city,userType)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {


                    when {
                        response.code() == 400 -> {
                            val loginBase = gson.fromJson(response.errorBody()?.charStream(), Error::class.java)
                            Toast.makeText(applicationContext, loginBase.message, Toast.LENGTH_LONG).show()
                        }
                        response.code() == 200 -> {
                            val loginBase = gson.fromJson(response.body().toString(), SignupResponse::class.java)
                            Toast.makeText(applicationContext, loginBase.message, Toast.LENGTH_LONG).show()
                            if (loginBase.status == resources.getString(R.string.success)) {

                                Toast.makeText(applicationContext, loginBase.message, Toast.LENGTH_LONG).show()

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


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(parent?.id) {
            R.id.countrySpinner -> {
                countryId = countryList.get(position).country_id
                RetrofitClient.instance.stateList(countryId)
                    .enqueue(object : Callback<JsonObject>{
                        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                            when{

                                response.code() == 200 -> {

                                    StateSpinnerLoad(response)
                                }

                                response.code() == 400 -> {
                                    val res = gson.fromJson(response.errorBody()?.charStream(), Error::class.java)
                                }


                            }
                        }

                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                            Toast.makeText(applicationContext, "Some error",Toast.LENGTH_SHORT).show()
                        }

                    })

            }

            R.id.stateSpinner -> {
                stateId = stateList.get(position).state_id

                RetrofitClient.instance.cityList(stateId)
                    .enqueue(object : Callback<JsonObject>{
                        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                            when{

                                response.code() == 200 -> {
                                    CitySpinnerLoad(response)
                                }

                                response.code() == 400 -> {
                                    val res = gson.fromJson(response.errorBody()?.charStream(), Error::class.java)
                                }


                            }

                        }

                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                        }

                    })


            }

            R.id.citySpinner -> {
                cityId = cityList.get(position).city_id

            }

        }

    }

    private fun CountrySpinnerLoad(response: Response<JsonObject>) {

        val res = gson.fromJson(response.body().toString(), CountryResponse::class.java)
        countryList = res.data.toTypedArray()
        var country = arrayOfNulls<String>(countryList.size)


        for (i in countryList.indices) {
            country[i] = countryList[i].country_name

        }

        val adapter =  ArrayAdapter(this@SignUpActivity, android.R.layout.simple_spinner_item,country)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        countrySpinner.adapter = adapter


    }

    private fun CitySpinnerLoad(response: Response<JsonObject>) {
        val res = gson.fromJson(response.body().toString(), CityResponse::class.java)
        cityList = res.data.toTypedArray()
        var city = arrayOfNulls<String>(cityList.size)


        for (i in cityList.indices) {
            city[i] = cityList[i].city_name

        }

        val adapter =  ArrayAdapter(this@SignUpActivity, android.R.layout.simple_spinner_item,city)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        citySpinner.adapter = adapter

    }

    private fun StateSpinnerLoad(response: Response<JsonObject>) {
        val res = gson.fromJson(response.body().toString(), StateResponse::class.java)
        stateList = res.data.toTypedArray()
        var state = arrayOfNulls<String>(stateList.size)

        for (i in stateList.indices) {
            state[i] = stateList[i].state_name
        }
        val adapter =  ArrayAdapter(this@SignUpActivity, android.R.layout.simple_spinner_item,state)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        stateSpinner.adapter = adapter

    }


}
