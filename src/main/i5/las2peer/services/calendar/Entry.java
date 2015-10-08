package i5.las2peer.services.calendar;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;

import i5.las2peer.persistency.MalformedXMLException;
import i5.las2peer.persistency.XmlAble;
import i5.las2peer.services.calendar.MyCalendar;
import i5.las2peer.services.calendar.security.IdGeneration;
import i5.simpleXML.Element;
import i5.simpleXML.Parser;

/**
 * Class that represents a date created by the user. It will be used by the {@link MyCalendar} to organize dates.<br>
 * The class contains the start and of a date. Furthermore it contains the id of the creator.
 * 
 */
public class Entry implements XmlAble {
	
	/** unique String id to identify this entry**/
	private String uniqueID;
	/** agent id of the agent who created the entry**/
	private final long creatorId;
	/** start date of the calendar entry**/
	private Calendar start;
	/** end date of the calendar entry**/
	private Calendar end;
	/** title of the entry**/
	private String title;
	/** description of the entry**/
	private String description;
	/** list with comments of the calendar entry**/
	private final ArrayList<Comment> comments;
	
	public int commentAmount;
	
	/**
	 * Gets the title of this {@link Comment} .
	 * 
	 * @return the title of the comment.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title of this {@link Comment}.
	 * 
	 * @param title
	 *			 new title 
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the description of this {@link Comment} .
	 * 
	 * @return the description of the comment.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of this {@link Comment}.
	 * 
	 * @param description
	 *			 new description. 
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * returns a list of the comments of this {@link Comment}.
	 * 
	 * @return a list of the comments.
	 */
	public ArrayList<Comment> getComments() {
		return comments;
	}
	
	/**
	 * Constructor for a {@link Entry}. 
	 * 
	 * @param idCreatedBy
	 *            user agent id who created the entry
	 * @param title
	 * 		      title of the new entry
	 * @param description
	 * 			  description of the new entry
	 * @param amount
	 * 			  amount of comments that can be added
	 * 				           
	 */
	
	public Entry(long idCreatedBy, String title, String description, int amount){
		this.uniqueID = IdGeneration.createID();
		commentAmount = amount;
		this.creatorId = idCreatedBy;
		this.comments = new ArrayList<Comment>(amount);
		this.title = title;
		this.description = description;
	}
	
	public Entry(String uniqueID, long idCreatedBy, String title, String description, int amount){
		this.uniqueID = uniqueID;
		commentAmount = amount;
		this.creatorId = idCreatedBy;
		this.comments = new ArrayList<Comment>(amount);
		this.title = title;
		this.description = description;
	}
	
	public String getUniqueID() {
		return uniqueID;
	}

	/**
	 * Gets the id of the user agent who created this {@link Entry} .
	 * 
	 * @return the user agent id
	 */
	public long getCreatorId() {
		return creatorId;
	}
	
	/**
	 * Gets the start date of this {@link Entry} .
	 * 
	 * @return the start date
	 */
	public Calendar getStart() {
		return this.start;
	}
	
	/**
	 * Set the start date of this {@link Entry} .
	 * 
	 * @param year
	 *			 year in which the entry begins
	 * @param month
	 *			 month in which the entry begins
	 * @param dayOfMonth
	 *			 day of month in which the entry begins
	 * @param hour
	 * 			 hour in which the entry begins
	 * @param minute
	 * 			 minute in which the entry begins
	 * @return whether or not start date could be set
	 */
	public boolean setStart(int year, int month, int dayOfMonth, int hour, int minute) {
		
		GregorianCalendar date = new GregorianCalendar(year, month, dayOfMonth, hour, minute);
		if(this.getEnd()!=null){ // if endDate has been set already and is before start date
			if(this.getEnd().before(date)){
				return false;
			}
		}
		
		this.start = date;
		return true;
	
	}
	
	/**
	 * Gets the end date of this {@link Entry} .
	 * 
	 * @return the end date
	 */
	public Calendar getEnd() {
		return this.end;
	}

	/**
	 * Set the end date of this {@link Entry} .
	 * 
	 * @param year
	 *			 year in which the entry ends
	 * @param month
	 *			 month in which the entry ends
	 * @param dayOfMonth
	 *			 day of month in which the entry ends
	 * @param hour
	 * 			 hour in which the entry ends
	 * @param minute
	 * 			 minute in which the entry ends
	 * @return whether or not end date could be set
	 */
	public boolean setEnd(int year, int month, int dayOfMonth, int hour, int minute) {
		
		GregorianCalendar date = new GregorianCalendar(year, month, dayOfMonth, hour, minute);
		if(this.getStart()!= null){ // if start date exists and is before end date
			if(this.getStart().after(date)){
			   return false;
			}
		}
		
		this.end = date;
		return true;
	}
	
	
	/**
	 * creates a comment of this entry
	 * 
	 * @param id
	 * 			the id of the current agent 	 
	 * @param comment
	 * 			the content of the comment
	 * @return
	 * 			the id of the comment. nothing if comment was empty
	 */
	
	public String createComment(long id, String comment){
		
		if(this.comments.size() >= commentAmount){
			return "";
		}
		
		if(comment == null || comment == ""){ //comment cannot be empty
			return "";
		}
		
		GregorianCalendar stamp = new GregorianCalendar();
		
		Comment c1 = new Comment(id, stamp, comment);
		this.comments.add(c1);
		return c1.getUniqueID();
		
	}
	
