package ebook.ken.objects;

public class BookOnline extends ebook.ken.objects.Book {
	
	
	private String bookDesciption	= "";
	private float bookRate			= 0;
	private int bookTotalView		= 0;
	private int bookTotalDownload	= 0;
	private int bookSectionId		= 0;
	

	////////////////////////////////////////////////////////////////////////////////
	// TODO constructor
	
	public BookOnline() {
	}
	

	////////////////////////////////////////////////////////////////////////////////
	// TODO getters & setters
	
	public String getBookDesciption() {
		return bookDesciption;
	}
	
	public BookOnline setBookDesciption(String bookDesciption) {
		this.bookDesciption = bookDesciption;
		return this;
	}
	
	public float getBookRate() {
		return bookRate;
	}
	
	public BookOnline setBookRate(float bookRate) {
		this.bookRate = bookRate;
		return this;
	}
	
	public int getBookTotalView() {
		return bookTotalView;
	}
	
	public BookOnline setBookTotalView(int bookTotalView) {
		this.bookTotalView = bookTotalView;
		return this;
	}
	
	public int getBookTotalDownload() {
		return bookTotalDownload;
	}
	
	public BookOnline setBookTotalDownload(int bookTotalDownload) {
		this.bookTotalDownload = bookTotalDownload;
		return this;
	}

	public int getBookSectionId() {
		return bookSectionId;
	}

	public BookOnline setBookSectionId(int bookSectionId) {
		this.bookSectionId = bookSectionId;
		return this;
	}
	
	
	
}
