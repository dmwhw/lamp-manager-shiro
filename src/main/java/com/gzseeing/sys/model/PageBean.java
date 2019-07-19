package com.gzseeing.sys.model;

import java.util.ArrayList;
import java.util.List;

/**
 * totalPage; 总页数<br>
 * totalCount;  总记录数<br>
 * pageIndex; 当前页<br>
 * pageSize;  每页显示多少条记录<br>
 * pageStart;  从第几条开始<br>
 * pageEnd;  到第几条<br>
 * list 结果<br>
 * @Description 分页结果
 * @version 1.0.0  2018-11-14 下午5:34:08
 * @see       [相关类/方法]
 * @since     [产品/模块版本] 
 * @param <T>
 */
public class PageBean<T> {
	private Integer totalPage; // 总页数
	private Integer totalCount; // 总记录数
	private Integer pageIndex; // 当前页  1.表示第一页
	private Integer pageSize; // 每页显示多少条记录
	private Integer pageStart; // 从第几条开始
	private Integer pageEnd; // 到第几条
	private List<T> list=new ArrayList<>();
	
	private Integer deviation;//数据偏移值
	
	private final Integer defaultPageSize = 20;

	public PageBean(Integer totalCount, Integer pageIndex, Integer pageSize) {
		deviation=0;
		if (totalCount==null||totalCount<0) {
			this.totalCount = 0;
		}else {
			this.totalCount = totalCount;
		}
		if (pageIndex==null) {
			this.pageIndex = 1;
		}else {
			this.pageIndex = pageIndex;
		}
		if (pageSize==null) {
			this.pageSize = defaultPageSize;
		}else {
			this.pageSize = pageSize;
		}
		set();
	}

	public PageBean(Integer totalCount, Integer pageIndex, Integer pageSize,Integer deviation) {
		if (deviation==null) {
			deviation = 0;
		}else {
			this.deviation=deviation;
		}
		if (totalCount==null||totalCount<0) {
			this.totalCount = 0;
		}else {
			this.totalCount = totalCount;
		}
		if (pageIndex==null) {
			this.pageIndex = 1;
		}else {
			this.pageIndex = pageIndex;
		}
		if (pageSize==null) {
			this.pageSize = defaultPageSize;
		}else {
			this.pageSize = pageSize;
		}
		set();
	}

	public List<T> getList() {
		return list;
	}

	public PageBean<T> setList(List<T> list) {
		this.pageSize = list.size()+deviation;
		this.list = list;
		return this;
	}

	private void set() {
		if (totalCount % pageSize != 0) {
			totalPage = totalCount / pageSize + 1;
		} else {
			totalPage = totalCount / pageSize;
		}
		if (pageIndex == null || pageIndex <= 0) {
			pageIndex = 1;
		}
		if (pageIndex > totalPage) {
			pageIndex = totalPage;
		}
		pageStart = pageSize * (pageIndex - 1) ;
		if (pageStart < 0 ) {
			pageStart = 0;
		}
		pageEnd = pageSize * pageIndex;
		if (pageEnd > totalCount) {
			pageEnd = totalCount;
		}
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public Integer getPageSize() {
		if (pageIndex != 1) {
			return pageSize;
		}
		if (pageSize > deviation) {
			return pageSize - deviation;
		}else {
			return pageSize;
		}
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public Integer getPageStart() {
		if(pageStart-deviation<0){
			return pageStart;
		}else {
			return pageStart-deviation;
		}
	}

	public Integer getPageEnd() {
		if(pageEnd-deviation<=0){
			return pageEnd;
		}else {
			return pageEnd;
			//return pageEnd-deviation;
		}
	}

	@Override
	public String toString() {
		return "PageBean [totalPage=" + totalPage + ", totalCount="
				+ totalCount + ", pageIndex=" + pageIndex + ", pageSize="
				+ pageSize + ", pageStart=" + pageStart + ", pageEnd="
				+ pageEnd + ", list=" + list + "]";
	}

}