	/**
	 * deletes a comment of this entry
	 * 
	 * @param id
	 * 			the id of the comment that shall be deleted 	 
	 * @return
	 * 			if the comment could be found and deleted
	 */
	
	public boolean deleteComment(String id){
		
		for(Comment aComment: this.comments){
			if(aComment.getUniqueID().equals(id)){	
				this.comments.remove(aComment);
				return true;	
			}	
		}
		return false;
	}
	
	/**
	 * turns a calendar date into a string
	 * 
	 * @param date 
	 * 		  the date to be turned into a string
	 * @return
	 * 		  a string representation of the date
	 */
	public String dateToString(Calendar date){ //pretty nasty way to do this, but the easier way to do this threw strange error
		String ret = date.toString();
		int year = Integer.parseInt(ret.substring(ret.indexOf("YEAR=")+5, ret.indexOf(",",ret.indexOf("YEAR="))));
		int month = Integer.parseInt(ret.substring(ret.indexOf("MONTH=")+6, ret.indexOf(",",ret.indexOf("MONTH="))));
		int day = Integer.parseInt(ret.substring(ret.indexOf("DAY_OF_MONTH=")+13, ret.indexOf(",",ret.indexOf("DAY_OF_MONTH="))));
		int hour = Integer.parseInt(ret.substring(ret.indexOf("HOUR=")+5, ret.indexOf(",",ret.indexOf("HOUR="))));
		int minute = Integer.parseInt(ret.substring(ret.indexOf("MINUTE=")+7, ret.indexOf(",",ret.indexOf("MINUTE="))));
		return year + ":" + month + ":" + day + ":" + hour + ":" + minute;
	}
	
	/**
	 * parses a string into a calendar
	 * 
	 * @param sDate
	 * 		  the string to be parsed
	 * @return
	 * 		  the calendar object parsed from the string
	 */
	public static Calendar stringToDate(String sDate){
		
		String[] parseArray = sDate.split(":");
		int year = Integer.parseInt(parseArray[0]);
		int month = Integer.parseInt(parseArray[1]);
		int day = Integer.parseInt(parseArray[2]);
		int hour = Integer.parseInt(parseArray[3]);
		int minute = Integer.parseInt(parseArray[4]);
		
		return new GregorianCalendar(year, month, day, hour, minute);
		
	}
	
	@Override
	public String toXmlString(){ //no date or comments at the moment
		
		String returnString = "";
		
		returnString += "<las2peer:entry id=\"" + this.uniqueID + "\" creator=\"" + this.creatorId 
				+ "\" description=\"" + this.description + "\" start=\"" + dateToString(this.getStart()) + "\" end=\""  
				+ dateToString(this.getEnd()) + "\" comment=\""; 
		
		
		for(Comment aComment: this.comments){ // every comment to a string
			returnString += "UID:" + aComment.getUniqueID() + ":" + aComment.getCreatorId() + ":" + aComment.getMessage() 
			                + ":" + aComment.getTime().get(Calendar.YEAR) + ":" + aComment.getTime().get(Calendar.MONTH) + ":"
			                + aComment.getTime().get(Calendar.DAY_OF_MONTH) + ":" + aComment.getTime().get(Calendar.HOUR_OF_DAY) + ":" + aComment.getTime().get(Calendar.MINUTE) 
			                + ":";
		}
		
	    returnString += "\">" + this.title +  "</las2peer:entry>\n";
	    
	    return returnString;
	
	}
	
	public static Entry createFromXml(String xml) throws MalformedXMLException {
		
		try{
			
			Element root = Parser.parse(xml, false);
			Element child = root.getFirstChild();
			
			String uniqueID = root.getAttribute("id");
			long creator = Long.parseLong(root.getAttribute("creator"));
			String title = child.getText();
			String description = root.getAttribute("description");
			String comment = root.getAttribute("comment");
			String startDate = root.getAttribute("start");
			String endDate = root.getAttribute("end");
			int occurance = StringUtils.countMatches(comment, "UID");
			String[] commentArray = comment.split(":"); // split the different comments
			
			Entry xmlEntry = new Entry(uniqueID, creator, title, description, 2); //create Entry

			for(int i = 0; i < occurance; i++){ // go through the commentArray for one comment
				int iter = i*9 + 1; 
				String unique = commentArray[iter];
				String creatorID = commentArray[iter+1];
				long creatorLong = Long.parseLong(creatorID);
				String message = commentArray[iter+2];
				int year = Integer.parseInt(commentArray[iter+3]);
				int month = Integer.parseInt(commentArray[iter+4]);
				int day = Integer.parseInt(commentArray[iter+5]);
				int hour = Integer.parseInt(commentArray[iter+6]);
				int minute = Integer.parseInt(commentArray[iter+7]);
				GregorianCalendar time = new GregorianCalendar(year, month, day, hour, minute);
				Comment aComment = new Comment(unique, creatorLong, time, message);
				xmlEntry.comments.add(aComment); //add the comment to the list of the entry
			}
		
			xmlEntry.start = stringToDate(startDate); //add the dates to the entry
			xmlEntry.end = stringToDate(endDate);
			
			return xmlEntry;
			
		}
		
		catch(Exception e){
 			return null;
		}
	}
	
}
