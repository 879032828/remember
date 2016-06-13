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
import com.seventh.util.TimeUtil;

import android.content.Context;

public class MainActivityService {

	/**
	 * �����б�1����
	 */
	public static List<Map<String, String>> getDataSource1(float totalInto, float totalOut, String name,
			Context context) {
		List<Map<String, String>> map_list1 = new ArrayList<Map<String, String>>();

		Map<String, String> map = new HashMap<String, String>();
		Budget budget = new Budget();
		BudgetDBdao budgetDBdao = new BudgetDBdao(context);
		if (budgetDBdao.isExistBudget(name)) {
			budget = budgetDBdao.findBudget(name);
			map = new HashMap<String, String>();
			map.put("txtCalculationName", "Ԥ�����");
			map.put("txtMoney", Float.toString(budget.getBudget()));
			map_list1.add(map);
		} else {
			map = new HashMap<String, String>();
			map.put("txtCalculationName", "Ԥ�����");
			map.put("txtMoney", Float.toString((float) 0));
			map_list1.add(map);
		}

		String textRevenue = "" + totalInto;
		map = new HashMap<String, String>();
		map.put("txtCalculationName", "�����ܶ�");
		map.put("txtMoney", textRevenue);
		map_list1.add(map);

		String textExpenditure = "" + totalOut;
		map = new HashMap<String, String>();
		map.put("txtCalculationName", "֧���ܶ�");
		map.put("txtMoney", textExpenditure);
		map_list1.add(map);

		AccountDBdao accountDBdao = new AccountDBdao(context);
		String textThisMonth_income = Float.toString(accountDBdao.fillMonthInto(name, TimeUtil.Get_Year_Month()));
		map = new HashMap<String, String>();
		map.put("txtCalculationName", "��������");
		map.put("txtMoney", textThisMonth_income);
		map_list1.add(map);

		String textThisMonth_expend = Float.toString(accountDBdao.fillMonthOut(name, TimeUtil.Get_Year_Month()));
		map = new HashMap<String, String>();
		map.put("txtCalculationName", "����֧��");
		map.put("txtMoney", textThisMonth_expend);
		map_list1.add(map);

		
		
		return map_list1;
	}

	/**
	 * �����˵�һ��������
	 */
	public static List<DataRange> getDataSource2(String name, Context context) {
		AccountDBdao accountDBdao;
		accountDBdao = new AccountDBdao(context);
		List<DataRange> dataRanges = new ArrayList<DataRange>();
		DataRange dataRange = null;
		dataRange = new DataRange();

		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00")); // ��ȡ������ʱ��
		int year = c.get(Calendar.YEAR); // ��ȡ��
		int month = c.get(Calendar.MONTH) + 1; // ��ȡ�·ݣ�0��ʾ1�·�
		int day = c.get(Calendar.DAY_OF_MONTH); // ��ȡ��ǰ����
		String time1 = year + "/" + month + "/" + day;
		String time2 = year + "/" + month + "%";
		String time3 = year + "%";

		dataRange.setText1("������Ŀһ����");
		accountDBdao.fillTodayInto(name, time1);
		dataRange.setText2("���룤" + accountDBdao.fillTodayInto(name, time1));
		dataRange.setText3("֧�� ��" + accountDBdao.fillTodayOut(name, time1));
		dataRanges.add(dataRange);

		dataRange = null;
		dataRange = new DataRange();
		dataRange.setText1("������Ŀһ����");
		dataRange.setText2("���룤" + accountDBdao.fillMonthInto(name, time2));
		dataRange.setText3("֧����" + accountDBdao.fillMonthOut(name, time2));
		dataRanges.add(dataRange);

		dataRange = null;
		dataRange = new DataRange();
		dataRange.setText1("������Ŀһ����");
		dataRange.setText2("���룤" + accountDBdao.fillYearInto(name, time3));
		dataRange.setText3("֧�� ��" + accountDBdao.fillYearOut(name, time3));
		dataRanges.add(dataRange);

		return dataRanges;
	}
}