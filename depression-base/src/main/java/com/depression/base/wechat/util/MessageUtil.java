package com.depression.base.wechat.util;

import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.depression.base.wechat.entity.message.resp.Article;
import com.depression.base.wechat.entity.message.resp.TextMessage;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class MessageUtil
{
	private static XStream xstream = new XStream(new XppDriver()
	{
		public HierarchicalStreamWriter createWriter(Writer out)
		{
			return new PrettyPrintWriter(out)
			{
				boolean cdata = true;

				protected void writeText(QuickWriter writer, String text)
				{
					if (this.cdata)
					{
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else
					{
						writer.write(text);
					}
				}
			};
		}
	});

	public static Map<String, String> parseXml(HttpServletRequest request) throws Exception
	{
		Map map = new HashMap();

		InputStream inputStream = request.getInputStream();

		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);

		Element root = document.getRootElement();

		List elementList = root.elements();

		for (Object o : elementList)
		{
			map.put(((Element) o).getName(), ((Element) o).getText());
		}

		inputStream.close();
		inputStream = null;

		return map;
	}

	public static String messageToXml(Object message)
	{
		xstream.alias("xml", message.getClass());
		return xstream.toXML(message);
	}

	public static String textMessageToXml(TextMessage textMessage)
	{
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}

	public static String newsMessageToXml(com.depression.base.wechat.entity.message.resp.NewsMessage obj)
	{
		xstream.alias("xml", obj.getClass());
		xstream.alias("item", new Article().getClass());
		return xstream.toXML(obj);
	}

	public static String emoji(int hexEmoji)
	{
		return String.valueOf(Character.toChars(hexEmoji));
	}
}
