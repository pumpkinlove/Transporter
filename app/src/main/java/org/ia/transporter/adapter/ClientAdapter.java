package org.ia.transporter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ia.transporter.R;
import org.ia.transporter.domain.Client;
import org.ia.transporter.utils.Constants;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2017/2/6 0006.
 */

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {

    private List<Client> clientList;
    private Context context;

    public ClientAdapter(List<Client> clientList, Context context) {
        this.clientList = clientList;
        this.context = context;
    }

    @Override
    public ClientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ClientViewHolder(LayoutInflater.from(context).inflate(R.layout.item_client, parent, false));
    }

    @Override
    public void onBindViewHolder(ClientViewHolder holder, int position) {
        Client c = clientList.get(position);
        if (null != c) {
            holder.tv_client_name.setText(c.getName());
        }
    }

    @Override
    public int getItemCount() {
        if (null != clientList) {
            return clientList.size();
        } else {
            return 0;
        }

    }

    class ClientViewHolder extends RecyclerView.ViewHolder {


        @ViewInject(R.id.tv_client_name)    private TextView tv_client_name;

        public ClientViewHolder(View itemView) {
            super(itemView);
            x.view().inject(this, itemView);
        }

    }

    public void setClientList(List<Client> clientList) {
        this.clientList = clientList;
    }

}
