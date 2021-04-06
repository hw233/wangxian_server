<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="web/comfile/js/common_loading.js" ></script>
<!-- 显示加载中和超时对话框 -->
<div id="timeoutDiv" style="display: none">
	<div class="popup">
		<div class="popup-header">
			<h2>提示信息</h2>
			<a href="javascript:;" onclick="closeTimeOut()" title="Close" class="close-link"></a>
		</div>
		<div class="popup-body">
			<div id="ajaxMsg" class="up_font2">网络超时，请重试!</div>
			<div class="button_box">
				<label class="btn_blue_ty"><a href="javascript:;" onclick="closeTimeOut()">确定</a></label>
			</div>
		</div>
	</div>
</div>
<div id="loadingDiv" style="display: none">
	<img alt="" src="web/comfile/loading/loadding_indicator_circle_thickbox.gif" style="text-align: middle">
	<label style="color:white; font-size: 16px"></label>
</div>
<div id="common_confirm_div" style="display: none">
	<div class="popup">
		<div class="popup-header">
			<h2>提示信息</h2>
			<a href="javascript:;" onclick="closeConfirmPopup()" title="Close" class="close-link"></a>
		</div>
		<div class="popup-body">
			<div id="common_confirm_msg" class="up_font2">网络超时，请重试!</div>
			<div class="button_box">
				<label class="but_orange"><a href="javascript:;" onclick="closeConfirmPopup()" name="confirm">确定</a></label>
				<label class="but_white"><a href="javascript:;" onclick="closeConfirmPopup()" name="cancel">取消</a></label>
			</div>
		</div>
	</div>
</div>
