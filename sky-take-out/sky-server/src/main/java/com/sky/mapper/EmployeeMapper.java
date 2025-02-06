package com.sky.mapper;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Update;

import java.util.List;



@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);


    @Insert("insert into employee (name, username, password, phone, sex, id_number, create_time, update_time, create_user, update_user, status)" +
            "values" +
            "(#{name}, #{username}, #{password}, #{phone}, #{sex}, #{idNumber}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser}, #{status})")
    void saveEmployee(Employee employee);

    /**
     * @Select("select * from employee limit #{num} offset #{begin}")
    List<Employee> queryEmployee(int begin, int num);

    @Select("select * from employee where username = #{name} limit #{num} offset #{begin}")
    List<Employee> queryEmployeeByName(String name, int begin, int num);\
    **/

    Page<Employee> queryEmployee(EmployeePageQueryDTO employeePageQueryDTO);

    @Update("update employee set status = #{status} where id = #{id}")
    void modifyStatus(Integer status, Long id);

    void modifyEmployee(Employee employee);
}
