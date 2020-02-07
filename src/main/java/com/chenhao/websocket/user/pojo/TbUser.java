package com.chenhao.websocket.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.Random;

@Data
@Table(name = "tb_user")
@AllArgsConstructor
@NoArgsConstructor
public class TbUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    //上线时间
    private Date joinTime;

    private Date createTime;

    public TbUser(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.avatar = "/static.image/avatar/avatar" + new Random().nextInt(10) + ".jpg";
        this.joinTime = new Date();
    }

}
