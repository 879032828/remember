package com.seventh.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.seventh.db.AccountDBdao;
import com.seventh.db.Budget;
import com.seventh.db.BudgetDBdao;

import android.content.Context;
import android.support.v4.media.MediaBrowserCompat.MediaItem.Flags;

public class MainActivityService {

	/**
	 * 设置列表1数据
	 */
	public static List<Map<String, String>> getDataSource1(float totalInto,float totalOut, String name,Context context) {
		List<Map<String, String>> map_list1 = new ArrayList<Map<String, String>>();

		Map<String, String> map = new HashMap<String, String>();
		Budget budget = new Budget();
		BudgetDBdao budgetDBdao = new BudgetDBdao(context);
		if (budgetDBdao.isExistBudget(name)) {
			budget = budgetDBdao.findBudget(name);
			map = new HashMap<String, String>();
			map.put("txtCalculationName", "预算余额");
			map.put("txtMoney", Float.toString(budget.getBudget()));
			map_list1.add(map);
		}else {
			map = new HashMap<String, String>();
			map.put("txtCalculationName", "预算余额");
			map.put("txtMoney", Float.toString((float)0));
			map_list1.add(map);
		}
	
		String textRevenue = "" + totalInto;
		map = new HashMap<String, String>();
		map.put("txtCalculationName", "收入总额");
		map.put("txtMoney", textRevenue);
		map_list1.add(map);

		String textExpenditure = "" + totalOut;
		map = new HashMap<String, String>();
		map.put("txtCalculationName", "支出总额");
		map.put("txtMoney", textExpenditure);
		map_list1.add(map);

		String textThisMonth_income = "0";
		map = new HashMap<String, String>();
		map.put("txtCalculationName", "本月收入");
		map.put("txtMoney", textThisMonth_income);
		map_list1.add(map);
		
		String textThisMonth_expend = "0";
		map = new HashMap<String, String>();
		map.put("txtCalculationName", "本月支出");
		map.put("txtMoney", textThisMonth_expend);
		map_list1.add(map);
		

		return map_list1;
	}

	/**
	 * 设置账单一览表数据
	 */
	public static List<DataRange> getDataSource2(String name, Context context) {
		AccountDBdao accountDBdao;
		accountDBdao = new AccountDBdao(context);
		List<DataRange> dataRanges = new ArrayList<DataRange>();
		DataRange dataRange = null;
		dataRange = new DataRange();

		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00")); // 获取东八区时间
		int year = c.get(Calendar.YEAR); // 获取年
		int month = c.get(Calendar.MONTH) + 1; // 获取月份，0表示1月份
		int day = c.get(Calendar.DAY_OF_MONTH); // 获取当前天数
		String time1 = year + "/" + month + "/" + day;
		String time2 = year + "/" + month + "%";
		String time3 = year + "%";

		dataRange.setText1("今天账目一览表");
		accountDBdao.fillTodayInto(name, time1);
		dataRange.setText2("收入￥" + accountDBdao.fillTodayInto(name, time1));
		dataRange.setText3("支出 ￥" + accountDBdao.fillTodayOut(name, time1));
		dataRanges.add(dataRange);

		dataRange = null;
		dataRange = new DataRange();
		dataRange.setText1("本月账目一览表");
		dataRange.setText2("收入￥" + accountDBdao.fillMonthInto(name, time2));
		dataRange.setText3("支出￥" + accountDBdao.fillMonthOut(name, time2));
		dataRanges.add(dataRange);

		dataRange = null;
		dataRange = new DataRange();
		dataRange.setText1("本年账目一览表");
		dataRange.setText2("收入￥" + accountDBdao.fillYearInto(name, time3));
		dataRange.setText3("支出 ￥" + accountDBdao.fillYearOut(name, time3));
		dataRanges.add(dataRange);

		return dataRanges;
	}
}