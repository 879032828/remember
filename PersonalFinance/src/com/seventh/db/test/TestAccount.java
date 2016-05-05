package com.seventh.db.test;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import com.seventh.db.Account;
import com.seventh.db.AccountDBdao;

import android.test.AndroidTestCase;

public class TestAccount extends AndroidTestCase {
	public void testfind() throws Exception {
		AccountDBdao dao = new AccountDBdao(getContext());
		boolean result = dao.find("admin1");
		assertEquals(true, result);
	}

	public void testAdd() throws Exception {
		AccountDBdao dao = new AccountDBdao(getContext());
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00")); // 获取东八区时间
		int year = c.get(Calendar.YEAR); // 获取年
		int month = c.get(Calendar.MONTH) + 1; // 获取月份，0表示1月份
		int day = c.get(Calendar.DAY_OF_MONTH); // 获取当前天数
		String time = year + "/" + month + "/" + day;// 获取系统当前时间
		for (int i = 0; i < 5; i++) {
			dao.add(time, 100, "衣", false, "", "admin1");
		}

	}

	public void testDelete() throws Exception {
		AccountDBdao dao = new AccountDBdao(getContext());
		dao.delete("1");
	}

	public void testUpdate() throws Exception {
		AccountDBdao dao = new AccountDBdao(getContext());
		String time = 2014 + "/" + 5 + "/" + 18;// 获取系统当前时间
		dao.update("1", time, 123, "衣", true, "");
	}

	public void testfindall() throws Exception {
		AccountDBdao dao = new AccountDBdao(getContext());
		List<Account> accounts = dao.findAll();
		// assertEquals(100, persons.size());
		for (Account account : accounts) {
			System.out.print(account.getId() + "  ");
			System.out.print(account.getName() + "  ");
			System.out.print(account.getTime() + "  ");
			System.out.print(account.getType() + "  ");
			System.out.print(account.isEarnings() + "  ");
			System.out.println(account.getMoney());
		}
	}

	public void testfindallbyname() throws Exception {
		AccountDBdao dao = new AccountDBdao(getContext());
		List<Account> accounts = dao.findAllByName("admin1");
		// assertEquals(100, persons.size());
		for (Account account : accounts) {
			System.out.print(account.getId() + "  ");
			System.out.print(account.getName() + "  ");
			System.out.print(account.getTime() + "  ");
			System.out.print(account.getType() + "  ");
			System.out.print(account.isEarnings() + "  ");
			System.out.println(account.getMoney());
		}
	}

	public void testfindsometimebyname() throws Exception {
		AccountDBdao dao = new AccountDBdao(getContext());
		List<Account> accounts = dao.findSomeTimeByName("admin1", "2014/5%");
		for (Account account : accounts) {
			System.out.print(account.getId() + "  ");
			System.out.print(account.getName() + "  ");
			System.out.print(account.getTime() + "  ");
			System.out.print(account.getType() + "  ");
			System.out.print(account.isEarnings() + "  ");
			System.out.println(account.getMoney());
		}
	}

	public void testfillTotalInto() throws Exception {
		AccountDBdao dao = new AccountDBdao(getContext());
		String all = dao.fillTotalInto("admin1") + "";
		System.out.println(all);
	}

	public void testfillTotalOut() throws Exception {
		AccountDBdao dao = new AccountDBdao(getContext());
		String all = dao.fillTotalOut("admin1") + "";
		System.out.println(all);
	}

	public void testfillTodayOut() throws Exception {
		AccountDBdao dao = new AccountDBdao(getContext());
		String all = dao.fillTodayOut("admin1", "2014/5/18") + "";
		System.out.println(all);
	}

	public void testfillTodayInto() throws Exception {
		AccountDBdao dao = new AccountDBdao(getContext());
		String all = dao.fillTodayInto("admin1", "2014/5/20") + "";
		System.out.println(all);
	}

	public void testfillMonthInto() throws Exception {
		AccountDBdao dao = new AccountDBdao(getContext());
		String all = dao.fillMonthInto("admin1", "2014/5%") + "";
		System.out.println(all);
	}

	public void testfillMonthOut() throws Exception {
		AccountDBdao dao = new AccountDBdao(getContext());
		String all = dao.fillMonthOut("admin1", "2014/5%") + "";
		System.out.println(all);
	}

	public void testfillYearOut() throws Exception {
		AccountDBdao dao = new AccountDBdao(getContext());
		String all = dao.fillYearOut("admin1", "2014%") + "";
		System.out.println(all);
	}

	public void testfillYearInto() throws Exception {
		AccountDBdao dao = new AccountDBdao(getContext());
		String all = dao.fillYearInto("admin1", "2014%") + "";
		System.out.println(all);
	}

	public void testfindinfobyid() throws Exception {
		AccountDBdao dao = new AccountDBdao(getContext());
		Account account = dao.findInfoById("1");

		System.out.print(account.getId() + "  ");
		System.out.print(account.getName() + "  ");
		System.out.print(account.getTime() + "  ");
		System.out.print(account.getType() + "  ");
		System.out.print(account.isEarnings() + "  ");
		System.out.println(account.getMoney());

	}
}
