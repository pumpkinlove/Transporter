package org.ia.transporter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ia.transporter.R;
import org.ia.transporter.domain.TransMessage;
import org.ia.transporter.utils.Base64Util;
import org.ia.transporter.utils.DateUtil;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/3/26 0026.
 */

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.SessionViewHolder> {
    private List<TransMessage> tMsgList;
    private Context context;

    public SessionAdapter(List<TransMessage> tMsgList, Context context) {
        this.tMsgList = tMsgList;
        this.context = context;
    }

    @Override
    public SessionAdapter.SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SessionAdapter.SessionViewHolder(LayoutInflater.from(context).inflate(R.layout.item_session, parent, false));
    }

    @Override
    public void onBindViewHolder(SessionAdapter.SessionViewHolder holder, int position) {
        TransMessage tMsg = tMsgList.get(position);
        holder.iv_session_photo.setImageBitmap(Base64Util.stringtoBitmap(tMsg.getFromClient().getPhoto()));
        holder.tv_session_name.setText(tMsg.getFromClient().getName());
        holder.tv_session_info.setText(tMsg.getMessage());
        holder.tv_session_opTime.setText(DateUtil.toHourMinString(tMsg.getOpTime()));
    }

    @Override
    public int getItemCount() {
        if (null != tMsgList) {
            return tMsgList.size();
        } else {
            return 0;
        }
    }

    class SessionViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.iv_session_photo)   private ImageView iv_session_photo;

        @ViewInject(R.id.tv_session_name)    private TextView tv_session_name;
        @ViewInject(R.id.tv_session_info)    private TextView tv_session_info;
        @ViewInject(R.id.tv_session_opTime)  private TextView tv_session_opTime;

        public SessionViewHolder(View itemView) {
            super(itemView);
            x.view().inject(this, itemView);
        }

    }

    public void setMessageList(List<TransMessage> tMsgList) {
        this.tMsgList = tMsgList;
    }
}
