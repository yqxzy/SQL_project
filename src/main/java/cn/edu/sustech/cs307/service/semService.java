package cn.edu.sustech.cs307.service;

import cn.edu.sustech.cs307.database.SQLDataSource;
import cn.edu.sustech.cs307.dto.Semester;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class semService implements SemesterService{
    @Override
    public int addSemester(String name, Date begin, Date end) {
        int id=0;
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("select * from addInstructor(?::varchar ,?::data ,?::data )")){
            stmt.setString(1,name);
            stmt.setDate(2,begin);
            stmt.setDate(3,end);
            ResultSet res =stmt.executeQuery();
            id=res.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public void removeSemester(int semesterId) {
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("call removeSemester(?)")){
            stmt.setInt(1,semesterId);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Semester> getAllSemesters() {
        List<Semester> list=new ArrayList<>();
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("select * from getAllSemesters()")){
            ResultSet res =stmt.executeQuery();
            while (res.next()){
                Semester semester=new Semester();
                semester.id=res.getInt(1);
                semester.name=res.getString(2);
                semester.begin=res.getDate(3);
                semester.end=res.getDate(4);
                list.add(semester);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Semester getSemester(int semesterId) {
        Semester semester=new Semester();
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("select * from getSemester(?)")){
            stmt.setInt(1,semesterId);
            ResultSet res =stmt.executeQuery();
            if (res.next()){
                semester.id=res.getInt(1);
                semester.name=res.getString(2);
                semester.begin=res.getDate(3);
                semester.end=res.getDate(4);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return semester;
    }
}
