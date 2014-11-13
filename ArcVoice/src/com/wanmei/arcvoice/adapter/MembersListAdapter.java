package com.wanmei.arcvoice.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wanmei.arcvoice.ArcVoiceHelper;
import com.wanmei.arcvoice.R;
import com.wanmei.arcvoice.model.Member;
import com.wanmei.arcvoice.utils.LogUtils;
import com.wanmei.arcvoice.view.ArcMemberView;

import java.util.ArrayList;
import java.util.List;


public class MembersListAdapter extends ParentAdapter<Member> {

    private DisplayImageOptions options;
    private ArcMemberView.Direction mDirection = ArcMemberView.Direction.TEXT_RIGHT;
    private boolean isNeedRecreate = false;

    public MembersListAdapter(Context context) {
        super(context);
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnFail(R.drawable.connected)
                .showImageForEmptyUri(R.drawable.connected)
                .showImageOnLoading(R.drawable.connected)
                .build();
    }

    public void setDirection(ArcMemberView.Direction direction){
        this.mDirection = direction;
        this.isNeedRecreate = true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = null;
        if (convertView == null || isNeedRecreate) {
            convertView = inflateView(parent);
            mViewHolder = new ViewHolder();
            mViewHolder.mAvatarView = (ImageView) convertView.findViewById(R.id.avatar);
            mViewHolder.mNameView = (TextView) convertView.findViewById(R.id.playerName);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        final Member member = mData.get(position);

        StringBuilder sb = new StringBuilder();
        sb.append(member.getUserId());
        sb.append("(").append(TextUtils.isEmpty(member.getUserName()) ? "Guest" : member.getUserName()).append(")");
        mViewHolder.mNameView.setText(sb.toString());

        switch (member.getUserState()) {
            case CONNECTED:
                // Connected refers to users who are on the voice session, but not currently speaking.
                mViewHolder.mAvatarView.setBackgroundResource(R.drawable.grey);
                break;

            case DISCONNECTED:
                // Disconnected means the user is not on the voice session and will not hear any sound.
                mViewHolder.mAvatarView.setBackgroundResource(R.drawable.red);
                break;

            case SPEAKING:
                // Speaking means the user is currently connected and talking on the voice session.
                mViewHolder.mAvatarView.setBackgroundResource(R.drawable.green);
                break;
        }

        ImageLoader.getInstance().displayImage(member.getUserAvatar(), mViewHolder.mAvatarView, options);
        mViewHolder.mAvatarView.setTag(member.getUserId());
        mViewHolder.mAvatarView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (v.getTag().equals(ArcVoiceHelper.getInstance(mContext).getUserId())) {
                    ArcVoiceHelper.getInstance(mContext).doMuteMyself();
                } else
                    ArcVoiceHelper.getInstance(mContext).doMuteOthers();
                return true;
            }
        });
        return convertView;
    }

    private View inflateView(ViewGroup parent){
        int viewId = R.layout.layout_member_item_right;
        if(mDirection == ArcMemberView.Direction.TEXT_DOWN){
            viewId = R.layout.layout_member_item_bottom;
        }else if(mDirection == ArcMemberView.Direction.TEXT_UP){
            viewId = R.layout.layout_member_item_top;
        }else if(mDirection == ArcMemberView.Direction.TEXT_LEFT){
            viewId = R.layout.layout_member_item_left;
        }else{
            viewId = R.layout.layout_member_item_right;
        }

        return mInflater.inflate(viewId, parent, false);
    }

    static class ViewHolder{
        ImageView mAvatarView;
        TextView mNameView;
    }
}
