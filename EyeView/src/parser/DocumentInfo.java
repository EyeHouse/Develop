package parser;

/**
 * This class creates a new object of type <code>DocumentInfo</code>. It
 * contains the information about the slideshow, including the author, version,
 * the group ID, and a brief comment about the document's content.
 * 
 * @version 2.1
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 */
public class DocumentInfo {

	private String author, version, comment, groupid;

	/**
	 * Returns the author of the slideshow.
	 * 
	 * @param author
	 *            Author/Company name
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * Sets the author of the slideshow.
	 * 
	 * @return Author/Company name
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Sets the version number of the slideshow.
	 * 
	 * @param version
	 *            Version of slideshow document
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Sets the version number of the slideshow.
	 * 
	 * @return Version of slideshow document
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets the comment giving a brief description of the slideshow.
	 * 
	 * @param comment
	 *            Slideshow description
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * Returns the comment giving a brief description of the slideshow.
	 * 
	 * @return Slideshow description
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Sets the group ID for this slideshow's group.
	 * 
	 * @param groupid
	 *            Slideshow's group ID
	 */
	public void setGroupID(String groupid) {
		this.groupid = groupid;
	}

	/**
	 * Returns the group ID for this slideshow's group.
	 * 
	 * @return Slideshow's group ID
	 */
	public String getGroupID() {
		return groupid;
	}
}
