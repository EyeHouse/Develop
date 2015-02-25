/**
 * Description of the class goes here
 *
 * @company EyeHouse Ltd.
 * @version 1.4, 24/02/15
 * @authors Peter
 */


package parser;

public class DocumentInfo {
	
	private String author, version, comment, groupid;
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getGroupID() {
		return groupid;
	}
	
	public void setGroupID(String groupid) {
		this.groupid = groupid;
	}
}
