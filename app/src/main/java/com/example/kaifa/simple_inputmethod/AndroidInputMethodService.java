package com.example.kaifa.simple_inputmethod;

import android.app.Service;
import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

public class AndroidInputMethodService extends InputMethodService implements View.OnClickListener {
    TextView textView;
    StringBuffer stringBuffer = new StringBuffer();


    @Override
    public View onCreateInputView() {
        // 装载keyboard.xml文件
        View view = getLayoutInflater().inflate(R.layout.keyboard, null);
        // 设置布局中5个按钮的单击事件
        view.findViewById(R.id.btn1).setOnClickListener(this);
        view.findViewById(R.id.btn2).setOnClickListener(this);
        view.findViewById(R.id.btn3).setOnClickListener(this);
        view.findViewById(R.id.btn4).setOnClickListener(this);
        view.findViewById(R.id.btn_hide).setOnClickListener(this);
        view.findViewById(R.id.btn_send).setOnClickListener(this);
        view.findViewById(R.id.btn_delete).setOnClickListener(this);
        Log.d("tag", "onCreateInputView()");
        // 返回View对象
        return view;
    }

    @Override
    public View onCreateCandidatesView() {
        View view = getLayoutInflater().inflate(R.layout.candidate, null);
        textView = (TextView) view.findViewById(R.id.tv);
        return view;
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        // 获得InputConnection对象
        InputConnection inputConnection = getCurrentInputConnection();
        if (v.getId() == R.id.btn_hide) {
            // 隐藏软键盘
            hideWindow();
        } else if (v.getId() == R.id.btn_send) {
            textView.setHint("候选词");
            setCandidatesViewShown(false);
            stringBuffer.delete(0, stringBuffer.toString().length());
            // 向当前获得焦点的EditText控件输出文本
            // commitText方法第2个参数值为1，表示在当前位置插入文本
            inputConnection.commitText(textView.getText(), textView.getText().length() == 0 ? 1 : textView.getText().length());
        } else if (v.getId() == R.id.btn_delete) {
//            返回一个包含光标前指定个数的字符 CharSequence
//            inputConnection.getTextBeforeCursor()

//            返回一个包含光标后面指定个数的字符 CharSequence
//            inputConnection.getTextAfterCursor()
            Log.e("tag","stringbuffer" + stringBuffer.toString() + stringBuffer.toString().length());
            if (stringBuffer.toString().length() != 0) {
                String str = stringBuffer.toString();
                int length = str.length();
                stringBuffer.delete(length-1, length);
                textView.setText(stringBuffer.toString());
                inputConnection.setComposingText(stringBuffer.toString().trim(), stringBuffer.toString().length() == 0 ? 1 : stringBuffer.toString().length());
            } else {
                //            删除光标前后指定个数的字符
                inputConnection.deleteSurroundingText(1, 0);
            }

        } else {
            setCandidatesViewShown(true);
            // 设置预输入文本
            // setComposingText方法的第2个参数值为1，表示在当前位置预输入文本
            stringBuffer.append(button.getText().toString().trim());
            String str = stringBuffer.toString();
            textView.setText(str);
            inputConnection.setComposingText(str, str.length() == 0 ? 1 : str.length());
        }
    }
}


