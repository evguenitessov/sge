package model;

import static javax.persistence.InheritanceType.SINGLE_TABLE;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;

@Entity
@Inheritance(strategy = SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType=DiscriminatorType.INTEGER)
public class Usuario {	

	@Id
	@Column(length = 50)
	private String username;
	
	@Column(nullable = false, length = 200)
	private String password;

	
	public Usuario(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean passwordMatches(String password) {
		return this.password.equals(password);
	}
	
	public Usuario() {}
	
	public boolean esAdmin() {
		return true;
	}

}
