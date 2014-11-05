package com.wanmei.arcvoice.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wanmei.arcvoice.R;
import com.wanmei.arcvoice.model.Player;

import java.util.List;



public class MembersAdapter extends ArrayAdapter<Player> {
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnFail(R.drawable.connected)
            .showImageForEmptyUri(R.drawable.connected)
            .showImageOnLoading(R.drawable.connected)
            .build();


    public MembersAdapter(Context context, int layoutResourceId, int textViewResourceId) {
        super(context, layoutResourceId, textViewResourceId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, null, parent);

        Player player = this.getItem(position);

        TextView nameView = (TextView) v.findViewById(R.id.playerName);
        CircleImageView avatarView = (CircleImageView) v.findViewById(R.id.avatar);

        // Each MemberCallStatus object represents a single user of the current voice session.
        // getUserId() will return the unique userId for that client.
        // getUserState() will return an enum for the current state of that user.

        nameView.setText(player.getUserId());

        switch (player.getUserState()) {
            case CONNECTED:
                // Connected refers to users who are on the voice session, but not currently speaking.
                avatarView.setBackgroundResource(R.drawable.grey);
                break;

            case DISCONNECTED:
                // Disconnected means the user is not on the voice session and will not hear any sound.
                avatarView.setBackgroundResource(R.drawable.red);
                break;

            case SPEAKING:
                // Speaking means the user is currently connected and talking on the voice session.
                avatarView.setBackgroundResource(R.drawable.green);
                break;
        }

        ImageLoader.getInstance().displayImage(player.getUserAvatar(), avatarView, options);
        return v;
    }

    public void update(List plays) {
        setNotifyOnChange(false);
        clear();
        setNotifyOnChange(true);
        addAll(plays);
    }

}
