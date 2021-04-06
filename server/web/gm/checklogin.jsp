
<script type="text/javascript">
    function check(){
          <% 
             if(session.getAttribute("username")!=null){
             out.print("var username = "+session.getAttribute("username").toString()); 
          
          %>
		  $.post('http://221.179.174.53:8888/game_boss/checklogin.sq',
			     {
			       username,username
				 },
				 function callback(json){
				    alert(json);
					if(json!="true")
					  window.location.replace("visitfobiden.jsp");
				  },
				  "String");
		  <%  }else
		       out.print("window.location.replace(\"visitfobiden.jsp\");");
		    %>
	  }
	  window.onload = check;
</script>
