package top.xuwuruoshui.springsecurity.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import top.xuwuruoshui.springsecurity.pojo.User;
import java.util.List;

@Repository
public interface MyUserDetailsServiceMapper {

    //根据username查询用户信息
    @Select("SELECT username,password,enabled\n" +
            "FROM sys_user u\n" +
            "WHERE u.username = #{username} or u.phone= #{username}")
    User findByUserName(@Param("username") String username);

    //根据username查询用户角色
    @Select("SELECT role_code\n" +
            "FROM sys_role r\n" +
            "LEFT JOIN sys_user_role ur ON r.id = ur.role_id\n" +
            "LEFT JOIN sys_user u ON u.id = ur.user_id\n" +
            "WHERE u.username = #{username} or u.phone= #{username}")
    List<String> findRoleByUserName(@Param("username") String username);


    //根据用户角色查询用户权限
    @Select({
      "<script>",
         "SELECT url " ,
         "FROM sys_menu m " ,
         "LEFT JOIN sys_role_menu rm ON m.id = rm.menu_id " ,
         "LEFT JOIN sys_role r ON r.id = rm.role_id ",
         "WHERE r.role_code IN ",
         "<foreach collection='roleCodes' item='roleCode' open='(' separator=',' close=')'>",
            "#{roleCode}",
         "</foreach>",
      "</script>"
    })
    List<String> findAuthorityByRoleCodes(@Param("roleCodes") List<String> roleCodes);


    //根据userID查询用户资源权限信息
    @Select("SELECT url\n" +
            "FROM sys_menu m\n" +
            "LEFT JOIN sys_role_menu rm ON m.id = rm.menu_id\n" +
            "LEFT JOIN sys_role r ON r.id = rm.role_id\n" +
            "LEFT JOIN sys_user_role ur ON r.id = ur.role_id\n" +
            "LEFT JOIN sys_user u ON u.id = ur.user_id\n" +
            "WHERE u.username = #{username} or u.phone= #{username}")
    List<String> findUrlsByUserName(@Param("username") String username);
}
