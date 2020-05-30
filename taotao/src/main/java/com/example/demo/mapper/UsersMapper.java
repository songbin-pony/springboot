package com.example.demo.mapper;

import com.example.demo.bean.Users;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface UsersMapper {
    public List<Users> getUsers();
    public void rePasswd(String user_name,String repasswd);
    public void delUser(String user_name);
}
