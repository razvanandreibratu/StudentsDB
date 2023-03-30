package studentsapp;

import java.util.Scanner;

public class Student
{
    private String firstName;
    private String lastName;
    private int gradeYear;

    public Student(){
        Scanner in = new Scanner(System.in);
        System.out.print("Enter student first name: ");
        this.firstName = in.nextLine();

        System.out.print("Enter student last name: ");
        this.lastName = in.nextLine();

        System.out.print("Enter student class level:\n 1-Freshmen\n 2-Intermediate\n 3-Senior\n");
        System.out.print("Your answear: ");
        this.gradeYear = in.nextInt();
        if (this.gradeYear > 3)
        {
            this.gradeYear = 3;
        }
    }
    public String getFirstName()
    {
        return this.firstName;
    }
    public String getLastName()
    {
        return this.lastName;
    }
    public int getGradeYear()
    {
        return this.gradeYear;
    }

}
