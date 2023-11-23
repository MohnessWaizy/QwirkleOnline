package de.upb.cs.swtpra_04.qwirkle.view;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;

import de.upb.cs.swtpra_04.qwirkle.R;

public class LoadingDialog {

    private Dialog dialog;

    public LoadingDialog(AppCompatActivity activity, String message) {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_loading_dialog);

        TextView text = (TextView) dialog.findViewById(R.id.textView_loadingMessage);
        text.setText(message);
    }

    public void showDialog() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
