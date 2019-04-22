package io.renren.common.io.body;

import org.jim.common.packets.Message;

public class UserGroupBody extends Message {
    String id;
    String userId;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
