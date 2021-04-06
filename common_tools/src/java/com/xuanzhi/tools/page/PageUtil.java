package com.xuanzhi.tools.page;

/**
 * 
 *
 */
public class PageUtil {
	
	private final static int defaultPageNums = 7;
	private final static String defaultPageNumParam = "page_num";
	/**
	 * 分页
	 * @param preUrl        页面上的url，如："/browse.jsp?order=4"
	 * @param pageNums      一个页面最多显示的页面脚码
	 * @param pageNumParam  自定义的page_num参数
	 * @param pageNum       第几页
	 * @param pageSize      每页包含多少条记录
	 * @param totalRecord   总的记录数
	 * @return
	 */
	public static String makePageHTML(String preUrl,int pageNums, String pageNumParam,int pageNum ,int pageSize, int totalRecord ){
		
		Page page = new Page(pageNum,pageSize,totalRecord);
		
		return makePageHTML(preUrl, pageNums, pageNumParam, page);
	}
	
	/**
	 * 分页
	 * @param preUrl         页面上的url，如："/browse.jsp?order=4"
	 * @param pageNum        第几页
	 * @param pageSize       每页包含多少条记录
	 * @param totalRecord    总的记录数
	 * @return
	 */
	public static String makePageHTML(String preUrl,int pageNum ,int pageSize, int totalRecord ){
		
		return makePageHTML(preUrl,defaultPageNums, defaultPageNumParam, pageNum , pageSize, totalRecord );
	}

	/**
	 * 分页
	 * @param preUrl       页面上的url，如："/browse.jsp?order=4"
	 * @param pageNums     一个页面最多显示的页面脚码
	 * @param pageNum      第几页
	 * @param pageSize     每页包含多少条记录
	 * @param totalRecord  总的记录数
	 * @return
	 */
	public static String makePageHTML(String preUrl,int pageNums, int pageNum ,int pageSize, int totalRecord ){
		
		return makePageHTML(preUrl,pageNums, defaultPageNumParam, pageNum , pageSize, totalRecord );
	}
	
	/**
	 * 分页
	 * @param preUrl         页面上的url，如："/browse.jsp?order=4"
	 * @param pageNums       一个页面最多显示的页面脚码
	 * @param pageNumParam   自定义的page_num参数
	 * @param page           Page类
	 * @return
	 */
	public static String makePageHTML(String preUrl,int pageNums, String pageNumParam,Page page){
		
		if(pageNums < 1){
			pageNums = 1;
		}
		
		if( pageNums%2 == 0){
			pageNums++;
		}
		
		//preUrl中是否含有参数
		boolean hasParam = true;
		if(preUrl == null){
			hasParam = false;
		}else{
			hasParam = ( preUrl.indexOf("?") > 0 );
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("<DIV class=\"pl\">");
		if (!page.isFirstPage()) {
			sb.append("<a href=\"");
			sb.append(preUrl);
			if(hasParam){
				sb.append("&");
			}else{
				sb.append("?");
			}
			sb.append(pageNumParam + "=");
			sb.append(page.getPrevPage());
			sb.append("\">上一页</a>&nbsp;");
		} else {
			sb.append("");
		}
		
		int totalPage = page.getTotalPage();
		int curPage = page.getCurPage();
		int middlePage = pageNums/2 ;
		
		if(curPage - middlePage > 0 && totalPage - curPage -middlePage >0){
			
			for(int i = 0 ;i < pageNums;i++){
				
				int tempPage = curPage - middlePage + i;
				if(tempPage == curPage){
					sb.append("<span>" + curPage + "</span>");
					sb.append("&nbsp;");
				}else{
					sb.append("<a href=\"");
					sb.append(preUrl);
					if(hasParam){
						sb.append("&");
					}else{
						sb.append("?");
					}
					sb.append(pageNumParam + "=");
					sb.append(tempPage );
					sb.append("\">");
					sb.append(tempPage);
					sb.append("</a>&nbsp;");
				}	
			}
		}else if(totalPage < pageNums || curPage < (middlePage+1)){
			
			int count = totalPage > pageNums ? pageNums :totalPage;
			
			for(int i = 0 ;i < count;i++){
				
				int tempPage =  i + 1;
				if(tempPage == curPage){
					sb.append("<span>" + curPage + "</span>");
					sb.append("&nbsp;");
				}else{
					sb.append("<a href=\"");
					sb.append(preUrl);
					if(hasParam){
						sb.append("&");
					}else{
						sb.append("?");
					}
					sb.append(pageNumParam + "=");
					sb.append(tempPage );
					sb.append("\">");
					sb.append(tempPage);
					sb.append("</a>&nbsp;");
				}	
			}			
		}else{
			for(int i = 0 ;i < pageNums;i++){
				
				int tempPage = totalPage - pageNums + 1  + i;
				if(tempPage == curPage){
					sb.append("<span>" + curPage + "</span>");
					sb.append("&nbsp;");
				}else{
					sb.append("<a href=\"");
					sb.append(preUrl);
					if(hasParam){
						sb.append("&");
					}else{
						sb.append("?");
					}
					sb.append(pageNumParam + "=");
					sb.append(tempPage );
					sb.append("\">");
					sb.append(tempPage);
					sb.append("</a>&nbsp;");
				}	
			}			
		}
		
		if (!page.isLastPage()) {
			sb.append("<a href=\"");
			sb.append(preUrl);
			if(hasParam){
				sb.append("&");
			}else{
				sb.append("?");
			}
			sb.append(pageNumParam + "=");
			sb.append(page.getNextPage());
			sb.append("\">下一页</a>");
		} else {
			sb.append("");
		}
		
		sb.append("&nbsp;&nbsp;&nbsp;&nbsp;</div>");
		return sb.toString();
		
	}
}
