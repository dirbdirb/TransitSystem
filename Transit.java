package cs4350Lab4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Transit {
	public static final Scanner sc = new Scanner(System.in);
	
    public static void main(String[] args) throws ClassNotFoundException, SQLException
    {    
    	
		Connection con = null;
        Statement stmt = null;

        con = DriverManager.getConnection("jdbc:ucanaccess://C:/Projects/Transit.mdb;memory=true;immediatelyReleaseResources=true");
        stmt = con.createStatement();
            
        String input = startProgram();
        Boolean bool = true;
        while(bool) {
        	if(input.equals("editt"))
        		getUpdateCommand();   
        	else if(input.equals("addb"))
        		addBus(stmt);
        	else if(input.equals("delb"))
        		deleteBus(stmt);
        	else if(input.equals("delb"))
        		deleteBusDriver(stmt);
        	else if(input.equals("addd"))
        		addBusDriver(stmt);
        	else if(input.equals("sch"))
        		showSchedule(stmt);
        	else if(input.equals("Stops"))
        		showStops(stmt);
        		
       
        	System.out.println("Exit Program? (Y/N): ");	
        	String inp = sc.nextLine();
            inp = inp.toLowerCase();
            
        	if(inp.equals("y")){
        		System.out.println("Exiting..");
        		System.exit(0);
        	}
        }
     
          	
        stmt.close();
        con.close();     
        
    }
    
    public static String startProgram() {
    	
    	System.out.println("Pomona Transit System");
    	System.out.println("Please Type in a Command from the List (e.g. type 'ADD' for add function");
    	System.out.println("'EditT' to edit trip offering");
    	System.out.println("'AddB' to add bus");
    	System.out.println("'DelB' to delete bus");
    	System.out.println("'AddD' to add driver");
    	System.out.println("'EditS' to edit schedule");
    	System.out.println("'Stops' to show stops for a trip");
    	System.out.println("'Sch' to show schedule of trips for given start/destination");
    	System.out.println("'Quit' to quit");
    	String userInput = sc.nextLine();
    	userInput = userInput.toLowerCase();
    	System.out.println("-----------------------------------------------------------------------------------------");
    	
    	return userInput;
    }
  
    public static void updateTripOffering(Statement stmt) throws SQLException
    {
    	String useInput = getUpdateCommand();
    	if(useInput.equals("add")) 
    		addTrip(stmt);
    	else if(useInput.equals("del")) 
    		deleteTrip(stmt);
    	else if(useInput.equals("driver"))
    		changeDriver(stmt);
    	return;
    	
    }
        
    
    public static String getUpdateCommand(){
    	System.out.println("-----------------------------------------------------------------------------------------");
    	System.out.println("Edit Schedule");
    	System.out.println("Please Type in a command from the list");
    	System.out.println("'add' to add a trip offering");
    	System.out.println("'Del' to delete a trip offering");
    	System.out.println("'Driver' to change driver");
    	String input2 = sc.nextLine();
    	input2 = input2.toLowerCase();
    	return input2;
    }
    public static void addTrip(Statement stmt) throws SQLException{
    	Boolean bool = true;
    	while(bool) {
    		System.out.println("Enter trip number: ");
    		String tripNumber = sc.nextLine();
    		System.out.println("Enter date");
    		String date = sc.nextLine();
    		System.out.println("Enter scheduled start time (Military Format: HH:MM)");
    		String startTime = sc.nextLine();
    		System.out.println("Enter scheduled arrival time");
    		String arrivalTime = sc.nextLine();
    		System.out.println("Enter driver name");
    		String driverName = sc.nextLine();
    		System.out.println("Enter bus id");
    		String busID = sc.nextLine();
    	
    		stmt.execute("INSERT INTO TripOffering VALUES ('" + tripNumber + "', '" + date + "', '" + startTime + "', '" + arrivalTime + "', '" + driverName + "', '" + busID + "')");
    		
    		System.out.println("Trip added");
    		System.out.println("Would you like to enter another trip? (Y/N)");
    		String input = sc.nextLine();
    		input.toLowerCase();
    		if(input.equals("n")) {
    			bool = false;
    		}
    	
    	} 
    }
    
    public static void deleteTrip(Statement stmt) throws SQLException {
    	System.out.println("Enter trip number: ");
		String tripNumber = sc.nextLine();
		System.out.println("Enter date");
		String date = sc.nextLine();
		System.out.println("Enter scheduled start time (Military Format: HH:MM)");
		String startTime = sc.nextLine();
		
		stmt.executeUpdate("DELETE TripOffering " + 
				"WHERE TripNumber = '" + tripNumber + "' AND " + 
				"Date = '" + date + "' AND " +
				"ScheduledStartTime = '" + startTime + "'");
		
		System.out.println("Trip deleted");

    	
    }
    
    public static void changeDriver(Statement stmt) throws SQLException {
    	System.out.println("Enter trip number: ");
		String tripNumber = sc.nextLine();
		System.out.println("Enter date");
		String date = sc.nextLine();
		System.out.println("Enter scheduled start time (Military Format: HH:MM)");
		String startTime = sc.nextLine();
		System.out.println("Enter name of new driver");
		String newDriver = sc.nextLine();
		
		stmt.executeUpdate("UPDATE TripOffering " + 
				"SET DriverName = '" + newDriver + "' " +
				"WHERE TripNumber = '" + tripNumber + "' AND " + 
				"Date = '" + date + "' AND " +
				"ScheduledStartTime = '" + startTime + "'");
		System.out.println("Driver Changed");
    }
    
    
    public static void showSchedule(Statement stmt) throws SQLException{
		System.out.print("Enter start location: ");
		String startLocation = sc.nextLine();
		System.out.print("Enter destination: ");
		String destinationName = sc.nextLine();
		System.out.print("Enter date: ");
		String date = sc.nextLine();
		//get table data
		
		ResultSet set = stmt.executeQuery("SELECT T.ScheduledStartTime, T.ScheduledArrivalTime, O.DriverName, O.BusID " +
										"FROM TripOffering O, Trip T " +
										"WHERE O.StartLocationName LIKE '" + startLocation +
										"' AND "+ "O.DestinationName LIKE '" + destinationName + "' AND " +
										"' AND " +"O.Date = '" + date + "' AND " +
										"T.TripNumber = O.TripNumber "+
										"Order by ScheduledStartTime ");
		
			System.out.println("Data Columns in order: Start Time, Arrival Time, Driver Name, Bus ID");
			while(set.next()){
				for(int x = 1; x <= columns; x++)
					System.out.print(set.getString(x));
				System.out.println();
			}		
	
    }
    
    
   public static void addBus(Statement stmt)throws SQLException{
	    
		System.out.print("Enter bus ID: ");
		String busID = sc.nextLine();
		System.out.print("Enter bus model: ");
		String busModel = sc.nextLine();
		System.out.print("Enter bus year: ");
		String busYear = sc.nextLine();
	
		stmt.execute("INSERT INTO Bus VALUES ('" + busID + "', '" + busModel + "', '" +busYear + "')");
		System.out.println("Bus added");
	  
   }
   
   public static void deleteBus(Statement stmt) throws SQLException{
	    
		System.out.print("Enter bus ID: ");
		String busID = sc.nextLine();
		
		stmt.executeUpdate("DELETE Bus " + 
				"WHERE BusID = '" + busID + "'");
  }
   
   public static void addBusDriver(Statement stmt)throws SQLException{
	    
		System.out.print("Enter driver name: ");
		String driverName = sc.nextLine();
		System.out.print("Enter driver telephone number: ");
		String driverPhoneNum = sc.nextLine();
		
		stmt.execute("INSERT INTO Driver VALUES ('" + driverName + "', '" + driverPhoneNum + "')");
		System.out.println("Driver added");
   }
   
   public static void deleteBusDriver(Statement stmt)throws SQLException{
	    
		System.out.print("Enter driver name: ");
		String driverName = sc.nextLine();
		
		stmt.executeUpdate("DELETE Bus " + 
				"WHERE BusID = '" + driverName + "'");
 }
   
   public static void showStops(Statement stmt)throws SQLException{
		System.out.print("Enter trip number: ");
		String tripNo = sc.nextLine();
		
		ResultSet set = stmt.executeQuery("SELECT * " + "FROM TripStopInfo " +
						"WHERE TripNumber = '" + tripNo + "' " +
						"Order By SequenceNumber ");
		
		System.out.println("Columns in order: TripNumber | ShopNumber | Sequence Number | Driving Time");
		
		while(set.next()){
			for(int x = 1; x <= columns; x++)
				System.out.print(set.getString(x));
			System.out.println();
		}
	}
