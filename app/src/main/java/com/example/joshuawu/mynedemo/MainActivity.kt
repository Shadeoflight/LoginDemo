package com.example.joshuawu.mynedemo

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.joshuawu.mynedemo.DataModels.MyneLoginPost
import com.example.joshuawu.mynedemo.DataModels.MyneLoginResponse
import com.example.joshuawu.mynedemo.RetrofitToolkit.MyneLoginService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Libraries to consider
 *
 * butterknife - Helps with preventing code repetition with R.id.example
 *
 */

// MainActivity extends AppCompatActivity
class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // View element references
        var _text_user_name = findViewById<EditText>(R.id.et_user_name)
        var _text_password = findViewById<EditText>(R.id.et_password)
        var _button_login = findViewById<Button>(R.id.btn_login_button)

        /**
         * Login page code
         */

        _button_login.setOnClickListener{
            login();

            //
            // TEST CODE - Works, needs modifications
            //

            //
            //
            //

        }
    }

    private fun login() {
        Log.d(TAG, "Login")

        // View element references
        var _text_user_name = findViewById<EditText>(R.id.et_user_name)
        var _text_password = findViewById<EditText>(R.id.et_password)
        var _button_login = findViewById<Button>(R.id.btn_login_button)

        if(!validated()) {
            //onLoginFailed(); // Testing purposes
            return;
        }

        // Disable the login button
        _button_login.isEnabled = false;

        // Initialize HTTP POST request
        val userLoginObj = MyneLoginPost(_text_user_name.text.toString(),_text_password.text.toString())
        val loginAPI = MyneLoginService.create();

        // Initialize and show authentication/loading spinner
        val dialog_authProgress = ProgressDialog(this);
        dialog_authProgress.isIndeterminate = true;
        dialog_authProgress.setMessage("Authenticating...")
        // Block UI Interaction
        dialog_authProgress.setCancelable(false)

        // Authentication logic
        //

        // Send HTTP POST request
        val result = loginAPI.login(userLoginObj)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    result ->
                    // Do UI stuff with the HTTP request response/result
                    if(result.success == false)
                    {
                        dialog_authProgress.dismiss();
                        onLoginFailed();
                    } else {
                        Log.d("Result", result.token);
                        dialog_authProgress.dismiss();
                        Log.d("Result", "AFTER TOKEN");
                        onLoginSuccess();
                    }
                },{
                    error ->
                    // Handles HTTP request error
                    Log.d("Result", error.toString());
                })

        fun cancelRequest(){
            // Cancel the request
            result.dispose();
        }
        // Add a cancel button to the progress dialog
        dialog_authProgress.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", DialogInterface.OnClickListener(fun(_:DialogInterface,_:Int){
            dialog_authProgress.dismiss()
            // Close HTTP request
            cancelRequest();
            //
            onLoginFailed()
        }))
        dialog_authProgress.show()


        // Run on UI thread
        var mHandler: Handler? = null
        var runnable = Runnable {
            fun run(){
                // Login successful - Segue to next page
                onLoginSuccess()

                // Login failed
                //dialog_authProgress.dismiss()
                onLoginFailed()
            }
        }
        mHandler?.postDelayed(runnable, 1000)
    }

    private fun onLoginSuccess(){

        var _button_login = findViewById<Button>(R.id.btn_login_button)

        _button_login.isEnabled = true;

        // TODO: Go to next page after success
        //

        // Call onDestroy()
        //finish()
    }

    private fun onLoginFailed(){

        var _button_login = findViewById<Button>(R.id.btn_login_button)

        Toast.makeText(baseContext,"Login failed", Toast.LENGTH_SHORT).show()
        _button_login.isEnabled = true;
    }

    /**
     * Performs check on valid inputs
     */
    private fun validated(): Boolean{

        var _text_user_name = findViewById<EditText>(R.id.et_user_name)
        var _text_password = findViewById<EditText>(R.id.et_password)

        var valid = true;

        val user_name = _text_user_name.text.toString()
        val password = _text_password.text.toString()

        if(user_name.isEmpty())
        {
            Toast.makeText(baseContext, "Enter a valid username", Toast.LENGTH_SHORT).show()
            valid = false
        }
        if(password.isEmpty() || password.length < 7 || password.length > 20)
        {
            Toast.makeText(baseContext, "Password must be between 7 and 20 alphanumeric characters", Toast.LENGTH_SHORT).show()
            valid = false
        }

        return valid
    }

    /**
     * Utility functions
     */

    // Sign-up code
    /*
    override protected fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

    }
    */

    // Overrides back button pressed
    override fun onBackPressed(){
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

}
