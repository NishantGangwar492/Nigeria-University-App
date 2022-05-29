package com.iotait.schoolapp.ui.homepage.ui.chat.interfaces;

import java.util.List;

public interface ChatListListener {
    public void onRoomJoinClick(String roomId, List<String> member);
}
