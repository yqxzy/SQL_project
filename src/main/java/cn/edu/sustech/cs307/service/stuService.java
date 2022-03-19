package cn.edu.sustech.cs307.service;

import cn.edu.sustech.cs307.database.SQLDataSource;
import cn.edu.sustech.cs307.dto.*;
import cn.edu.sustech.cs307.dto.grade.Grade;
import cn.edu.sustech.cs307.dto.grade.HundredMarkGrade;
import cn.edu.sustech.cs307.dto.grade.PassOrFailGrade;

import javax.annotation.Nullable;
import java.sql.*;
import java.sql.Date;
import java.time.DayOfWeek;
import java.util.*;

public class stuService implements StudentService {

    @Override
    public void addStudent(int userId, int majorId, String firstName, String lastName, Date enrolledDate) {
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("call add_student(?,?,?::varchar ,?::varchar ,?::data )")){
            stmt.setInt(1,userId);
            stmt.setInt(2,majorId);
            stmt.setString(3,firstName);
            stmt.setString(4,lastName);
            stmt.setDate(5,enrolledDate);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<CourseSearchEntry> searchCourse(int studentId, int semesterId, @Nullable String searchCid, @Nullable String searchName,
                                                @Nullable String searchInstructor, @Nullable DayOfWeek searchDayOfWeek,
                                                @Nullable Short searchClassTime, @Nullable List<String> searchClassLocations,
                                                CourseType searchCourseType, boolean ignoreFull, boolean ignoreConflict,
                                                boolean ignorePassed, boolean ignoreMissingPrerequisites, int pageSize, int pageIndex) {
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("call searchCourse(?,?,?::varchar ,?::varchar ," +
                        "?::varchar ,?::integer ,?::integer ,?::varchar [],?,?,?,?,?,?,?)")){

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public EnrollResult enrollCourse(int studentId, int sectionId) {
         EnrollResult enrollResult = null;
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("call enrollCourse(?,? )")){
            stmt.setInt(1,studentId);
            stmt.setInt(2,sectionId;
            ResultSet res =stmt.executeQuery();
            if(res.next()){
                enrollResult=EnrollResult.valueOf(res.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enrollResult;
    }

    @Override
    public void dropCourse(int studentId, int sectionId) throws IllegalStateException {
        try (
            Connection connection= SQLDataSource.getInstance().getSQLConnection();
            PreparedStatement stmt =connection.prepareStatement("call drop_course(?, ?)")){
            stmt.setInt(1,studentId);
            stmt.setInt(2,sectionId);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        @Override
    public void addEnrolledCourseWithGrade(int studentId, int sectionId, @Nullable Grade grade) {
            try (
                    Connection connection= SQLDataSource.getInstance().getSQLConnection();
                    PreparedStatement stmt =connection.prepareStatement("call addEnrolledCourseWithGrade(?, ?, ?,?,?)")){
                stmt.setInt(1,studentId);
                stmt.setInt(2,sectionId);
                if(grade.getClass()== PassOrFailGrade.class){
                    stmt.setString(3,"PASS_OR_FAIL");
                    stmt.setString(5, String.valueOf(grade));
                }else {
                    stmt.setString(3,"HUNDRED");
                    stmt.setShort(4,((HundredMarkGrade)grade).mark);
                }
                stmt.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }

    }

    @Override
    public void setEnrolledCourseGrade(int studentId, int sectionId, Grade grade) {
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("call setEnrolledCourseGrade(?, ?, ?,?,?)")){
            stmt.setInt(1,studentId);
            stmt.setInt(2,sectionId);
            if(grade.getClass()== PassOrFailGrade.class){
                stmt.setString(3,"PASS_OR_FAIL");
                stmt.setString(5, String.valueOf(grade));
            }else {
                stmt.setString(3,"HUNDRED");
                stmt.setShort(4,((HundredMarkGrade)grade).mark);
            }
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Map<Course, Grade> getEnrolledCoursesAndGrades(int studentId, @Nullable Integer semesterId) {
        Map<Course, Grade> map=new HashMap<>();
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("select * from getEnrolledCoursesAndGrades(?, ?)")){
            stmt.setInt(1,studentId);
            stmt.setInt(2,semesterId);
            ResultSet res =stmt.executeQuery();
            while (res.next()){
                Course course=new Course();
                course.id=res.getString(1);
                course.name=res.getString(2);
                course.classHour=res.getInt(4);
                course.credit=res.getInt(3);
                course.grading= Course.CourseGrading.valueOf(res.getString(5));
                if(course.grading.toString()=="PASS_OR_FAIL"){
                    PassOrFailGrade grade=PassOrFailGrade.valueOf(res.getString(6));
                    map.put(course,grade);
                }else {
                    Grade grade=new HundredMarkGrade(res.getShort(7));
                    map.put(course,grade);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }

    @Override
    public CourseTable getCourseTable(int studentId, Date date) {
        CourseTable courseTable=new CourseTable();
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("select * from getCourseTabl(?, ?)")){
            stmt.setInt(1,studentId);
            stmt.setDate(2,date);
            ResultSet res =stmt.executeQuery();//我没错
            Set<CourseTable.CourseTableEntry> set1=new HashSet<>();
            Set<CourseTable.CourseTableEntry> set2=new HashSet<>();
            Set<CourseTable.CourseTableEntry> set3=new HashSet<>();
            Set<CourseTable.CourseTableEntry> set4=new HashSet<>();
            Set<CourseTable.CourseTableEntry> set5=new HashSet<>();
            Set<CourseTable.CourseTableEntry> set6=new HashSet<>();
            Set<CourseTable.CourseTableEntry> set7=new HashSet<>();
            while (res.next()){
                CourseTable.CourseTableEntry courseTableEntry = new CourseTable.CourseTableEntry();
                courseTableEntry.courseFullName = res.getString(1);
                Instructor instructor = new Instructor();
                instructor.id = res.getInt(2);
                instructor.fullName = res.getString(3);
                courseTableEntry.instructor = instructor;
                courseTableEntry.classBegin = res.getShort(4);
                courseTableEntry.classEnd = res.getShort(5);
                courseTableEntry.location = res.getString(6);
                if(String.valueOf(res.getString(7))=="MONDAY") {
                    set1.add(courseTableEntry);
                }else if(String.valueOf(res.getString(7))=="TUESDAY"){
                    set2.add(courseTableEntry);
                }else if(String.valueOf(res.getString(7))=="WEDNESDAY"){
                    set3.add(courseTableEntry);
                }else if(String.valueOf(res.getString(7))=="THURSDAY"){
                    set4.add(courseTableEntry);
                }else if(String.valueOf(res.getString(7))=="FRIDAY"){
                    set5.add(courseTableEntry);
                }else if(String.valueOf(res.getString(7))=="SATURDAY"){
                    set6.add(courseTableEntry);
                }else if(String.valueOf(res.getString(7))=="SUNDAY"){
                    set7.add(courseTableEntry);
                }
            }
            Map<DayOfWeek, Set<CourseTable.CourseTableEntry>> map=new HashMap<>();
            map.put(DayOfWeek.MONDAY,set1);
            map.put(DayOfWeek.TUESDAY,set2);
            map.put(DayOfWeek.WEDNESDAY,set3);
            map.put(DayOfWeek.THURSDAY,set4);
            map.put(DayOfWeek.FRIDAY,set5);
            map.put(DayOfWeek.SATURDAY,set6);
            map.put(DayOfWeek.SUNDAY,set7);
            courseTable.table=map;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseTable;
    }

    @Override
    public boolean passedPrerequisitesForCourse(int studentId, String courseId) {

        return false;
    }

    @Override
    public Major getStudentMajor(int studentId) {
        Major major=new Major();
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("select * from getStudentMajor(?)")){
            stmt.setInt(1,studentId);
            ResultSet res=stmt.executeQuery();
            while (res.next()){
                Department department=new Department();
                department.id=res.getInt(2);
                department.name=res.getString(4);
                major.name=res.getString(3);
                major.id=res.getInt(1);
                major.department=department;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return major;
    }
}

