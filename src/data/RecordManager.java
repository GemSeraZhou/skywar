package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class RecordManager {

	private static String recFileName = "rec.txt";

	/**
	 * add new record to records list. 
	 * New record is added, list is sorted by highest scores and top 10 are stored into the file
	 * */
	public static void addRecord(Record record){
		
		ArrayList<Record> allRecords = loadRecords();
		allRecords.add(record);
		
		// sort the list based on the scores
		Collections.sort(allRecords , (Record r1 , Record r2) -> r2.getScore() - r1.getScore());
		
		if(allRecords.size() > 10){
			allRecords.remove(10);		// the last
		}
		
		saveRecords(allRecords);
	}

	public static ArrayList<Record> getRecords(){
		return loadRecords();
	}

	private static void saveRecords(ArrayList<Record> records){

		File file  = new File(recFileName);
		if(file.exists()) file.delete();
		
		PrintWriter pw = null;
		FileWriter fos = null;
		
		
		try {
			fos = new FileWriter(file);
			pw = new PrintWriter(fos);
			
			// write each record to file line by line
			for (Record record : records){
				String line = record.getFirstName() + "," + record.getLastName() + "," + record.getScore() +
									"," + record.getDate();
				
				pw.println(line);
			}
			
		} catch (IOException e) {
			
		} finally {
			pw.close();
			try { if(fos!= null) fos.close(); } catch (IOException e2) {}
		}

	}

	private static ArrayList<Record> loadRecords(){

		ArrayList<Record> list = new ArrayList<Record>();

		File file  = new File(recFileName);

		if(!file.exists()){	// if file does not exist
			return  list;
		}

		BufferedReader br = null;
		FileReader fr = null;

		try{
			fr = new FileReader(file);
			br = new BufferedReader(fr);

			String line;

			while( (line = br.readLine()) != null ){  
				String parts[] = line.split(",");   // all items of a record are comma separated in the file

				Record record = new Record(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3]);
				list.add(record);
			}

		} catch(IOException huh){

		} finally{
			try { if(br!= null) br.close(); } catch (IOException e) {}
			try { if(fr!= null) fr.close(); } catch (IOException e) {}
		}

		return list;
	}

}
