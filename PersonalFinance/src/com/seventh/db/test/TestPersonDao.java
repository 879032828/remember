package com.seventh.db.test;

import java.util.List;

import com.seventh.db.Person;
import com.seventh.db.PersonDBdao;

import android.test.AndroidTestCase;

public class TestPersonDao extends AndroidTestCase {

	public void testfind() throws Exception {
		PersonDBdao dao = new PersonDBdao(getContext());
		boolean result = dao.find("admin1");
		assertEquals(true, result);
	}

	public void testfindLogin() throws Exception {
		PersonDBdao dao = new PersonDBdao(getContext());
		boolean result = dao.findLogin("admin", "123456");
		assertEquals(true, result);
	}

	public void testAdd() throws Exception {
		PersonDBdao dao = new PersonDBdao(getContext());
		String a;
		for (int i = 0; i < 10; i++) {
			a = "admin" + i;
			dao.add(a, "123456");
		}

	}

	public void testDelete() throws Exception {
		PersonDBdao dao = new PersonDBdao(getContext());
		dao.delete("admin9");
	}

	public void testUpdate() throws Exception {
		PersonDBdao dao = new PersonDBdao(getContext());
		dao.update("admin8", "admin0", "123456");
	}

	public void testfindall() throws Exception {
		PersonDBdao dao = new PersonDBdao(getContext());
		List<Person> persons = dao.findAll();
		for (Person person : persons) {
			System.out.println(person.getName());
			System.out.println(person.isLogin());
		}
	}

	public void testfindloginok() throws Exception {
		PersonDBdao dao = new PersonDBdao(getContext());
		Person person = dao.findLoginOk();

		if(person==null){
			
		}else{
		System.out.println(person.getName());
		System.out.println(person.isLogin());
		}

	}

}
