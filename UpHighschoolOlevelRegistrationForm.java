/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Coursework;

import java.util.Scanner;
/**
 *
 * @author brix
 */
public class UpHighschoolOlevelGradingSystem {
    public static void main (String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter student's score out of 100: ");
int score = input.nextInt();
//variable declarion
int grade;
String remark;
//determining the grading and remark using if else if
if (score >= 80 && score <= 100) {
    grade = 1;
    remark = "D1";
} else if (score >= 75) {
    grade = 2;
    remark = "D2";
} else if (score >= 66) {
    grade = 3;
    remark = "C3";
} else if (score >= 60) {
    grade = 4;
    remark = "C4";
} else if (score >= 50) {
    grade = 5;
    remark = "C5";
} else if (score >= 45) {
    grade = 6;
    remark = "C6";
} else if (score >= 35) {
    grade = 7;
    remark = "P7";
} else if (score >= 30) {
    grade = 8;
    remark = "P8";
} else {
    grade = 9;
    remark = "F";
}


System.out.println("Score: " + score);
System.out.println("Grade: " + grade);
System.out.println("Remark: " + remark);

//closing the scanner
input.close();

   }

    
}
