package com.gzseeing.manager.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gzseeing.manager.entity.BackendUser;

/**
 * <p>
 * 后台管理人员 Mapper 接口
 * </p>
 *
 * @author Haowen
 * @since 2018-08-25
 */
@Mapper

public interface BackendUserMapper extends BaseMapper<BackendUser> {

	
	@Select("select * from backend_user u where u.id=#{id} ")
	public  BackendUser selectById(Integer id);
	
	
	// @Insert(" INSERT INTO "
	//		+ "backend_user(username, password, salt, phone, nick_name, is_root, `status`, create_date) "
	//		+ "VALUES (#{username}, #{password}, #{salt}, #{phone}, #{nickName}, #{isRoot}, #{status}, #{createDate})")
   // @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
	//public  Integer insertAndReturnId(BackendUser backendUser);
	
	
	
}
