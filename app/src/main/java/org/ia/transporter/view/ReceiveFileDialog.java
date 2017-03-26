package org.ia.transporter.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ia.transporter.R;
import org.ia.transporter.utils.Constants;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Administrator on 2017/3/25 0025.
 */

public class ReceiveFileDialog extends DialogFragment {

    private String fileName;

    @ViewInject(R.id.rd_content)    private TextView rd_content;
    @ViewInject(R.id.ll_rd_bottom)  private LinearLayout ll_rd_bottom;
    @ViewInject(R.id.pb_receive)    private ContentLoadingProgressBar pb_receive;

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
        View view = inflater.inflate(R.layout.dialog_receive, container);
        x.view().inject(this, view);

        return view;
    }

    @Event(R.id.rd_cancel)
    private void cancelClick(View view) {
        dismiss();
    }

    @Event(R.id.rd_confirm)
    private void receiveFile(View view) {

        ll_rd_bottom.setVisibility(View.GONE);
        pb_receive.setVisibility(View.VISIBLE);

        try {
            ServerSocket recFileSocket = new ServerSocket(Constants.FILE_PORT);
            Socket socket = recFileSocket.accept();
            InputStream dataStream = socket.getInputStream();
            File dir = new File(Constants.SAVE_PATH);
            if (!dir.exists()) {
                dir.mkdir();
            }
            FileOutputStream fos = new FileOutputStream(Constants.SAVE_PATH + File.separator + fileName, false);
            byte[] buffer = new byte[1024];
            int size = -1;
            int length = dataStream.available();
            pb_receive.setMax(length);
            while ((size = dataStream.read(buffer)) != -1) {
                fos.write(buffer, 0 ,size);
                pb_receive.setProgress(size);
            }
            fos.close();
            dataStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dismiss();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
