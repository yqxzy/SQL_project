package cn.edu.sustech.cs307.service;

import cn.edu.sustech.cs307.database.SQLDataSource;
import cn.edu.sustech.cs307.dto.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class depService implements DepartmentService{
    @Override
    public int addDepartment(String name) {
        int id=0;
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("select addDepartment(?::varchar )")){
            stmt.setString(1,name);
            ResultSet res =stmt.executeQuery();
            id=res.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public void removeDepartment(int departmentId) {
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("call removeDepartment(?)")){
            stmt.setInt(1,departmentId);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Department> getAllDepartments() {
        List<Department> list=new ArrayList<>();
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("select getAllDepartments()")){
            ResultSet res =stmt.executeQuery();
            while (res.next()){
                Department department=new Department();
                department.id=res.getInt(1);
                department.name=res.getString(2);
                list.add(department);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Department getDepartment(int departmentId) {
        Department department=new Department();
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("select getDepartment(?)")){
            stmt.setInt(1,departmentId);
            ResultSet res =stmt.executeQuery();
            if(res.next()){
                department.id=res.getInt(1);
                department.name=res.getString(2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return department;
    }
}
