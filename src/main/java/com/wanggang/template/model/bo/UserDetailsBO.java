package com.wanggang.template.model.bo;

import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.util.Collection;

/**
 * UserDetailsBO
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/24 16:13
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
@EqualsAndHashCode
public class UserDetailsBO extends User implements Serializable {
    private String id;
    private String nickName;
    private String photo;
    private Integer type;

    public UserDetailsBO(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    @Override
    public String toString() {
        return "UserDetailsBO{" +
                "id='" + id + '\'' +
                ", nickName='" + nickName + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
