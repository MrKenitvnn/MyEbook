package ebook.ken.objects;

public class BookFavorite {
	
	private int favoriteId		= 0;
	private int bookOfflineId	= 0;

	
	////////////////////////////////////////////////////////////////////////////////
	
	// constructor
	public BookFavorite() {
	}
	

	////////////////////////////////////////////////////////////////////////////////
	
	// getters & setters
	public int getFavoriteId() {
		return favoriteId;
	}
	
	public BookFavorite setFavoriteId(int favoriteId) {
		this.favoriteId = favoriteId;
		return this;
	}
	
	public int getBookOfflineId() {
		return bookOfflineId;
	}
	
	public BookFavorite setBookOfflineId(int bookOfflineId) {
		this.bookOfflineId = bookOfflineId;
		return this;
	}
	
}
