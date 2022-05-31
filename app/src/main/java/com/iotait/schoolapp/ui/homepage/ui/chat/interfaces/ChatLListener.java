package com.iotait.schoolapp.ui.homepage.ui.chat.interfaces;

import com.iotait.schoolapp.ui.homepage.ui.chat.models.Message;

public interface ChatLListener {
    public void onImageSendSuccess();

    public void onMessageSendSuccess(Message msg);

    public void onImageSendSuccessforGroup();

    public void onMessageSendSuccessforGroup(Message msg);
}
