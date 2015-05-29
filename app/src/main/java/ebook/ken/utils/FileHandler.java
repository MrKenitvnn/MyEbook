package ebook.ken.utils;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@SuppressLint("DefaultLocale")
public class FileHandler {

	public static final String
							ROOT_FOLDER	 = "myEbookEpub",
							DATA_FOLDER  = "data",
							COVER_FOLDER = "covers",
							EPUB_FOLDER  = "epubs";

	public static String ROOT_PATH	= Environment.getExternalStorageDirectory().getAbsolutePath()
												+ File.separator + ROOT_FOLDER + File.separator;

	public static String DATA_PATH	= ROOT_PATH + DATA_FOLDER + File.separator;
	public static String EPUB_PATH	= DATA_PATH + EPUB_FOLDER + File.separator;
	public static String COVER_PATH	= DATA_PATH + COVER_FOLDER + File.separator;

	public static String ncxPath;
	public static String contentPath;
	public static String coverPath;
	public static String chapterPath;


	////////////////////////////////////////////////////////////////////////////////
	
	public static void createRootFolder() {
		try {
			if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				Log.d(">>> ken <<<", "No SDCARD");
			} else {

				// root's app's folder
				File directory = new File(ROOT_PATH);

				if (directory.exists()) {
					deleteFolderFromSdcard(directory);
				}

				directory.mkdir();

				// data's app's folder
				File data = new File(DATA_PATH);
				data.mkdir();

				// cover's app's folder
				File covers = new File(COVER_PATH);

				// epub's app's folder
				File epubs = new File(EPUB_PATH);
				covers.mkdir();
				epubs.mkdir();
			}// end-if
		} catch ( Exception ex){
			Log.d( ">>> ken <<<", Log.getStackTraceString(ex) );
		}
	}// end-func createRootFolder


	///////////////////////////////////////////////////////////////////////////////

	public static String createBookFolder(String _bookFolderName) {

		try {
			File aBook = new File(EPUB_PATH + _bookFolderName);
			aBook.mkdirs();
		} catch ( Exception ex){
			Log.d( ">>> ken <<<", Log.getStackTraceString(ex) );
			return  null;
		}
		return EPUB_PATH + _bookFolderName + File.separator;

	}// end-func createBookFolder


	////////////////////////////////////////////////////////////////////////////////

	public static void writeData(String str_data, String file_name) {
		
		FileOutputStream fOut			= null;
		OutputStreamWriter myOutWriter	= null;
		try {
			File myFile = new File(ROOT_PATH + DATA_FOLDER + File.separator + file_name);
			
			myFile.createNewFile();
			
			fOut		= new FileOutputStream(myFile, false);
			myOutWriter	= new OutputStreamWriter(fOut);
			
			myOutWriter.append(str_data);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				myOutWriter.close();
				fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}// end-try

	}// end-func writeData


	////////////////////////////////////////////////////////////////////////////////

	public static boolean deleteFolderFromSdcard(File path) {
		
		if (path.exists()) {
			File[] files = path.listFiles();
			if (files == null) {
				return true;
			}// end-if
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteFolderFromSdcard(files[i]);
				} else {
					files[i].delete();
				}// end-if
			}// end-for
		}// end-if
		return (path.delete());

	}// end-func deleteBookFolder


	public static boolean deleteFileFromSdcard(String path){
		File file = new File(path);
		boolean deleted = file.delete();
		return deleted;
	}// end-func deleteFileFromSdcard


	////////////////////////////////////////////////////////////////////////////////

	static public void doUnzip(String inputZip, String destinationDirectory)
			throws IOException {

		int BUFFER = 2048;
		List zipFiles = new ArrayList();

//		File tmp = new File(inputZip);
//		tmp.renameTo( new File(inputZip.substring(0, inputZip.lastIndexOf(".")) + ".zip" ) );
//		File sourceZipFile = new File(inputZip.substring(0, inputZip.lastIndexOf(".")) + ".zip" );

		File sourceZipFile = new File(inputZip);
		File unzipDestinationDirectory = new File(destinationDirectory);
		unzipDestinationDirectory.mkdir();

		ZipFile zipFile;
		// Open Zip file for reading
		zipFile = new ZipFile(sourceZipFile, ZipFile.OPEN_READ);

		// Create an enumeration of the entries in the zip file
		Enumeration zipFileEntries = zipFile.entries();

		// Process each entry
		while (zipFileEntries.hasMoreElements()) {
			// grab a zip file entry
			ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();

			String currentEntry = entry.getName();

			File destFile = new File(unzipDestinationDirectory, currentEntry);

			if (currentEntry.endsWith(".zip")) {
				zipFiles.add(destFile.getAbsolutePath());
			}

			// grab file's parent directory structure
			File destinationParent = destFile.getParentFile();

			// create the parent directory structure if needed
			destinationParent.mkdirs();

			try {
				// extract file if not a directory
				if (!entry.isDirectory()) {
					BufferedInputStream is =
							new BufferedInputStream(zipFile.getInputStream(entry));
					int currentByte;
					// establish buffer for writing file
					byte data[] = new byte[BUFFER];

					// write the current file to disk
					FileOutputStream fos = new FileOutputStream(destFile);
					BufferedOutputStream dest =
							new BufferedOutputStream(fos, BUFFER);

					// read and write until last byte is encountered
					while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
						dest.write(data, 0, currentByte);
					}
					dest.flush();
					dest.close();
					is.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		zipFile.close();

		for (Iterator iter = zipFiles.iterator(); iter.hasNext();) {
			String zipName = (String)iter.next();
			doUnzip(
					zipName,
					destinationDirectory +
							File.separatorChar +
							zipName.substring(0,zipName.lastIndexOf(".zip"))
			);
		}

	}


	////////////////////////////////////////////////////////////////////////////////

	// tìm kiếm file trong thư mục
	public static void findChapterPath(String name, String _pathFoler) {
		
		File directory = new File(_pathFoler);
		File[] list = directory.listFiles();
		if (list != null) {
			for (File fil : list) {
				if (fil.isDirectory()) {
					findChapterPath(name, _pathFoler + "\\" + fil.getName());
				} else if (name.equalsIgnoreCase(fil.getName())) {
					chapterPath = fil.getPath();
				}
			}
		}
	}


	////////////////////////////////////////////////////////////////////////////////

	public static String getChapterPath(File dir, String chapter_name) {
		File listFile[] = dir.listFiles();
		if (listFile != null && listFile.length > 0) {
			for (int i = 0; i < listFile.length; i++) {

				if (listFile[i].isDirectory()) {
					getChapterPath(listFile[i], chapter_name);
				} else {
					if (listFile[i].getName().equals(chapter_name)) {
						chapterPath = listFile[i].getPath();
					}
				}
			}
		}
		return chapterPath;
	}


	////////////////////////////////////////////////////////////////////////////////

	// get path file Toc.ncx
	@SuppressLint("DefaultLocale")
	public static String getNcxFilePath(String direct) {
		File dir = new File(direct);
		File listFile[] = dir.listFiles();
		if (listFile != null && listFile.length > 0) {
			for (int i = 0; i < listFile.length; i++) {

				if (listFile[i].isDirectory()) {
					// fileList.add(listFile[i]);
					getNcxFilePath(direct + "/" + listFile[i].getName());
				} else {
					if (listFile[i].getName().toLowerCase().equals("toc.ncx")) {
						// fileList.add(listFile[i]);
						ncxPath = listFile[i].getPath();
					}
				}
			}
		}
		return ncxPath;
	}


	////////////////////////////////////////////////////////////////////////////////

	// get path file content.opf
	public static String getContentFilePath(String direct) {
		
		File dir = new File(direct);
		File listFile[] = dir.listFiles();
		if (listFile != null && listFile.length > 0) {
			for (int i = 0; i < listFile.length; i++) {
				if (listFile[i].isDirectory()) {
					getContentFilePath(direct + "/" + listFile[i].getName());
				} else {
					if (listFile[i].getName().toLowerCase().equals("content.opf")) {
						contentPath = listFile[i].getPath();
					}// end-if
				}// end-if
			}// end-for
		}// end-if
		
		return contentPath;
	}


	////////////////////////////////////////////////////////////////////////////////

	// get chapter path
	public static String getChapterFilePath(String direct, String chapter_name) {
		
		File dir = new File(direct);
		File listFile[] = dir.listFiles();
		if (listFile != null && listFile.length > 0) {
			for (int i = 0; i < listFile.length; i++) {
				if (listFile[i].isDirectory()) {
					getChapterFilePath(direct + "/" + listFile[i].getName(),
							chapter_name);
				} else {
					if (listFile[i].getName().toLowerCase()
							.equals(chapter_name)) {
						chapterPath = listFile[i].getPath();
					}// end-if
				}// end-if
			}// end-for
		}// end-if
		
		return chapterPath;
	}


	////////////////////////////////////////////////////////////////////////////////

	// get path file cover
	public static String getCoverFilePath(String direct) {
		
		File dir = new File(direct);
		File listFile[] = dir.listFiles();
		if (listFile != null && listFile.length > 0) {
			for (int i = 0; i < listFile.length; i++) {
				if (listFile[i].isDirectory()) {
					getCoverFilePath(direct + "/" + listFile[i].getName());
				} else {
					if (listFile[i].getName().startsWith("cover")) {
						coverPath = listFile[i].getPath();
					}// end-if
				}// end-if
			}// end-for
		}// end-if
		
		return coverPath;
	}// end-func getCoverFilePath


	////////////////////////////////////////////////////////////////////////////////

	public static String getLastTokenizer(String string, String delimiters) {
		
		StringTokenizer tokens = new StringTokenizer(string, delimiters);
		String result = "";
		while (tokens.hasMoreTokens()) {
			result = tokens.nextToken();
		}// end-while

		StringTokenizer newToken = new StringTokenizer(result, "#");
		if (newToken.hasMoreTokens()) {
			result = newToken.nextToken();
		}// end-if

		return result;
	}
}
