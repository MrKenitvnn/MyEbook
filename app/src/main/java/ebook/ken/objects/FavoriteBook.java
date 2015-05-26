package ebook.ken.objects;

public class FavoriteBook {
	
	private int favoriteId		= 0;
	private int bookOfflineId	= 0;

	
	////////////////////////////////////////////////////////////////////////////////
	
	// constructor
	public FavoriteBook() {
	}
	

	////////////////////////////////////////////////////////////////////////////////
	
	// getters & setters
	public int getFavoriteId() {
		return favoriteId;
	}
	
	public FavoriteBook setFavoriteId(int favoriteId) {
		this.favoriteId = favoriteId;
		return this;
	}
	
	public int getBookOfflineId() {
		return bookOfflineId;
	}
	
	public FavoriteBook setBookOfflineId(int bookOfflineId) {
		this.bookOfflineId = bookOfflineId;
		return this;
	}
	
}
