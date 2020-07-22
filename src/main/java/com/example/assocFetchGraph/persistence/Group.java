package com.example.assocFetchGraph.persistence;

import javax.persistence.*;
import java.util.EnumSet;
import java.util.Set;

/**
 * @author Benjamin Maurer (maurer.benjamin@gmail.com)
 * @since 22.07.2020
 */
@Entity
@NamedEntityGraph(name = Group.ENTITY_GRAPH,
		attributeNodes = {
				@NamedAttributeNode("permissions")
		})
@Table(name = "groups") // Name 'group' not accepted by H2
public class Group {
	public static final String ENTITY_GRAPH = "group-with-permissions";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Version
	private Long version;

	@Enumerated(EnumType.STRING)
	@ElementCollection(targetClass = Permission.class)
	@CollectionTable(
			name = "GROUPS_PERMISSIONS",
			joinColumns = @JoinColumn(name = "gid")
	)
	private Set<Permission> permissions = EnumSet.noneOf(Permission.class);

	public Group() {}

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof Group))
			return false;

		Group other = (Group) o;

		return id != null &&
				id.equals(other.getId());
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public String toString() {
		return "Group{" +
				"id=" + id +
				'}';
	}
}