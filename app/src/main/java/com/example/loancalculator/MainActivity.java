package com.example.loancalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;

// Controller class that handles user interaction and updates the view
public class MainActivity extends AppCompatActivity {
    // UI elements (View components)
    private EditText loanAmountInput;
    private EditText interestRateInput;
    private RadioButton monthlyButton;
    private TextView resultText;
    private SeekBar yearsSeekBar;
    private TextView yearsLabel;
    private Spinner paymentsPerYearSpinner;
    private RadioGroup calculationTypeGroup;

    // Model instance
    private LoanCalculator calculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the Model
        calculator = new LoanCalculator();

        // Connect to UI elements (View initialization)
        loanAmountInput = findViewById(R.id.editTextLoanAmount);
        interestRateInput = findViewById(R.id.editTextInterestRate);
        monthlyButton = findViewById(R.id.radioButtonMonthly);
        resultText = findViewById(R.id.textViewResult);
        Button calculateButton = findViewById(R.id.buttonCalculate);
        yearsSeekBar = findViewById(R.id.seekBarYears);
        yearsLabel = findViewById(R.id.textViewYearsLabel);
        paymentsPerYearSpinner = findViewById(R.id.spinnerPaymentsPerYear);
        calculationTypeGroup = findViewById(R.id.radioGroupCalculationType);

        // Set up button click handler
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateLoan();
            }
        });

        // Set up SeekBar
        yearsSeekBar.setMax(30); // 1-30 years
        yearsSeekBar.setProgress(1);
        yearsLabel.setText("Loan Term: 1 year");
        yearsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int years = Math.max(progress, 1);
                yearsLabel.setText("Loan Term: " + years + " year" + (years > 1 ? "s" : ""));
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Set up Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.payments_per_year_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentsPerYearSpinner.setAdapter(adapter);
    }

    // Controller method that handles calculation logic and view updates
    private void calculateLoan() {
        try {
            // Get values from View
            double principal = Double.parseDouble(loanAmountInput.getText().toString());
            double annualRate = Double.parseDouble(interestRateInput.getText().toString());
            int years = Math.max(yearsSeekBar.getProgress(), 1); // Use SeekBar value
            int paymentsPerYear = Integer.parseInt(paymentsPerYearSpinner.getSelectedItem().toString());

            // Update Model with values
            calculator.setPrincipal(principal);
            calculator.setAnnualRate(annualRate);
            calculator.setYears(years);
            calculator.setPaymentsPerYear(paymentsPerYear);

            // Get calculation from Model and update View
            int checkedId = calculationTypeGroup.getCheckedRadioButtonId();
            if (checkedId == R.id.radioButtonMonthly) {
                double monthlyPayment = calculator.calculateMonthlyPayment();
                resultText.setText("Monthly Payment: $" + String.format("%.2f", monthlyPayment));
            } else {
                double totalPayment = calculator.calculateTotalPayment();
                resultText.setText("Total Payment: $" + String.format("%.2f", totalPayment));
            }

        } catch (NumberFormatException e) {
            // Simple error handling for invalid input
            resultText.setText("Please enter valid numbers");
        }
    }
} 