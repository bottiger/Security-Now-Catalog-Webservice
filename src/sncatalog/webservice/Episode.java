package sncatalog.webservice;

public class Episode{
	
	private String Title;
	private String Link;
	private String Description;
	private String Author;
	private String pubDate;
	private String Category;
	private String Comments;
	private String Enclosure;
	private String Guid;
	private String Subtitle;
	private String Summary;
	private String Duration;

	// FIXME String[] category
	public Episode(String title, String link, String description,
			String author, String pubDate, String category, String comments,
			String enclosure, String guid, String subtitle, String summary,
			String duration) {
		this.Title = title;
		this.Link = link;
		this.Description = description;
		this.Author = author;
		this.pubDate = pubDate;
		this.Category = category;
		this.Comments = comments;
		this.Enclosure = enclosure;
		this.Guid = guid;
		this.Subtitle = subtitle;
		this.Summary = summary;
		this.Duration = duration;
	}
	
	

	public String getTitle() {
		return Title;
	}

	public String getLink() {
		return Link;
	}

	public String getDescription() {
		return Description;
	}

	public String getAuthor() {
		return Author;
	}

	public String getPubDate() {
		return pubDate;
	}

	public String getCategory() {
		return Category;
	}

	public String getComments() {
		return Comments;
	}

	public String getEnclosure() {
		return Enclosure;
	}

	public String getGuid() {
		return Guid;
	}

	public String getSubtitle() {
		return Subtitle;
	}

	public String getSummary() {
		return Summary;
	}

	public String getDuration() {
		return Duration;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public void setLink(String link) {
		Link = link;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public void setAuthor(String author) {
		Author = author;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public void setCategory(String category) {
		Category = category;
	}

	public void setComments(String comments) {
		Comments = comments;
	}

	public void setEnclosure(String enclosure) {
		Enclosure = enclosure;
	}

	public void setGuid(String guid) {
		Guid = guid;
	}

	public void setSubtitle(String subtitle) {
		Subtitle = subtitle;
	}

	public void setSummary(String summary) {
		Summary = summary;
	}

	public void setDuration(String duration) {
		Duration = duration;
	}

}
