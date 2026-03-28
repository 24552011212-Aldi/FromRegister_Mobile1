package com.example.formregister

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    // Deklarasi view agar mudah diakses di seluruh fungsi
    private lateinit var etName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etConfirmPassword: TextInputEditText
    private lateinit var rgGender: RadioGroup
    private lateinit var cbHobbies: List<CheckBox>
    private lateinit var spinnerCity: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi View
        initViews()

        // 04: Spinner Data Custom
        val cities = arrayOf("Jakarta", "Bandung", "Surabaya", "Yogyakarta", "Semarang")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cities)
        spinnerCity.adapter = adapter

        // 02: Advanced Validation (Real-time Email & Password Match)
        setupRealTimeValidation()

        // 05: Gesture Interaction (Long Press untuk Reset)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        btnSubmit.setOnLongClickListener {
            showResetDialog() // Konfirmasi sebelum reset (opsional tapi lebih baik)
            true
        }

        // Action Submit
        btnSubmit.setOnClickListener {
            if (validateAll()) {
                showConfirmationDialog()
            }
        }
    }

    private fun initViews() {
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        rgGender = findViewById(R.id.rgGender)
        spinnerCity = findViewById(R.id.spinnerCity)

        // List checkbox hobi (sesuaikan ID dengan layout)
        cbHobbies = listOf(
            findViewById(R.id.cbCoding),
            findViewById(R.id.cbGaming),
            findViewById(R.id.cbReading),
            findViewById(R.id.cbMusic)
        )
    }

    private fun setupRealTimeValidation() {
        // Validasi Email
        etEmail.addTextChangedListener {
            val email = it.toString()
            val tilEmail = findViewById<TextInputLayout>(R.id.tilEmail)
            if (email.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                tilEmail.error = "Format email tidak valid"
            } else {
                tilEmail.error = null
            }
        }

        // Validasi Match Password
        etConfirmPassword.addTextChangedListener {
            val tilConfirm = findViewById<TextInputLayout>(R.id.tilConfirmPassword)
            if (it.toString() != etPassword.text.toString()) {
                tilConfirm.error = "Password tidak cocok"
            } else {
                tilConfirm.error = null
            }
        }
    }

    private fun validateAll(): Boolean {
        var isValid = true

        // Ambil semua TextInputLayout untuk reset error di awal pengecekan
        val tilName = findViewById<TextInputLayout>(R.id.tilName)
        val tilEmail = findViewById<TextInputLayout>(R.id.tilEmail)
        val tilPassword = findViewById<TextInputLayout>(R.id.tilPassword)
        val tilConfirm = findViewById<TextInputLayout>(R.id.tilConfirmPassword)

        // Reset semua error sebelum divalidasi ulang
        tilName.error = null
        tilEmail.error = null
        tilConfirm.error = null

        // 1. Validasi Nama
        if (etName.text.isNullOrBlank()) {
            tilName.error = "Nama wajib diisi"
            isValid = false
        }

        // 2. Validasi Email
        val emailStr = etEmail.text.toString()
        if (emailStr.isEmpty()) {
            tilEmail.error = "Email wajib diisi"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            tilEmail.error = "Format email salah"
            isValid = false
        }

        // 3. Validasi Password Match
        val pass = etPassword.text.toString()
        val confirm = etConfirmPassword.text.toString()
        if (confirm != pass) {
            tilConfirm.error = "Password tidak cocok"
            isValid = false
        } else if (confirm.isEmpty()) {
            tilConfirm.error = "Konfirmasi password wajib diisi"
            isValid = false
        }

        // 4. Validasi RadioGroup
        if (rgGender.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Pilih jenis kelamin", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi")
            .setMessage("Apakah Anda sudah mengisi data dengan benar?")
            .setPositiveButton("Ya") { _, _ ->
                Toast.makeText(this, "Akun berhasil dibuat!", Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showResetDialog() {
        AlertDialog.Builder(this)
            .setTitle("Reset Form")
            .setMessage("Apakah Anda yakin ingin menghapus semua inputan?")
            .setPositiveButton("Ya") { _, _ ->
                resetForm()
            }
            .setNegativeButton("Tidak", null)
            .show()
    }

    private fun resetForm() {
        // Clear all text fields
        etName.text = null
        etEmail.text = null
        etPassword.text = null
        etConfirmPassword.text = null

        // Clear Selection Controls
        rgGender.clearCheck()
        cbHobbies.forEach { it.isChecked = false }

        // Reset Spinner to first item
        spinnerCity.setSelection(0)

        // Clear all errors
        findViewById<TextInputLayout>(R.id.tilName).error = null
        findViewById<TextInputLayout>(R.id.tilEmail).error = null
        findViewById<TextInputLayout>(R.id.tilConfirmPassword).error = null

        Toast.makeText(this, "Form telah dibersihkan", Toast.LENGTH_SHORT).show()
    }
}