package com.almis.awe.dao;

import com.almis.awe.model.dto.User;

public interface UserDAO {

	/**
	 * Find user by name. Used in authentication process.
	 * 
	 * @param userName User name
	 * @return <code>UserDetails</code> with credentials. It can be
	 *         <code>null</code> value if user don´t exist.
	 */
	User findByUserName(String userName);
}
