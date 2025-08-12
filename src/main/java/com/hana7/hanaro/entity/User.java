package com.hana7.hanaro.entity;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.hana7.hanaro.enums.USERROLE;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@DynamicInsert
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

	@OneToMany(
		mappedBy = "user",
		cascade = CascadeType.ALL
	)
	@Builder.Default
	@ToString.Exclude
	private List<Orders> orders = new ArrayList<>();
}
