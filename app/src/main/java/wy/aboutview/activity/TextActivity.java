package wy.aboutview.activity;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import wy.aboutview.R;

public class TextActivity extends AppCompatActivity {

    private TextInputLayout usernameTv;
    private TextInputLayout passwordTv;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        usernameTv = (TextInputLayout) findViewById(R.id.edt_use);
        passwordTv = (TextInputLayout) findViewById(R.id.edt_pwd);
        editText = (EditText) findViewById(R.id.edt_user);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = usernameTv.getEditText().getText().toString();
                if (!name.contains("a")) {
                    usernameTv.setError("Not a valid user name!");
                }else {
                    usernameTv.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
