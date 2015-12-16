package main.java.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import main.java.model.base.BaseModel;

/**
 * A User class with below fields
 * 1. Id
 * 2. User Email
 * 3. User Password
 * 4. User Created Time
 * 5. User Last Updated Date
 * 6. List of Notes
 * @Author shreyas patil
 */
public class User extends BaseModel {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column
	private Long id;
	@NotNull
	@Email
	@Column
	private String userEmail;
	@Size(min = 8)
	@NotNull
	@Column
	private String emailPassword;
	@Column
	private Date userCreatedTime;
	@Column
	private Date userLastUpdatedTime;
	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name="userId")
	private List<Note> notes;
	
	public User(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String email) {
		this.userEmail = email;
	}

	public String getEmailPassword() {
		return emailPassword;
	}

	public void setEmailPassword(String password) {
		this.emailPassword = password;
	}

	public Date getUserCreatedTime() {
		return userCreatedTime;
	}

	public void setUserCreatedTime(Date createdTime) {
		this.userCreatedTime = createdTime;
	}

	public Date getUserLastUpdatedTime() {
		return userLastUpdatedTime;
	}

	public void setUserLastUpdatedTime(Date lastUpdatedTime) {
		this.userLastUpdatedTime = lastUpdatedTime;
	}

	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}
}
