/*
 * @Title FileInfo.java
 * @Copyright Copyright 2010-2015 Yann Software Co,.Ltd All Rights Reserved.
 * @Description��
 * @author Yann
 * @date 2015-8-7 ����10:13:28
 * @version 1.0
 */
package citycircle.com.OA.entities;

import java.io.Serializable;


public class FileInfo implements Serializable
{
	private int id;
	private String url;
	private String fileName;
	private int length;
	private int finished;
	private String docuid;

	/**
	 *@param id
	 *@param url
	 *@param fileName
	 *@param length
	 *@param finished
	 */
	public FileInfo(int id, String url, String fileName, int length,
					int finished,String docuid)
	{
		this.id = id;
		this.url = url;
		this.fileName = fileName;
		this.length = length;
		this.finished = finished;
		this.docuid=docuid;
	}
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getFileName()
	{
		return fileName;
	}
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	public int getLength()
	{
		return length;
	}
	public void setLength(int length)
	{
		this.length = length;
	}
	public int getFinished()
	{
		return finished;
	}
	public void setFinished(int finished)
	{
		this.finished = finished;
	}

	public String getDocuid() {
		return docuid;
	}

	public void setDocuid(String docuid) {
		this.docuid = docuid;
	}

	@Override
	public String toString()
	{
		return "FileInfo [id=" + id + ", url=" + url + ", fileName=" + fileName
				+ ", length=" + length + ", finished=" + finished + "]";
	}


}
