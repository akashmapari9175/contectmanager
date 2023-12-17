package com.contactmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.contactmanager.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

	@Query("from Contact as u where u.user.id =:userId")
	public List<Contact> getContactByUser(@Param("userId") int userId);
}
