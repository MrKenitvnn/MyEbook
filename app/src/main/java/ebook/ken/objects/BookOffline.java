package ebook.ken.objects;


public class BookOffline extends Book{

	private int bookIdOnline  = 0;
	private String bookFolder = "";
	private String bookFolderPath = "";
	private String bookOpfPath	  = "";
	private String bookNcxPath	  = "";
	
	public BookOffline(){}

	public String getBookFolder() {
		return bookFolder;
	}

	public BookOffline setBookFolder(String bookFolder) {
		this.bookFolder = bookFolder;
		return this;
	}

	public int getBookIdOnline() {
		return bookIdOnline;
	}

	public BookOffline setBookIdOnline(int bookIdOnline) {
		this.bookIdOnline = bookIdOnline;
		return this;
	}

	public String getBookFolderPath() {
		return bookFolderPath;
	}

	public BookOffline setBookFolderPath(String bookFolder) {
		this.bookFolderPath = bookFolder;
		return this;
	}

	public String getBookOpfPath() {
		return bookOpfPath;
	}

	public BookOffline setBookOpfPath(String bookOpfPath) {
		this.bookOpfPath = bookOpfPath;
		return this;
	}

	public String getBookNcxPath() {
		return bookNcxPath;
	}

	public BookOffline setBookNcxPath(String bookNcxPath) {
		this.bookNcxPath = bookNcxPath;
		return this;
	}
}
