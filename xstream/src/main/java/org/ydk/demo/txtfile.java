package org.ydk.demo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class txtfile {
	public static List<String> getMatchers(String regex, String source) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(source);
		List<String> list = new ArrayList<String>();
		while (matcher.find()) {
			list.add(matcher.group());
		}
		return list;
	}

	private static Map<String, Integer> sortMapByValues(Map<String, Integer> aMap) {
		Set<Map.Entry<String, Integer>> mapEntries = aMap.entrySet();
	
		List<Map.Entry<String, Integer>> aList = new LinkedList<Map.Entry<String, Integer>>(mapEntries);
		// sorting the List
		Collections.sort(aList, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(Map.Entry<String, Integer> ele1, Map.Entry<String, Integer> ele2) {
				return ele1.getValue().compareTo(ele2.getValue());
			}
		});
		// Storing the list into Linked HashMap to preserve the order of insertion.
		Map<String, Integer> aMap2 = new LinkedHashMap<String, Integer>();
		for (Map.Entry<String, Integer> entry : aList) {
			aMap2.put(entry.getKey(), entry.getValue());
		}
		return aMap2;
	}

	public static void main(String[] args) throws IOException {
		File file = new File(txtfile.class.getResource("").getPath() + "/demo.txt");
		HashMap<String, String> regexMap = new HashMap<String, String>();
		regexMap.put("1","\\d+年合同签订情况季报表");
		regexMap.put("2","第\\S+季度");
		regexMap.put("3","软件中心|政务互联网中心|工程中心");
		//将文件按行转化为List<String>
		//List<String> list = FileUtils.readLines(file, "UTF-8");
		//0、如果是1，2，3种情况那么就直接调用原函数
		if(regexMap == null || regexMap.size() == 0) {
		    //getListFromTXT（fileName,dataStart,dataEnd,rowHeaderBegin,rowHeaderEnd,rowBegin,rowEnd,fieldSeparator)
		}
		//1、通过dataStart和dataEnd计算出要导入的内容
		String content = "";
		String[] rowArr =content.split("\\r\\n");
		//2、获取到年度标题所有行数，按顺序保存
		    //(1)用regexMap.get(0)正则表达式匹配出第一级分类的
		    //(2)通过如下rowArr计算出标题在文档中的行号,并通过行号排序
		    //(3)String[] yearArr=content.split("\\d+年合同签订情况季报表");
		    //(4)通过（2）、（3）可以确定yearArr数组中每个元素内容是属于那分类的
	}
	
	public List<Object> getContent(HashMap contentMap,HashMap regexMap)
	{
		return null;
		//(1)用regexMap.get(0)正则表达式匹配出第一级分类的
	    //(2)通过如下rowArr计算出标题在文档中的行号,并通过行号排序
	    //(3)String[] yearArr=content.split("\\d+年合同签订情况季报表");
	    //(4)通过（2）、（3）可以确定yearArr数组中每个元素内容是属于那分类的
	}
}
//			
//1、将文件按行转化为List<String>
//List<String> list = FileUtils.readLines(file, "UTF-8");
//String content = FileUtils.readFileToString(file, "UTF-8");
// System.out.println(content);
// String[] arr=content.split("\\d+年合同签订情况季报表");
// String rs=arr[1].trim();
// System.out.println(rs);
// String[] arr=content.split("软件中心|政务互联网中心|工程中心");
/*
HashMap<String, Integer> hs = new HashMap<String, Integer>();
List<String> list0 = txtfile.getMatchers(regexMap.get("1"), content);
for (int i = 0; i < list0.size(); i++) {
		for (int j = 0; j < list.size(); j++) {
			if (list0.get(i).equals(list.get(j).trim())) {
				hs.put(list0.get(i), j);
			}
		}
}*/
	
	//Map<String, Integer> sorths = txtfile.sortMapByValues(hs);