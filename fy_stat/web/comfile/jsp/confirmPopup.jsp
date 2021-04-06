<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="web/comfile/js/common_loading.js" ></script>
<!-- 显示加载中和超时对话框 -->
<div id="confirmPopDiv" style="display: none">
	<div class="popup">
		<div class="popup-header">
			<!-- <h2></h2> -->
			<!-- <a href="javascript:;" onclick="closeTimeOut()" title="Close" class="close-link"><img src="web/oam/newStyles/images/icon_closewin.gif" align="middle" /></a> -->
		</div>
		<div class="popup-body">
            <p style="text-align: center;">
				<label id="ajaxMsg" style="font-size: 14px; font-style: inherit"></label>
            </p>
			<p class="text-center">
				<div align="center">
					<a href="javascript:;" onclick="doSubmit()"><span class="btn_blue_ty">确定</span></a>
                    &nbsp;
                    <a href="javascript:;" onclick="closeTimeOut()"><span class="btn_blue_ty">取消</span></a>
				</div>
            </p>
		</div>
	</div>
</div>
<div id="loadingDiv" style="display: none">
	<img alt="" src="web/comfile/loading/loadding_indicator_circle_thickbox.gif" style="text-align: middle">
	<label style="color:white; font-size: 16px"></label>
</div>
