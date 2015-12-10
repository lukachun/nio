package com.chungang.Demo;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * Hello world!
 *
 */
public class App 
{
	private static class PointsHistoryProperty {
		@Override
		public String toString() {
			return "PointsHistoryProperty [createTime=" + createTime + ", amount=" + amount + "]";
		}

		public String getCreateTime() {
			return createTime;
		}

		public PointsHistoryProperty setCreateTime(String createTime) {
			this.createTime = createTime;
			return this;
		}

		public int getAmount() {
			return amount;
		}

		public PointsHistoryProperty setAmount(int amount) {
			this.amount = amount;
			return this;
		}

		private String createTime;
		
		private int amount;
		
		private Property property;

		public Property getProperty() {
			return property;
		}

		public PointsHistoryProperty setProperty(Property property) {
			this.property = property;
			return this;
		}
	}
	
	private static void sortByCreateTime(List<PointsHistoryProperty> pointsRegisted) {
		Collections.sort(pointsRegisted, new Comparator<PointsHistoryProperty>() {
			public int compare(PointsHistoryProperty o1, PointsHistoryProperty o2) {
				return o1.getCreateTime().compareTo(o2.getCreateTime());
			}
		});
		
		// 从最近开始签到的一天开始算起
		Iterator<PointsHistoryProperty> iter = pointsRegisted.iterator();
		while (iter.hasNext()) {
			PointsHistoryProperty property = iter.next();
			if (property.getAmount() > 0) {
				break;
			} else {
				iter.remove();
			}
		}
	}
	
	private static DateTimeFormatter format = DateTimeFormat .forPattern("yyyy-MM-dd");
	
	private boolean dateRegisted(List<PointsHistoryProperty> pointsRegisted, String date) {
		if (CollectionUtils.isEmpty(pointsRegisted)) {
			return false;
		}
		for (PointsHistoryProperty property : pointsRegisted) {
			if (StringUtils.equals(date, property.getCreateTime()) && property.getAmount() > 0) {
				return true;
			}
		}
		return false;
	}
	
	private List<DateTime> getFiveDays(String date) {
		List<DateTime> days = new ArrayList<DateTime>(5);
		DateTime begin = format.parseDateTime(date);
		for (int i = 0; i < 5; i++) {
			days.add(begin.plusDays(i));
		}
		return days;
	}
	
	private void populateForFuture(List<PointsHistoryProperty> pointsRegisted, int plusPoints) {
		List<DateTime> fiveDays = null;
		DateTime today = new DateTime();
		today = today.withTime(0, 0, 0, 0);
		String todayStr = today.toString("yyyy-MM-dd");
		
		if (CollectionUtils.isEmpty(pointsRegisted)) {
			fiveDays = getFiveDays(todayStr); // 从今天开始
		} else {
			fiveDays = getFiveDays(pointsRegisted.get(0).getCreateTime());
		}
		
		int futureDays = 0;
		for (DateTime date : fiveDays) {
			if (date.isAfter(today)) {
				futureDays++;
			} else if(date.isBefore(today)) {
				if (!dateRegisted(pointsRegisted, date.toString("yyyy-MM-dd"))) { // 填充没有签到的日期
					pointsRegisted.add(new PointsHistoryProperty().setCreateTime(date.toString("yyyy-MM-dd")).setAmount(0));
				}
			} else { // 填充今天
				if (!dateRegisted(pointsRegisted, date.toString("yyyy-MM-dd"))) {
					pointsRegisted.add(new PointsHistoryProperty().setCreateTime(todayStr).setAmount(plusPoints));
				}
			}
		}
		int preAmount = plusPoints;
		// 填充未来的n天
		for (int i = 0; i < futureDays; i++) {
			PointsHistoryProperty property = new PointsHistoryProperty();
			property.setCreateTime(today.plusDays(i + 1).toString("yyyy-MM-dd"));
			preAmount = doCountPoints(preAmount);
			property.setAmount(preAmount);
			pointsRegisted.add(property);
		}
	}
	
	private int doCountPoints(int prePoints) {
		if (prePoints < 2) { // 前一天没有签到
			return 2;
		} else if (prePoints >= 2 && prePoints <= 4) {
			return prePoints + 1;
		} else {
			return 5;
		}
	}
	
	private static class Property {
		private String name;

		public String getName() {
			return name;
		}

		public Property setName(String name) {
			this.name = name;
			return this;
		}
	}
	private void setP(PointsHistoryProperty p) {
		p.setAmount(10);
	}
	
    public static void main( String[] args ) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
    {
    	
//    	PointsHistoryProperty p = new PointsHistoryProperty();
//    	new App().setP(p);
//    	System.out.println(p);
//    	Long userId = 0l;
//    	String userIdFromInvocation = "";
//    	if (StringUtils.isNotEmpty(userIdFromInvocation)) {
//			userId = Long.valueOf(userIdFromInvocation);
//		}
//    	String json = "{\"k1\":\"v1\"}";
//    	JSONObject jsonObject = JSONObject.parseObject(json);
//    	jsonObject.put("k1", "v2");
//    	jsonObject.put("k2", "v2");
//    	System.out.println(jsonObject);
//    	PointsHistoryProperty p = new PointsHistoryProperty().setProperty(new Property().setName("cg")); 
//    	DateTime dt = new DateTime(1449047011000l);
//    	System.out.println(dt.toString("yyyy-dd-MM HH:mm:ss"));
//    	Field[] fs = p.getClass().getDeclaredFields();
//    	for (Field f : fs) {
//    		System.out.println(f.getName());
//    	}
    	
//    	Field f = p.getClass().getDeclaredField("property");
//    	f.setAccessible(true);
//    	Property pro = (Property) f.get(p);
//    	System.out.println(pro.getName());
    	
//    	PointsHistoryProperty p1 = new PointsHistoryProperty();
//    	p1.setCreateTime("2015-11-01");
//    	p1.setAmount(1);
//    	
//    	PointsHistoryProperty p2 = new PointsHistoryProperty();
//    	p2.setCreateTime("2015-11-02");
//    	p2.setAmount(0);
//    	
//    	PointsHistoryProperty p3 = new PointsHistoryProperty();
//    	p3.setCreateTime("2015-11-03");
//    	p3.setAmount(3);
//    	
//    	List<PointsHistoryProperty> list = new ArrayList<PointsHistoryProperty>();
//    	list.add(p1);
//    	list.add(p2);
//    	list.add(p3);
//    	sortByCreateTime(list);
//    	System.out.println(list);
    	
//    	ArrayList<PointsHistoryProperty> pointsRegisted = new ArrayList<PointsHistoryProperty>();
//    	pointsRegisted.add(new PointsHistoryProperty().setCreateTime("2015-11-23").setAmount(0));
//		new App().populateForFuture(pointsRegisted, 2);
//    	System.out.println(pointsRegisted);
//    	String redirectUrl = String.format("http://shenghuo.xiaomi.com/o2o/friend/home?partner=%s&redirectUrl=%s", "shengri_new", URLEncoder.encode("http://m.shengri.cn/~xiaomi/hd/ganenjie"));
//    	System.out.println(redirectUrl);
    	
//    	PointsHistoryProperty p = new PointsHistoryProperty();
//    	p.setCreateTime(null);
    }
}
