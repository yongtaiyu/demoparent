package org.ydk.demo;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class Test {
	/*
	 * 【https://www.cnblogs.com/LiZhiW/p/4313493.html】
	 *  注意：Xstream序列化XML时需要引用的jar包：xstream-[version].jar、xpp3-[version].jar、xmlpull-[version].jar。
	 *  Xstream序列化Json需要引用的jar包：jettison-[version].jar。
	 *  使用Xstream序列化时，对JavaBean没有任何限制。JavaBean的字段可以是私有的，也可以没有getter或setter方法，还可以没有默认的构造函数。
	 */
	@org.junit.Test
	public void test1()
	{
		Person bean=new Person("张三",19);
		XStream xstream = new XStream();
		xstream.setupDefaultSecurity(xstream);
		xstream.allowTypes(new Class[]{Person.class});
		//XML序列化
		String xml = xstream.toXML(bean);
		System.out.println(xml);
		//XML反序列化
		bean=(Person)xstream.fromXML(xml);
		System.out.println(bean);
		
		/*xstream = new XStream(new JettisonMappedXmlDriver());
		xstream.setMode(XStream.NO_REFERENCES);
		//Json序列化
		String json=xstream.toXML(bean);
		System.out.println(json);
		//Json反序列
		bean=(Person)xstream.fromXML(json);
		System.out.println(bean);*/
	}
	/**
	 * (1)Xstream序列化XML
     * Xstream序列化XML时可以允许用户使用不同的XML解析器，用户可以使用一个标准的JAXP DOM解析器或自Java6集成StAX解析器。
     * 这样用户就不需要依赖xpp3-[version].jar。
     * Xstream序列化XML时，也可以对XML节点重命名。
	 */
	@org.junit.Test
	public void test2()
	{
		Person bean=new Person("张三",19);
		//XStream xstream = new XStream();//需要XPP3库
		//XStream xstream = new XStream(new DomDriver());//不需要XPP3库
		XStream xstream = new XStream(new StaxDriver());//不需要XPP3库开始使用Java6 
		xstream.alias("人",Person.class);//为类名节点重命名
		//XML序列化
		String xml = xstream.toXML(bean);
		System.out.println(xml);
		//XML反序列化
		bean=(Person)xstream.fromXML(xml);
		System.out.println(bean);
	}
	/**
	 * (2)Xstream序列化Json
     *    Xstream序列化Json与序列化XML类似，例如：
	 */
	@org.junit.Test
	public void test3()
	{
		Person bean=new Person("张三",19);
		XStream xstream = new XStream(new JettisonMappedXmlDriver());//设置Json解析器
        xstream.setMode(XStream.NO_REFERENCES);//设置reference模型,不引用
		xstream.alias("人",Person.class);//为类名节点重命名
		//Json序列化
		String xml = xstream.toXML(bean);
		System.out.println(xml);
		//Json反序列化
		bean=(Person)xstream.fromXML(xml);
		System.out.println(bean);
	}
}
