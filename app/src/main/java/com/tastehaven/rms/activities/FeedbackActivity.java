package com.tastehaven.rms.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.tastehaven.rms.R;
import com.tastehaven.rms.models.Feedback;

public class FeedbackActivity extends AppCompatActivity {
    RatingBar ratingBar;
    TextInputEditText etFeedback;
    Button btnSubmitFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        ratingBar = findViewById(R.id.ratingBar);
        etFeedback = findViewById(R.id.etFeedback);
        btnSubmitFeedback = findViewById(R.id.btnSubmitFeedback);

        btnSubmitFeedback.setOnClickListener(v -> submitFeedback());
    }

    private void submitFeedback() {
        float rating = ratingBar.getRating();
        String feedbackText = etFeedback.getText().toString().trim();

        if (rating == 0) {
            Toast.makeText(this, "Please provide a rating", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Feedback feedback = new Feedback(userId, rating, feedbackText, System.currentTimeMillis());
        FirebaseDatabase.getInstance().getReference("feedback").push()
                .setValue(feedback)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(this, ThankYouActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to submit feedback", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}