package in.ankit.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Table(name = "USER")
@Data
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@NotBlank(message = "Full Name field is required")
	@Size(min = 2, max = 20, message = "Min 2 and Max 20 characters are allowed")
	private String name;

	@NotBlank(message = "Email field is required")
	@Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Valid Email example : john@gmail.com")
	@Column(name = "EMAIL_ID", unique = true)
	private String email;

	@NotBlank(message = "Password field is required")
	private String password;
	
	@NotBlank(message = "Confirm Password field is required")
	private String confirmPassword;
	
	private String role;
	private String status;
	private boolean enabled;
	
	@CreationTimestamp
	@Column(name = "CREATED_DATE", updatable = false)
	private LocalDate createdDate;

	@UpdateTimestamp
	@Column(name = "UPDATED_DATE", insertable = false)
	private LocalDate updatedDate;
	
	@Column(name = "DEACTIVATED_DATE")
	private LocalDate deactivatedDate;

}
