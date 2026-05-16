package com.example.securechat;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText messageInput;
    private ImageButton sendButton;
    private LinearLayout chatLayout;
    private ScrollView scrollView;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        chatLayout = findViewById(R.id.chatLayout);
        scrollView = findViewById(R.id.scrollView);

        // Initialization Message
        addMessage("SECURE CONNECTION ESTABLISHED. AES-256 HANDSHAKE COMPLETE.", false);

        sendButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (message.isEmpty()) return;

            addMessage(message, true);
            messageInput.setText("");
            
            simulateReply(message);
        });
    }

    private void simulateReply(String userMessage) {
        // Typing indicator
        final TextView typing = new TextView(this);
        typing.setText("SECURECHAT AI IS TYPING...");
        typing.setTextColor(Color.parseColor("#334155"));
        typing.setTextSize(10);
        typing.setTypeface(Typeface.MONOSPACE, Typeface.ITALIC);
        typing.setPadding(20, 10, 0, 10);
        chatLayout.addView(typing);
        scrollToBottom();

        handler.postDelayed(() -> {
            chatLayout.removeView(typing);
            String reply = getContextAwareReply(userMessage);
            addMessage(reply, false);
        }, 1500 + new Random().nextInt(1000));
    }

    private String getContextAwareReply(String msg) {
        String lower = msg.toLowerCase();
        if (lower.contains("hello") || lower.contains("hi")) return "Hello. Secure connection established.";
        if (lower.contains("how are you")) return "All systems operational.";
        if (lower.contains("thanks") || lower.contains("thank")) return "You're welcome. Stay secure.";
        
        String[] generic = {
            "Message received successfully.",
            "Encryption verified.",
            "Connection stable.",
            "Understood.",
            "Copy that.",
            "Secure channel active.",
            "Transmission completed.",
            "Data received securely.",
            "Authentication successful.",
            "Tunnel integrity confirmed.",
            "Communication link secured.",
            "All systems operational.",
            "Response acknowledged.",
            "Proceeding securely.",
            "Secure relay active.",
            "Session authenticated.",
            "Endpoint verified.",
            "Packet integrity confirmed.",
            "Awaiting further communication."
        };
        return generic[new Random().nextInt(generic.length)];
    }

    private void addMessage(String message, boolean isSent) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = isSent ? Gravity.END : Gravity.START;
        params.setMargins(isSent ? 80 : 0, 12, isSent ? 0 : 80, 12);

        TextView bubble = new TextView(this);
        bubble.setLayoutParams(params);
        bubble.setBackgroundResource(isSent ? R.drawable.bubble_sent : R.drawable.bubble_received);
        bubble.setPadding(30, 24, 30, 24);
        bubble.setTypeface(Typeface.MONOSPACE);
        bubble.setTextColor(isSent ? Color.WHITE : Color.parseColor("#CBD5E1"));
        bubble.setTextSize(13);

        // Encryption Logic
        String encrypted = EncryptionHelper.encrypt(message);
        
        String displayTitle = isSent ? "YOU" : "SECURECHAT AI";
        String statusText = isSent ? "Securely transmitted" : "Secure response received";
        
        String fullText = displayTitle + "\n\n" +
                "Encrypted Message:\n" + encrypted.substring(0, Math.min(encrypted.length(), 24)) + "...\n\n" +
                "Decrypted Message:\n" + message + "\n\n" +
                "Status:\n" + statusText;
        
        SpannableString spannable = new SpannableString(fullText);
        
        int accent = isSent ? Color.parseColor("#E0F2FE") : Color.parseColor("#00CFFF");
        
        // Style Header
        spannable.setSpan(new ForegroundColorSpan(accent), 0, displayTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, displayTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new RelativeSizeSpan(1.1f), 0, displayTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Style Labels (Encrypted Message:, Decrypted Message:, Status:)
        String[] labels = {"Encrypted Message:", "Decrypted Message:", "Status:"};
        for (String label : labels) {
            int start = fullText.indexOf(label);
            if (start != -1) {
                spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#64748B")), start, start + label.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new RelativeSizeSpan(0.8f), start, start + label.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        bubble.setText(spannable);
        chatLayout.addView(bubble);
        scrollToBottom();
    }

    private void scrollToBottom() {
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }
}
