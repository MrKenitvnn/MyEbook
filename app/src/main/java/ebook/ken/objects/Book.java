package ebook.ken.objects;

import java.io.Serializable;

public abstract class Book implements Serializable{

	protected int bookId			= 0;
	protected String bookName		= "";
	protected String bookAuthor		= "";
	protected String bookCoverPath	= "";
	protected String bookFilePath	= "";

	
	////////////////////////////////////////////////////////////////////////////////
	// TODO getters & setters

	public int getBookId() {
		return bookId;
	}
	
	public Book setBookId(int bookId) {
		this.bookId = bookId;
		return this;
	}
	
	public String getBookName() {
		return bookName;
	}
	
	public Book setBookName(String bookName) {
		this.bookName = bookName;
		return this;
	}
	
	public String getBookAuthor() {
		return bookAuthor;
	}
	
	public Book setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
		return this;
	}
	
	public String getBookCoverPath() {
		return bookCoverPath;
	}
	
	public Book setBookCoverPath(String bookCoverPath) {
		this.bookCoverPath = bookCoverPath;
		return this;
	}
	
	public String getBookFilePath() {
		return bookFilePath;
	}
	
	public Book setBookFilePath(String bookFilePath) {
		this.bookFilePath = bookFilePath;
		return this;
	}
	
	
}
