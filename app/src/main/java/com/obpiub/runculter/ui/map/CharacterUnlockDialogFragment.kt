package com.obpiub.runculter.ui.map

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.obpiub.runculter.databinding.DialogCharacterUnlockBinding

class CharacterUnlockDialogFragment : DialogFragment() {
    private var _binding: DialogCharacterUnlockBinding? = null
    private val binding get() = _binding!!
    
    private var onYesClick: (() -> Unit)? = null
    private var onNoClick: (() -> Unit)? = null
    private var onRepeatClick: (() -> Unit)? = null

    companion object {
        fun newInstance(
            onYesClick: () -> Unit,
            onNoClick: () -> Unit,
            onRepeatClick: () -> Unit
        ): CharacterUnlockDialogFragment {
            val fragment = CharacterUnlockDialogFragment()
            fragment.onYesClick = onYesClick
            fragment.onNoClick = onNoClick
            fragment.onRepeatClick = onRepeatClick
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        android.util.Log.d("CharacterUnlockDialog", "onCreateDialog вызван")
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.let { window ->
            android.util.Log.d("CharacterUnlockDialog", "Настройка окна диалога")
            window.requestFeature(Window.FEATURE_NO_TITLE)
            window.setBackgroundDrawableResource(android.R.color.transparent)
            
            // Устанавливаем размер диалога (80% ширины экрана, максимум 400dp)
            val displayMetrics = resources.displayMetrics
            val maxWidth = (400 * displayMetrics.density).toInt()
            val screenWidth = displayMetrics.widthPixels
            val dialogWidth = minOf((screenWidth * 0.8).toInt(), maxWidth)
            
            window.setLayout(
                dialogWidth,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            
            // Центрируем диалог
            val layoutParams = window.attributes
            layoutParams.gravity = android.view.Gravity.CENTER
            window.attributes = layoutParams
            
            // Устанавливаем флаги для отображения поверх всего
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
            )
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            window.setDimAmount(0.5f)
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogCharacterUnlockBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        android.util.Log.d("CharacterUnlockDialog", "onViewCreated вызван, настройка кнопок")

        binding.yesButton.setOnClickListener {
            android.util.Log.d("CharacterUnlockDialog", "=== КНОПКА 'ДА' НАЖАТА ===")
            android.util.Log.d("CharacterUnlockDialog", "onYesClick callback: ${if (onYesClick != null) "установлен" else "NULL"}")
            onYesClick?.invoke()
            android.util.Log.d("CharacterUnlockDialog", "onYesClick вызван, закрываем диалог")
            dismiss()
        }

        binding.noButton.setOnClickListener {
            onNoClick?.invoke()
            dismiss()
        }

        binding.repeatButton.setOnClickListener {
            // Вызываем callback БЕЗ закрытия диалога - пусть callback сам решает
            onRepeatClick?.invoke()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
