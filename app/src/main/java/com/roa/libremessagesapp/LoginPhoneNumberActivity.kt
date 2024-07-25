package com.roa.libremessagesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.hbb20.CountryCodePicker
import com.roa.libremessagesapp.databinding.ActivityLoginPhoneNumberBinding
import com.roa.libremessagesapp.databinding.ActivitySplashBinding



class LoginPhoneNumberActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginPhoneNumberBinding

    lateinit var countryCodePicker: CountryCodePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPhoneNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        countryCodePicker = binding.loginCountrycode
        countryCodePicker.registerCarrierNumberEditText(binding.loginMobileNumber)

        binding.loginProgressBar.visibility = View.GONE

        binding.sendOtpBtn.setOnClickListener{
            if(!countryCodePicker.isValidFullNumber){
                binding.loginMobileNumber.error = "Phone number not valid"
                return@setOnClickListener
            }
            val intent = Intent(this, LoginOtpActivity::class.java)
            intent.putExtra("phone", countryCodePicker.fullNumberWithPlus )
            startActivity(intent)
        }
    }
}