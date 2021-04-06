import com.xuanzhi.tools.text.StringUtil;


public class TestMd5ForNineOne {
	public static void main(String[] args) {
	
		/*HashToMD5Hex("最新的测试结果表明，IE9 的预览版本已经完全支持W3C Web 
				Standards HTML5 和 CSS3。") */
		System.out.println(StringUtil.hash("最新的测试结果表明，IE9 的预览版本已经完全支持W3C Web"+ 
				"Standards HTML5 和 CSS3。"));
	}
	
	
}
