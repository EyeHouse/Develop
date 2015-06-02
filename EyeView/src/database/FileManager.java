package database;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

/**
 * File manager uses JSch in order to implement methods of uploading,
 * downloading and deleting files from our server via sftp.
 * 
 * @version 1.48 (15.03.15)
 * @author Copyright (c) 2015 EyeHouse Ltd. All rights reserved.
 * 
 *         Copyright 2015 EyeHouse
 */
public class FileManager {

	public static Properties props;
	public static final String PREFIX = "stream2file";
	public static final String SUFFIX = ".tmp";
	private static String USERNAME = "ehfile1004";
	private static String PASSWORD = "Jigsaw12!";
	public static String puttY = "127.0.0.1";

	/**
	 * Downloads a file from the database.
	 * 
	 * @param propertiesFilename
	 * @param fileToDownload
	 * @return true on success
	 */
	public boolean downloadFTP(String propertiesFilename, String fileToDownload) {

		// Setup properties
		props = new Properties();

		// Initialise file manager
		StandardFileSystemManager manager = new StandardFileSystemManager();

		try {
			// Sets up details for local file
			props.load(new FileInputStream(propertiesFilename));
			String serverAddress = props.getProperty("serverAddress").trim();
			String remoteDirectory = props.getProperty("remoteDirectory")
					.trim();
			String localDirectory = props.getProperty("localDirectory").trim();

			// Start the file manager
			manager.init();

			// Setup our SFTP configuration
			FileSystemOptions opts = new FileSystemOptions();
			SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(
					opts, "no");
			SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts,
					true);
			SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

			/*
			 * Create the SFTP URI using the host name, userid, password, remote
			 * path and file name.
			 */
			String port = "";

			// Appends URI depending on connection method
			if (Database.url.equals(puttY)) {
				port = ":8022/";
			}

			String sftpUri = "sftp://" + USERNAME + ":" + PASSWORD + "@"
					+ serverAddress + port + "/" + remoteDirectory
					+ fileToDownload;

			// Create local file object
			String filepath = localDirectory + fileToDownload;
			File file = new File(filepath);
			FileObject localFile = manager.resolveFile(file.getAbsolutePath());

			// Create remote file object
			FileObject remoteFile = manager.resolveFile(sftpUri, opts);

			// Copy local file to sftp server
			localFile.copyFrom(remoteFile, Selectors.SELECT_SELF);
			System.out.println("File download successful");

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			manager.close();
		}
		return true;
	}

	/**
	 * Reads a file from the server and returns it in type File
	 * 
	 * @param filepath
	 * @return File
	 * @throws IOException
	 */
	public static File readFile(String filepath) throws IOException {

		// Initialise file manager
		StandardFileSystemManager manager = new StandardFileSystemManager();

		// Create remote file object
		FileObject remoteFile;
		File media = null;
		try {

			// Start the file manager
			manager.init();

			// Setup our SFTP configuration
			FileSystemOptions opts = new FileSystemOptions();
			SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(
					opts, "no");
			SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts,
					true);
			SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

			/*
			 * Create the SFTP URI using the host name, userid, password, remote
			 * path and file name.
			 */
			String port = "";

			// Appends URI depending on connection method
			if (Database.url.equals(puttY)) {
				port = ":8022/";
			}

			String sftpUri = "sftp://" + USERNAME + ":" + PASSWORD + "@"
					+ Database.url + port + "/group/eyeHouse.net/" + filepath;

			System.out.println(sftpUri);

			// Put the file into a type FileContent variable
			remoteFile = manager.resolveFile(sftpUri, opts);
			FileContent temp = remoteFile.getContent();

			// Get the input stream of the type FileContent
			InputStream is = temp.getInputStream();

			// Create a type File from the input stream
			media = stream2file(is);

		} catch (FileSystemException e) {
			e.printStackTrace();
			remoteFile = null;
			System.out.println("File Read: Failed\n remoteFile is null");
		}
		return media;
	}

	/**
	 * Reads an input stream
	 * 
	 * @param filepath
	 * @return
	 * @throws IOException
	 */
	public static InputStream readInputStream(String filepath)
			throws IOException {

		// Initialise file manager
		StandardFileSystemManager manager = new StandardFileSystemManager();

		// Create remote file object
		FileObject remoteFile;
		InputStream is;

		try {

			// Starts the file manager
			manager.init();

			// Setup our SFTP configuration
			FileSystemOptions opts = new FileSystemOptions();
			SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(
					opts, "no");
			SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts,
					true);
			SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

			/*
			 * Create the SFTP URI using the host name, userid, password, remote
			 * path and file name.
			 */
			String port = "";

			// Appends URI depending on connection method
			if (Database.url.equals(puttY)) {
				port = ":8022/";
			}

			String sftpUri = "sftp://" + USERNAME + ":" + PASSWORD + "@"
					+ Database.url + port + "/group/eyeHouse.net/" + filepath;

			// Resolve path
			remoteFile = manager.resolveFile(sftpUri, opts);
			// Get content
			FileContent temp = remoteFile.getContent();

			// Convert to input stream
			is = temp.getInputStream();

		} catch (FileSystemException e) {
			e.printStackTrace();
			is = null;
			System.out.println("File Read: Failed\nFileSystem Exception");
		}
		return is;
	}

	/**
	 * Takes an input stream and converts it into type File
	 * 
	 * @param InputStream
	 * @return File
	 * @throws IOException
	 */
	public static File stream2file(InputStream in) throws IOException {
		// Create temporary file
		final File tempFile = File.createTempFile(PREFIX, SUFFIX);
		tempFile.deleteOnExit();

		// Convert File
		try (FileOutputStream out = new FileOutputStream(tempFile)) {
			IOUtils.copy(in, out);
		}
		return tempFile;
	}

	/**
	 * Read a video from the server.
	 * 
	 * @param userDetails
	 * @param videoDetails
	 * @return File
	 */
	public static File readVideo(User userDetails, HouseVideo videoDetails) {

		String filepath = videoDetails.videoLocation;

		InputStream is;
		File file = null;

		// Read the video into an input stream
		try {
			is = readInputStream(filepath);
		} catch (IOException e) {
			System.out.println("\nInput Stream Failure");
			is = null;
			e.printStackTrace();
		}

		// Convert the input stream to a file
		if (is != null) {
			try {
				file = stream2file(is);
			} catch (IOException e) {
				System.out.println("\nFile Failure");
				e.printStackTrace();
				file = null;
			}
		}
		return file;
	}

	/**
	 * Upload a file to the server.
	 * 
	 * @param userDetails
	 * @param houseDetails
	 * @param localDirectory
	 * @param filename
	 * @return true on success
	 */
	public boolean uploadVideo(User userDetails, House houseDetails,
			String localDirectory, String filename) {

		// Get house ID number for use in location setting
		int hid = Database.getID(userDetails, houseDetails, 2);
		String filepath;

		// Initialise manager
		StandardFileSystemManager manager = new StandardFileSystemManager();

		try {
			// Handle wrong slashes
			if (localDirectory.endsWith("/") || localDirectory.endsWith("\\")) {
				filepath = localDirectory + filename;
			} else {
				filepath = localDirectory + "/" + filename;
			}

			System.out.println("\nLocalfilepath is: " + filepath);

			// New file
			File file = new File(filepath);

			// Check file exists
			if (!file.exists())
				throw new RuntimeException("Error. Local file not found");

			// Starts the file manager
			manager.init();

			// Setup our SFTP configuration
			FileSystemOptions opts = new FileSystemOptions();
			SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(
					opts, "no");
			SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts,
					true);
			SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

			/*
			 * Create the SFTP URI using the host name, userid, password, remote
			 * path and file name.
			 */
			String port = "";

			// Appends URI depending on connection method
			if (Database.url.equals(puttY)) {
				port = ":8022/";
			}

			String sftpUri = "sftp://" + USERNAME + ":" + PASSWORD + "@"
					+ Database.url + port + "/group/eyeHouse.net/eyehouse/"
					+ userDetails.username + "/" + hid + "/" + filename;

			System.out.println(sftpUri);

			// Create local file object
			FileObject localFile = manager.resolveFile(file.getAbsolutePath());

			// Create remote file object
			FileObject remoteFile = manager.resolveFile(sftpUri, opts);

			// Copy local file to sftp server
			remoteFile.copyFrom(localFile, Selectors.SELECT_SELF);
			System.out.println("File upload successful");

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			manager.close();
		}
		return true;
	}

	/**
	 * Deletes a video file from the database.
	 * 
	 * @param videoDetails
	 * @return true on success
	 */
	public static boolean deleteVideo(HouseVideo videoDetails) {

		// Initialise file manager
		StandardFileSystemManager manager = new StandardFileSystemManager();

		try {
			// Starts the file manager
			manager.init();

			// Setup our SFTP configuration
			FileSystemOptions opts = new FileSystemOptions();
			SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(
					opts, "no");
			SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts,
					true);
			SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

			/*
			 * Create the SFTP URI using the host name, userid, password, remote
			 * path and file name.
			 */
			String port = "";

			// Appends URI depending on connection method
			if (Database.url.equals(puttY)) {
				port = ":8022/";
			}

			String sftpUri = "sftp://" + USERNAME + ":" + PASSWORD + "@"
					+ Database.url + port + "/group/eyeHouse.net/"
					+ videoDetails.videoLocation;

			// Create remote file object
			FileObject remoteFile = manager.resolveFile(sftpUri, opts);

			// Check if the file exists
			if (remoteFile.exists()) {
				remoteFile.delete();
				System.out.println("File delete successful");
			} else {
				System.out.println("\nFile does not exist");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			manager.close();
		}

		return true;
	}
}
