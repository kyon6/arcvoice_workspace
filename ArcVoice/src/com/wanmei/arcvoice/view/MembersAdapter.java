package com.wanmei.arcvoice.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wanmei.arcvoice.ArcVoiceHelper;
import com.wanmei.arcvoice.R;
import com.wanmei.arcvoice.model.Player;

import java.util.ArrayList;
import java.util.List;


public class MembersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static enum Direction {LEFT,RIGHT,UP,DOWN};

    private final Context mContext;
    private DisplayImageOptions options;

    private List<Player> mData = new ArrayList<Player>();

    private Direction mDirection = Direction.RIGHT;

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

    public void setData(List<Player> list) {
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
        View v = LayoutInflater.from(mContext).inflate(R.layout.voice_member, null);

        return new PlayerHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        Player player = mData.get(i);


        // Each MemberCallStatus object represents a single user of the current voice session.
        // getUserId() will return the unique userId for that client.
        // getUserState() will return an enum for the current state of that user.

        PlayerHolder playerHolder = (PlayerHolder) viewHolder;

        String name = player.getUserId()+":"+player.getUserName();
        if(mDirection == Direction.RIGHT){
            playerHolder.playerNameLeft.setVisibility(View.GONE);
            playerHolder.playerNameRight.setVisibility(View.VISIBLE);
            playerHolder.playerNameRight.setText(name);
        }else if(mDirection == Direction.LEFT){
            playerHolder.playerNameRight.setVisibility(View.GONE);
            playerHolder.playerNameLeft.setVisibility(View.VISIBLE);
            playerHolder.playerNameLeft.setText(name);
        }

        switch (player.getUserState()) {
            case CONNECTED:
                // Connected refers to users who are on the voice session, but not currently speaking.
                playerHolder.avatar.setBackgroundResource(R.drawable.grey);
                break;

            case DISCONNECTED:
                // Disconnected means the user is not on the voice session and will not hear any sound.
                playerHolder.avatar.setBackgroundResource(R.drawable.red);
                break;

            case SPEAKING:
                // Speaking means the user is currently connected and talking on the voice session.
                playerHolder.avatar.setBackgroundResource(R.drawable.green);
                break;
        }

        ImageLoader.getInstance().displayImage(player.getUserAvatar(), playerHolder.avatar, options);
        playerHolder.avatar.setTag(i);
        playerHolder.avatar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (v.getTag().equals(ArcVoiceHelper.getInstance(getContext()).getUserId())) {
                    ArcVoiceHelper.getInstance(getContext()).doMuteMyself();
                } else
                    ArcVoiceHelper.getInstance(getContext()).doMuteOthers();
                return true;
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

    class PlayerHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView playerNameRight;
        TextView playerNameLeft;

        public PlayerHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            playerNameRight = (TextView) itemView.findViewById(R.id.playerName_right);
            playerNameLeft = (TextView)itemView.findViewById(R.id.playerName_left);
        }
    }
}
