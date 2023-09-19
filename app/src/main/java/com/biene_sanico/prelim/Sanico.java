package com.biene_sanico.prelim;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.util.Random;
public class Sanico extends AppCompatActivity {
    Button playBtn;
    TextView getWLResult, getReel1, getReel2, getReel3;
    EditText getBetReel1, getBetReel2, getBetReel3, getBetAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sanico);
        initializeViews(); // Initialize the UI elements.
        setEditTextListeners(); // Set up text watchers for EditTexts
        setDigitInputFilters(); // Set input filters to allow only digits
    }

    private void initializeViews() {
        playBtn = findViewById(R.id.btnAction);
        getReel1 = findViewById(R.id.reel1);
        getReel2 = findViewById(R.id.reel2);
        getReel3 = findViewById(R.id.reel3);
        getBetReel1 = findViewById(R.id.betInput1);
        getBetReel2 = findViewById(R.id.betInput2);
        getBetReel3 = findViewById(R.id.betInput3);
        getBetAmount = findViewById(R.id.betAmount);
        getWLResult = findViewById(R.id.winORlose);
    }

    private void setEditTextListeners() {
        getBetReel1.addTextChangedListener(new DigitTextWatcher(getBetReel1));
        getBetReel2.addTextChangedListener(new DigitTextWatcher(getBetReel2));
        getBetReel3.addTextChangedListener(new DigitTextWatcher(getBetReel3));

        // Add a TextWatcher to restrict betAmount to one decimal place
        getBetAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String betAmountText = s.toString();
                if (!isValidBetAmount(betAmountText)) {
                    // Remove the last character if it's not a valid decimal
                    s.delete(s.length() - 1, s.length());
                }
            }
        });
    }

    private void setDigitInputFilters() {
        InputFilter digitFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                StringBuilder filteredStringBuilder = new StringBuilder();

                for (int i = start; i < end; i++) {
                    char currentChar = source.charAt(i);
                    if (Character.isDigit(currentChar)) {
                        filteredStringBuilder.append(currentChar);
                    }
                }

                return filteredStringBuilder.toString();
            }
        };

        getBetReel1.setFilters(new InputFilter[]{digitFilter});
        getBetReel2.setFilters(new InputFilter[]{digitFilter});
        getBetReel3.setFilters(new InputFilter[]{digitFilter});
    }

    private boolean isValidBetAmount(String betAmount) {
        // Use a regular expression to allow one digit before and after a decimal point
        if (betAmount.isEmpty()) {
            return true; // Empty input is considered valid
        }

        if (betAmount.equals(".")) {
            return false; // Input of only "." is considered invalid
        }

        return betAmount.matches("\\d*\\.?\\d{0,2}");
    }


    public void actionBtn(View view) {
        String buttonText = ((TextView) view).getText().toString();

        String betInput1 = getBetReel1.getText().toString().trim();
        String betInput2 = getBetReel2.getText().toString().trim();
        String betInput3 = getBetReel3.getText().toString().trim();
        String betAmount = getBetAmount.getText().toString().trim();

        if (buttonText.equals("PLAY")) {
            if (betInput1.isEmpty()) {
                getBetReel1.setError("Field cannot be empty");
                getBetReel1.requestFocus();
                return;
            }
            if (betInput2.isEmpty()) {
                getBetReel2.setError("Field cannot be empty");
                getBetReel2.requestFocus();
                return;
            }
            if (betInput3.isEmpty()) {
                getBetReel3.setError("Field cannot be empty");
                getBetReel3.requestFocus();
                return;
            }
            if (betAmount.isEmpty()) {
                getBetAmount.setError("Field cannot be empty");
                getBetAmount.requestFocus();
                return;
            }
            // Perform your PLAY action here
            String tmpBetInput = betInput1 + betInput2 + betInput3;
            getWLResult.setText(tmpBetInput);



            // Generate random numbers
            Random random = new Random();
            int tmpNum1 = random.nextInt(9); // Change 10 to your desired upper limit
            int tmpNum2 = random.nextInt(9); // Change 10 to your desired upper limit
            int tmpNum3 = random.nextInt(9); // Change 10 to your desired upper limit

            // Display the random numbers in the TextViews
            getReel1.setText(String.valueOf(tmpNum1));
            getReel2.setText(String.valueOf(tmpNum2));
            getReel3.setText(String.valueOf(tmpNum3));


        }
    }

    // TextWatcher to limit input to one digit
    private class DigitTextWatcher implements TextWatcher {
        private EditText editText;

        DigitTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 1) {
                // If more than one character is entered, limit it to one character
                editText.setText(s.subSequence(0, 1));
                editText.setSelection(1);
            }
        }
    }
}
