package com.paypal.psix.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.paypal.psix.R;
import com.paypal.psix.activities.SetupEventActivity;
import com.paypal.psix.models.Event;
import com.paypal.psix.services.FacebookService;
import com.paypal.psix.utils.ClipboardUtil;
import com.paypal.psix.utils.HasEvent;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by shay on 3/3/15.
 */
public class ShareDialogFragment extends DialogFragment {

    @InjectView(R.id.share_url) EditText urlTextField;

    Event event;
    Activity activity;

    public static final String TAG = "Share";

    public static void show(FragmentActivity activity) {
        ShareDialogFragment newFragment = new ShareDialogFragment();
        newFragment.show(activity.getSupportFragmentManager(), ShareDialogFragment.TAG);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        activity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.fragment_share, null);
        event = ((HasEvent)activity).getEvent();
        builder.setView(view)
            .setTitle(R.string.share_payment_link_title)
            .setMessage(getString(R.string.share_instructions))
            .setIcon(android.R.drawable.ic_menu_share)
            .setPositiveButton(getString(R.string.exit_share), new DialogInterface.OnClickListener() {
                // this is actually the negative one - you can't order them :(
                public void onClick(DialogInterface dialog, int id) {
                finishShareFlow();
                }
            })
            .setNegativeButton(getString(R.string.share_by_post), new DialogInterface.OnClickListener() {
                // this is the positive one
                public void onClick(DialogInterface dialog, int id) {
                   onShare();
                }
            });
        ButterKnife.inject(this, view);

        urlTextField.setText(event.getShareURL());

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.share_copy_button)
    public void onCopy(Button button) {
        ClipboardUtil.copy(activity, getString(R.string.psix_share_url_tag), urlTextField.getText().toString());
        Toast.makeText(activity, activity.getString(R.string.share_copied), Toast.LENGTH_SHORT).show();
    }

    private void onShare() {
        FacebookService.postInEvent(event.fbEventId, event.getShareMessage(activity), new Request.Callback() {
            @Override
            public void onCompleted(Response response) {
                if (response.getError() != null) {
                    Toast.makeText(activity, response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(activity, activity.getString(R.string.shared), Toast.LENGTH_LONG).show();
                }
                finishShareFlow();
            }
        });
    }

    private void finishShareFlow() {
        dismiss();
        if (activity.getClass() == SetupEventActivity.class) {
            activity.finish();
        }
    }
}