package ebook.ken.objects;

public class Chapter {
	
	private int chapterId			 = 0;
	private int bookId				 = 0;
	private String chapterTitle		 = "";
	private String chapterContent	 = "";
	private int chapterPercent		 = 0;

	
	////////////////////////////////////////////////////////////////////////////////
	
	// getters & setters
	public int getChapterId() {
		return chapterId;
	}
	
	public Chapter setChapterId(int chapterId) {
		this.chapterId = chapterId;
		return this;
	}
	
	public int getBookId() {
		return bookId;
	}
	
	public Chapter setBookId(int bookId) {
		this.bookId = bookId;
		return this;
	}
	
	public String getChapterTitle() {
		return chapterTitle;
	}
	
	public Chapter setChapterTitle(String chapterTitle) {
		this.chapterTitle = chapterTitle;
		return this;
	}
	
	public String getChapterContent() {
		return chapterContent;
	}
	
	public Chapter setChapterContent(String chapterContent) {
		this.chapterContent = chapterContent;
		return this;
	}
	
	public int getChapterPercent() {
		return chapterPercent;
	}
	
	public Chapter setChapterPercent(int chapterPercent) {
		this.chapterPercent = chapterPercent;
		return this;
	}
	
}
