package com.roa.libremessagesapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.roa.libremessagesapp.databinding.ActivityLoginOtpBinding
import com.roa.libremessagesapp.utils.AndroidUtil
import java.util.concurrent.TimeUnit


class LoginOtpActivity : AppCompatActivity() {

    lateinit var phoneNumber: String
    var timeoutSeconds: Long = 60L
    lateinit var verificationCode: String

    private lateinit var binding: ActivityLoginOtpBinding
    var mAuth: FirebaseAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        phoneNumber = intent.extras?.getString("phone").toString()

        sendOtp(phoneNumber, false)

    }

    fun sendOtp(phoneNumber: String, isResend: Boolean){
        setInProgress(true)

        val builder = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    signIn(phoneAuthCredential)
                    setInProgress(false)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    AndroidUtil.showToast(applicationContext, "OTP verification failed")
                    setInProgress(false)
                }

                override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                    super.onCodeSent(s, forceResendingToken)
                    verificationCode = s
                    resendingToken = forceResendingToken
                    AndroidUtil.showToast(applicationContext, "OTP sent successfully")
                    setInProgress(false)
                }
            })

    }

    fun setInProgress(inProgress: Boolean){
        if(inProgress){
            binding.loginProgressBar.visibility = View.VISIBLE
            binding.loginNextBtn.visibility = View.GONE
        }else{
            binding.loginProgressBar.visibility = View.GONE
            binding.loginNextBtn.visibility = View.VISIBLE
        }
    }

    fun signIn(phoneAuthCredential: PhoneAuthCredential){
        //login and go the next activity
    }
}