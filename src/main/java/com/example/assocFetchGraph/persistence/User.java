package com.example.assocFetchGraph.persistence;

import javax.persistence.*;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Benjamin Maurer (maurer.benjamin@gmail.com)
 * @since 22.07.2020
 */
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Version
	private Long version;

	@Enumerated(EnumType.STRING)
	@ElementCollection(targetClass = Permission.class)
	@CollectionTable(name = "USERS_PERMISSIONS", joinColumns = @JoinColumn(name = "uid"))
	private Set<Permission> permissions = EnumSet.of(Permission.FOO);

	@ManyToMany(fetch = FetchType.LAZY)
	private Set<Group> groups = new HashSet<>();

	public User() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}
	public void addPermission(Permission p) {
		this.permissions.add(p);
	}

	public Set<Group> getGroups() {
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	public void addGroup(Group g) {
		this.groups.add(g);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof User))
			return false;

		User other = (User) o;

		return id != null &&
				id.equals(other.getId());
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				'}';
	}
}