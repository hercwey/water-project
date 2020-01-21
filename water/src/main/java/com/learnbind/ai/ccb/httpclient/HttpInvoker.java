package com.learnbind.ai.ccb.httpclient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;



/**

 * http invoker for doPost request or doGet request, return memory stream

 * <code>OutputStream</code>,ie. <code>ByteArrayOutputStream</code>

 * 

 * 

 * @author 

 * @see org.apache.http.client.HttpClient

 */

public class HttpInvoker

{



	/**

	 * doGet方式访问URL

	 * 

	 * @param url

	 * @return OutputStream

	 */

	public static OutputStream doGet(String url)

	{

		HttpClient httpclient = new DefaultHttpClient();

		OutputStream os = null;

		try

		{

			HttpGet httpget = new HttpGet(url);

			HttpResponse response = httpclient.execute(httpget);

			HttpEntity entity = response.getEntity();



			if (entity != null)

			{

				InputStream instream = entity.getContent();

				os = new ByteArrayOutputStream();

				int temp = 0;

				while ((temp = instream.read()) != -1)

				{

					os.write(temp);

				}



				os.flush();

				os.close();

				return os;

			}

		}

		catch (Exception e)

		{

		}

		finally

		{

			httpclient.getConnectionManager().shutdown();

		}

		return null;



	}



	/**

	 * http url request with parameter map

	 * 

	 * @param url server url

	 * @param parameterMap Parameters

	 * @return OutputStream

	 */

	public static OutputStream doPostWithUrlParams(String url,

			Map<String , Object> parameterMap)

	{

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		for (Map.Entry<String , Object> element : parameterMap.entrySet())

		{

			nameValuePairs.add(new BasicNameValuePair(element.getKey(), String

					.valueOf(element.getValue())));

		}

		try

		{

			return doPost(url, new UrlEncodedFormEntity(nameValuePairs));

		}

		catch (UnsupportedEncodingException e)

		{

		}

		return null;

	}



	/**

	 * post方式写对象流到服务端

	 * 

	 * @param url server url

	 * @param obj Object

	 * @return OutputStream

	 */

	public static OutputStream doPostWithObject(String url, final Object obj)

	{

		ContentProducer cp = new ContentProducer()

		{

			// 二进制流

			public void writeTo(OutputStream outstream) throws IOException

			{

				ObjectOutputStream oos = new ObjectOutputStream(outstream);

				oos.writeObject(obj);

				oos.flush();

				oos.close();

			}

		};

		return doPost(url, new EntityTemplate(cp));

	}



	/**

	 * 

	 * httpClient 执行post请求 返回 OutputStream对象

	 * 

	 * @param url String

	 * @param entity HttpEntity

	 * @return OutputStream

	 */

	private static OutputStream doPost(String url, HttpEntity entity)

	{

		HttpClient client = new DefaultHttpClient();

		HttpPost post = new HttpPost(url);

		OutputStream os = null;

		post.setEntity(entity);

		try

		{

			HttpResponse response = client.execute(post);

			HttpEntity resEntity = response.getEntity();

			InputStream in = resEntity.getContent();



			if (resEntity != null)

			{

				os = new ByteArrayOutputStream();

				int temp = 0;

				while ((temp = in.read()) != -1)

				{

					os.write(temp);

				}

				os.flush();

				os.close();

				EntityUtils.consume(resEntity);

				return os;

			}

		}

		catch (Exception e)

		{

		}

		finally

		{

			client.getConnectionManager().shutdown();

		}

		return null;

	}

}
