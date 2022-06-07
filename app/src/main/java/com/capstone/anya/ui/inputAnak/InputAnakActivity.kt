package com.capstone.anya.ui.inputAnak

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.activity.viewModels
import com.capstone.anya.R
import com.capstone.anya.databinding.ActivityInputAnakBinding
import com.capstone.anya.databinding.ActivityLoginBinding
import com.capstone.anya.login.LoginActivity
import com.capstone.anya.register.RegisterViewModel
import com.capstone.anya.ui.listAnak.ListAnakActivity
import java.text.SimpleDateFormat
import java.util.*

class InputAnakActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var bindingInputAnak: ActivityInputAnakBinding
    private val inputAnakViewModel by viewModels<InputAnakViewModel>()

    private val myCalendar = Calendar.getInstance()
    private val simpleDate = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingInputAnak = ActivityInputAnakBinding.inflate(layoutInflater)
        setContentView(bindingInputAnak.root)

        setupViewModel()
        setupAction()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myCalendar.set(year, month, dayOfMonth)
        updateCalendar(myCalendar.timeInMillis)
    }

    private fun setDatePicker() {
        DatePickerDialog(
            this,
            this,
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateCalendar(mCalendar: Long){
        bindingInputAnak.dateText.setText(simpleDate.format(mCalendar))
    }

    private fun setupViewModel() {
        inputAnakViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        inputAnakViewModel.isDone.observe(this) {
            intentListAnak(it)
        }

    }

    private fun setupAction() {
        bindingInputAnak.dateText.setOnClickListener {
            setDatePicker()
        }

        bindingInputAnak.buttonInputAnak.setOnClickListener {
            hideWarning()
            inputAnakValidation()
        }
    }

    private fun hideWarning() {
        bindingInputAnak.tempatLahirEditTextLayoutInputAnak.error = null
        bindingInputAnak.nameEditTextLayoutInputAnak.error = null
        bindingInputAnak.datePickerLayout.error = null
    }



    private fun inputAnakValidation(){
        val nama = bindingInputAnak.nameEditTextInputAnak.text.toString()
        val tempatLahir = bindingInputAnak.tempatLahirEditTextInputAnak.text.toString()
        val tlAnak = bindingInputAnak.dateText.text.toString()
        when {
            nama.isEmpty() -> {
                bindingInputAnak.nameEditTextLayoutInputAnak.error = "Nama tidak boleh kosong"
            }
            tempatLahir.isEmpty() -> {
                bindingInputAnak.tempatLahirEditTextLayoutInputAnak.error = "Tempat Lahir tidak boleh kosong"
            }
            tlAnak.isEmpty() -> {
                bindingInputAnak.datePickerLayout.error = "Tanggal Lahir tidak boleh kosong"
            }
            else -> {
                inputAnakViewModel.postRegisterAnak(nama, tempatLahir, tlAnak)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        bindingInputAnak.progressBarInputAnak.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun intentListAnak(isDone: Boolean) {
        if(isDone){
            val intent = Intent(this, ListAnakActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}