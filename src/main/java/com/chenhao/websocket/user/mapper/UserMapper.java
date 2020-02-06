package com.chenhao.websocket.user.mapper;

import com.chenhao.websocket.user.pojo.TbUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<TbUser> {

    @Insert("INSERT INTO tb_user_friend VALUES (NULL,#{id1},#{id2})")
    void insertUserFriend(@Param(value = "id1") Long id1, @Param(value = "id2") Long id2);

    @Select("SELECT\n" +
            "\t*\n" +
            "FROM\n" +
            "\t(\n" +
            "\t\tSELECT\n" +
            "\t\t\tuf.friend_id\n" +
            "\t\tFROM\n" +
            "\t\t\ttb_user u,\n" +
            "\t\t\ttb_user_friend uf\n" +
            "\t\tWHERE\n" +
            "\t\t\tuf.user_id = u.id\n" +
            "\t\tAND u.username = #{username}\n" +
            "\t) users,\n" +
            "\ttb_user u\n" +
            "WHERE\n" +
            "\tu.id = users.friend_id")
    List<TbUser> listFriend(@Param(value = "username") String username);
}
