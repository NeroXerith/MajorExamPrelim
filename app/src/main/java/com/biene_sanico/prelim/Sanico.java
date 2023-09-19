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
import android.widget.Toast;

import java.util.Random;
import java.text.DecimalFormat;
public class Sanico extends AppCompatActivity {
    Button playBtn;
    TextView getUserBal, getWLResult, txtMultiplier, getReel1, getReel2, getReel3;
    EditText getBetReel1, getBetReel2, getBetReel3, getBetAmount;

    Button resetButton;
    String[] reelsResult = new String[3];
    double userBalance = 1000.00;
    int ctrMultiplier = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sanico);
        initializeViews(); // Initialize the UI elements.
        setEditTextListeners(); // Set up text watchers for EditTexts
        setDigitInputFilters(); // Set input filters to allow only digits
        txtMultiplier.setText("x"+String.valueOf(ctrMultiplier));
        getUserBal.setText(String.valueOf(userBalance));
    }

    private void initializeViews() {
        playBtn = findViewById(R.id.btnAction);
        resetButton = findViewById(R.id.btnReset);
        txtMultiplier = findViewById(R.id.multiplier);
        getUserBal = findViewById(R.id.userBalance);
        getReel1 = findViewById(R.id.reel1);
        getReel2 = findViewById(R.id.reel2);
        getReel3 = findViewById(R.id.reel3);
        getBetReel1 = findViewById(R.id.betInput1);
        getBetReel2 = findViewById(R.id.betInput2);
        getBetReel3 = findViewById(R.id.betInput3);
        getBetAmount = findViewById(R.id.betAmount);
        getWLResult = findViewById(R.id.winORlose);
        updateResetButtonVisibility();
    }

    private void setEditTextListeners() {
        getBetReel1.addTextChangedListener(new DigitTextWatcher(getBetReel1, getBetReel2));
        getBetReel2.addTextChangedListener(new DigitTextWatcher(getBetReel2, getBetReel3));
        getBetReel3.addTextChangedListener(new DigitTextWatcher(getBetReel3, getBetAmount));


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

    private void updateResetButtonVisibility() {
        if (userBalance <= 0) {
            resetButton.setVisibility(View.VISIBLE);
        } else {
            resetButton.setVisibility(View.GONE);
        }
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

            double parsedBetAmount = Double.parseDouble(betAmount);

            if (parsedBetAmount == 0) {
                // Bet amount is 0, show toast and return
                Toast.makeText(this, "Bet amount cannot be 0", Toast.LENGTH_SHORT).show();
                return;
            }

            if (parsedBetAmount > userBalance) {
                // Bet amount is greater than user balance, show toast and return
                Toast.makeText(this, "Not enough balance", Toast.LENGTH_SHORT).show();
                return;
            }

            // Generate a random number between 0 and 3 to represent the winning chance
            Random random = new Random();
            int winningChance = random.nextInt(4); // 0, 1, 2, or 3

            // Generate random numbers for reels
            int tmpNum1 = random.nextInt(10); // 0 to 9
            int tmpNum2 = random.nextInt(10); // 0 to 9
            int tmpNum3 = random.nextInt(10); // 0 to 9
            reelsResult[0] = String.valueOf(tmpNum1);
            reelsResult[1] = String.valueOf(tmpNum2);
            reelsResult[2] = String.valueOf(tmpNum3);

            // Display the random numbers in the TextViews
            getReel1.setText(String.valueOf(tmpNum1));
            getReel2.setText(String.valueOf(tmpNum2));
            getReel3.setText(String.valueOf(tmpNum3));

            // Check if the reels match the bet with a 25% probability (winningChance == 0)
            if (winningChance == 0 &&
                    reelsResult[0].equals(betInput1) &&
                    reelsResult[1].equals(betInput2) &&
                    reelsResult[2].equals(betInput3)) {

                double compute = ctrMultiplier * parsedBetAmount;
                userBalance += compute;

                // Format userBalance to two decimal places
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                userBalance = Double.parseDouble(decimalFormat.format(userBalance));

                getUserBal.setText(String.valueOf(userBalance));
                getWLResult.setText("WIN");
                ctrMultiplier += 1;
                txtMultiplier.setText("x" + String.valueOf(ctrMultiplier));
            } else {
                getWLResult.setText("LOSE");
                ctrMultiplier = 2;
                txtMultiplier.setText("x" + String.valueOf(ctrMultiplier));

                // Deduct bet amount from user balance
                userBalance -= parsedBetAmount;

                // Format userBalance to two decimal places
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                userBalance = Double.parseDouble(decimalFormat.format(userBalance));

                getUserBal.setText(String.valueOf(userBalance));
            }
            playBtn.setText("SET");
            updateResetButtonVisibility();
        } else if (buttonText.equals("RESET")){
            userBalance = 1000.00;
            getUserBal.setText("1000.00");
            getBetReel1.setText("");
            getBetReel2.setText("");
            getBetReel3.setText("");
            getBetAmount.setText("");
            getWLResult.setText("");
            playBtn.setText("PLAY");
            updateResetButtonVisibility();
        } else if (buttonText.equals("SET")){
            getBetReel1.setText("");
            getBetReel2.setText("");
            getBetReel3.setText("");
            getBetAmount.setText("");
            getWLResult.setText("");
            playBtn.setText("PLAY");
        }
    }



    // TextWatcher to limit input to one digit and move focus to the next EditText
    private class DigitTextWatcher implements TextWatcher {
        private EditText editText;
        private EditText nextEditText;

        DigitTextWatcher(EditText editText, EditText nextEditText) {
            this.editText = editText;
            this.nextEditText = nextEditText;
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

            // Move focus to the next EditText if text is not empty
            if (s.length() > 0 && nextEditText != null) {
                nextEditText.requestFocus();
            }
        }
    }
}