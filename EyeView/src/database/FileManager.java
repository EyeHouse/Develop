package database;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

public class FileManager {

	public static Properties props;
	public static final String PREFIX = "stream2file";
	public static final String SUFFIX = ".tmp";
	private static String USERNAME = "ehfile1004";
	private static String PASSWORD = "Jigsaw12!";

	public boolean downloadFTP(String propertiesFilename, String fileToDownload) {

		props = new Properties();
		StandardFileSystemManager manager = new StandardFileSystemManager();

		try {

			props.load(new FileInputStream(propertiesFilename));
			String serverAddress = props.getProperty("serverAddress").trim();
			String remoteDirectory = props.getProperty("remoteDirectory")
					.trim();
			String localDirectory = props.getProperty("localDirectory").trim();

			// Initializes the file manager
			manager.init();

			// Setup our SFTP configuration
			FileSystemOptions opts = new FileSystemOptions();
			SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(
					opts, "no");
			SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts,
					true);
			SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

			// Create the SFTP URI using the host name, userid, password, remote
			// path and file name
			String sftpUri = "sftp://" + USERNAME + ":" + PASSWORD + "@"
					+ serverAddress + ":22/" + "/" + remoteDirectory
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

	public static File readFile(String filepath) throws IOException {
		StandardFileSystemManager manager = new StandardFileSystemManager();
		// Create remote file object
		FileObject remoteFile;
		File media = null;
		try {
			// Initializes the file manager
			manager.init();
			// Setup our SFTP configuration
			FileSystemOptions opts = new FileSystemOptions();
			SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(
					opts, "no");
			SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts,
					true);
			SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

			// Create the SFTP URI using the host name, userid, password, remote
			// path and file name
			String sftpUri = "sftp://" + USERNAME + ":" + PASSWORD + "@"
					+ Database.url + ":22//" + "group/eyeHouse.net/"
					+ filepath;
			remoteFile = manager.resolveFile(sftpUri, opts);
			FileContent temp = remoteFile.getContent();
			InputStream is = temp.getInputStream();
			media = stream2file(is);
		} catch (FileSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			remoteFile = null;
			System.out.println("File Read: Failed\n remoteFile is null");
		}
		return media;
	}

	public static InputStream readInputStream(String filepath)
			throws IOException {

		StandardFileSystemManager manager = new StandardFileSystemManager();
		// Create remote file object
		FileObject remoteFile;
		InputStream is;

		try {
			// Initializes the file manager
			manager.init();
			// Setup our SFTP configuration
			FileSystemOptions opts = new FileSystemOptions();
			SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(
					opts, "no");
			SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts,
					true);
			SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

			// Create the SFTP URI using the host name, userid, password, remote
			// path and file name
			String sftpUri = "sftp://" + USERNAME + ":" + PASSWORD + "@"
					+ Database.dbConnect() + ":22//" + "group/eyeHouse.net/"
					+ filepath;

			remoteFile = manager.resolveFile(sftpUri, opts);
			FileContent temp = remoteFile.getContent();
			is = temp.getInputStream();
		} catch (FileSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			is = null;
			System.out.println("File Read: Failed\nFileSystem Exception");
		}
		return is;
	}

	public static File stream2file(InputStream in) throws IOException {
		final File tempFile = File.createTempFile(PREFIX, SUFFIX);
		tempFile.deleteOnExit();
		try (FileOutputStream out = new FileOutputStream(tempFile)) {
			IOUtils.copy(in, out);
		}
		return tempFile;
	}

	public static File readVideo(User userDetails, HouseVideo videoDetails) {

		String filepath = videoDetails.videoLocation;

		InputStream is;
		File file = null;

		try {
			is = readInputStream(filepath);
		} catch (IOException e) {
			System.out.println("\nInput Stream Failure");
			is = null;
			e.printStackTrace();
		}

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

	public boolean uploadVideo(User userDetails, House houseDetails,
			String localDirectory, String filename) {

		// Get house ID number for use in location setting
		int hid = Database.getID(userDetails, houseDetails, 2);
		String filepath;

		StandardFileSystemManager manager = new StandardFileSystemManager();

		try {
			// check if the file exists
			if (localDirectory.endsWith("/") || localDirectory.endsWith("\\")) {
				filepath = localDirectory + filename;
			} else {
				filepath = localDirectory + "/" + filename;
			}

			System.out.println("\nLocalfilepath is: " + filepath);

			File file = new File(filepath);

			if (!file.exists())
				throw new RuntimeException("Error. Local file not found");

			// Initializes the file manager
			manager.init();

			// Setup our SFTP configuration
			FileSystemOptions opts = new FileSystemOptions();
			SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(
					opts, "no");
			SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts,
					true);
			SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

			// Create the SFTP URI using the host name, userid, password, remote
			// path and file name

			String sftpUri = "sftp://" + USERNAME + ":" + PASSWORD + "@"
					+ Database.dbConnect() + ":22//" + "group/eyeHouse.net/eyehouse/"
					+ userDetails.username + "/" + hid + "/" + filename;

			// upload a default image
			/*
			 * String sftpUri = "sftp://" + USERNAME + ":" + PASSWORD + "@" +
			 * Database.url + ":8080//" +
			 * "group/eyeHouse.net/eyehouse/defaults/" + filename;
			 */

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

	public static boolean deleteVideo(HouseVideo videoDetails) {

		StandardFileSystemManager manager = new StandardFileSystemManager();

		String url = Database.dbConnect();

		try {
			// Initializes the file manager
			manager.init();

			// Setup our SFTP configuration
			FileSystemOptions opts = new FileSystemOptions();
			SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(
					opts, "no");
			SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts,
					true);
			SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

			// Create the SFTP URI using the host name, userid, password, remote
			// path and file name
			String sftpUri = "sftp://" + USERNAME + ":" + PASSWORD + "@" + url
					+ ":22//" + "group/eyeHouse.net/"
					+ videoDetails.videoLocation;

			// String sftpUri = "sftp://tb77931004:72dw42WRq!2345@" + url +
			// "/" + videoDetails.videoLocation;

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

	public static void main(String[] args) throws Exception {

		int mode = 1;
		String propertiesFile = "D://EE course/SWEng/Java/Server Tool/properties.txt";
		String filename = "default_profpic.jpg";
		String localDirectory = "D:/EE course/SWEng/Java/";

		Database.dbConnect();

		User tempu68 = Database.getUser("MVPTom");
		House temph68 = Database.getHouse(8);

		switch (mode) {
		case 1:
			FileManager update = new FileManager();
			update.uploadVideo(tempu68, temph68, localDirectory, filename);
			break;
		case 2:
			FileManager download = new FileManager();
			String downloadFile = "Disco1.jpg";
			download.downloadFTP(propertiesFile, downloadFile);
			break;
		// delete
		case 3:
			// FileManager delete = new FileManager();
			// String deleteFile = "Disco1.jpg";
			// delete.deleteVideo(propertiesFile, deleteFile);
			break;
		case 4:
			File picture = null;
			picture = readFile("eyehouse/defaults/default_profpic.jpg");
			BufferedImage image = null;
			System.out.println(picture);
			try {
				image = ImageIO.read(picture);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
			JFrame frame = new JFrame();
			JLabel label = new JLabel(new ImageIcon(image));
			frame.getContentPane().add(label, BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);
			break;
		default:
			break;

		}
	}
}
