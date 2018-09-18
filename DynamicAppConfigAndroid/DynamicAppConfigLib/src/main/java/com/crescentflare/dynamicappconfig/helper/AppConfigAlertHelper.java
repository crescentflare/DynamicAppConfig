package com.crescentflare.dynamicappconfig.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.method.DigitsKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.crescentflare.dynamicappconfig.R;

/**
 * Library helper: show alert dialog
 * A helper library to show an alert dialog (for text input)
 */
public class AppConfigAlertHelper
{
    // ---
    // An input dialog with ok/cancel buttons
    // ---

    public static void inputDialog(Context context, String title, String hintText, String preFilledValue, InputType inputType, final OnAlertInputListener listener)
    {
        // Set up container
        FrameLayout editTextContainer = new FrameLayout(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = AppConfigViewHelper.dp(20);
        layoutParams.leftMargin = margin;
        layoutParams.rightMargin = margin;

        // Add edit text
        final EditText editText = new EditText(context);
        editText.setLayoutParams(layoutParams);
        editText.setSingleLine();
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setHint(hintText);
        editText.setText(preFilledValue);
        editText.setSelection(editText.getText().length());
        editText.setId(R.id.app_config_dialog_input);
        switch (inputType)
        {
            case NumbersOnly:
                editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                editText.setKeyListener(DigitsKeyListener.getInstance(true, false));
                break;
            default:
                editText.setInputType(android.text.InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                break;
        }
        editTextContainer.addView(editText);

        // Create dialog
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(editTextContainer)
                .setPositiveButton(context.getString(R.string.app_config_action_ok), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String enteredText = String.valueOf(editText.getText());
                        if (listener != null)
                        {
                            listener.onInputEntered(enteredText);
                        }
                    }
                })
                .setNegativeButton(context.getString(R.string.app_config_action_cancel), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (listener != null)
                        {
                            listener.onInputCanceled();
                        }
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener()
                {
                    @Override
                    public void onCancel(DialogInterface dialog)
                    {
                        if (listener != null)
                        {
                            listener.onInputCanceled();
                        }
                    }
                })
                .create();

        // Manage keyboard
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus && dialog.getWindow() != null)
                {
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
                return true;
            }
        });

        // Show dialog
        dialog.show();
    }


    // ---
    // Input type enum
    // ---

    public enum InputType
    {
        Normal,
        NumbersOnly
    }


    // ---
    // Input callback listener
    // ---

    public interface OnAlertInputListener
    {
        void onInputEntered(String text);
        void onInputCanceled();
    }
}
