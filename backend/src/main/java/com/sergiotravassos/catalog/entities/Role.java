package com.sergiotravassos.catalog.entities;

import java.io.Serializable;
import java.util.Objects;

public class Role implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long id;
	private String authority;
	
	public Role() {
		
	}

	public Role(long id, String authority) {
		this.id = id;
		this.authority = authority;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		return id == other.id;
	}

}
