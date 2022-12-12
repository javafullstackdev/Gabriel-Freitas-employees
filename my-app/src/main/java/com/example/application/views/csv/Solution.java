package com.example.application.views.csv;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Solution {

    public Solution(){
    }

    //This is the function which converts string to LocalDate
    public LocalDate getDate(String str){
        //Supports multiple Data formats using DateTimeFormatterBuilder object
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendOptional(DateTimeFormatter.ISO_LOCAL_DATE)
                .appendOptional(DateTimeFormatter.ofPattern("d MMM uuuu"))
                .appendOptional(DateTimeFormatter.ofPattern("yyyyMMdd"))
//                .appendOptional(DateTimeFormatter.ofPattern("d/M/yyyy"))
//                .appendOptional(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("M/d/yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("E, MMM dd yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .appendOptional(DateTimeFormatter.ofPattern("yyyy-M-d"))
                .toFormatter();
        // eliminate all white zeros in the string and compares to NULL
        //returns Today if the value is NULL or returns LocalDate Object corresponding string value
        if (str.replaceAll("\0", "").equals("NULL")){
            return LocalDate.now();

        }
        else return LocalDate.parse(str, formatter);
    }

    //function which caculates common work Duration of 2 employees on the same project
    public long getDuration(String [] date1, String [] date2){

        LocalDate startDate1 = getDate(date1[0]);
        LocalDate startDate2 = getDate(date2[0]);
        LocalDate endDate1 = getDate(date1[1]);
        LocalDate endDate2 = getDate(date2[1]);
        LocalDate startDate = (startDate1.compareTo(startDate2) >= 0)? startDate1 : startDate2;
        LocalDate endDate = (endDate1.compareTo(endDate2) >= 0) ? endDate2 : endDate1;

        if ( startDate.compareTo(endDate) >= 0) return 0;
        else return ChronoUnit.DAYS.between(startDate,endDate);

    }
// main function (engine)
    public List<String[]> getLongestPair(List<String [] > employeeList){

      List<String []> resultList=new ArrayList<>();

      // Max is the Longest time
      long max = 0;

      //employeeList is the value from CSV file and these are the sample values for testing
//      employeeList.add(new String []{"1","1","04/07/2019","2020-08-14"});
//      employeeList.add(new String []{"1","2","25/12/2019","28/12/2020"});
//      employeeList.add(new String []{"1","3","12/10/2018","NULL"});
//      employeeList.add(new String []{"1","4","16/11/2019","NULL"});
//      employeeList.add(new String []{"1","5","05/01/2020","21/12/2020"});
//      employeeList.add(new String []{"2","1","03/10/2018","NULL"});
//      employeeList.add(new String []{"2","2","16/01/2019","24/03/2020"});
//      employeeList.add(new String []{"2","3","22/05/2019","26/12/2019"});
//      employeeList.add(new String []{"2","4","07/03/2020","NULL"});
//      employeeList.add(new String []{"2","5","24/01/2018","15/01/2019"});
//      employeeList.add(new String []{"3","1","21/03/2019","26/11/2020"});
//      employeeList.add(new String []{"3","5","28/09/2019","25/12/2020"});
//      employeeList.add(new String []{"4","2","22/10/2018","NULL"});
//      employeeList.add(new String []{"4","3","27/01/2018","28/08/2020"});
//      employeeList.add(new String []{"5","3","03/02/2018","14/10/2020"});
//      employeeList.add(new String []{"5","5","04/08/2018","NULL"});


// group the List according to the employeeID, and in the employee ID all the data is stored in the HashMap and the Key is the project ID
      HashMap<String, HashMap<String, String []>> employeeHash= new HashMap<>();
      for(String [] employee : employeeList){
          // if HashMap already has employee ID , add the project list, if not add employeeID and project List to the Hashmap
          if(employeeHash.get(employee[0]) == null){
              HashMap <String, String []> projectList= new HashMap<>();
              projectList.put(employee[1], new String []{employee[2], employee[3]});
              employeeHash.put(employee[0],projectList);
          }
          else{
              HashMap<String, String []> projectList= employeeHash.get(employee[0]);
              projectList.put(employee[1], new String [] {employee[2], employee[3]});
          }
      }
      // compare random 2 employees with the projectlist, if they has common project duration will be increased from getDuration method
      for(Map.Entry<String, HashMap<String, String []>> employeeSet1 : employeeHash.entrySet()){
         String employeeID = employeeSet1.getKey();
         HashMap<String, String []> projectHash = employeeSet1.getValue();
         for(Map.Entry<String, HashMap<String, String []>> employeeSet2 : employeeHash.entrySet()){
             ArrayList<String []> tempList= new ArrayList<>();
             if(employeeID != employeeSet2.getKey()){
             long daysInterval = 0;
             for( Map.Entry<String, String []> projectSet : projectHash.entrySet()) {
                 if (employeeSet2.getValue().get(projectSet.getKey())!=null) {
                     String [] date1 = projectSet.getValue();
                     String [] date2 = employeeSet2.getValue().get(projectSet.getKey());
                     daysInterval += getDuration(date1, date2);
                     if(getDuration(date1, date2)>0)
                        tempList.add(new String [] {employeeID, employeeSet2.getKey(),projectSet.getKey(),Long.toString(getDuration(date1, date2))});
                 }
             }
             //return result to the view
             if (daysInterval > max){
                 max=daysInterval;
                 resultList.clear();
                 resultList.add(new String [] {"EmployeeID #1", "EmployeeID #2", "Project ID", "Days Worked"});
                 if(!tempList.isEmpty())
                 for(int i=0;i<tempList.size();i++)
                     resultList.add(tempList.get(i));

             }
            }
         }
      }
      return resultList;
    }
}

