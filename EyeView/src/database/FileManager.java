package database;

import java.awt.BorderLayout;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

public class FileManager {

	static Properties props;

	public boolean downloadFTP(String propertiesFilename, String fileToDownload) {

		props = new Properties();
		StandardFileSystemManager manager = new StandardFileSystemManager();

		try {

			props.load(new FileInputStream(propertiesFilename));
			String serverAddress = props.getProperty("serverAddress").trim();
			String userId = props.getProperty("userId").trim();
			String password = props.getProperty("password").trim();
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
			String sftpUri = "sftp://" + userId + ":" + password + "@"
					+ serverAddress + "/" + remoteDirectory + fileToDownload;

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

	public boolean uploadFTP(String propertiesFilename, String fileToFTP) {

		props = new Properties();
		StandardFileSystemManager manager = new StandardFileSystemManager();

		try {

			props.load(new FileInputStream(propertiesFilename));
			String serverAddress = props.getProperty("serverAddress").trim();
			String userId = props.getProperty("userId").trim();
			String password = props.getProperty("password").trim();
			String remoteDirectory = props.getProperty("remoteDirectory")
					.trim();
			String localDirectory = props.getProperty("localDirectory").trim();

			// check if the file exists
			String filepath = localDirectory + fileToFTP;
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
			String sftpUri = "sftp://" + userId + ":" + password + "@"
					+ serverAddress + "/" + remoteDirectory + fileToFTP;

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

	public boolean deleteFTP(String propertiesFilename, String fileToDelete) {

		props = new Properties();
		StandardFileSystemManager manager = new StandardFileSystemManager();

		try {

			props.load(new FileInputStream(propertiesFilename));
			String serverAddress = props.getProperty("serverAddress").trim();
			String userId = props.getProperty("userId").trim();
			String password = props.getProperty("password").trim();
			String remoteDirectory = props.getProperty("remoteDirectory")
					.trim();

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
			String sftpUri = "sftp://" + userId + ":" + password + "@"
					+ serverAddress + "/" + remoteDirectory + fileToDelete;

			// Create remote file object
			FileObject remoteFile = manager.resolveFile(sftpUri, opts);

			// Check if the file exists
			if (remoteFile.exists()) {
				remoteFile.delete();
				System.out.println("File delete successful");
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

		int mode = 4;
		String propertiesFile = "D://EE course/SWEng/Java/Server Tool/properties.txt";
		
		switch (mode) {
		case 1:
			FileManager update = new FileManager();
			String uploadFile = "Disco1.jpg";
			update.uploadFTP(propertiesFile, uploadFile);
			break;
		case 2:

			FileManager download = new FileManager();
			String downloadFile = "Disco1.jpg";
			download.downloadFTP(propertiesFile, downloadFile);
			break;
		// delete
		case 3:
			FileManager delete = new FileManager();
			String deleteFile = "Disco1.jpg";
			delete.deleteFTP(propertiesFile, deleteFile);
			break;
		default:
			break;
		}

	}
}
