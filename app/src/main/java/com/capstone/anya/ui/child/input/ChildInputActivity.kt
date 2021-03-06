package com.capstone.anya.ui.child.input

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstone.anya.R
import com.capstone.anya.databinding.ActivityChildInputBinding
import com.capstone.anya.main.MainViewModel
import com.capstone.anya.main.ViewModelFactory
import com.capstone.anya.model.UserPreference
import java.text.SimpleDateFormat
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "authLogin")

class InputAnakActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var bindingInputAnak: ActivityChildInputBinding
    private val inputAnakViewModel by viewModels<ChildInputViewModel>()
    private lateinit var mainViewModel: MainViewModel

    private val myCalendar = Calendar.getInstance()
    private val simpleDate = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingInputAnak = ActivityChildInputBinding.inflate(layoutInflater)
        setContentView(bindingInputAnak.root)

        title = getString(R.string.title_input_anak)
        supportActionBar?.setDisplayShowHomeEnabled(true)

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
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        inputAnakViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        inputAnakViewModel.isDone.observe(this) {
            intentFinish(it)
        }
    }

    private fun setupAction() {
        val jkText =  resources.getStringArray(R.array.kelamin_array)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_menu, jkText)
        bindingInputAnak.genderTextView.setAdapter(arrayAdapter)

        bindingInputAnak.dateText.setOnClickListener {
            setDatePicker()
        }

        mainViewModel.getToken().observe(this) { user ->
            if (user.token.toString().isNotEmpty()) {
                bindingInputAnak.buttonInputAnak.setOnClickListener {
                    hideWarning()
                    inputAnakValidation(user.token.toString())
                }
            }
        }
    }

    private fun hideWarning() {
        bindingInputAnak.tempatLahirEditTextLayoutInputAnak.isErrorEnabled = false
        bindingInputAnak.nameEditTextLayoutInputAnak.isErrorEnabled = false
        bindingInputAnak.datePickerLayout.isErrorEnabled = false
        bindingInputAnak.genderLayout.isErrorEnabled = false
    }

    private fun inputAnakValidation(token:String){
        val nama = bindingInputAnak.nameEditTextInputAnak.text.toString()
        val tempatLahir = bindingInputAnak.tempatLahirEditTextInputAnak.text.toString()
        val tlAnak = bindingInputAnak.dateText.text.toString()

        var tlGender = bindingInputAnak.genderTextView.text.toString()
        if(tlGender == "Laki-Laki"){
            tlGender = "M"
        }else if(tlGender == "Perempuan"){
            tlGender = "F"
        }

        when {
            nama.isEmpty() -> {
                bindingInputAnak.nameEditTextLayoutInputAnak.error = "Masukkan nama lengkap"
            }
            tempatLahir.isEmpty() -> {
                bindingInputAnak.tempatLahirEditTextLayoutInputAnak.error = "Masukkan tempat tinggal atau lahir"
            }
            tlAnak.isEmpty() -> {
                bindingInputAnak.datePickerLayout.error = "Masukkan tanggal lahir"
            }
            tlGender.isEmpty() -> {
                bindingInputAnak.datePickerLayout.error = "Pilih jenis kelamin"
            }
            else -> {
                inputAnakViewModel.postRegisterChild(token, nama, tempatLahir, tlAnak, tlGender)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        bindingInputAnak.progressBarInputAnak.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun intentFinish(isDone: Boolean) {
        if(isDone){
            finish()
        }
    }
}