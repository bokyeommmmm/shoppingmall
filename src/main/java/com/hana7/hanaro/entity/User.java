package com.hana7.hanaro.entity;


import org.hibernate.annotations.ColumnDefault;

import com.hana7.hanaro.enums.USERROLE;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class User extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="userName",length = 30,nullable = false,unique = true)
	private String userName;

	@Column(name="email",length = 30,nullable = false,unique = true)
	private String email;

	@Column(name="password",length = 60,nullable = false,unique = false)
	private String password;
	@Column(name="role",nullable = false)
	@Enumerated(EnumType.STRING)
	@ColumnDefault("'ROLE_USER'")
	private USERROLE role = USERROLE.ROLE_USER;

	@OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
	private Cart cart;
}
