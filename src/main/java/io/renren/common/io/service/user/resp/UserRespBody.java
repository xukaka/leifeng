package io.renren.common.io.service.user.resp;

import org.jim.common.Status;
import org.jim.common.packets.Command;
import org.jim.common.packets.RespBody;
import org.jim.common.packets.User;

/**
 * @auther: Easy
 * @date: 19-4-18 11:24
 * @description:
 */
public class UserRespBody extends RespBody {
    private static final long serialVersionUID = 1L;
    private String token;
    private User user;

    public UserRespBody(Command command, Status status) {
        this(command, status, (User)null);
    }

    public UserRespBody(Command command, Status status, User user) {
        super(command, status);
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public void clear() {
        this.setToken((String)null);
        this.setUser((User)null);
    }
}
