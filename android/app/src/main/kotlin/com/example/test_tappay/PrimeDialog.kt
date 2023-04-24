package com.example.test_tappay

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import com.example.test_tappay.databinding.DialogMessageBinding
import tech.cherri.tpdirect.api.TPDCard
import tech.cherri.tpdirect.api.TPDServerType
import tech.cherri.tpdirect.api.TPDSetup
import tech.cherri.tpdirect.model.TPDStatus

/**
 * Created by Wayne Chen in Apr. 2023.
 */
class PrimeDialog(context: Context, val listener: PrimeDialogListener) : AppCompatDialog(context) {

    private var _binding: DialogMessageBinding? = null

    private val binding get() = _binding!!

    interface PrimeDialogListener {
        fun onSuccess(prime: String)
        fun onFailure(error: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = DialogMessageBinding.inflate(layoutInflater)
        val root: View = binding.root
        setContentView(root)


        TPDSetup.initInstance(
            context,
            12348,
            "app_pa1pQcKoY22IlnSXq5m5WP5jFKzoRG58VEXpT7wU62ud7mMbDOGzCYIlzzLF",
            TPDServerType.Sandbox
        )

        val tpdForm = _binding?.formTpd
        var tpdErrorMessage = "Credit Card information error"
        var isCanGetPrime = false

        tpdForm?.setTextErrorColor(context.getColor(R.color.red_d0021b))
        tpdForm?.setOnFormUpdateListener { tpdStatus ->
            when {
                tpdStatus.cardNumberStatus == TPDStatus.STATUS_ERROR ->
                    tpdErrorMessage = "Invalid Card Number"
                tpdStatus.expirationDateStatus == TPDStatus.STATUS_ERROR ->
                    tpdErrorMessage = "Invalid Expiration Date"
                tpdStatus.ccvStatus == TPDStatus.STATUS_ERROR ->
                    tpdErrorMessage = "Invalid CCV"
            }
            isCanGetPrime = tpdStatus.isCanGetPrime
        }

        val tpdCard = TPDCard.setup(tpdForm)
            .onSuccessCallback { primeToken: String, _, _: String, _ ->
                Log.d(TAG, "onSuccessCallback, prime=$primeToken")

                listener.onSuccess(primeToken)
                dismiss()
            }
            .onFailureCallback { status: Int, message: String ->
                Log.d(TAG, "onFailureCallback, status: $status, message: $message")

                listener.onFailure("status: $status, message: $message")
                dismiss()
            }


        _binding?.buttonPrime?.setOnClickListener {
            Log.d(TAG, "click get prime")
            tpdCard?.getPrime()
        }
    }

    companion object {
        const val TAG = "anan-android"
    }
}
