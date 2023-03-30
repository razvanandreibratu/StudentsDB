package studentsapp;


import java.sql.*;

public abstract class StudentController
{
    public static void createStudentDb()
    {
        try(Connection con = ConnectionDb.getInstance().getConnection())
        {
            DatabaseMetaData metadata = con.getMetaData();
            try(ResultSet rs = metadata.getTables(null,null, "students", null);
                Statement stmt = con.createStatement())
            {
                boolean tableExists = rs.next();
                if(!tableExists)
                {
                    stmt.execute("create table students\n" +
                            "(\n" +
                            "    student_id    int auto_increment\n" +
                            "        primary key,\n" +
                            "    student_fname text not null,\n" +
                            "    student_lname text not null,\n" +
                            "    gradeYear     int  not null,\n" +
                            "    balance       int default 0 not null\n" +
                            ");");
                } else {
                    System.out.println("Already Exists");

                }
            }
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void insertStudent(Student student)
    {
        try(Connection con = ConnectionDb.getInstance().getConnection();
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO students(student_fname, student_lname, gradeYear) VALUES (?, ?, ?)"))
        {

            pstmt.setString(1, student.getFirstName());
            pstmt.setString(2, student.getLastName());
            pstmt.setInt(3, student.getGradeYear());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void displayStudentsDb()
    {
        try(Connection con = ConnectionDb.getInstance().getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM students"))
        {
            while(rs.next())
            {
                String name = rs.getString("student_fname");
                String last_name = rs.getString("student_lname");
                String gradeYear;
                switch (rs.getInt("gradeYear"))
                {
                    case 1: gradeYear = "Freshmen";
                            break;
                    case 2: gradeYear = "Intermediate";
                            break;
                    default: gradeYear = "Senior";
                            break;
                }
                System.out.println("Name:" + name + " " + "Last Name:" + last_name + " " + "Level:" + gradeYear);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
