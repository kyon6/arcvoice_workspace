package com.wanmei.arcvoice.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;


public class MembersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static enum Direction {TEXT_LEFT, TEXT_RIGHT, TEXT_UP, TEXT_DOWN};

    private final Context mContext;
    private DisplayImageOptions options;

    private List<Member> mData = new ArrayList<Member>();

    private Direction mDirection = Direction.TEXT_RIGHT;

    public MembersAdapter(Context context) {
        mContext = context;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnFail(R.drawable.connected)
                .showImageForEmptyUri(R.drawable.connected)
                .showImageOnLoading(R.drawable.connected)
                .build();
    }

    public void setDirection(Direction direction){
        this.mDirection = direction;
    }

    public void setData(List<Member> list) {
        mData.clear();
        mData.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //todo 通过ViewHolder不同的layout实现
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_member_item, null);

        return new MemberHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        final Member player = mData.get(i);


        // Each MemberCallStatus object represents a single user of the current voice session.
        // getUserId() will return the unique userId for that client.
        // getUserState() will return an enum for the current state of that user.

        MemberHolder playerHolder = (MemberHolder) viewHolder;

        String name = player.getUserId()+":"+player.getUserName();
        if(mDirection == Direction.TEXT_RIGHT){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerHolder.mAvatarView.getLayoutParams();
            params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            playerHolder.mAvatarView.setLayoutParams(params);

            RelativeLayout.LayoutParams nameParams = (RelativeLayout.LayoutParams) playerHolder.mNameView.getLayoutParams();
            nameParams.removeRule(RelativeLayout.LEFT_OF);
            nameParams.addRule(RelativeLayout.RIGHT_OF,R.id.avatar);
            playerHolder.mNameView.setLayoutParams(nameParams);

        }else if(mDirection == Direction.TEXT_LEFT){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerHolder.mAvatarView.getLayoutParams();
            params.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            playerHolder.mAvatarView.setLayoutParams(params);

            RelativeLayout.LayoutParams nameParams = (RelativeLayout.LayoutParams) playerHolder.mNameView.getLayoutParams();
            nameParams.removeRule(RelativeLayout.RIGHT_OF);
            nameParams.addRule(RelativeLayout.LEFT_OF,R.id.avatar);
            playerHolder.mNameView.setLayoutParams(nameParams);
        }else if(mDirection == Direction.TEXT_DOWN){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerHolder.mAvatarView.getLayoutParams();
            params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            playerHolder.mAvatarView.setLayoutParams(params);

            RelativeLayout.LayoutParams nameParams = (RelativeLayout.LayoutParams) playerHolder.mNameView.getLayoutParams();
            nameParams.removeRule(RelativeLayout.ABOVE);
            nameParams.addRule(RelativeLayout.BELOW,R.id.avatar);
            playerHolder.mNameView.setLayoutParams(nameParams);
        }else if(mDirection == Direction.TEXT_UP){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerHolder.mAvatarView.getLayoutParams();
            params.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            playerHolder.mAvatarView.setLayoutParams(params);

            RelativeLayout.LayoutParams nameParams = (RelativeLayout.LayoutParams) playerHolder.mNameView.getLayoutParams();
            nameParams.removeRule(RelativeLayout.BELOW);
            nameParams.addRule(RelativeLayout.ABOVE,R.id.avatar);
            playerHolder.mNameView.setLayoutParams(nameParams);
        }
        playerHolder.mNameView.setText(name);

        switch (player.getUserState()) {
            case CONNECTED:
                // Connected refers to users who are on the voice session, but not currently speaking.
                playerHolder.mAvatarView.setBackgroundResource(R.drawable.grey);
                break;

            case DISCONNECTED:
                // Disconnected means the user is not on the voice session and will not hear any sound.
                playerHolder.mAvatarView.setBackgroundResource(R.drawable.red);
                break;

            case SPEAKING:
                // Speaking means the user is currently connected and talking on the voice session.
                playerHolder.mAvatarView.setBackgroundResource(R.drawable.green);
                break;
        }

        ImageLoader.getInstance().displayImage(player.getUserAvatar(), playerHolder.mAvatarView, options);
        playerHolder.mAvatarView.setTag(player.getUserId());
        playerHolder.mAvatarView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (v.getTag().equals(ArcVoiceHelper.getInstance(getContext()).getUserId())) {
                    ArcVoiceHelper.getInstance(getContext()).doMuteMyself();
                } else
                    ArcVoiceHelper.getInstance(getContext()).doMuteOthers();
                return true;
            }
        });
        playerHolder.mAvatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e("member mAvatarView click:" + player.getUserName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public Context getContext() {
        return mContext;
    }

    class MemberHolder extends RecyclerView.ViewHolder {
        ImageView mAvatarView;
        TextView mNameView;

        public MemberHolder(View itemView) {
            super(itemView);
            mAvatarView = (ImageView) itemView.findViewById(R.id.avatar);
            mNameView = (TextView) itemView.findViewById(R.id.playerName);
        }
    }
}
