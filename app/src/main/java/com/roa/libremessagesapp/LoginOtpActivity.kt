package com.roa.libremessagesapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.roa.libremessagesapp.databinding.ActivityLoginOtpBinding
import com.roa.libremessagesapp.utils.AndroidUtil
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit


class LoginOtpActivity : AppCompatActivity() {

    private lateinit var phoneNumber: String
    var timeoutSeconds: Long = 60L
    lateinit var verificationCode: String
    lateinit var resendingToken: ForceResendingToken

    private lateinit var binding: ActivityLoginOtpBinding
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        phoneNumber = intent.extras?.getString("phone").toString()

        sendOtp(phoneNumber, false)

        binding.loginNextBtn.setOnClickListener{
            val enteredOtp = binding.loginOtp.text.toString()
            val credential = PhoneAuthProvider.getCredential(verificationCode, enteredOtp)
            signIn(credential)
            //setInProgress(true)
        }

        binding.resendOtpTextview.setOnClickListener{
            sendOtp(phoneNumber, true)
        }

    }

    private fun sendOtp(phoneNumber: String, isResend: Boolean){
        startResendTimer()
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

        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build())
        }else{
            PhoneAuthProvider.verifyPhoneNumber(builder.build())
        }

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
        setInProgress(true)
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener { task ->
            setInProgress(false)
            if (task.isSuccessful) {
                val intent = Intent(
                    this@LoginOtpActivity,
                    LoginUsernameActivity::class.java
                )
                AndroidUtil.showToast(applicationContext, "OTP verification success")
                intent.putExtra("phone", phoneNumber)
                startActivity(intent)
            } else {
                AndroidUtil.showToast(applicationContext, "OTP verification failed")
            }
        }
    }

    private fun startResendTimer() {
        binding.resendOtpTextview.isEnabled = false
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                timeoutSeconds--
                binding.resendOtpTextview.text = "Resend OTP in $timeoutSeconds seconds"
                if (timeoutSeconds <= 0) {
                    timeoutSeconds = 60L
                    timer.cancel()
                    runOnUiThread { binding.resendOtpTextview.isEnabled = true }
                }
            }
        }, 0, 1000)
    }
}