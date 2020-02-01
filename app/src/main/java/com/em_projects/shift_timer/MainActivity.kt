package com.em_projects.shift_timer

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.em_projects.shift_timer.config.Dynamic
import com.em_projects.shift_timer.model.DataWrapper
import com.em_projects.shift_timer.model.remote.connector.users.LoginResponse
import com.em_projects.shift_timer.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val TAG: String = "MainActivity"

    // Communication
    private var userViewModel: UserViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "MainActivity")

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        empPasswordEditText.setOnEditorActionListener { _, _, _ ->
            connectButton.performClick()
            true
        }

        connectButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val empCod: String? = empCodeEditText.text.toString()
                val empTag: String? = empTagNumberEditText.text.toString()
                val empPwd: String? = empPasswordEditText.text.toString()
                if (isCodeValid(empCod) && isTagValid(empTag) && isPasswordValid(empPwd)) {
                    userViewModel?.login(empCod, empTag, empPwd)
                        ?.observe(this@MainActivity, object : Observer<DataWrapper<LoginResponse>> {
                            override fun onChanged(t: DataWrapper<LoginResponse>?) {
                                if (t?.data != null) {
                                    if (t.data.resultCode == 0 && t.data.jwt.isNotEmpty()) {
                                        Dynamic.jwt = t.data.jwt
                                        moveToNextScreen()
                                    } else {
                                        Toast.makeText(
                                            this@MainActivity,
                                            t.data.errorMessage,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        this@MainActivity,
                                        t?.throwable?.message ?: getString(R.string.login_failed),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        })
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        R.string.missing_login_data,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun isPasswordValid(pwd: String?): Boolean {
        if (pwd.isNullOrEmpty()) return false
        if (pwd.length < 8) return false
        return true
    }

    private fun isTagValid(tag: String?): Boolean {
        if (tag.isNullOrEmpty()) return false
        if (tag.length < 8) return false
        return true

    }

    private fun isCodeValid(code: String?): Boolean {
        if (code.isNullOrEmpty()) return false
        if (code.length < 8) return false
        return true
    }
}
