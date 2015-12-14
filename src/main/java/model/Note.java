package main.java.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import main.java.model.base.BaseModel;

public class Note extends BaseModel {

	/**
	 * @Author shreyas patil
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column
	private Long id;
	@NotNull
	@Size(min=1,max=50)
	@Column
	private String noteTitle;
	@Size(max = 1000)
	@Column
	private String noteString;
	@Column
	private Date noteCreatedTime;
	@Column
	private Date noteLastUpdatedTime;
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinTable(
			name="user",
			joinColumns=
			@JoinColumn(name="user_id",referencedColumnName="id"),
			inverseJoinColumns = 
			@JoinColumn(name="group_id",referencedColumnName="id")
	)
	private User user;
	
	public Note(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNoteTitle() {
		return noteTitle;
	}
	@Size(min=1,max=50)
	public void setNoteTitle(String title) {
		this.noteTitle = title;
	}

	public String getNoteString() {
		return noteString;
	}
	@Size(max=1000)
	public void setNoteString(String note) {
		this.noteString = note;
	}

	public Date getNoteCreatedTime() {
		return noteCreatedTime;
	}

	public void setNoteCreatedTime(Date createdTime) {
		this.noteCreatedTime = createdTime;
	}

	public Date getNoteLastUpdatedTime() {
		return noteLastUpdatedTime;
	}

	public void setNoteLastUpdatedTime(Date lastUpdatedTime) {
		this.noteLastUpdatedTime = lastUpdatedTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
