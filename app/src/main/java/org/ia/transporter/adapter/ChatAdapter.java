package org.ia.transporter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ia.transporter.R;
import org.ia.transporter.domain.TransMessage;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2017/2/12 0012.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<TransMessage> tMsgList;
    private Context context;

    public ChatAdapter(List<TransMessage> tMsgList, Context context) {
        this.tMsgList = tMsgList;
        this.context = context;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false));
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        TransMessage message = tMsgList.get(position);
        holder.tv_message.setText(message.getMessage());
        if (message.isSelf()) {
            holder.tv_message.setBackground(context.getResources().getDrawable(R.drawable.corner_bg_pink));
            holder.ll_message.setGravity(Gravity.CENTER_VERTICAL|Gravity.END);
        } else {
            holder.tv_message.setBackground(context.getResources().getDrawable(R.drawable.corner_bg_white));
            holder.ll_message.setGravity(Gravity.CENTER_VERTICAL|Gravity.START);
        }
    }

    @Override
    public int getItemCount() {
        if (null != tMsgList) {
            return tMsgList.size();
        } else {
            return 0;
        }
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.tv_message)    private TextView tv_message;
        @ViewInject(R.id.ll_message)    private LinearLayout ll_message;

        public ChatViewHolder(View itemView) {
            super(itemView);
            x.view().inject(this, itemView);
        }

    }

    public void setMessageList(List<TransMessage> tMsgList) {
        this.tMsgList = tMsgList;
    }
}
