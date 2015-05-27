package ebook.ken.objects;


public class BookOffline extends Book{
	private int bookIdOnline = 0;
	
	public BookOffline(){}


	public int getBookIdOnline() {
		return bookIdOnline;
	}

	public BookOffline setBookIdOnline(int bookIdOnline) {
		this.bookIdOnline = bookIdOnline;
		return this;
	}

}
