package com.wanmei.arcvoice.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.talkray.arcvoice.MemberCallStatus;
import com.wanmei.arcvoice.R;

import java.util.Collection;

public class MembersAdapter extends ArrayAdapter<MemberCallStatus> {


    public MembersAdapter(Context context, int layoutResourceId, int textViewResourceId) {
        super(context, layoutResourceId, textViewResourceId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MemberCallStatus memberStatus = this.getItem(position);

        View v = convertView;
        if (v == null) {
            v = super.getView(position, convertView, parent);
        }

        TextView nameView = (TextView) v.findViewById(R.id.playerName);
        ImageView avatarView = (ImageView) v.findViewById(R.id.avatar);

        // Each MemberCallStatus object represents a single user of the current voice session.
        // getUserId() will return the unique userId for that client.
        // getUserState() will return an enum for the current state of that user.

        nameView.setText(memberStatus.getUserId());

        switch(memberStatus.getUserState()) {
            case CONNECTED:
                // Connected refers to users who are on the voice session, but not currently speaking.
                avatarView.setImageResource(R.drawable.connected);
                break;

            case DISCONNECTED:
                // Disconnected means the user is not on the voice session and will not hear any sound.
                avatarView.setImageResource(R.drawable.disconnected);
                break;

            case SPEAKING:
                // Speaking means the user is currently connected and talking on the voice session.
                avatarView.setImageResource(R.drawable.talking);
                break;
        }

        return v;
    }

}
