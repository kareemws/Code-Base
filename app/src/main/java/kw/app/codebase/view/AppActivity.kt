package kw.app.codebase.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kw.app.codebase.R
import kw.app.codebase.vm.App

class AppActivity : AppCompatActivity() {

    private val vm: App by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
    }
}

