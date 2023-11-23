package de.upb.cs.swtpra_04.qwirkle.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import de.upb.cs.swtpra_04.qwirkle.R;
import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.ClientType;

/**
 * Provide UI Related Functions used for the in-game chat
 */
public class ChatFragment extends Fragment {

    private EditText editText_message;
    private LinearLayout linearLayout_messages;
    private ScrollView scrollView_messageScroll;
    private Button button_send;

    private OnChatFragmentListener listener;

    /**
     * Create a new View from Layout and bind functions to UI Elements
     *
     * @param inflater           Layout inflater
     * @param container          Parent View
     * @param savedInstanceState State
     * @return View
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Create View from Layout
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // Find Elements
        editText_message = view.findViewById(R.id.textView_message);
        button_send = view.findViewById(R.id.button_send);
        linearLayout_messages = view.findViewById(R.id.linearLayout_messages);
        scrollView_messageScroll = (ScrollView) linearLayout_messages.getParent();

        // get InputMethodManager
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE
        );

        // handle "Send"-Button Click
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = editText_message.getText().toString();
                listener.onClickSend(msg);

                // Clear Input
                editText_message.setText("");

                // Hide Keyboard
                imm.hideSoftInputFromWindow(editText_message.getWindowToken(), 0);
            }
        });

        return view;
    }

    /**
     * Updates chat with a new message.
     *
     * @param client    client who sent the message
     * @param msg       new Message to be displayed
     */
    public void updateChat(Client client, String msg){
        if (getActivity() == null){
            return;
        }

        getActivity().runOnUiThread(() -> {
            if(!isVisible()){
                return;
            }

            // prevent Empty messages
            if (!msg.equals("")) {

                View v = getLayoutInflater().from(getContext()).inflate(R.layout.scrollitem_chat_message, linearLayout_messages, false);
                TextView usernameView = v.findViewById(R.id.textView_chatSender);
                TextView messageView = v.findViewById(R.id.textView_message);

                usernameView.setText(client.getClientName());

                if(client.getClientType() == ClientType.PLAYER) {
                    usernameView.setTextColor(0xFF000000);
                }else if(client.getClientType() == ClientType.SPECTATOR) {
                    usernameView.setTextColor(0xFF333333);
                }
                messageView.setText(msg);

                // Attach view to Chat log
                linearLayout_messages.addView(v);

                // Scroll Chat to Bottom
                scrollView_messageScroll.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView_messageScroll.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        });
    }


    /**
     * Defines a method in parent for notifying it about a new chat message to send to server
     */
    public interface OnChatFragmentListener {
        public void onClickSend(String msg);
    }

    /**
     * Attaches this fragment to the life cycle of the parent activity.
     * Android intern method.
     * Used here to make sure parent activity uses our interface.
     *
     * @param context   parent activity
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChatFragmentListener) {
            listener = (OnChatFragmentListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement ChatFragment.OnChatFragmentListener");
        }
    }

    /**
     * Detaches this fragment from parent activity
     */
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}

