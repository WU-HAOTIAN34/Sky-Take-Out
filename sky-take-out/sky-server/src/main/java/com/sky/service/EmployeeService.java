package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;



public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 员工创建
     * @param
     * @return
     */
    Employee save(EmployeeDTO employeeDTO, long id);

    /**
     *
     * @param employeePageQueryDTO
     * @return
     */
    PageResult query(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     *
     * @param status
     * @param id
     */
    void modifyStatus(Integer status, Long id);

    void editEmployee(EmployeeDTO employeeDTO);
}
