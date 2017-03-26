package org.ia.transporter.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import org.ia.transporter.R;
import org.ia.transporter.domain.Client;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Administrator on 2017/2/7 0007.
 */

public class ClientDialog extends DialogFragment {

    @ViewInject(R.id.et_name)       private EditText et_name;
    @ViewInject(R.id.et_ip)         private EditText et_ip;

    private View.OnClickListener modListener;
    private View.OnClickListener delListener;

    private Client client = new Client();

    public void setModListener(View.OnClickListener modListener) {
        this.modListener = modListener;
    }

    public void setDelListener(View.OnClickListener delListener) {
        this.delListener = delListener;
    }

    public Client getClient() {
        client.setName(et_name.getText().toString());
        client.setIp(et_ip.getText().toString());
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.77), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_client, container);
        x.view().inject(this, view);
        return view;
    }

    @Override
    public void onResume() {
        if (client != null) {
            et_name.setText(client.getName());
            et_ip.setText(client.getIp());
        }
        super.onResume();
    }

    @Event(R.id.tv_confirm)
    private void onConfirmClicked(View view) {
        modListener.onClick(view);
    }

    @Event(R.id.tv_cancel)
    private void onCancelClicked(View view) {
        delListener.onClick(view);
    }

}
